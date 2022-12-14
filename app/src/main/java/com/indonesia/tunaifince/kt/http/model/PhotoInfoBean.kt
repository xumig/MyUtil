package com.indonesia.tunaifince.kt.http.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

class PhotoInfoBean : Serializable {
    @SerializedName("longitudeG")
    var longitudeG:String = ""
    @SerializedName("latitudeG")
    var latitudeG: String = ""
    @SerializedName("model")
    var model: String = ""
    @SerializedName("date")
    var date: String = ""
    @SerializedName("name")
    var name: String = ""
    @SerializedName("make")
    var make: String = ""
    @SerializedName("width")
    var width: Int =0
    @SerializedName("height")
    var height: Int = 0
}