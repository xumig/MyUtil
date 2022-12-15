package com.indonesia.tunaifince.kt.main

import android.content.Context
import android.widget.Toast

class MyToast {

    fun show(s: String): String {
        return s
    }

    fun loge(c: Context) {
        Toast.makeText(c, "你好啊", Toast.LENGTH_LONG).show()
    }
}