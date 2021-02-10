package com.example.schoolairdroprefactoredition.utils

import com.blankj.utilcode.constant.TimeConstants
import java.util.*

class TimeUtil {
    companion object {

        /**
         * 获取与当前时间的差
         * 一天之内显示 20:35
         * 昨天显示 昨天 20:35
         * 昨天之前显示 2021.1.31
         */
        fun getChatTimeSpanByNow(date: Date): String {
            val millis = date.time
            val wee = getWeeOfToday()
            return when {
                millis >= wee -> {
                    String.format("%tR", millis)
                }
                millis >= wee - TimeConstants.DAY -> {
                    String.format("昨天 %tR", millis)
                }
                else -> {
                    String.format("%tF", millis)
                }
            }
        }

        private fun getWeeOfToday(): Long {
            val cal = Calendar.getInstance()
            cal[Calendar.HOUR_OF_DAY] = 0
            cal[Calendar.SECOND] = 0
            cal[Calendar.MINUTE] = 0
            cal[Calendar.MILLISECOND] = 0
            return cal.timeInMillis
        }
    }
}