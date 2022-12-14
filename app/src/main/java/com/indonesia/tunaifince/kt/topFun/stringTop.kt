package com.indonesia.tunaifince.kt.topFun

import android.widget.Toast
import com.indonesia.tunaifince.kt.aes.AES
import com.indonesia.tunaifince.kt.base.App
import com.indonesia.tunaifince.kt.utils.MyLog


fun String.loge(s: String?) {
    MyLog().log("$s = :${this}")
}

fun String.toDecode() = AES.decodeTV(this)

fun String.toast() {
    Toast.makeText(App.instance, AES.decodeTV(this), Toast.LENGTH_SHORT).show()

}

