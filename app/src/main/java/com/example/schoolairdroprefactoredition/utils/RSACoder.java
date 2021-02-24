package com.example.schoolairdroprefactoredition.utils;

import android.util.Base64;

import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.SignatureException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

import javax.crypto.Cipher;

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

    public static String encryptWithPublicKey(String publicK, String textToEncrypt) {
        String encoded = "";
        byte[] encrypted;
        try {
            PublicKey pubKey = getFromString(publicK);
            Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1PADDING"); //or try with "RSA"
            cipher.init(Cipher.ENCRYPT_MODE, pubKey);
            encrypted = cipher.doFinal(textToEncrypt.getBytes(StandardCharsets.UTF_8));
            encoded = Base64.encodeToString(encrypted, Base64.DEFAULT);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return encoded;
    }

    /**************************************************************************************/


    public static PublicKey getFromString(String keystr) {
        try {
            String pubKeyDER = keystr.replace("-----BEGIN PUBLIC KEY-----", "");
            pubKeyDER = pubKeyDER.replace("-----END PUBLIC KEY-----", "");
            pubKeyDER = pubKeyDER.replace("\n", "");

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