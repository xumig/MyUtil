package com.indonesia.tunaifince.kt.utils

import android.Manifest
import android.content.pm.PackageManager

import androidx.fragment.app.FragmentActivity
import com.indonesia.tunaifince.kt.topFun.loge
import com.permissionx.guolindev.PermissionX
import com.permissionx.guolindev.PermissionX.isGranted

object PermissionXM {

    //todo 权限更新
    private val permissionList = arrayOf(

        Manifest.permission.READ_CONTACTS,
        Manifest.permission.READ_EXTERNAL_STORAGE,
        Manifest.permission.WRITE_EXTERNAL_STORAGE,
        Manifest.permission.ACCESS_NETWORK_STATE,
        Manifest.permission.ACCESS_COARSE_LOCATION,
        Manifest.permission.ACCESS_WIFI_STATE,
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.READ_PHONE_STATE,
        Manifest.permission.BLUETOOTH
    ).toList()


    /**
     * @param allGranted true 所有申请的权限都已通过
     */
    fun requestPerission(activity: FragmentActivity, granted: ((Boolean) -> Unit)) {
        PermissionX.init(activity)
            .permissions(permissionList)
            .request { allGranted, grantedList, deniedList ->
                "${allGranted}".loge("allGranted")
                "${grantedList}".loge("grantedList")
                "${deniedList}".loge("deniedList")

                if (!allGranted) {

                }
                granted.invoke(allGranted)
            }
    }

    /**
     * 多权限检查方法
     * @return 所有请求都通过，true
     */
    fun checkPermission(activity: FragmentActivity): Boolean {
        for (per in permissionList) {
            if (!isGranted(activity, per)) {
                return false
            }
        }
        return true
    }

    /**
     * CAMERA权限检查方法
     *
     */
    fun checkCAMERA(activity: FragmentActivity, granted: ((Boolean) -> Unit)) {

        if (activity.checkSelfPermission(
                Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            granted.invoke(true)
        } else {
            PermissionX.init(activity)
                .permissions(Manifest.permission.CAMERA)
                .request { allGranted, grantedList, deniedList ->
                    granted.invoke(allGranted)
                }
        }
    }


    /**
     * CONTACTS权限检查方法
     *
     */
    fun checkCONTACTS(activity: FragmentActivity, granted: ((Boolean) -> Unit)) {

        if (activity.checkSelfPermission(
                Manifest.permission.READ_CONTACTS
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            granted.invoke(true)
        } else {
            PermissionX.init(activity)
                .permissions(Manifest.permission.CAMERA)
                .request { allGranted, grantedList, deniedList ->
                    granted.invoke(allGranted)
                }
        }
    }




}