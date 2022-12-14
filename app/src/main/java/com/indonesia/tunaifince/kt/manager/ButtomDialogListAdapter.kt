package com.indonesia.tunaifince.kt.manager


import android.app.Activity
import androidx.viewbinding.ViewBinding
import com.indonesia.tunaifince.kt.R
import com.indonesia.tunaifince.kt.base.BaseAdapter
import com.indonesia.tunaifince.kt.databinding.AdapterDiaButtomBinding
import com.indonesia.tunaifince.kt.http.model.LoanOrderRecord
import com.indonesia.tunaifince.kt.topFun.onClick


class ButtomDialogListAdapter(context: Activity, listDatas: ArrayList<ButtomListDialogBask>) :
    BaseAdapter<AdapterDiaButtomBinding, ButtomListDialogBask>(AdapterDiaButtomBinding::inflate, context, listDatas) {

    override fun convert(v: AdapterDiaButtomBinding, t: ButtomListDialogBask, position: Int) {
        v.contentTV.apply {
            text =t.Value
            onClick {
                //选中
               // setBackgroundResource(R.mipmap.dia_buttom_on_back)
                click?.invoke(t)
            }
        }
    }

    var click: ((ButtomListDialogBask) -> Unit?)? = null
    fun setOnClick(click: ((ButtomListDialogBask) -> Unit?)?) {
        this.click = click
    }

}
