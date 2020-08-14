package com.example.schoolairdroprefactoredition.utils;

import android.util.Base64;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.SignatureException;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.CipherOutputStream;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

/**
 * RSA加密解密
 */
public class RSACoder {
    public static final String SIGNATURE_ALGORITHM_MD5withRSA = "MD5withRSA";
    public static final String SIGNATURE_ALGORITHM_SHA1withRSA = "SHA1withRSA";
    public static final String PUBLIC_KEY = "RSAPublicKey";
    public static final String PRIVATE_KEY = "RSAPrivateKey";
    private static final String KEY_ALGORITHM = "RSA";

    /**
     * 签名
     *
     * @param data
     * @param privateKey
     * @return
     * @throws Exception
     */
    public static byte[] sign(byte[] data, byte[] privateKey) throws Exception {
        return sign(data, privateKey, SIGNATURE_ALGORITHM_SHA1withRSA);
    }

    /**
     * 签名
     *
     * @param data
     * @param privateKey
     * @param signatureAlgorithm
     * @return
     * @throws Exception
     */
    public static byte[] sign(byte[] data, byte[] privateKey, String signatureAlgorithm) {
        PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(privateKey);

        KeyFactory keyFactory = null;
        try {
            keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
            PrivateKey priKey = keyFactory.generatePrivate(pkcs8KeySpec);

            Signature signature = Signature.getInstance(signatureAlgorithm);
            signature.initSign(priKey);
            signature.update(data);
            return signature.sign();
        } catch (NoSuchAlgorithmException | InvalidKeySpecException | InvalidKeyException | SignatureException e) {
            e.printStackTrace();
        }
        return "".getBytes();
    }

    /**
     * 验证
     *
     * @param data
     * @param publicKey
     * @param sign
     * @return
     * @throws Exception
     */
    public static boolean verify(byte[] data, byte[] publicKey, byte[] sign)
            throws Exception {
        return verify(data, publicKey, sign, SIGNATURE_ALGORITHM_SHA1withRSA);
    }

    /**
     * 验证
     *
     * @param data
     * @param publicKey
     * @param sign
     * @param signatureAlgorithm
     * @return
     * @throws Exception
     */
    public static boolean verify(byte[] data, byte[] publicKey, byte[] sign,
                                 String signatureAlgorithm) throws Exception {
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(publicKey);

        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);

        PublicKey pubKey = keyFactory.generatePublic(keySpec);

        Signature signature = Signature.getInstance(signatureAlgorithm);
        signature.initVerify(pubKey);
        signature.update(data);

        return signature.verify(sign);
    }


    /***************************************公钥加密*************************************/

    /**
     * 公钥加密
     *
     * @param textToEncrypt 要加密的信息
     * @param publicK       公钥
     * @return 加密后的信息
     */
    public static String encryptWithPublicKey(String publicK, String textToEncrypt) {
        try {
            PublicKey publicKey = getFromString(publicK);
            Cipher inCipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
            inCipher.init(Cipher.ENCRYPT_MODE, publicKey);

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            CipherOutputStream cipherOutputStream = new CipherOutputStream(
                    outputStream, inCipher);
            cipherOutputStream.write(Base64.decode(textToEncrypt, Base64.DEFAULT));
            cipherOutputStream.close();

            byte[] vals = outputStream.toByteArray();
            return Base64.encodeToString(vals, Base64.DEFAULT);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 公钥加密
     *
     * @param data      要加密的信息
     * @param publicKey 公钥
     * @return 加密后的信息
     */
    public static String encryptByPublicKey(String publicKey, String data) {
        try {
            PublicKey publicK = getFromString(publicKey);
            Cipher cipher = Cipher.getInstance("RSA/NONE/PKCS1PADDING");
            cipher.init(Cipher.ENCRYPT_MODE, publicK);
            byte[] encrypted = cipher.doFinal(data.getBytes(StandardCharsets.UTF_8));
            String s = Base64.encodeToString(encrypted, Base64.NO_WRAP);
            Log.d("RSA", "encrypted alipay id ---- > " + s);
            return s;
        } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | BadPaddingException | IllegalBlockSizeException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**************************************************************************************/


    public static PublicKey getFromString(String keystr) {
        try {
            String pubKeyDER = keystr.replace("-----BEGIN PUBLIC KEY-----", "");
            pubKeyDER = pubKeyDER.replace("-----END PUBLIC KEY-----", "");
            pubKeyDER = pubKeyDER.replace("\n", "");

            Log.d("RSA public key DER", pubKeyDER);
            byte[] encoded = Base64.decode(pubKeyDER, Base64.NO_WRAP);
            X509EncodedKeySpec keySpec = new X509EncodedKeySpec(encoded);
            KeyFactory kf = KeyFactory.getInstance(KEY_ALGORITHM);

            return kf.generatePublic(keySpec);
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            e.printStackTrace();
        }
        return null;
    }

}