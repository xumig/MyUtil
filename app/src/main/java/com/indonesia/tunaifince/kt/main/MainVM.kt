package com.indonesia.tunaifince.kt.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.indonesia.tunaifince.kt.base.BaseViewModel
import com.indonesia.tunaifince.kt.http.model.OneBody

class MainVM : BaseViewModel() {

    private var gpsConfigLD = MutableLiveData<OneBody>()
    val mgpsConfigLD: LiveData<OneBody> = gpsConfigLD

    private var reAdjustLD = MutableLiveData<Boolean>()
    val mreAdjustLD: LiveData<Boolean> = reAdjustLD

    private var registryTokenInfoLD = MutableLiveData<Boolean>()
    val mregistryTokenInfoLD: LiveData<Boolean> = registryTokenInfoLD


    fun registryTokenInfo(key: String) {
        rnd.rnd({
            registryTokenInfo(
                rnd.post(
                    hashMapOf("registryToken" to key)
                )
            )
        }, {
            registryTokenInfoLD.postValue(it)
        }, {}, false)
    }


    fun getGpsConfigcdp() {
        rnd.rnd({
            getHomeConfig(
                rnd.get(hashMapOf("configKey" to "location_check_gps"))
            )
        }, {
            gpsConfigLD.postValue(it)
        }, {}, false)
    }


}