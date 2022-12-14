package com.indonesia.tunaifince.kt.http.model

import com.google.gson.annotations.SerializedName


data class LoginBody(
    @SerializedName("checkCollection")
    val checkCollection: Boolean,
    @SerializedName("createTime")
    val createTime: String,
    @SerializedName("onTest")
    val onTest: Boolean,
    @SerializedName("sourceChannel")
    val sourceChannel: String,
    @SerializedName("userId")
    val userId: String,
    @SerializedName("userStatus")
    val userStatus: String,
    @SerializedName("uuId")
    val uuId: String
)
