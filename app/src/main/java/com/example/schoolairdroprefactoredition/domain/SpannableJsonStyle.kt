package com.example.schoolairdroprefactoredition.domain

import com.google.gson.Gson


/**
 * @author kennen
 * @date 2021/4/11
 *
 * 多个range使用|分隔，有value的字段同样使用|与range对应分隔
 * 就算分隔之间的value相同也需要分隔，否则无法解析
 *
 * 所有样式中range每个间隔范围都是闭区间(included,included)
 */
data class SpannableJsonStyle(
        val string: String,
        val style: Style
) {
    data class Style(
            val bold: Bold = Bold(),
            val color: Color = Color(),
            val delete: Delete = Delete(),
            val size: Size = Size()
    ) {
        data class Bold(
                var range: String = "",
                var value: String = ""
        ) {
            override fun toString(): String {
                return "Bold(range='$range', value='$value')"
            }
        }

        data class Color(
                var range: String = "",
                var value: String = ""
        ) {
            override fun toString(): String {
                return "Color(range='$range', value='$value')"
            }
        }

        data class Delete(
                var range: String = "",
                var value: String = ""
        ) {
            override fun toString(): String {
                return "Delete(range='$range', value='$value')"
            }
        }

        data class Size(
                var range: String = "",
                var value: String = ""
        ) {
            override fun toString(): String {
                return "Size(range='$range', value='$value')"
            }
        }

        override fun toString(): String {
            return "Style(bold=$bold, color=$color, delete=$delete, size=$size)"
        }
    }

    override fun toString(): String {
        return "SpannableJsonStyle(string='$string', style=$style)"
    }
}

