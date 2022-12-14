package com.indonesia.tunaifince.kt.http.model
import com.google.gson.annotations.SerializedName


data class OcrCheckBody(
    @SerializedName("idNo")
    val idNo: String = "",
    @SerializedName("name")
    val name: String = "",
    @SerializedName("resCode")
    val resCode: String = "",
    @SerializedName("status")
    val status: String = "",
    @SerializedName("resMsg")
    val resMsg: String = ""


)