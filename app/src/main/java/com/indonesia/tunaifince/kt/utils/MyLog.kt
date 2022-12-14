package com.indonesia.tunaifince.kt.utils

import android.util.Log

class MyLog {

    val isBut = true
    val tag = "xm"


     fun log(message: String,level: Int = 5 ) {
        if (!isBut) return
        when (level) {
            1 -> Log.v(tag, message)
            2 -> Log.d(tag, message)
            3 -> Log.i(tag, message)
            4 -> Log.w(tag, message)
            5 -> Log.e(tag, message)
        }
    }
}