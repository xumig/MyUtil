package com.indonesia.tunaifince.kt.http.model
import com.google.gson.annotations.SerializedName


data class ConfirmBody(
    @SerializedName("orders")
    val orders: List<Order> = listOf(),
    @SerializedName("userId")
    val userId: String = ""
)

data class Order(
    @SerializedName("applyId")
    val applyId: String = "",
    @SerializedName("message")
    val message: String = "",
    @SerializedName("productCode")
    val productCode: String = "",
    @SerializedName("status")
    val status: Boolean = false
)