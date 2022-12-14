package com.indonesia.tunaifince.kt.http

import com.indonesia.tunaifince.kt.http.interceptor.HeadInterceptor
import com.indonesia.tunaifince.kt.http.interceptor.ResponseInterceptor
import com.indonesia.tunaifince.kt.topFun.loge
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.net.URLDecoder
import java.util.concurrent.TimeUnit

object MyRetrofit {

    private fun createOkHttpClient(): OkHttpClient {
        val builder = OkHttpClient.Builder()
            .sslSocketFactory(
                SSLParamUtils.getSslSocketFactory().sSLSocketFactory,
                SSLParamUtils.getSslSocketFactory().trustManager
            )
            .readTimeout(6000L, TimeUnit.MILLISECONDS)
            .writeTimeout(6000L, TimeUnit.MILLISECONDS)
            .connectTimeout(6000L, TimeUnit.MILLISECONDS)
            .addInterceptor(HeadInterceptor())

            .addInterceptor(
                HttpLoggingInterceptor {
//                    "${
//                        URLDecoder.decode(
//                            it,
//                            "utf-8"
//                        )
//                    }".loge("http")
                }.setLevel(
                    HttpLoggingInterceptor.Level.BODY
                )
            )
            .addInterceptor(ResponseInterceptor())
         //   .retryOnConnectionFailure(true)
        return builder.build()
    }

    fun createRetrofit(baseHttpUrl: String): Retrofit {
        return Retrofit.Builder()
            .client(createOkHttpClient())
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(baseHttpUrl)
            .build()
    }
}