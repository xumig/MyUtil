package com.indonesia.tunaifince.kt.http.model
import com.google.gson.annotations.SerializedName


data class ExtensionRepayBody(
    @SerializedName("expired")
    val expired: String = "",
    @SerializedName("extensionTime")
    val extensionTime: String = "",
    @SerializedName("process")
    val process: List<Proces> = listOf(),
    @SerializedName("repayAmount")
    val repayAmount: Double = 0.0,
    @SerializedName("resCode")
    val resCode: String = "",
    @SerializedName("resMsg")
    val resMsg: String = "",
    @SerializedName("status")
    val status: String = "",
    @SerializedName("term")
    val term: String = "",
    @SerializedName("vaNumber")
    val vaNumber: String = ""
)

