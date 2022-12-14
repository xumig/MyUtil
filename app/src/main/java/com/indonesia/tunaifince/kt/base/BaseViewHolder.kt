package com.indonesia.tunaifince.kt.base

import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding


open class BaseViewHolder(itemView: ViewBinding) : RecyclerView.ViewHolder(itemView.root) {

    val v = itemView
}
