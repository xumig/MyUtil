package com.indonesia.tunaifince.kt.manager

import com.indonesia.tunaifince.kt.http.model.QueryUserDetailBody
import com.indonesia.tunaifince.kt.http.model.VerifyType
import com.indonesia.tunaifince.kt.utils.MMKVCache

object UserVerifyManager {

    private var currentVerifyState: VerifyType = VerifyType.FACE
    val getVerifyState = currentVerifyState

    private var isUserSilence: Boolean = false //true为静默
    val getUserSilence = isUserSilence

    private var marketingCopy: Boolean = false //为true则显示用户操作引导栏
    val getMarketingCopy = marketingCopy

    fun upVerify(it: QueryUserDetailBody) {
        MMKVCache.bankNo = it.bankNo
        isUserSilence = it.isUseSilence
        marketingCopy = it.marketingCopy

        currentVerifyState = if (it.livenessStatus) {
            if (it.holdIdReslut == "0") {
                if (it.matchResult == "0") {
                    if (it.bindCardStatus == "0") {
                        VerifyType.ALL
                    } else {
                        VerifyType.BANKCARD
                    }
                } else {
                    VerifyType.CONTACTS
                }
            } else {
                VerifyType.OCR
            }
        } else {
            VerifyType.FACE
        }
    }

}