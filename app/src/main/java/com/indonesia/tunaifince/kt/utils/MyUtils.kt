package com.indonesia.tunaifince.kt.utils

object MyUtils {

    private const val PHONE_CHECK = "^\\b[028][12345789][123456789]\\d{9,14}\\b$"


    fun isPhone(phone: String): Boolean {
        return !phone.matches(PHONE_CHECK.toRegex())
    }



    fun formatString(s: String): String {
        val builder = StringBuilder()

        builder.append(s)
        val pointIndex = builder.indexOf(".")
        if (pointIndex > -1) {
            val length = builder.length
            builder.delete(pointIndex, length)
        }
        builder.reverse()
        val length1 = builder.length / 3
        for (i in 1..length1) {
            builder.insert(i * 3 + (i - 1), '.')
        }
        builder.reverse()
        if (builder[0] == '.') builder.delete(0, 1)
       // builder.delete(0, builder.length)
        return builder.toString()


    }




}