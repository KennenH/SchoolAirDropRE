package com.example.schoolairdroprefactoredition.utils

object ConstantUtil {
    const val USER_ID = "19858120611"

    /**
     * 检查用户是否同意使用服务协议和隐私政策
     */
    const val START_UP_PREFERENCE = "IfUserAgreeToTerms"
    const val START_UP_IS_TERMS_AGREED = "IfUserAgreeToTerms"

    /**
     * 图片压缩比率 范围在0-100之内
     */
    const val ORIGIN = 100
    const val DEFAULT_COMPRESS = 80

    /**
     * location
     */
    const val LONGITUDE = "nowLongitude"
    const val LATITUDE = "nowLatitude"

    /**
     * data fetch num
     */
    const val DATA_FETCH_DEFAULT_SIZE = 50

    /**
     * home fragment singleton
     */
    @JvmField
    var FRAGMENT_NUM = "fragment_num"
    var FRAGMENT_NUM_NEWS = 101
    var FRAGMENT_NUM_NEARBY = 102

    /**
     * SOB base Api
     */
    var SOB_API_BASE_URL = "https://api.sunofbeach.net/shop/"

    /**
     * 移动端接口密码
     */
    var CLIENT_GRANT_TYPE = "client_credentials"

    @JvmField
    var CLIENT_ID = "testclient"

    @JvmField
    var CLIENT_SECRET = "123456"

    /**
     * 校园空投 网络接口base url
     */
    var SCHOOL_AIR_DROP_BASE_URL = "http://106.54.110.46/inf/"

    @JvmField
    var SCHOOL_AIR_DROP_BASE_URL_NEW = "http://106.54.110.46:8000/"
    var LOCAL = "http://10.0.2.2:8000/"
    const val MENU_CLICK_GAP = 1000

    /**
     * 要登陆的alipayID
     */
    const val KEY_ALIPAY_FOR_LOGIN = "RequestedLoginAlipayID"

    /**
     * 主页获取用户修改信息的标识
     */
    const val KEY_UPDATED = "UserInfoModified"

    /**
     * 页面获取用户授权信息的键
     */
    const val KEY_AUTHORIZE = "AUTH2User"

    /**
     * 使用token请求获取的用户信息键
     */
    const val KEY_USER_INFO = "UserInfo"
    const val KEY_USER_BASE_INFO = "User?BASEInfo"

    /**
     * 获取物品信息键
     */
    const val KEY_GOODS_INFO = "GoodsInfo"

    /**
     * 打开用户个人信息页面时是否可修改信息
     */
    const val KEY_INFO_MODIFIABLE = "modifiableOrNot?"

    /**
     * 标志有些页面是否是我的个人页面
     */
    const val KEY_IS_MINE = "isMyPageOrNOT?"

    /**
     * 报价页状态码
     */
    const val QUOTE_STATE_UNHANDLED = 0
    const val QUOTE_STATE_ACCEPTED = 1
    const val QUOTE_STATE_REFUSED = 2
}