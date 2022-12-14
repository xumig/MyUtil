package com.indonesia.tunaifince.kt.http.interceptor


import com.google.gson.Gson
import com.indonesia.tunaifince.kt.aes.AES
import com.indonesia.tunaifince.kt.baseConfig.AdjustUtil
import com.indonesia.tunaifince.kt.baseConfig.HttpConfiguration.TOKEN_KEY
import com.indonesia.tunaifince.kt.baseConfig.HttpConfiguration.TOKEN_NAME
import com.indonesia.tunaifince.kt.topFun.loge
import com.indonesia.tunaifince.kt.utils.MMKVCache
import okhttp3.Interceptor
import okhttp3.Response


class HeadInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val builder = chain.request().newBuilder()
        builder.addHeader("packageName", AdjustUtil.getPackageName())

        if (chain.request().method().lowercase() == "post") {
            builder.addHeader("Context-Type", "text/plain; charset=utf-8")
        }
        val tokenMap = hashMapOf<String, String>().apply {
            this["sourceChannel"] = AdjustUtil.trackerName().toString()
            this["packageName"] = AdjustUtil.getPackageName()
            this["adid"] = AdjustUtil.adid().toString()
            this["version"] = AdjustUtil.getAppVersion()

            if (MMKVCache.uuId.isNullOrEmpty().not()) {
                this["uuId"] = MMKVCache.uuId!!
            }
            if (MMKVCache.userId.isNullOrEmpty().not()) {
                this["userId"] = MMKVCache.userId!!
            }

        }

        //  JSONObject(tokenMap as Map<*, *>?).toString()

        val json = Gson().toJson(tokenMap)
        AES.toastEncrypt(json, TOKEN_KEY)?.let {
            builder.addHeader(TOKEN_NAME, it)
        }

        return chain.proceed(builder.build())
    }
}