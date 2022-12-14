package com.indonesia.tunaifince.kt.http.model

import com.google.gson.annotations.SerializedName


data class AwardBoby(
    @SerializedName("amountList")
    val amountList: List<String>,
    @SerializedName("availableSize")
    val availableSize: Int =0,   //抽奖次数
    @SerializedName("lotteryAmount")
    val lotteryAmount: Int =0,   //次抽奖需要中的金额
    @SerializedName("totalAmount")
    val totalAmount:String ="",   // 抽奖中的总金额

)

