package com.indonesia.tunaifince.kt.http.model
import com.google.gson.annotations.SerializedName


data class ConfigBody(
    @SerializedName("configKey")
    val configKey: String = "",
    @SerializedName("configRemark")
    val configRemark: String = "",
    @SerializedName("configType")
    val configType: String = "",
    @SerializedName("configValue")
    val configValue: String = ""
)