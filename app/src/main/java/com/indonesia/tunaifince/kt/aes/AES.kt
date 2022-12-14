package com.indonesia.tunaifince.kt.aes

import android.util.Log
import com.indonesia.tunaifince.kt.baseConfig.HttpConfiguration.API_KEY
import com.indonesia.tunaifince.kt.topFun.loge
import javax.crypto.Cipher
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec

object AES {
    private const val charset = "utf-8"

    // 偏移量
    private const val offset = 16

    // 加密器类型:加密算法为AES,加密模式为CBC,补码方式为PKCS5Padding
    private const val transformation = "AES/CBC/PKCS5Padding"

    // 算法类型：用于指定生成AES的密钥
    private const val algorithm = "AES"


    /**
     * api加密
     *
     * @param content 需要加密的内容
     * @param key     加密密码
     * @return
     */
    fun encrypt(content: String, key: ByteArray?): String? {
        "$content".loge("加密前")
        try {
            //构造密钥
            val skey = SecretKeySpec(key, algorithm)
            //创建初始向量iv用于指定密钥偏移量(可自行指定但必须为128位)，因为AES是分组加密，下一组的iv就用上一组加密的密文来充当
            val iv = IvParameterSpec(key, 0, offset)
            //创建AES加密器
            val cipher = Cipher.getInstance(transformation)
            val byteContent = content.toByteArray(charset(charset))
            //使用加密器的加密模式
            cipher.init(Cipher.ENCRYPT_MODE, skey, iv)
            // 加密
            val result = cipher.doFinal(byteContent)
            //使用BASE64对加密后的二进制数组进行编码
            return Base64Util.encodeBase64String(result)
        } catch (e: Exception) {
            Log.e("AES加密异常", e.message!!)
        }
        return null
    }

    /**
     * api加密
     * @param content 需要加密的内容
     * @param key     加密密码
     * @return
     */
    fun apiEncrypt(content: String, key: String): String? {
        return encrypt(content, Base64Util.decodeBase64(key))
    }


    /**
     * toast
     * @param content 需要加密的内容
     * @param key     加密密码
     * @return
     */
    fun toastEncrypt(content: String, key: String): String? {
        return encrypt(content, key.toByteArray())
    }


    /**
     * AES（256）解密
     *
     * @param content 待解密内容
     * @param key     解密密钥
     * @return 解密之后
     * @throws Exception
     */
    fun decrypt(content: String, key: String): String? {
        try {
            val skey = SecretKeySpec(Base64Util.decodeBase64(key), algorithm)
            val iv = IvParameterSpec(Base64Util.decodeBase64(key), 0, offset)
            val cipher = Cipher.getInstance(transformation)
            //解密时使用加密器的解密模式
            // 初始化
            cipher.init(Cipher.DECRYPT_MODE, skey, iv)
            val result = cipher.doFinal(Base64Util.decodeBase64(content))
            // 解密
            return String(result)
        } catch (e: Exception) {
            Log.e("AES解密异常:{}", e.message!!)
        }
        return null
    }


    fun decodeTV(content: String): String {
        content.loge("文案解密前")
        return try {
            val skey = SecretKeySpec(Base64Util.decodeBase64(API_KEY), algorithm)
            val iv = IvParameterSpec(Base64Util.decodeBase64(API_KEY), 0, offset)
            val cipher = Cipher.getInstance(transformation)
            //解密时使用加密器的解密模式
            // 初始化
            cipher.init(Cipher.DECRYPT_MODE, skey, iv)
            val result = cipher.doFinal(Base64Util.decodeBase64(content))
            // 解密
            String(result).loge("文案解密后")
            return String(result)
        } catch (e: Exception) {
            return content
        }
    }
}