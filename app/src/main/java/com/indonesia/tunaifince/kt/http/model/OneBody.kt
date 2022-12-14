package com.indonesia.tunaifince.kt.http.model

import com.google.gson.annotations.SerializedName


data class OneBody(
    @SerializedName("configKey")
    val configKey: String = "",
    @SerializedName("configRemark")
    val configRemark: String = "",
    @SerializedName("configType")
    val configType: String = "",
    @SerializedName("configValue")
    val configValue: String = "",
    @SerializedName("extInfoJSON")
    val extInfoJSON: ExtInfoJSON = ExtInfoJSON(),
//    @SerializedName("open")
//    val `open`: Boolean = false
)

data class ExtInfoJSON(
    @SerializedName("location")
    val location: List<Location> = listOf(),
    @SerializedName("homeList")
    val homeList: List<HomeList> = listOf()
)

data class Location(
    @SerializedName("r")
    val r: String = "",
    @SerializedName("lng")
    val lng: String = "",
    @SerializedName("lat")
    val lat: String = ""
)

data class HomeList(
    @SerializedName("tenor")
    val tenor: String = "",
    @SerializedName("money")
    val money: String = "",
    @SerializedName("icon")
    val icon: String = "",
    @SerializedName("name")
    val name: String = "",
    @SerializedName("bunga")
    val bunga: String = ""
)



