package com.indonesia.tunaifince.kt.main

import com.indonesia.tunaifince.kt.base.BaseViewModel
import com.indonesia.tunaifince.kt.baseConfig.AdjustUtil.sendEvent
import com.indonesia.tunaifince.kt.baseConfig.MyAdjustEvent.LOGIN_SEND_VOICE_SMS_CODE_XM
import com.indonesia.tunaifince.kt.baseConfig.MyAdjustEvent.loginSendSmsCodeXm
import com.indonesia.tunaifince.kt.topFun.loge

class LoginVM : BaseViewModel() {

    fun sendCode(phone: String, type: String) {
        if ("text" == type) {
            sendEvent(loginSendSmsCodeXm)
        } else {
            sendEvent(LOGIN_SEND_VOICE_SMS_CODE_XM)
        }
        rnd.rnd({
            sendVerifiyCode(
                rnd.post(
                    hashMapOf(
                        "mobile" to phone,
                        "type" to type
                    )
                )
            )
        }, {
            "2222222".loge("111")
               "${it.status1}".loge("111")
        })

    }


//    fun login(phone: String, code: String) {
//        http.rnd({
//            login(
//                http.post(
//                    hashMapOf(
//                        "code" to code,
//                        "phone" to phone
//                    )
//                )
//            )
//        }, {
//
//        })
//
//    }

}