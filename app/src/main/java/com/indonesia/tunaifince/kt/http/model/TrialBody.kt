package com.indonesia.tunaifince.kt.http.model
import com.google.gson.annotations.SerializedName


data class TrialBody(
    @SerializedName("extServiceAmount")
    val extServiceAmount: String = "",
    @SerializedName("extensionAmount")
    val extensionAmount: String = "",
    @SerializedName("extensionPlanAmount")
    val extensionPlanAmount: String = "",
    @SerializedName("extensionTerm")
    val extensionTerm: String = "",
    @SerializedName("extensionTime")
    val extensionTime: String = "",
    @SerializedName("overdueAmount")
    val overdueAmount: String = "",
    @SerializedName("serviceAmount")
    val serviceAmount: String = ""
)