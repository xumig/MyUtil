package com.indonesia.tunaifince.kt.main

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.net.Uri
import android.util.Log
import android.view.MotionEvent
import androidx.core.app.ActivityCompat
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.findNavController
import com.adjust.sdk.Adjust
import com.google.android.gms.tasks.Task
import com.google.firebase.messaging.FirebaseMessaging
import com.indonesia.tunaifince.kt.R
import com.indonesia.tunaifince.kt.base.App
import com.indonesia.tunaifince.kt.base.BaseActivity
import com.indonesia.tunaifince.kt.baseConfig.AdjustUtil
import com.indonesia.tunaifince.kt.databinding.ActivityMainBinding
import com.indonesia.tunaifince.kt.http.model.OneBody
import com.indonesia.tunaifince.kt.topFun.loge
import com.indonesia.tunaifince.kt.topFun.onClick
import com.indonesia.tunaifince.kt.topFun.toast
import com.indonesia.tunaifince.kt.utils.GPRSUtilcdp
import com.indonesia.tunaifince.kt.utils.GPSUtilcdp
import com.indonesia.tunaifince.kt.utils.MMKVCache
import com.indonesia.tunaifince.kt.utils.PermissionXM
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.lang.Exception

class MainActivity : BaseActivity<ActivityMainBinding>(ActivityMainBinding::inflate) {

    private lateinit var navController: NavController
    private val vm by getViewModel<MainVM>()

    override fun initView() {
        initButtom()
        applyPermission()
    }

    override fun initData() {
        super.initData()
        initSdk()
    }

    override fun onResume() {
        super.onResume()
        //   initCheckLocation()
    }

    override fun initViewMode() {
        super.initViewMode()


        vm.mgpsConfigLD.observe(this) {
            mgpsConfigLD(it)
        }
    }


    private fun initSdk() {
        FirebaseMessaging.getInstance().token.addOnCompleteListener { task: Task<String> ->
            "${task.isSuccessful}".loge("FirebaseMessaging")
            if (!task.isSuccessful) {
                return@addOnCompleteListener
            }
            "${task.result}".loge("FirebaseMessaging")
            if (task.result.isNullOrBlank().not()) {
                vm.registryTokenInfo(task.result)
            }

        }
        // GoogleApiAvailability.getInstance().makeGooglePlayServicesAvailable(this)
    }



    private fun mgpsConfigLD(it: OneBody) {
        if (latitude != 0.0 && longitude != 0.0) {
            if ("location_check_gps" == it.configKey) {
                for (item in it.extInfoJSON.location) {
                    val v: Double = GPSUtilcdp.gps2m(
                        latitude,
                        longitude,
                        item.lat.toDouble(),
                        item.lng.toDouble()
                    )
                    if (item.lat.toDouble() != 0.0 && item.lng.toDouble() != 0.0
                    ) {
                        if (v <= item.r.toInt()) {
                            "在目标区域内".loge("")

                            return
                        } else {
                            "不在范围".loge("")
                        }
                    }
                }
            }
        }
    }


    override fun onDestroy() {
        super.onDestroy()
        GPRSUtilcdp.getInstance(this@MainActivity).removeListener()
    }

    private var latitude = 0.0
    private var longitude = 0.0

    private fun initCheckLocation() {

        if (MMKVCache.isLogin()) {
            if (ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
            ) if (ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                return
            }
            GPRSUtilcdp.getInstance(this@MainActivity)
                .getLngAndLat(object : GPRSUtilcdp.OnLocationResultListener {
                    override fun onLocationResult(location: Location?) {
                        Log.e("GPS", "onLocationResult")
                        addGps(location)
                    }

                    override fun OnLocationChange(location: Location?) {
                        Log.e("GPS", "OnLocationChange")
                        addGps(location)
                    }
                })
        }
    }

    private fun addGps(location: Location?) {
        try {
            if (location != null) {
                Log.e(
                    "Map",
                    "Location Result : Lat: " + location.latitude + " Lng: " + location.longitude
                )
                if (location.latitude != latitude || longitude != location.longitude) {
                    latitude = location.latitude // 经度
                    longitude = location.longitude // 纬度
                    vm.getGpsConfigcdp()
                }

            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun initButtom() {
        val destinationMap = mapOf(
            R.id.homePageFragment to vb.homePageIconLayout.homePageMotionLayout,
            R.id.accountFragment to vb.accountIconLayout.accountMotionLayout
        )

        navController = findNavController(R.id.fragment)

        destinationMap.forEach { map ->
            map.value.onClick { navController.navigate(map.key) }
        }

        navController.addOnDestinationChangedListener { controller, destination, arguments ->
            controller.popBackStack()
            destinationMap.values.forEach { it.progress = 0.001f }
            destinationMap[destination.id]?.transitionToEnd()
        }
    }

    override fun dispatchTouchEvent(event: MotionEvent): Boolean {
        if (event.action == MotionEvent.ACTION_DOWN
            && !PermissionXM.checkPermission(this)
        ) {
            if (intervalTime(500)) {
                applyPermission()
                return false
            }
        }
        return super.dispatchTouchEvent(event)
    }

    private fun applyPermission() {
        PermissionXM.requestPerission(this) {
            if (!it) {
                dialogM.permissionReminde {
                    startActivity(Intent().apply {
                        addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                        action = "android.settings.APPLICATION_DETAILS_SETTINGS"
                        data = Uri.fromParts("package", packageName, null)
                    })
                }
            } else {

            }
        }
    }

    override fun onBackPressed() {
        if (intervalTime(1500)) {
            "Tekan lagi untuk keluar dari aplikasi".toast()
        } else {
            App.instance.exitApp()
        }
    }


}