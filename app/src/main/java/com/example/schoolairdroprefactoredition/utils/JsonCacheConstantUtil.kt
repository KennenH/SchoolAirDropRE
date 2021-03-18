package com.example.schoolairdroprefactoredition.utils

/**
 * 关于[JsonCacheUtil]的所有常量定义
 */
object JsonCacheConstantUtil {

    /**
     * 使用auth code换来的用户alipay id
     * 使用该id可以注册或者登录校园空投app
     *
     * 用户id将在一次授权之后永久保存，若本地没有改值的缓存将会引导用户进行支付宝授权
     */
    const val USER_ALIPAY_ID = "thisusersALIPAYID"

    /**
     * 是否对于同一个用户在一定时限之内已经请求过用户信息了[Boolean]
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

    /**
     * 物品在24小时之内已经被当前用户浏览过了，24小时之内再次浏览都不会为次物品增加浏览量 [Boolean]
     */
    const val GOODS_BROWSED = "goods-was-browsed-"

    /**
     * 进行频繁操作之后用户将被禁止点击这些检查频繁操作的按钮 冷却时间15s
     */
    const val ACTION_TOO_FREQUENT_COOLDOWN = 15_000L

    /**
     * 所有检查频繁操作的计数键 [Int]
     */
    const val FREQUENT_ACTION_COUNT = "refreshtoofrequent"
}