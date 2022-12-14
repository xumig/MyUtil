package com.indonesia.tunaifince.kt.http.model


open class BaseResult<T>(
    val message: String,
    val status: String,
    val timestamp: String,
    val body: T,
    val requestUrl: String
)

enum class VerifyType {
    ALL, BANKCARD, CONTACTS, OCR, FACE
}