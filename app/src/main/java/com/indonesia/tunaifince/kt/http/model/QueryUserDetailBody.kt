package com.indonesia.tunaifince.kt.http.model
import com.google.gson.annotations.SerializedName


data class QueryUserDetailBody(
    @SerializedName("bankNo")
    val bankNo: String = "",
    @SerializedName("bindCardStatus")
    val bindCardStatus: String = "",
    @SerializedName("checkCollection")
    val checkCollection: Boolean = false,
    @SerializedName("faceCompareResult")
    val faceCompareResult: String = "",
    @SerializedName("faceCompareStatus")
    val faceCompareStatus: Boolean = false,
    @SerializedName("holdIdReslut")
    val holdIdReslut: String = "",
    @SerializedName("livenessStatus")
    val livenessStatus: Boolean = false,
    @SerializedName("matchResult")
    val matchResult: String = "",
    @SerializedName("isUseSilence")
    val isUseSilence: Boolean = false,
    @SerializedName("marketingCopy")
    val marketingCopy: Boolean = false,




)