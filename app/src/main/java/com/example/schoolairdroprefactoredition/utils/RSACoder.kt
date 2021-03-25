package com.example.schoolairdroprefactoredition.utils

import android.util.Base64
import java.nio.charset.StandardCharsets
import java.security.KeyFactory
import java.security.KeyPairGenerator
import java.security.PrivateKey
import java.security.PublicKey
import java.security.spec.PKCS8EncodedKeySpec
import java.security.spec.X509EncodedKeySpec
import java.util.*
import javax.crypto.Cipher

/**
 * RSA加密解密
 */
object RSACoder {

    const val PUBLIC_KEY = "RSAPublicKey"

    const val PRIVATE_KEY = "RSAPrivateKey"

    private const val KEY_ALGORITHM = "RSA"

    private const val KEY_LENGTH = 2048

    /**
     * 使用公钥加密内容
     *
     * @param publicKey       公钥
     * @param textToEncrypt 需要加密的内容
     * @return 使用公钥加密后的内容
     */
    fun encryptWithPublicKey(publicKey: String, textToEncrypt: String): String? {
        try {
            val pubKey = getPublicKeyFromString(publicKey)
            val cipher = Cipher.getInstance("RSA/ECB/PKCS1PADDING")
            cipher.init(Cipher.ENCRYPT_MODE, pubKey)
            val encrypted = cipher.doFinal(textToEncrypt.toByteArray(StandardCharsets.UTF_8))
            return Base64.encodeToString(encrypted, Base64.DEFAULT)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }

    /**
     * 私钥解密
     *
     * @param textToDecrypt       需要解密的内容
     * @param privateKey 私钥
     * @return 私钥解密后的内容
     */
    fun decryptWithPrivateKey(privateKey: String, textToDecrypt: String): String? {
        try {
            val privateK = getPrivateKeyFromString(privateKey)
            val cipher = Cipher.getInstance("RSA/ECB/PKCS1PADDING")
            cipher.init(Cipher.DECRYPT_MODE, privateK)
            val decrypted = cipher.doFinal(textToDecrypt.toByteArray(StandardCharsets.UTF_8))
            return Base64.encodeToString(decrypted, Base64.DEFAULT)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }

    /**
     * 从row公钥获取被修剪的公钥
     */
    private fun getPublicKeyFromString(public: String): PublicKey? {
        try {
            // 去掉头部和尾部
            val pubKeyDER = public.replace("-----BEGIN PUBLIC KEY-----", "")
                    .replace("-----END PUBLIC KEY-----", "")
                    .replace("\n", "")

            val encoded = Base64.decode(pubKeyDER, Base64.NO_WRAP)
            val keySpec = X509EncodedKeySpec(encoded)
            val kf = KeyFactory.getInstance(KEY_ALGORITHM)
            return kf.generatePublic(keySpec)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }

    /**
     * 从row私钥获取被修剪的私钥
     */
    private fun getPrivateKeyFromString(private: String): PrivateKey? {
        try {
            // 去掉头部和尾部
            val priKeyDER = private.replace("-----BEGIN PRIVATE KEY-----", "")
                    .replace("-----END PRIVATE KEY-----", "")
                    .replace("\n", "")

            val encoded = Base64.decode(priKeyDER, Base64.NO_WRAP)
            val keySpec = PKCS8EncodedKeySpec(encoded)
            val kf = KeyFactory.getInstance(KEY_ALGORITHM)
            return kf.generatePrivate(keySpec)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }

    /**
     * 生成一对密钥
     *
     * @return 生成的密钥对
     */
    fun generateKeys(): Map<String, String>? {
        val keyMap: MutableMap<String, String> = HashMap(2)
        try {
            val keyPairGen = KeyPairGenerator.getInstance(KEY_ALGORITHM).also {
                it.initialize(KEY_LENGTH)
            }
            val keyPair = keyPairGen.generateKeyPair()
            keyMap[PUBLIC_KEY] = Base64.encodeToString(keyPair.public.encoded, Base64.DEFAULT)
            keyMap[PRIVATE_KEY] = Base64.encodeToString(keyPair.private.encoded, Base64.DEFAULT)
            return keyMap
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }
}