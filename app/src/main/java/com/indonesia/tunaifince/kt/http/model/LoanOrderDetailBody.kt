package com.indonesia.tunaifince.kt.http.model
import com.google.gson.annotations.SerializedName


data class LoanOrderDetailBody(
    @SerializedName("applyAmount")
    val applyAmount: Int = 0,
    @SerializedName("applyPeriod")
    val applyPeriod: Int = 0,
    @SerializedName("applyStatus")
    val applyStatus: String = "",
    @SerializedName("applyTime")
    val applyTime: String = "",
    @SerializedName("bankCode")
    val bankCode: String = "",
    @SerializedName("bankNum")
    val bankNum: String = "",
    @SerializedName("estimateApplyTime")
    val estimateApplyTime: String = "",
    @SerializedName("estimateLoanTime")
    val estimateLoanTime: String = "",
    @SerializedName("estimateRepayTime")
    val estimateRepayTime: String = "",
    @SerializedName("extension")
    val extension: Boolean = false,
    @SerializedName("icon")
    val icon: String = "",
    @SerializedName("interestAmount")
    val interestAmount: Int = 0,
    @SerializedName("loanTime")
    val loanTime: String = "",
    @SerializedName("overdueAmount")
    val overdueAmount: Int = 0,
    @SerializedName("overdueDay")
    val overdueDay: Int = 0,
    @SerializedName("productCode")
    val productCode: String = "",
    @SerializedName("productName")
    val productName: String = "",
    @SerializedName("repayDate")
    val repayDate: String = "",
    @SerializedName("repayDay")
    val repayDay: Int = 0,
    @SerializedName("repayPaid")
    val repayPaid: Int = 0,
    @SerializedName("repayPlanId")
    val repayPlanId: Int = 0,
    @SerializedName("repayStatus")
    val repayStatus: String = "",
    @SerializedName("riskTime")
    val riskTime: String = "",
    @SerializedName("serviceAmount")
    val serviceAmount: Int = 0,
    @SerializedName("unpaid")
    val unpaid: Int = 0
)