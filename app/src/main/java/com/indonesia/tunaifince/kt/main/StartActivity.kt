package com.indonesia.tunaifince.kt.main

import androidx.lifecycle.lifecycleScope
import com.indonesia.tunaifince.kt.base.BaseActivity
import com.indonesia.tunaifince.kt.baseConfig.HttpConfiguration
import com.indonesia.tunaifince.kt.databinding.ActivityLoginBinding
import com.indonesia.tunaifince.kt.databinding.ActivityStartBinding
import com.indonesia.tunaifince.kt.topFun.loge
import com.indonesia.tunaifince.kt.utils.MMkvCache
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import java.util.concurrent.Flow

class StartActivity : BaseActivity<ActivityStartBinding>(ActivityStartBinding::inflate) {


    override fun initView() {

        rnd.ipRequeat({
            HttpConfiguration.UI_SHOW_URL = it
            start()
        })

    }

    private fun start() {
        lifecycleScope.launch {
            flow {
                delay(1500)
                emit("")
            }.collect() {
                if (MMkvCache.isLogin()) {
                    startActivity<LoginActivity>()

                } else {
                    startActivity<MainActivity>()
                }
                finish()
            }
        }
    }


}