package com.indonesia.tunaifince.kt.http.model
import com.google.gson.annotations.SerializedName

data class SendSmsBody(
    @SerializedName("expiry") val expiry: Int, // 60
    @SerializedName("status") val status1: String?, // S
    @SerializedName("transId") val transId: String? // SE20220428194535031CC2EF6629D23C
)
