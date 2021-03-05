package com.example.schoolairdroprefactoredition.utils

object ConstantUtil {

    /**
     * mi push app key
     */
    const val MIPUSH_APP_ID = "2882303761518719324"
    const val MIPUSH_APP_KEY = "5561871983324"

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
     * 默认获取数据一页
     */
    const val DEFAULT_PAGE_SIZE = 15

    /**
     * 聊天界面一次性最大可发送的字符长度
     */
    const val IM_MAX_TEXT_LENGTH = 255

    /**
     * retrofit 最大缓存数量
     */
    const val CACHE_SIZE = (10 * 1024 * 1024).toLong()

    /**
     * 缓存最大寿命(单位 s)
     *
     * 当缓存时间超过最大寿命时将被丢弃 30天
     */
    const val CACHE_MAX_AGE = (30 * 24 * 60 * 60).toLong()

    /**
     * 可议价 物品类型
     */
    const val GOODS_TYPE_BARGAIN = "bargain"

    /**
     * 二手 物品类型
     */
    const val GOODS_TYPE_SECONDHAND = "secondHand"

    /**
     * 移动端接口密码
     */
    const val CLIENT_GRANT_TYPE = "client_credentials"
    const val CLIENT_ID = "testclient"
    const val CLIENT_SECRET = "123456"

    /**
     * 校园空投 网络接口base url
     */
    const val SCHOOL_AIR_DROP_BASE_URL_DEPRECATED = "http://81.69.14.64:8000/"
    const val SCHOOL_AIR_DROP_BASE_URL = "http://81.69.14.64:8080/"
    const val SCHOOL_AIR_DROP_BASE_URL_IM = "http://81.69.14.64:2020/"
    const val LOCAL_BASE_URL = "http://10.0.2.2:2020/"

    /**
     * 七牛云图片基础url
     */
    const val QINIU_BASE_URL = "http://res.schoolairdrop.com/"

    /**
     * IP
     */
    const val SCHOOL_AIR_DROP_IP = "81.69.14.64"
    const val LOCAL_IP = "10.0.2.2"

    /**
     * 网络请求成功码
     */
    const val HTTP_OK = 200

    /**
     * 不存在
     */
    const val HTTP_NOT_FOUND = 404

    /**
     * 默认头像url
     */
    const val DEFAULT_AVATAR = "assets/img/avatar.png"

    /**
     * 图片上传类型 聊天
     */
    const val UPLOAD_TYPE_IM = "im"

    /**
     * 图片上传类型 聊天
     */
    const val UPLOAD_TYPE_GOODS = "goods"

    /**
     * 图片上传类型 帖子
     */
    const val UPLOAD_TYPE_POST = "post"

    /**
     * 图片上传类型 头像
     */
    const val UPLOAD_TYPE_AVATAR = "user"

    /**
     * 图片上传时若为物品和帖子，需要传递封面的键
     */
    const val KEY_UPLOAD_COVER = "cover"


    /**
     * 点击间隔最短1秒
     */
    const val MENU_CLICK_GAP = 1000


    /**
     * IM消息类型 文本
     */
    const val MESSAGE_TYPE_TEXT = 0

    /**
     * IM消息类型 图片
     */
    const val MESSAGE_TYPE_IMAGE = 1


    //////////////////////////// KEYS ////////////////////////////////
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
     * 页面获取用户授权信息的键[com.example.schoolairdroprefactoredition.domain.DomainToken]
     */
    const val KEY_TOKEN = "AUTH2User"

    /**
     * 仅获取用户id，是当前登录的用户的id
     */
    const val KEY_USER_ID = "Key_user_id!"

    /**
     * 页面获取用户信息的键
     *
     * 所有页面都尽量使用该键统一来获取用户信息，混合使用不同的键容易使数据cast失败而奔溃
     * 获取[com.example.schoolairdroprefactoredition.domain.DomainUserInfo.DataBean]
     * 而不是[com.example.schoolairdroprefactoredition.domain.DomainUserInfo]
     */
    const val KEY_USER_INFO = "UserInfo"

    /**
     * 尽量不要使用该键获取来用户信息，否则两个键混用很容易造成cast失败而崩溃
     * 一般条件下使用[ConstantUtil.KEY_USER_INFO]来获取完整的用户信息，即
     * [com.example.schoolairdroprefactoredition.domain.DomainUserInfo.DataBean]
     *
     * 使用该键获取用户信息的页面在这里记录，之后若有，也在这里添加
     * [com.example.schoolairdroprefactoredition.scene.chat.ChatActivity]
     *
     *
     * 只有完美契合已下情况时才考虑使用该Key作为页面用户信息获取键
     * 1、对于进入目标页面的页面：
     * 进入目标页面的方式中有若干方式 没有办法 或者 没有好的办法 或者 综合考虑完全没必要 来获取完整的用户信息
     * 2、对于目标页面：
     * 该页面只需要userId、userName、userAvatar三个用户信息
     * 3、对于由目标页面进入的页面：
     * 由目标页面进入的 大部分页面都不需要完整的用户信息 或者 大部分页面本身逻辑就不需要完整的信息即可正常
     * 打开 或者 这部分页面本身就是使用userId去获取完整用户信息的页面
     */
    @Deprecated("只是为了引起注意")
    const val KEY_USER_SIMPLE_INFO = "UserSimpleInfo"

    /**
     * 获取物品封面信息 键
     */
    const val KEY_GOODS_INFO = "key_goods_info"

    /**
     * 获取物品id 键
     */
    const val KEY_GOODS_ID = "key_goods_id"

    /**
     * 打开用户个人信息页面时是否可修改信息
     */
    const val KEY_INFO_MODIFIABLE = "modifiableOrNot?"

    /**
     * 标志有些页面是否是我的个人页面
     */
    const val KEY_IS_MINE = "isMyPageOrNOT?"
}