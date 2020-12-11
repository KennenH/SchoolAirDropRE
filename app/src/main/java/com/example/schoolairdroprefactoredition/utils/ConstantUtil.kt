package com.example.schoolairdroprefactoredition.utils

object ConstantUtil {
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
    const val AMAP_LOCATION = "amapLocation"

    /**
     * data fetch num
     */
    const val DATA_FETCH_DEFAULT_SIZE = 50

    /**
     * home fragment singleton
     */
    const val FRAGMENT_NUM = "fragment_num"
    const val FRAGMENT_NUM_NEWS = 101
    const val FRAGMENT_NUM_NEARBY = 102

    /**
     * SOB base Api
     */
    const val SOB_API_BASE_URL = "https://api.sunofbeach.net/shop/"

    /**
     * 移动端接口密码
     */
    const val CLIENT_GRANT_TYPE = "client_credentials"
    const val CLIENT_ID = "testclient"
    const val CLIENT_SECRET = "123456"

    /**
     * 校园空投 网络接口base url
     */
    const val SCHOOL_AIR_DROP_BASE_URL = "http://106.54.110.46:8000/"
    const val SCHOOL_AIR_DROP_BASE_URL_NEW = "http://81.69.14.64:8000/"
    const val LOCAL = "http://10.0.2.2:8000/"

    /**
     * 点击间隔最短1秒
     */
    const val MENU_CLICK_GAP = 1000


    /**
     * tab页面页号
     */
    const val KEY_ARG_SECTION_NUMBER = "section_number"

    /**
     * AddNewActivity 页面类型
     */
    const val KEY_ADD_NEW_TYPE = "addNewPageType"

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
    const val KEY_TOKEN = "AUTH2User"

    /**
     * 仅获取用户id，是当前登录的用户的id
     */
    const val KEY_USER_ID = "Key_user_id!"

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
     * 获取物品封面信息键
     */
    const val KEY_GOODS_BASE_INFO = "key_goods_base_info"

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