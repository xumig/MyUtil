package com.indonesia.tunaifince.kt.http.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

class ContactBean : Serializable {
    @SerializedName("name")
    var name: String = "unknown"

    @SerializedName("phone")
    var phone: String = ""

    @SerializedName("lastUpdateTime")
    var lastUpdateTime: Long = -1L

    @SerializedName("lastContactTime")
    var lastContactTime: Long = -1L

    @SerializedName("contactTimes")
    var contactTimes: Int = -1
}