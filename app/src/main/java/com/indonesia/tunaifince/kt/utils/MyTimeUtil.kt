package com.indonesia.tunaifince.kt.utils

import kotlinx.coroutines.*

object MyTimeUtil {

    private var coroutine: Job? = null

    fun start(scope: CoroutineScope, t: Int, s: (Int) -> Unit, end: () -> Unit) {
        coroutine?.cancel()
        coroutine = scope.launch {
            for (i in t downTo 0) {
                delay(1000)
                s.invoke(i)
            }
            end.invoke()
        }
    }

    fun cancel() {
        coroutine?.cancel()
    }



    fun stoString(s: Int): String {
        var s = s
        val minutes = s / 60 //转换分钟
        s %= 60 //剩余秒数
        return unitFormat(minutes) + ":" + unitFormat(s)
    }


    private fun unitFormat(i: Int): String {
        return if (i in 0..9) "0$i" else "" + i
    }

}