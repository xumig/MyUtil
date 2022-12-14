package com.indonesia.tunaifince.kt.http.model
import com.google.gson.annotations.SerializedName


data class CheckBody(
    @SerializedName("approveStatus")
    val approveStatus: Boolean = false,  //当前包是否审核中
    @SerializedName("onAppList")
    val onAppList: Boolean = false,   //是否已获取APP信息
    @SerializedName("onApprove")
    val onApprove: Boolean = false,    //用户是否审批中
    @SerializedName("onBlackCache")
    val onBlackCache: Boolean = false,   //用户是否黑名单缓存中
    @SerializedName("onContactsList")
    val onContactsList: Boolean = false,
    @SerializedName("onDevice")
    val onDevice: Boolean = false,   //是否已获取设备信息
    @SerializedName("onOrder")
    val onOrder: Boolean = false,  //用户是否借款
    @SerializedName("onQuota")
    val onQuota: Boolean = false,  //新客是否进入额度申请页面
    @SerializedName("onTestAccount")
    val onTestAccount: Boolean = false,   //用户是否为测试账号
    @SerializedName("onUser")
    val onUser: Boolean = false,   //	用户是否在途
    @SerializedName("product")
    val product: Boolean = false,   //是否展示产品信息
    @SerializedName("userId")
    val userId: String = ""       //用户编号
)