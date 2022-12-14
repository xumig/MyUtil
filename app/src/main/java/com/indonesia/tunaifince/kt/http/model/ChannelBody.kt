package com.indonesia.tunaifince.kt.http.model
import com.google.gson.annotations.SerializedName


data class ChannelBody(
    @SerializedName("repayChannels")
    val repayChannels: List<RepayChannel> = listOf()
)

data class RepayChannel(
    @SerializedName("isBank")
    val isBank: Int = 0,
    @SerializedName("value")
    val value: String = "",
    @SerializedName("key")
    val key: String = ""
)