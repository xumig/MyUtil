package com.indonesia.tunaifince.kt.manager

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.FragmentActivity
import com.indonesia.tunaifince.kt.approve.BankInfoActivity
import com.indonesia.tunaifince.kt.approve.BaseInfoActivity
import com.indonesia.tunaifince.kt.approve.CertificateActivity
import com.indonesia.tunaifince.kt.approve.LivingActivity
import com.indonesia.tunaifince.kt.baseConfig.AdjustUtil
import com.indonesia.tunaifince.kt.baseConfig.HttpConfiguration
import com.indonesia.tunaifince.kt.http.HTTPRequest
import com.indonesia.tunaifince.kt.http.model.VerifyType
import com.indonesia.tunaifince.kt.main.SedangDitinjauActivity
import com.indonesia.tunaifince.kt.placeAnOrder.ApplyActivity
import com.indonesia.tunaifince.kt.placeAnOrder.ProductListActivity

class VerifyJump(private val context: FragmentActivity, private val rnd: HTTPRequest) {


    fun verifyJump(isJumpH5: Boolean = false, isFinish: Boolean = false) {
        when (UserVerifyManager.getVerifyState) {
            VerifyType.FACE ->                 //跳转活体认证
                context.startActivity(Intent(context, LivingActivity::class.java))
            VerifyType.OCR ->                 //跳转身份证认证
                context.startActivity(Intent(context, CertificateActivity::class.java))
            VerifyType.CONTACTS ->                 //跳转活联系人认证
                context.startActivity(Intent(context, BaseInfoActivity::class.java))
            VerifyType.BANKCARD ->                 //跳转银行卡认证
                context.startActivity(Intent(context, BankInfoActivity::class.java))
            VerifyType.ALL -> {
                if (isJumpH5) {
                    check()
                }
            }
        }
        if (isFinish) {
            context.finish()
        }
    }


    private fun check() {
        rnd.rnd({ check() }, {
            if (!it.product) {
                //审核
                context.startActivity(Intent(context, SedangDitinjauActivity::class.java))
                return@rnd
            }
            if (it.onBlackCache) {
                //黑名单
                context.startActivity(Intent(context, ApplyActivity::class.java))
                return@rnd
            }
            if (it.onOrder && it.onUser) {
                //一键多申
                context.startActivity(Intent(context, ApplyActivity::class.java).apply {
                    putExtra("XM_BUNDLE", Bundle().apply {
                        putBoolean("IS_BLACKLIST", true)
                    })
                })
            } else {
                initD()
            }
        }, {
            //黑名单
            context.startActivity(Intent(context, ApplyActivity::class.java))
        })
    }


    private fun initD() {
        rnd.rnd({
            init(rnd.post(hashMapOf("pageNum" to "1", "version" to AdjustUtil.getAppVersion())))
        }, {
            it.applyInfos.forEach {
                if (it.apply) {
                    context.startActivity(Intent(context, ProductListActivity::class.java))
                    return@rnd
                }
            }
            context.startActivity(Intent(context, ApplyActivity::class.java))
        }, {
            //黑名单
            context.startActivity(Intent(context, ApplyActivity::class.java))
        })
    }


}