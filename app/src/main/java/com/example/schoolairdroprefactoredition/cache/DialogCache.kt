package com.example.schoolairdroprefactoredition.cache


/**
 * @author kennen
 * @date 2021/3/26
 */
data class DialogCache(

        /**
         * 用户是否同意了协议和服务条款
         *
         * 若未同意则每次打开app都会提示需要同意，否则将无法进入app
         */
        val isAgreeToPolicy: Boolean,

        /**
         * 用户是否已知晓安卓版app可能无法收到系统通知
         */
        val isKnowMessageTip: Boolean
) {
    companion object {
        /**
         * 是否同意服务协议和条款 键
         */
        const val KEY_IS_AGREE_TO_POLICY = "IfUserAgreeToTerms"

        /**
         * 是否知晓安卓版app无法收到系统通知 键
         */
        const val KEY_ID_KNOW_MESSAGE_TIP = "isKnowingMessageTip"
    }
}