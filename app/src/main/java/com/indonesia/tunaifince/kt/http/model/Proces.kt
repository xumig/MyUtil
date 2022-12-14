package com.indonesia.tunaifince.kt.http.model

import com.google.gson.annotations.SerializedName

data class Proces(
    @SerializedName("processData")
    val processData: List<ProcessData> = listOf(),
    @SerializedName("repayMethod")
    val repayMethod: String = "",
    @SerializedName("repayCode")
    val repayCode: String = "",
    @SerializedName("repayType")
    val repayType: String = ""
)
data class ProcessData(
    @SerializedName("process_code")
    val processCode: Int = 0,
    @SerializedName("process_desc")
    val processDesc: String = ""
)