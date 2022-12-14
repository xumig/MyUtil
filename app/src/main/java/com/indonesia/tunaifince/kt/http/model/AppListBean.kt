package com.indonesia.tunaifince.kt.http.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

class AppListBean : Serializable {
    @SerializedName("app_name")
    var app_name: String = ""

    @SerializedName("app_type")
    var app_type: Int = 0

    @SerializedName("package_name")
    var package_name: String = ""

    @SerializedName("version_name")
    var version_name: String = ""

    @SerializedName("flags")
    var flags: Int = 0

    @SerializedName("version_code")
    var version_code: Int = 0

    @SerializedName("in_time")
    var in_time: Long = 0L

    @SerializedName("up_time")
    var up_time: Long = 0L
}