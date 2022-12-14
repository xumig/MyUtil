package com.indonesia.tunaifince.kt.http

import com.google.gson.Gson
import com.indonesia.tunaifince.kt.aes.AES
import com.indonesia.tunaifince.kt.base.IUIAction
import com.indonesia.tunaifince.kt.base.IUIActionEventObserver
import com.indonesia.tunaifince.kt.baseConfig.HttpConfiguration.API_KEY
import com.indonesia.tunaifince.kt.baseConfig.HttpConfiguration.IP_URL
import com.indonesia.tunaifince.kt.baseConfig.HttpConfiguration.REQUEST_URL
import com.indonesia.tunaifince.kt.http.MyRetrofit.createRetrofit
import com.indonesia.tunaifince.kt.http.model.BaseResult
import com.indonesia.tunaifince.kt.main.LoginActivity
import com.indonesia.tunaifince.kt.topFun.loge
import com.indonesia.tunaifince.kt.topFun.toast
import com.indonesia.tunaifince.kt.utils.MMKVCache
import kotlinx.coroutines.*
import okhttp3.MediaType
import okhttp3.RequestBody


class HTTPRequest(
    private val vmScope: IUIAction? = null,
    private val aScope: IUIActionEventObserver? = null
) {

    private fun getScope(): CoroutineScope {

        if (vmScope != null) {
            return vmScope.scope
        }
        if (aScope != null) {
            return aScope.lifecycleSupportedScope
        }
        return GlobalScope
    }

//
//    companion object : MySingleton<HTTPRequest, IUIAction?>(::HTTPRequest)


    private val getService by lazy { createRetrofit(REQUEST_URL).create(ApiService::class.java) }
    private val getIPService by lazy { createRetrofit(IP_URL).create(ApiService::class.java) }

    fun post(map: HashMap<String, String>): RequestBody {
        "dd".loge("post")
        return RequestBody.create(
            MediaType.parse("text/plain; charset=utf-8"),
            AES.apiEncrypt(Gson().toJson(map), API_KEY)
        )
    }

    fun post(s: String): RequestBody {
        return RequestBody.create(
            MediaType.parse("text/plain; charset=utf-8"),
            AES.apiEncrypt(s, API_KEY)
        )
    }

    /**
     * api get
     */
    fun get(map: HashMap<String?, String?>): String? {
        return AES.apiEncrypt(Gson().toJson(map), API_KEY)?.replace("\r|\n*", "")
    }


    private fun <T> myHttp(
        block: suspend CoroutineScope.() -> BaseResult<T>,
        okCallable: (T) -> Unit,
        errorCallable: ((String) -> Unit)? = null,
        isShowLoading: Boolean = true,
        isShowToast: Boolean = true,
    ) {
        if (isShowLoading) {
            showLoad()
        }
        getScope().launch(Dispatchers.Main) {

            try {
                ensureActive()
                val data = withContext(Dispatchers.IO) { block() }
                when (data.status) {
                    "200" -> {
                        okCallable.let { it.invoke(data.body) }
                    }
                    "301" -> {
                        jumpLogin()
                        MMKVCache.logOut()
                    }
                    "304" -> {

                    }
                    "702" -> {}
                    else -> {
                        if (isShowToast) {
                            data.message.toast()
                        }
                        errorCallable.let { it?.invoke(data.message) }
                    }
                }
                if (isShowLoading) {
                    dismissLoad()
                }
            } catch (throwable: Throwable) {
                throwable.cause?.message?.loge("error")

                if(throwable is CancellationException){
                }else{
                    "Tolong periksa jaringanmu".toast()
                }

            } finally {
                if (isShowLoading) {
                    dismissLoad()
                }
            }
        }
    }

    private fun dismissLoad() {
        vmScope?.dismissLoading() ?: aScope?.dismissLoading()
    }

    private fun jumpLogin() {
        vmScope?.jumpActivity(LoginActivity::class.java)
            ?: aScope?.jumpActivity(LoginActivity::class.java)
    }

    private fun showLoad() {
        vmScope?.showLoading() ?: aScope?.showLoading()
    }


    private suspend fun <T> executeApi(apiFun: suspend ApiService.() -> BaseResult<T>): BaseResult<T> {
        return apiFun.invoke(getService)
    }


    /**
     * 请求网络数据
     * apiFun  api接口
     * okCallable  成功回调
     * errorCallable  错误回调
     */
    fun <T> rnd(
        apiFun: suspend ApiService.() -> BaseResult<T>,
        okCallable: (T) -> Unit,
        errorCallable: ((String) -> Unit)? = null,
        isShowLoading: Boolean = true,
    ) {
        myHttp({ executeApi(apiFun) }, okCallable, errorCallable,isShowLoading)
    }

    fun ipRequeat(
        okCallable: (String) -> Unit,
        errorCallable: ((String) -> Unit)? = null
    ) {
        myHttp({ getIPService.ipPool() }, okCallable, errorCallable, false)
    }


}