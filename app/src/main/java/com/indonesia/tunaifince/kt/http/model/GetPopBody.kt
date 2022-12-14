package com.indonesia.tunaifince.kt.http.model
import com.google.gson.annotations.SerializedName


data class GetPopBody(
    @SerializedName("isClick")
    val isClick: Int = 0,
    @SerializedName("popId")
    val popId: String = "",
    @SerializedName("popJson")
    val popJson: String = "",
    @SerializedName("popName")
    val popName: String = "",
    @SerializedName("popText")
    val popText: String = "",
    @SerializedName("popTitle")
    val popTitle: String = "",
    @SerializedName("popType")
    val popType: String = ""
)

data class PopJson(
    @SerializedName("page")
    val page: String = "",
    @SerializedName("amount")
    val amount: String = "",
    @SerializedName("button")
    val button: String = "",
    @SerializedName("buttonOne")
    val buttonOne: String = "",
    @SerializedName("buttonTwo")
    val buttonTwo: String = "",
    @SerializedName("textOne")
    val textOne: String = "",
    @SerializedName("textTwo")
    val textTwo: String = "",
    @SerializedName("textFive")
    val textFive: String = "",
    @SerializedName("textFour")
    val textFour: String = "",
    @SerializedName("textThree")
    val textThree: String = "",
    @SerializedName("max")
    val max: String = "",
    @SerializedName("min")
    val min: String = "",
    @SerializedName("availableSize")
    val availableSize: String = "",
)
