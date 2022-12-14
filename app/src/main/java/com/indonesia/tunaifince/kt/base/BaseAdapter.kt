package com.indonesia.tunaifince.kt.base

import android.app.Activity
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding

abstract class BaseAdapter<VB : ViewBinding, T>(
    private val mInflate: (LayoutInflater, ViewGroup?, Boolean) -> VB,
    var mContext: Activity,
    open var listDatas: ArrayList<T> = ArrayList()
) : RecyclerView.Adapter<BaseViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        return BaseViewHolder(mInflate(LayoutInflater.from(mContext),parent,false))
    }

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        holder.itemView.setOnClickListener {
            itemClick?.invoke(position, listDatas[position])
            itemClickView?.invoke(holder.v as VB, listDatas[position])
        }

        convert(holder.v as VB, listDatas[position], position)
    }

    abstract fun convert(v: VB, t: T, position: Int)

    override fun getItemCount(): Int {
        return listDatas.size
    }

    private var itemClick: ((Int, T) -> Unit)? = null

    fun itemClick(itemClick: (Int, T) -> Unit) {
        this.itemClick = itemClick
    }

    private var itemClickView: ((VB, T) -> Unit)? = null

    fun itemClickView(itemClickView: (VB, T) -> Unit) {
        this.itemClickView = itemClickView
    }

}
