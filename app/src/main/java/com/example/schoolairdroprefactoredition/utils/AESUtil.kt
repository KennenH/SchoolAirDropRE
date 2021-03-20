package com.example.schoolairdroprefactoredition.utils

import android.util.Base64
import java.nio.charset.StandardCharsets
import java.security.NoSuchAlgorithmException
import java.security.SecureRandom
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.spec.SecretKeySpec

/**
 * @author kennen
 * @date 2021/3/19
 */
object AESUtil {

    private const val KEY_ALGORITHM = "AES"

    private const val DEFAULT_CIPHER_ALGORITHM = "AES/CBC/PKCS5Padding" //默认的加密算法

    /**
     * AES 加密操作
     *
     * @param content  待加密内容
     * @param password 加密密码
     * @return 返回Base64转码后的加密数据
     */
    fun encryptWithAES(content: String, password: String): String? {
        try {
            val cipher = Cipher.getInstance(DEFAULT_CIPHER_ALGORITHM) // 创建密码器
            val byteContent = content.toByteArray(StandardCharsets.UTF_8)
            cipher.init(Cipher.ENCRYPT_MODE, getAESKey(password)) // 初始化为加密模式的密码器
            val result = cipher.doFinal(byteContent) // 加密
            return Base64.encodeToString(result, Base64.DEFAULT) //通过Base64转码返回
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
        return null
    }

    /**
     * AES 解密操作
     */
    fun decryptWithAES(content: String, password: String): String? {
        try {
            //实例化
            val cipher = Cipher.getInstance(DEFAULT_CIPHER_ALGORITHM)
            //使用密钥初始化，设置为解密模式
            cipher.init(Cipher.DECRYPT_MODE, getAESKey(password))
            //执行操作
            val result = cipher.doFinal(content.toByteArray(StandardCharsets.UTF_8))
            return String(result, StandardCharsets.UTF_8)
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
        return null
    }

    /**
     * 生成加密秘钥
     */
    private fun getAESKey(password: String): SecretKeySpec? {
        //返回生成指定算法密钥生成器的 KeyGenerator 对象
        try {
            val kg = KeyGenerator.getInstance(KEY_ALGORITHM)
            //AES 要求密钥长度为 128
            kg.init(128, SecureRandom(password.toByteArray()))
            //生成一个密钥
            val secretKey = kg.generateKey()
            return SecretKeySpec(secretKey.encoded, KEY_ALGORITHM) // 转换为AES专用密钥
        } catch (ex: NoSuchAlgorithmException) {
            ex.printStackTrace()
        }
        return null
    }
}