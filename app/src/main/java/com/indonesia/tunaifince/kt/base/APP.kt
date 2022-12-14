package com.indonesia.tunaifince.kt.base

import android.app.Activity
import android.app.Application
import android.os.Bundle
import android.util.Log
import coil.Coil
import coil.ImageLoader
import coil.request.CachePolicy
import com.adjust.sdk.Adjust
import com.adjust.sdk.AdjustConfig
import com.adjust.sdk.LogLevel
import com.flurry.android.FlurryAgent
import com.flurry.android.FlurryPerformance
import com.indonesia.tunaifince.kt.BuildConfig
import com.indonesia.tunaifince.kt.R
import com.indonesia.tunaifince.kt.baseConfig.HttpConfiguration.ADJUST_KEY
import com.indonesia.tunaifince.kt.baseConfig.HttpConfiguration.FLURRY_API_KEY
import com.indonesia.tunaifince.kt.utils.MMKVCache
import com.liveness.dflivenesslibrary.DFProductResult
import com.liveness.dflivenesslibrary.DFTransferResultInterface
import com.tencent.mmkv.MMKV

import java.util.*


class App : Application() {

    private lateinit var activityLinkedList: LinkedList<Activity>

    companion object {
        lateinit var instance: App
    }

    override fun onCreate() {
        super.onCreate()

        instance = this
        activityLinkedList = LinkedList()
        registerActivityLifecycleCallbacks(object : ActivityLifecycleCallbacks {
            override fun onActivityPaused(activity: Activity) {
                Adjust.onPause()
            }

            override fun onActivityStarted(activity: Activity) {

            }

            override fun onActivityDestroyed(activity: Activity) {
                activityLinkedList.remove(activity)
            }

            override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {

            }

            override fun onActivityStopped(activity: Activity) {

            }

            override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
                activityLinkedList.add(activity)
            }

            override fun onActivityResumed(activity: Activity) {
                Adjust.onResume()
            }
        })

        MMKV.initialize(this, this.filesDir.absolutePath + "/app")

        //图片加全局配置
        Coil.setImageLoader(ImageLoader.Builder(this).apply {
            placeholder(R.mipmap.image) //设置占位图
//            error(R.drawable.icon_image_error)  //设置出错时候的占位图
            diskCachePolicy(CachePolicy.ENABLED)
            memoryCachePolicy(CachePolicy.ENABLED)
            networkCachePolicy(CachePolicy.ENABLED)
            crossfade(true) //淡入效果加载
            crossfade(500) //淡入效果的时间设置
        }.build())

        initAdjustL()
        initFlurryAgent()
        Adjust.getGoogleAdId(this) { googleAdId -> MMKVCache.googleAdId = googleAdId }
    }


    /**
     * 初始化Flurry Crash
     */
    private fun initFlurryAgent() {

        if (!BuildConfig.DEBUG) {
            FlurryAgent.Builder()
                .withDataSaleOptOut(false) //CCPA - the default value is false
                .withCaptureUncaughtExceptions(true) //崩溃数据发送到 Flurry
                .withIncludeBackgroundSessionsInMetrics(true)
                .withLogLevel(Log.ERROR)
                .withPerformanceMetrics(FlurryPerformance.ALL)
                .withLogEnabled(true)
                .build(this, FLURRY_API_KEY)
        }
    }

    /**
     * 初始化AdjustL
     */
    private fun initAdjustL() {

        val config = AdjustConfig(
            instance,
            ADJUST_KEY,
            AdjustConfig.ENVIRONMENT_PRODUCTION,
            !BuildConfig.DEBUG
        )
        if (BuildConfig.DEBUG) {
            config.setLogLevel(LogLevel.SUPRESS)
        } else {
            config.setLogLevel(LogLevel.VERBOSE) // enable all logs
        }
        config.setUrlStrategy(AdjustConfig.URL_STRATEGY_CHINA)
        Adjust.onCreate(config)
    }


    /**
     * 退出APP
     * 逐个退出Activity
     */
    fun exitApp() {
        if (activityLinkedList.size > 0) {
            for (item in activityLinkedList) {
                item.finish()
            }
        }
    }

    /**
     *退出activity
     */
    fun exitActivity(activity: Activity) {
        if (activity in activityLinkedList) {
            activity.finish()
        }
    }

    /**
     *取最后一个activity
     */
    fun getAtLastActivity(): Activity? =
        activityLinkedList?.size?.let { activityLinkedList?.get(it - 1) }




}
