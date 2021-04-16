package com.example.schoolairdroprefactoredition.ui.components

import android.content.Context
import android.graphics.Typeface
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.style.*
import com.blankj.utilcode.util.LogUtils
import com.example.schoolairdroprefactoredition.domain.SpannableJsonStyle
import com.example.schoolairdroprefactoredition.utils.MyUtil
import com.google.gson.Gson
import java.lang.Exception


/**
 * @author kennen
 * @date 2021/4/11
 */
class SadSpannable(context: Context, spannableJson: SpannableJsonStyle) : SpannableStringBuilder(spannableJson.string) {

    companion object {
        /**
         * 将range转化为list
         */
        @JvmStatic
        private fun listFromRange(ranges: String): List<IntRange> {
            val list = ArrayList<IntRange>()
            for (range in ranges.split("|")) {
                val dot = range.indexOf(",")
                if (dot == -1) break
                list.add(IntRange(
                        range.substring(0, dot).toInt(),
                        range.substring(dot + 1, range.length).toInt()))
            }
            return list
        }

        /**
         * 解析json为[SpannableJsonStyle]对象
         */
        @JvmStatic
        fun parseJsonToSpannableJsonStyle(json: String): SpannableJsonStyle {
            return try {
                Gson().fromJson(json, SpannableJsonStyle::class.java)
            } catch (e: Exception) {
                SpannableJsonStyle(json, SpannableJsonStyle.Style())
            }
        }

        /**
         * 将文本按照Spannable中记录的规则转换为json
         * 实际转换出来的json可能比最开始使用的json要复杂，这是因为
         * 生成逻辑是对每一段被应用了不同style的文字进行分割，一段文字
         * 中的style样式不同、样式数量不同都算不同
         *
         * @param spanned 带样式的内容
         */
        @JvmStatic
        fun generateJson(context: Context, spanned: Spanned): String {
            val style = SpannableJsonStyle.Style()
            var i = 0
            while (i < spanned.length) {
                // 寻找下一个和当前style不同的区域，style应用类型、数量不同均算作不同区域
                val next = spanned.nextSpanTransition(i, spanned.length, CharacterStyle::class.java)
                val spans = spanned.getSpans(i, next, CharacterStyle::class.java)
                // 遍历所有的分区，将每个分区中所有应用的style都找出来并对每一种style生成一段range和value
                LogUtils.d("next -- > $next  spans -- > ${spans.size}")
                var absoluteSizeCounted = false
                var strikeThroughCounted = false
                var textColorCounted = false
                var boldCounted = false
                for (span in spans) {
                    if (absoluteSizeCounted
                            && strikeThroughCounted
                            && textColorCounted
                            && boldCounted) break

                    val nowRange = "$i,$next|"
                    when (span) {
                        is AbsoluteSizeSpan -> {
                            // size
                            val size = when (MyUtil.px2dp(span.size)) {
                                InputToolKit.TEXT_SIZE_S -> "s|"
                                InputToolKit.TEXT_SIZE_L -> "l|"
                                InputToolKit.TEXT_SIZE_XL -> "xl|"
                                else -> ""
                            }
                            if (size != "") {
                                style.size.apply {
                                    range += nowRange
                                    value += size
                                }
                            }
                            absoluteSizeCounted = true
                        }
                        is StrikethroughSpan -> {
                            // delete line
                            style.delete.range += nowRange
                            strikeThroughCounted = true
                        }
                        is ForegroundColorSpan -> {
                            // color
                            val color = when (span.foregroundColor) {
                                context.resources.getColor(InputToolKit.TEXT_COLOR_RED, context.theme) -> "red|"
//                                context.resources.getColor(InputToolKit.TEXT_COLOR_GREEN, context.theme) -> "green|"
//                                context.resources.getColor(InputToolKit.TEXT_COLOR_BLUE, context.theme) -> "blue|"
                                else -> ""
                            }
                            if (color.isNotBlank()) {
                                style.color.apply {
                                    range += nowRange
                                    value += color
                                }
                            }
                            textColorCounted = true
                        }
                        is MetricAffectingSpan -> {
                            style.bold.range += nowRange
                            boldCounted = true
                        }
                    }
                }
                i = next
            }

            val spannableJsonStyle = SpannableJsonStyle(spanned.toString(), style)
            return Gson().toJson(spannableJsonStyle)
        }
    }

    init {
        spannableJson.style.apply {
            bold.let {
                for (range in listFromRange(bold.range)) {
                    setSpan(StyleSpan(Typeface.BOLD),
                            range.first,
                            range.last,
                            Spannable.SPAN_INCLUSIVE_INCLUSIVE)
                }
            }
            size.let {
                val colorList = size.value.split("|")
                for ((index, range) in listFromRange(size.range).withIndex()) {
                    val size = when (colorList[index]) {
                        "s" -> {
                            InputToolKit.TEXT_SIZE_S
                        }
                        "l" -> {
                            InputToolKit.TEXT_SIZE_L
                        }
                        "xl" -> {
                            InputToolKit.TEXT_SIZE_XL
                        }
                        else -> {
                            InputToolKit.TEXT_SIZE_M
                        }
                    }
                    setSpan(
                            AbsoluteSizeSpan(MyUtil.dp2px(size)),
                            range.first,
                            range.last,
                            Spannable.SPAN_INCLUSIVE_INCLUSIVE)
                }
            }
            delete.let {
                for (range in listFromRange(delete.range)) {
                    setSpan(StrikethroughSpan(),
                            range.first,
                            range.last,
                            Spannable.SPAN_INCLUSIVE_INCLUSIVE)
                }
            }
            color.let {
                val colorList = color.value.split("|")
                for ((index, range) in listFromRange(color.range).withIndex()) {
                    val color: Int = when (colorList[index]) {
                        "red" -> {
                            InputToolKit.TEXT_COLOR_RED
                        }
//                        "blue" -> {
//                            InputToolKit.TEXT_COLOR_BLUE
//                        }
//                        "green" -> {
//                            InputToolKit.TEXT_COLOR_GREEN
//                        }
                        else -> {
                            InputToolKit.TEXT_COLOR_BLACK
                        }
                    }
                    setSpan(
                            ForegroundColorSpan(context.resources.getColor(color, context.theme)),
                            range.first,
                            range.last,
                            Spannable.SPAN_INCLUSIVE_INCLUSIVE)
                }
            }
        }

    }
}