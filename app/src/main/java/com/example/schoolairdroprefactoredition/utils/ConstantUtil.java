package com.example.schoolairdroprefactoredition.utils;

public class ConstantUtil {

    /**
     * location
     */
    public static final String LONGITUDE = "nowLongitude";
    public static final String LATITUDE = "nowLatitude";


    /**
     * home fragment data fetch num
     */
    public static final int DATA_FETCH_DEFAULT_SIZE = 40;

    /**
     * home fragment singleton
     */
    public static String FRAGMENT_NUM = "fragment_num";
    public static int FRAGMENT_NUM_NEWS = 101;
    public static int FRAGMENT_NUM_NEARBY = 102;

    /**
     * SOB base Api
     */
    public static String SOB_API_BASE_URL = "https://api.sunofbeach.net/shop/";


    /**
     * 移动端接口密码
     */
    public static String CLIENT_GRANT_TYPE = "client_credentials";
    public static String CLIENT_ID = "testclient";
    public static String CLIENT_SECRET = "123456";

    /**
     * 校园空投 网络接口base url
     */
    public static String SCHOOL_AIR_DROP_BASE_URL = "http://106.54.110.46/inf/";
    public static String SCHOOL_AIR_DROP_BASE_URL_NEW = "http://106.54.110.46:8000/";
    public static String LOCAL = "http://10.0.2.2:8000/";

    public static final int MENU_CLICK_GAP = 1000;

    /**
     * 主页获取用户修改信息的标识
     */
    public static final String KEY_UPDATED = "UserInfoModified";
    /**
     * 页面获取用户授权信息的键
     */
    public static final String KEY_AUTHORIZE = "AUTH2User";
    /**
     * 使用token请求获取的用户信息键
     */
    public static final String KEY_USER_INFO = "UserInfo";
    /**
     * 获取物品信息键
     */
    public static final String KEY_GOODS_INFO = "GoodsInfo";
}
