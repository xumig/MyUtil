package com.indonesia.tunaifince.kt.base

import android.app.Activity
import android.app.Application
import android.os.Bundle
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

            }
        })

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
