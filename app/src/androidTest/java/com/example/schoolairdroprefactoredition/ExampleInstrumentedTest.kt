package com.example.schoolairdroprefactoredition

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.blankj.utilcode.util.LogUtils
import com.example.schoolairdroprefactoredition.cache.UserTokenCache
import com.example.schoolairdroprefactoredition.cache.util.JsonCacheUtil
import com.example.schoolairdroprefactoredition.cache.util.UserLoginCacheUtil
import com.example.schoolairdroprefactoredition.domain.DomainToken
import com.example.schoolairdroprefactoredition.utils.RSACoder
import junit.framework.Assert.assertEquals
import org.junit.Assert
import org.junit.Ignore
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see [Testing documentation](http://d.android.com/tools/testing)
 */
@RunWith(AndroidJUnit4::class)
class ExampleInstrumentedTest {

    @Test
    fun RSACoder_encode_decode() {
        UserLoginCacheUtil.getInstance().saveUserToken(DomainToken(200,
                DomainToken.Data("kennen", 10, "Berear", ""),
                ""))
        val cache = JsonCacheUtil.getInstance().getCache(UserTokenCache.KEY, UserTokenCache::class.java)
                ?.token
        LogUtils.d("cache ac -- > ${cache?.access_token}")
        LogUtils.d("cache -- > ${cache?.data?.access_token}")
        val accessToken = UserLoginCacheUtil.getInstance().getUserToken()?.data?.access_token
        LogUtils.d("access -- > $accessToken")
        assertEquals(accessToken, "kennen")
    }
}