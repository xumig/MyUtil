package com.indonesia.tunaifince.kt.topFun

import android.view.View
import android.view.animation.Animation
import android.view.animation.TranslateAnimation
import com.tencent.mmkv.MMKV


var clickTime: Long = 0

/**
 * 防多次点击
 */
fun View.onClick(clickAction: () -> Unit) {
    this.setOnClickListener {
        if (System.currentTimeMillis() - clickTime >= 500) {
            clickTime = System.currentTimeMillis()
            clickAction()
        }

    }
}

var mTime: Long = 0

//true 时间外
fun intervalTime(time: Int): Boolean {
    if (System.currentTimeMillis() - mTime >= time) {
        mTime = System.currentTimeMillis()
        return true
    }
    return false
}

// 动画 duration:持续时间 repeatCount:重复次数
fun View.shake(duration: Long = 20, repeatCount: Int = 10) {
    val translateAnimation = TranslateAnimation(-10f, 10f, -10f, 10f)
    translateAnimation.duration = duration //每次时间
    translateAnimation.repeatCount = repeatCount //重复次数
    translateAnimation.repeatMode = Animation.REVERSE
    this.startAnimation(translateAnimation)

}


//fun showToast(context: Context?, message: String?) {
//    if (!TextUtils.isEmpty(message)) Toast.makeText(context, message, Toast.LENGTH_SHORT)
//        .show()
//}
//
//fun showToast(message: String?) {
//    if (!TextUtils.isEmpty(message)) Toast.makeText(App.instance, message, Toast.LENGTH_SHORT)
//        .show()
//}


//mmkv
fun MMKV.setV(key: String, v: String) {
    this.encode(key, v)
}

fun MMKV.setV(key: String, v: Int) {
    this.encode(key, v)
}

fun MMKV.setV(key: String, v: Boolean) {
    this.encode(key, v)
}

//mmkv
fun MMKV.getString(key: String) {
    this.decodeString("", "")
}

//mmkv
fun MMKV.getInt(key: String) {
    this.decodeInt("")
}

//mmkv
fun MMKV.getBool(key: String, defaultValue: Boolean = false) {
    this.decodeBool("", defaultValue)
}


/**
 * • setOutSideDismiss：是否允许点击PopupWindow外部时触发dismiss，默为True
• 如果设置为False，则点击蒙层不会触发dismiss

showPopupWindow(taskFragmentViewPager)
setOverlayStatusbarMode(OVERLAY_MASK)
 */

//fun dialog(context: Context) {
//    QuickPopupBuilder.with(context)
//        .contentView(R.layout.filter_popupwindow_layout)
//        .config(
//            QuickPopupConfig()
//                .gravity(Gravity.RIGHT or Gravity.CENTER_VERTICAL)
//                .withClick(R.id.fp_reset_but, View.OnClickListener {
//                    Toast.makeText(context, "clicked", Toast.LENGTH_LONG).show()
//                })
//        )
//        .show()
//}


//
//fun isFastClick(): Boolean {
//    val time = System.currentTimeMillis()
//    val timeD = time - lastClickTime
//    return if (timeD >= 500) { //点击间隔时间
//        lastClickTime = time
//        true
//    } else {
//        false
//    }
//}

