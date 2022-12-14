package com.indonesia.tunaifince.kt.main.fragment


import android.app.Activity
import androidx.viewbinding.ViewBinding
import coil.load
import com.indonesia.tunaifince.kt.base.BaseAdapter
import com.indonesia.tunaifince.kt.databinding.HomeAdapterBinding
import com.indonesia.tunaifince.kt.http.model.HomeList
import com.indonesia.tunaifince.kt.http.model.QueryAppUserQuestionBody


class HomeListAdapter(context: Activity, listDatas: ArrayList<HomeList>) :
    BaseAdapter<HomeAdapterBinding, HomeList>(HomeAdapterBinding::inflate, context, listDatas) {

    override fun convert(v: HomeAdapterBinding, t: HomeList, position: Int) {

        v.iconIMG.load(t.icon)
        v.nameTV.text = t.name
        v.tenorTv.text = t.tenor

    }

}
