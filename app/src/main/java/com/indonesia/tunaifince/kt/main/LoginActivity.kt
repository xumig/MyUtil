package com.indonesia.tunaifince.kt.main

import android.view.MotionEvent
import com.indonesia.tunaifince.kt.base.App
import com.indonesia.tunaifince.kt.base.BaseActivity
import com.indonesia.tunaifince.kt.databinding.ActivityLoginBinding
import com.indonesia.tunaifince.kt.http.HTTPRequest
import com.indonesia.tunaifince.kt.topFun.intervalTime
import com.indonesia.tunaifince.kt.topFun.loge
import com.indonesia.tunaifince.kt.topFun.onClick
import com.indonesia.tunaifince.kt.topFun.toast
import com.indonesia.tunaifince.kt.utils.MMkvCache
import com.indonesia.tunaifince.kt.utils.PermissionXM

class LoginActivity : BaseActivity<ActivityLoginBinding>(ActivityLoginBinding::inflate) {

    private val vm by getViewModel<LoginVM>()

    override fun initView() {

        vb.loginPhone.sendSMSBUT.onClick {
            vm.sendCode("81452324865","text")
        }

    }



    override fun dispatchTouchEvent(event: MotionEvent): Boolean {
        /**
         * 可能点击的事件不止一个点，多点触摸，比如两个以上手指同时触摸屏幕。
         *
         */

        if (event.action == MotionEvent.ACTION_DOWN
            && !PermissionXM.checkPermission(this)
        ) {
            //
            if (intervalTime(1500)) {
                if (MMkvCache.permissionToVoice) {
                        //todo 第一次权限弹框
                    MMkvCache.permissionToVoice=false
                } else {
                    PermissionXM.requestPerission(this) {
                        if (!it) {
                            //todo 没有权限弹框
                        }
                    }
                }
                return false
            }

        }
        return super.dispatchTouchEvent(event)
    }



    override fun onBackPressed() {
        if (intervalTime(2000)) {
            "Tekan lagi untuk keluar dari aplikasi".toast()
        }else{
            App.instance.exitApp()
        }
    }

}