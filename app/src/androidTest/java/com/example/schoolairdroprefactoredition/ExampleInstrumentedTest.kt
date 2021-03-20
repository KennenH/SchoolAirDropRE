package com.example.schoolairdroprefactoredition

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.blankj.utilcode.util.LogUtils
import com.example.schoolairdroprefactoredition.utils.RSACoder
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
    @Ignore("AS自动生成代码")
    fun useAppContext() {
        // Context of the app under test.
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        Assert.assertEquals("com.example.schoolairdroprefactoredition", appContext.packageName)
    }

    @Test
    fun RSACoder_generateKeys() {
        val keys = RSACoder.generateKeys()
        if (keys != null) {
//            LogUtils.d("public key -- > " + keys[RSACoder.PUBLIC_KEY]);
//            LogUtils.d("private key -- > " + keys[RSACoder.PRIVATE_KEY]);
            val encryptWithPublicKey = RSACoder.encryptWithPublicKey(keys[RSACoder.PUBLIC_KEY]!!, "kennen")
            LogUtils.d(encryptWithPublicKey)
            val decryptWithPrivateKey = RSACoder.decryptWithPrivateKey(keys[RSACoder.PRIVATE_KEY]!!, encryptWithPublicKey!!)
            Assert.assertEquals("kennen", decryptWithPrivateKey)
        }
    }
}