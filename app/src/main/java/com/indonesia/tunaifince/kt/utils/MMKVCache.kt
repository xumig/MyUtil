package com.indonesia.tunaifince.kt.utils

import com.indonesia.tunaifince.kt.topFun.boolean
import com.indonesia.tunaifince.kt.topFun.double
import com.indonesia.tunaifince.kt.topFun.string
import com.tencent.mmkv.MMKV


object MMKVCache {
    private const val MMKV_ID = "MMkvCache"
    private val kv = MMKV.mmkvWithID(MMKV_ID)

    var googleAdId by kv.string("googleAdId", "")

    var latitude by kv.double("latitude", 0.00)
    var longitude by kv.double("longitude", 0.00)


    //权限声明
    var permissionToVoice by kv.boolean("PERMISSION_TO_VOICE", true)
    //权限声明
    var isCheckTips by kv.boolean("IS_CHECK_TIPS", true)


    //测试账号
    var misTost by kv.boolean("IS_TOST", false)

    //登录ID
    var uuId by kv.string("UU_ID", "")

    //	用户编号
    var userId by kv.string("USER_ID", "")


    //bankNo
    var bankNo by kv.string("BANK_NO", "")


    //isSynAdjust
    var isSynAdjust by kv.boolean("isSynAdjust", false)


    // device User ids
    var deviceUserIds by kv.boolean(userId + "DEVICE_USER_IDS", false)

    // upload appList User ids
    var uploadAppListUserIds by kv.boolean(userId + "UPLOAD_APP_LIST_USER_IDS", false)

    // Contact User ids
    var contactUserIds by kv.boolean(userId + "CONTACT_USER_IDS", false)


//    fun login(loginUser: LoginUser, phoneNumber: String) {
//        uuId = loginUser.uuId
//        userId = loginUser.userId
//        loginPhone = phoneNumber
//        misTost = loginUser.onTest
//    }

    //true 没有登录
    fun isLogin(): Boolean {
        return userId.isNullOrBlank() && uuId.isNullOrEmpty()
    }

    fun logOut() {
        uuId = ""
        userId = ""
        misTost = false
        bankNo = ""
    }


}