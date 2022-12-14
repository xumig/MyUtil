package com.indonesia.tunaifince.kt.http.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable


/**
 * applyInfos	版本号
applyTotalAmount		合计金额
device	是否获取设备信息
product		是否展示产品信息
pageType		A/B产品页
countDownExpire		倒计时(秒)
recommendNum		推荐数量(3,4,5)

productCode		产品编码
productName		产品名称
productBrief		产品简介
productIcon		产品ICON
doubleLoan		是否复贷
top		是否置顶
apply		是否可申请
supplement		是否需要补充信息
rebind		是否需要重新绑卡
rebindType	重新绑定类型
thirdChannelCode		绑卡三方渠道code
loanAmount		借款金额
loanCycle		借款周期
due		到期应还
repayDate	还款日期
interest		利息
serviceCharge		服务费
applyNum		申请人数
loanAging		放款时效
lowestInterestRate		最低利率
priority	优先级，从小到达 1>2>3
 */
data class InitBody(
    @SerializedName("applyInfos")
    val applyInfos: List<ApplyInfo> = listOf(),
    @SerializedName("applyTotalAmount")
    val applyTotalAmount: Int = 0,
    @SerializedName("countDownExpire")
    val countDownExpire: Int = 0,
    @SerializedName("device")
    val device: Boolean = false,
    @SerializedName("pageType")
    val pageType: String = "",
    @SerializedName("product")
    val product: Boolean = false,
    @SerializedName("recommendNum")
    val recommendNum: Int = 0,
    @SerializedName("reductioMinAmount")
    val reductioMinAmount: Int = 0,
    @SerializedName("reductioMaxAmount")
    val reductioMaxAmount: Int = 0,
    @SerializedName("promptContent")
    val promptContent: Int = 0,
    @SerializedName("marketingCopyInit")
    val marketingCopyInit: Boolean = false,
)

data class ApplyInfo(
    @SerializedName("apply")
    val apply: Boolean = false,
    @SerializedName("applyNum")
    val applyNum: String = "",
    @SerializedName("doubleLoan")
    val doubleLoan: Boolean = false,
    @SerializedName("due")
    var due: String = "",
    @SerializedName("interest")
    val interest: String = "",
    @SerializedName("loanAging")
    val loanAging: String = "",
    @SerializedName("loanAmount")
    var loanAmount: Int = 0,
    @SerializedName("loanCycle")
    var loanCycle: Int = 0,
    @SerializedName("lowestInterestRate")
    val lowestInterestRate: String = "",
    @SerializedName("priority")
    val priority: Int = 0,
    @SerializedName("productBrief")
    val productBrief: String = "",
    @SerializedName("productCode")
    var productCode: String = "",
    @SerializedName("productIcon")
    var productIcon: String = "",
    @SerializedName("productName")
    var productName: String = "",
    @SerializedName("rebind")
    val rebind: Boolean = false,
    @SerializedName("repayDate")
    var repayDate: String = "",
    @SerializedName("serviceCharge")
    var serviceCharge: String = "",
    @SerializedName("supplement")
    val supplement: Boolean = false,
    @SerializedName("thirdChannelCode")
    val thirdChannelCode: String = "",
    @SerializedName("top")
    val top: Boolean = false,

    var isSelected: Boolean = false
) : Serializable

data class LimitSelect(
    val applyInfos: ArrayList<ApplyInfo> = arrayListOf(),
    val applyAmount: String = "",
    var isSelected: Boolean = false
)