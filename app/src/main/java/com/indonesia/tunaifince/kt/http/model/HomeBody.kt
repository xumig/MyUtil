package com.indonesia.tunaifince.kt.http.model;

import com.google.gson.annotations.SerializedName


data class HomeBody(
    @SerializedName("cashBannerResps")
    val cashBannerResps: List<CashBannerResp>,
    @SerializedName("cashConfigs")
    val cashConfigs: List<CashConfig>,
    @SerializedName("cashNoticeInfoResps")
    val cashNoticeInfoResps: List<CashNoticeInfoResp>,
    @SerializedName("cashPopResps")
    val cashPopResps: List<GetPopBody>

)

data class CashBannerResp(
    @SerializedName("bannerName")
    val bannerName: String,
    @SerializedName("bannerType")
    val bannerType: String,
    @SerializedName("bannerUrl")
    val bannerUrl: String
)

data class CashConfig(
    @SerializedName("configKey")
    val configKey: String,
    @SerializedName("configRemark")
    val configRemark: String,
    @SerializedName("configType")
    val configType: String,
    @SerializedName("configValue")
    val configValue: String,
    @SerializedName("open")
    val `open`: Boolean
)

data class CashNoticeInfoResp(
    @SerializedName("extInfo")
    val extInfo: String,
    @SerializedName("noticeType")
    val noticeType: String,
    @SerializedName("noticeValue")
    val noticeValue: String
)



