package com.indonesia.tunaifince.kt.http.model
import com.google.gson.annotations.SerializedName


data class BankCardListBody(
    @SerializedName("bankList")
    val bankList: List<Bank> = listOf()
)

data class Bank(
    @SerializedName("bankCode")
    val bankCode: String = "",
    @SerializedName("bankName")
    val bankName: String = "",
    @SerializedName("code")
    val code: String = "",
    @SerializedName("thirdChannelCode")
    val thirdChannelCode: String = ""
)