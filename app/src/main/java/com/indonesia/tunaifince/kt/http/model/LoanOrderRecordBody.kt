package com.indonesia.tunaifince.kt.http.model

import com.google.gson.annotations.SerializedName


data class LoanOrderRecordBody(
    @SerializedName("loanOrderRecords")
    val loanOrderRecords: List<LoanOrderRecord> = listOf(),
    @SerializedName("overdueCount")
    val overdueCount: Int = 0,   //	逾期数目
    @SerializedName("refuseCount")
    val refuseCount: Int = 0,   // 拒绝数目
    @SerializedName("repayCount")
    val repayCount: Int = 0,       //待还款数目
    @SerializedName("settleCount")
    val settleCount: Int = 0,       //	已结清数目
    @SerializedName("underReviewCount")
    val underReviewCount: Int = 0,      //审核中数目
    @SerializedName("repayAmountText")
    val repayAmountText: Int = 0

)

data class LoanOrderRecord(
    @SerializedName("applyAmount")
    val applyAmount: Int = 0,
    @SerializedName("applyId")
    val applyId: String = "",
    @SerializedName("applyPeriod")
    val applyPeriod: Int = 0,
    @SerializedName("applyStatus")
    val applyStatus: String = "",
    @SerializedName("applyTime")
    val applyTime: String = "",
    @SerializedName("doubleLoan")
    val doubleLoan: Boolean = false,
    @SerializedName("icon")
    val icon: String = "",
    @SerializedName("productCode")
    val productCode: String = "",
    @SerializedName("productName")
    val productName: String = "",
    @SerializedName("repayDate")
    val repayDate: String? = "",
    @SerializedName("extensionTime")
    val extensionTime: String? = ""
)