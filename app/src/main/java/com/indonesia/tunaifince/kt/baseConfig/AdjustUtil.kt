package com.indonesia.tunaifince.kt.baseConfig

import android.content.Context
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import com.adjust.sdk.Adjust
import com.adjust.sdk.AdjustEvent
import com.indonesia.tunaifince.kt.base.App


object AdjustUtil {


    /**
     *  获取Adjust 渠道名称 (后面需要使用)
     */
    fun trackerName(): String? {
        val attribution = Adjust.getAttribution()
        return attribution?.trackerName
    }

    /**
     * 获取Adjust ADID (后面需要使用)
     */
    fun adid(): String? {
        return Adjust.getAdid()
    }


    fun getPackageName(): String = App.instance.packageName

    fun getAppVersion(): String {
        val context: Context = App.instance
        val manager: PackageManager = context.packageManager
        return try {
            val info: PackageInfo = manager.getPackageInfo(context.packageName, 0)
            info.versionName
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
            "0.0.0"
        }
    }


    /**
     * 埋点
     */
    fun sendEvent(event: String) {
        if (event.isEmpty()) return
        if (!Adjust.isEnabled()) return
        val adjustEvent = AdjustEvent(event)
        Adjust.trackEvent(adjustEvent)
        Adjust.trackEvent(AdjustEvent(weight(event)))

    }


    private fun weight(event: String?): String {

        var result = ""
        when (event) {
            MyAdjustEvent.loginSendSmsCodeXm -> result =
                MyAdjustEvent.LOGIN_SEND_SMS_CODE_Heavy_XM
            MyAdjustEvent.LOGIN_SEND_SMS_CODE_SUCCESS_XM -> result =
                MyAdjustEvent.LOGIN_SEND_SMS_CODE_SUCCESS_Heavy_XM
            MyAdjustEvent.LOGIN_SEND_SMS_CODE_FAIL_XM -> result =
                MyAdjustEvent.LOGIN_SEND_SMS_CODE_FAIL_Heavy_XM
            MyAdjustEvent.LOGIN_SEND_VOICE_SMS_CODE_XM -> result =
                MyAdjustEvent.LOGIN_SEND_VOICE_SMS_CODE_Heavy_XM
            MyAdjustEvent.LOGIN_SEND_VOICE_SMS_CODE_SUCCESS_XM -> result =
                MyAdjustEvent.LOGIN_SEND_VOICE_SMS_CODE_SUCCESS_Heavy_XM
            MyAdjustEvent.LOGIN_SEND_VOICE_SMS_CODE_FAIL_XM -> result =
                MyAdjustEvent.LOGIN_SEND_VOICE_SMS_CODE_FAIL_Heavy_XM
            MyAdjustEvent.LOGIN_LOGIN_BTN_XM -> result = MyAdjustEvent.LOGIN_LOGIN_BTN_Heavy_XM
            MyAdjustEvent.LOGIN_LOGIN_SUCCESS_XM -> result =
                MyAdjustEvent.LOGIN_LOGIN_SUCCESS_Heavy_XM
            MyAdjustEvent.LOGIN_LOGIN_FAIL_XM -> result = MyAdjustEvent.LOGIN_LOGIN_FAIL_Heavy_XM
            MyAdjustEvent.IDENTITY_CHECK_FINISH_XM -> result =
                MyAdjustEvent.IDENTITY_CHECK_FINISH_Heavy_XM
            MyAdjustEvent.LOGIN_FACE_RECOGNITION_XM -> result =
                MyAdjustEvent.LOGIN_FACE_RECOGNITION_Heavy_XM
            MyAdjustEvent.LOGIN_GET_LIVENESS_CERTIFICATE_SUCCESS_XM -> result =
                MyAdjustEvent.LOGIN_GET_LIVENESS_CERTIFICATE_SUCCESS_Heavy_XM
            MyAdjustEvent.LOGIN_GET_LIVENESS_CERTIFICATE_FAIL_XM -> result =
                MyAdjustEvent.LOGIN_GET_LIVENESS_CERTIFICATE_FAIL_Heavy_XM
            MyAdjustEvent.LOGIN_OPEN_LIVENESS_DETECTION_XM -> result =
                MyAdjustEvent.LOGIN_OPEN_LIVENESS_DETECTION_Heavy_XM
            MyAdjustEvent.LOGIN_LIVENESS_DETECTION_SUCCESS_XM -> result =
                MyAdjustEvent.LOGIN_LIVENESS_DETECTION_SUCCESS_Heavy_XM
            MyAdjustEvent.LOGIN_LIVENESS_DETECTION_FAIL_XM -> result =
                MyAdjustEvent.LOGIN_LIVENESS_DETECTION_FAIL_Heavy_XM
            MyAdjustEvent.LOGIN_GET_LIVENESS_RESULT_SUCCESS_XM -> result =
                MyAdjustEvent.LOGIN_GET_LIVENESS_RESULT_SUCCESS_Heavy_XM
            MyAdjustEvent.LOGIN_GET_LIVENESS_RESULT_FAIL_XM -> result =
                MyAdjustEvent.LOGIN_GET_LIVENESS_RESULT_FAIL_Heavy_XM
            MyAdjustEvent.LOGIN_OCR_AUTHENTICATION_XM -> result =
                MyAdjustEvent.LOGIN_OCR_AUTHENTICATION_Heavy_XM
            MyAdjustEvent.LOGIN_OCR_AUTHENTICATION_SUCCESS_XM -> result =
                MyAdjustEvent.LOGIN_OCR_AUTHENTICATION_SUCCESS_Heavy_XM
            MyAdjustEvent.LOGIN_OCR_AUTHENTICATION_FAIL_XM -> result =
                MyAdjustEvent.LOGIN_OCR_AUTHENTICATION_FAIL_Heavy_XM
            MyAdjustEvent.LOGIN_OCR_AUTHENTICATION_SUBMIT_XM -> result =
                MyAdjustEvent.LOGIN_OCR_AUTHENTICATION_SUBMIT_Heavy_XM
            MyAdjustEvent.LOGIN_OCR_AUTHENTICATION_SUBMIT_SUCCESS_XM -> result =
                MyAdjustEvent.LOGIN_OCR_AUTHENTICATION_SUBMIT_SUCCESS_Heavy_XM
            MyAdjustEvent.LOGIN_OCR_AUTHENTICATION_SUBMIT_FAIL_XM -> result =
                MyAdjustEvent.LOGIN_OCR_AUTHENTICATION_SUBMIT_FAIL_Heavy_XM
            MyAdjustEvent.LOGIN_BASIC_INFO_AUTHENTICATION_XM -> result =
                MyAdjustEvent.LOGIN_BASIC_INFO_AUTHENTICATION_Heavy_XM
            MyAdjustEvent.LOGIN_BASIC_INFO_AUTHENTICATION_SUBMIT_XM -> result =
                MyAdjustEvent.LOGIN_BASIC_INFO_AUTHENTICATION_SUBMIT_Heavy_XM
            MyAdjustEvent.LOGIN_BASIC_INFO_AUTHENTICATION_SUCCESS_XM -> result =
                MyAdjustEvent.LOGIN_BASIC_INFO_AUTHENTICATION_SUCCESS_Heavy_XM
            MyAdjustEvent.LOGIN_BASIC_INFO_AUTHENTICATION_FAIL_XM -> result =
                MyAdjustEvent.LOGIN_BASIC_INFO_AUTHENTICATION_FAIL_Heavy_XM
            MyAdjustEvent.LOGIN_BANK_CARD_AUTHENTICATION_XM -> result =
                MyAdjustEvent.LOGIN_BANK_CARD_AUTHENTICATION_Heavy_XM
            MyAdjustEvent.LOGIN_BANK_CARD_AUTHENTICATION_INPUT_BANK_CARD_NUMBER_XM -> result =
                MyAdjustEvent.LOGIN_BANK_CARD_AUTHENTICATION_INPUT_BANK_CARD_NUMBER_Heavy_XM
            MyAdjustEvent.LOGIN_BANK_CARD_AUTHENTICATION_INPUT_BANK_CARD_NUMBER_AGAIN_XM -> result =
                MyAdjustEvent.LOGIN_BANK_CARD_AUTHENTICATION_INPUT_BANK_CARD_NUMBER_AGAIN_Heavy_XM
            MyAdjustEvent.LOGIN_BANK_CARD_AUTHENTICATION_SUBMIT_XM -> result =
                MyAdjustEvent.LOGIN_BANK_CARD_AUTHENTICATION_SUBMIT_Heavy_XM
            MyAdjustEvent.LOGIN_BANK_CARD_AUTHENTICATION_SUCCESS_XM -> result =
                MyAdjustEvent.LOGIN_BANK_CARD_AUTHENTICATION_SUCCESS_Heavy_XM
            MyAdjustEvent.LOGIN_BANK_CARD_AUTHENTICATION_FAIL_XM -> result =
                MyAdjustEvent.LOGIN_BANK_CARD_AUTHENTICATION_FAIL_Heavy_XM
            MyAdjustEvent.HOME_CLICK_APPLY_XM -> result = MyAdjustEvent.HOME_CLICK_APPLY_Heavy_XM
            MyAdjustEvent.CLICK_SERVICE_XM -> result = MyAdjustEvent.CLICK_SERVICE_Heavy_XM
            MyAdjustEvent.CONFIRM_LOGOUT_XM -> result = MyAdjustEvent.CONFIRM_LOGOUT_Heavy_XM
            MyAdjustEvent.LOGIN_ORDER_LIST_XM -> result = MyAdjustEvent.LOGIN_ORDER_LIST_Heavy_XM
            MyAdjustEvent.ORDER_DETAIL_CLICK_REPAY_XM -> result =
                MyAdjustEvent.ORDER_DETAIL_CLICK_REPAY_Heavy_XM
            MyAdjustEvent.LOGIN_PRODUCT_LIST_XM -> result =
                MyAdjustEvent.LOGIN_PRODUCT_LIST_Heavy_XM
            MyAdjustEvent.LOGIN_ONE_KEY_MULTI_APPLY_XM -> result =
                MyAdjustEvent.LOGIN_ONE_KEY_MULTI_APPLY_Heavy_XM
            MyAdjustEvent.LOGIN_PRODUCT_INFO_CONFIRM_XM -> result =
                MyAdjustEvent.LOGIN_PRODUCT_INFO_CONFIRM_Heavy_XM
            MyAdjustEvent.LOGIN_PRODUCT_LIST_APPLY_XM -> result =
                MyAdjustEvent.LOGIN_PRODUCT_LIST_APPLY_Heavy_XM
            MyAdjustEvent.LOGIN_ONE_KEY_APPLY_XM -> result =
                MyAdjustEvent.LOGIN_ONE_KEY_APPLY_Heavy_XM
            MyAdjustEvent.LOGIN_PRODUCT_INFO_CONFIRM_APPLY_XM -> result =
                MyAdjustEvent.LOGIN_PRODUCT_INFO_CONFIRM_APPLY_Heavy_XM
            MyAdjustEvent.CASH_BENEFITS_BOX_DISPLAYS_XM -> result =
                MyAdjustEvent.CASH_BENEFITS_POP_UP_DISPLAY_Heavy_XM
            MyAdjustEvent.INTEREST_RELIEF_BOX_DISPLAY_XM -> result =
                MyAdjustEvent.INTEREST_RELIEF_BOX_DISPLAY_Heavy_XM
            MyAdjustEvent.CASH_BENEFIT_BOX_CONFIRMATION_BUTTON_XM -> result =
                MyAdjustEvent.CASH_BENEFIT_BOX_CONFIRMATION_BUTTON_Heavy_XM
            MyAdjustEvent.INTEREST_RELIEF_BOX_CONFIRMATION_BUTTON_XM -> result =
                MyAdjustEvent.INTEREST_RELIEF_BOX_CONFIRMATION_BUTTON_Heavy_XM
            MyAdjustEvent.openTheWheelPageButton_XM -> result =
                MyAdjustEvent.openTheWheelPageButton_Heavy_XM
            MyAdjustEvent.clickTheTurnWheelButton_XM -> result =
                MyAdjustEvent.clickTheTurnWheelButton_Heavy_XM
            MyAdjustEvent.repaymentPopUpDisplay_XM -> result =
                MyAdjustEvent.repaymentPopUpDisplay_Heavy_XM
            MyAdjustEvent.rouletteFrameDisplay_XM -> result =
                MyAdjustEvent.rouletteFrameDisplay_Heavy_XM
            MyAdjustEvent.repaymentPopUpDisplayClickToRepay_XM -> result =
                MyAdjustEvent.repaymentPopUpDisplayClickToRepay_Heavy_XM
            MyAdjustEvent.clickToEnterTheRouletteWheel_XM -> result =
                MyAdjustEvent.clickToEnterTheRouletteWheel_Heavy_XM
            MyAdjustEvent.PERSONAL_CENTER_CLICK_TO_GO_TO_THE_ROULETTE_WHEEL_XM -> result =
                MyAdjustEvent.PERSONAL_CENTER_CLICK_TO_GO_TO_THE_ROULETTE_WHEEL_Heavy_XM

        }
        return result
    }


}
