package com.indonesia.tunaifince.kt.http.model

import com.google.gson.annotations.SerializedName


data class QueryCashUserBody(
    @SerializedName("idNo")
    val idNo: String,
    @SerializedName("isOverdue")
    val isOverdue: Int,
    @SerializedName("mobile")
    val mobile: String,
    @SerializedName("repayAmount")
    val repayAmount: Int,
    @SerializedName("repayAmountTotal")
    val repayAmountTotal: Int,
    @SerializedName("userName")
    val userName: String
)