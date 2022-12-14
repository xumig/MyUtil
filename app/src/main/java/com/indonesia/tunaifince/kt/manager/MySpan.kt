package com.indonesia.tunaifince.kt.manager

import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.view.View
import android.widget.TextView
import androidx.core.text.bold
import androidx.core.text.buildSpannedString
import androidx.core.text.color
import androidx.core.text.inSpans
import com.indonesia.tunaifince.kt.topFun.onClick

class MySpan {

    fun loginSP(tv: TextView, user: (String) -> Unit, privacy: (String) -> Unit) {
        tv.movementMethod = LinkMovementMethod.getInstance()
        tv.text = buildSpannedString {
            append("Masuk Akun Dianggap Setuju Perjanjian ")

            inSpans(object : ClickableSpan() {
                override fun onClick(widget: View) {
                    widget.onClick {
                        user.invoke("LayananPengguna")
                    }
                }
            }
            ) {
                color(0x0BC161) {
                    bold {
                        append("LayananPengguna")
                    }
                }
            }

            append(" dan ")

            inSpans(object : ClickableSpan() {
                override fun onClick(widget: View) {
                    widget.onClick {
                        privacy.invoke("Kebijakan Pribadi")
                    }
                }
            }
            ) {
                color(0x0BC161) {
                    bold {
                        append("Kebijakan Pribadi")
                    }
                }
            }
        }
    }

}
