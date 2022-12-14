package com.indonesia.tunaifince.kt.http.model

import com.google.gson.annotations.SerializedName


data class QueryAppUserQuestionBody(
    @SerializedName("answer")
    val answer: String,
    @SerializedName("answerTime")
    val answerTime: String,
    @SerializedName("question")
    val question: String,
    @SerializedName("questionSubject")
    val questionSubject: String,
    @SerializedName("questionTime")
    val questionTime: String,
    @SerializedName("questionType")
    val questionType: String,
    @SerializedName("status")
    val status: String
)