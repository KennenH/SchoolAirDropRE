package com.example.schoolairdroprefactoredition.utils

/**
 * 关于[JsonCacheUtil]的所有常量定义
 */
class JsonCacheConstantUtil {

    companion object {

        /**
         * 是否对于同一个用户在一定时限之内已经请求过用户信息了 键
         *
         * 使用时为了区分不同的用户，需要在键之后加上一个用户id
         *
         * [com.example.schoolairdroprefactoredition.repository.UserRepository.getUserInfoById]
         */
        const val IS_GET_USER_INFO_PRESENTLY = "isGetThisUserInfoPresently"

        /**
         * 在这个时间之内重复打开同一个用户的个人主页将不会获取网络请求
         *
         * [com.example.schoolairdroprefactoredition.repository.UserRepository.getUserInfoById]
         */
        const val NEXT_GET_TIME_SPAN = (5 * 60 * 1000).toLong()
    }
}