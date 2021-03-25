package com.example.schoolairdroprefactoredition.cache.util

/**
 * [JsonCacheUtil]中不成类的cache常量，成类cache常量一般在类本身里面定义
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
     * 物品在24小时之内已经被当前用户浏览过了，24小时之内再次浏览都不会为次物品增加浏览量 [Boolean]
     */
    const val GOODS_BROWSED = "goods-was-browsed-"


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
    const val NEXT_GET_TIME_SPAN = 12_000L


    /**
     * 频繁操作键
     * 检查频繁操作的键
     */
    const val KEY_FREQUENT_ACTION = "too_frequent_action"

    /**
     * 进行常规频繁操作之后用户将被禁止点击这些检查常规频繁操作的按钮 冷却时间
     */
    const val FREQUENT_ACTION_COOLDOWN = 10_000L


    /**
     * 快速操作flag键
     * 所有检查快速操作的flag键，由于快速操作检查冷却非常短，因此可以使用一个键作为所有动作的键
     */
    const val KEY_QUICK_ACTION_FLAG = "too_quick_action_flag"

    /**
     * 进行快速操作检查的动作后在冷却时间内的快速操作都将被无条件过滤，防止恶意调用
     */
    const val QUICK_ACTION_COOLDOWN = 10_000L
}