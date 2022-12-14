package com.indonesia.tunaifince.kt.main.fragment

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.indonesia.tunaifince.kt.base.BaseViewModel
import com.indonesia.tunaifince.kt.baseConfig.HttpConfiguration.MAIN_KEY
import com.indonesia.tunaifince.kt.http.model.HomeBody
import com.indonesia.tunaifince.kt.http.model.LoanOrderRecordBody
import com.indonesia.tunaifince.kt.http.model.OneBody
import com.indonesia.tunaifince.kt.http.model.QueryCashUserBody
import com.indonesia.tunaifince.kt.topFun.loge

class MainFragmentVM : BaseViewModel() {


    private var queryCashUserLD = MutableLiveData<QueryCashUserBody>()
    val mUserLD: LiveData<QueryCashUserBody> = queryCashUserLD
    fun queryCashUser() {
        rnd.rnd({
            queryCashUser()
        }, {
            queryCashUserLD.postValue(it)
        }, {}, false)
    }


}

