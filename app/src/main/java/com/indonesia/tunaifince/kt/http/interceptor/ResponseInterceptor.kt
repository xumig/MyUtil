package com.indonesia.tunaifince.kt.http.interceptor

import com.indonesia.tunaifince.kt.aes.AES
import com.indonesia.tunaifince.kt.baseConfig.HttpConfiguration.API_KEY
import com.indonesia.tunaifince.kt.topFun.loge
import okhttp3.Interceptor
import okhttp3.Response
import okhttp3.ResponseBody


class ResponseInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val response = chain.proceed(chain.request())
        val mediaType = response.body()?.contentType()

        chain.request().url()?.toString()?.loge("url")
        var content = response.body()?.string()
        content?.loge("ResponseInterceptor解密前")
        content = content?.let { AES.decrypt(it, API_KEY) }
        content?.loge("ResponseInterceptor解密后")

        return response.newBuilder()
            .body(ResponseBody.create(mediaType, content ?: ""))
            .build()
    }


//    fun jsonto(json: String) {
//        val jsonObject = JSONObject(json)
//        val s: String = jsonObject.getString("status")
//        if (s == "301") {
//            //清除用户信息
//
//        } else if (s == "304") {
//
//        }
//    }

}