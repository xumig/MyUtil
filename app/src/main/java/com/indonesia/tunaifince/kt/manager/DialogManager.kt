package com.indonesia.tunaifince.kt.manager


import android.app.Activity
import android.widget.TextView
import com.indonesia.tunaifince.kt.R
import com.liys.dialoglib.LAnimationsType
import com.liys.dialoglib.LDialog


class DialogManager(private val c: Activity) {

    /**
     * Loading
     */
    fun loading(): LDialog {
        return LDialog.newInstance(c, R.layout.dia_loading).apply {
            setCancelable(false)
            setAnimations(LAnimationsType.SCALE)
            show()
        }
    }

    /**
     * 权限提示
     */
    fun permissionReminde(okClick: (LDialog) -> Unit) {
        val dialog = LDialog.newInstance(c, R.layout.dia_permission_reminder)
        dialog.setAnimations(LAnimationsType.SCALE)
        dialog.getView<TextView>(R.id.okBut).setOnClickListener {
            okClick.invoke(dialog)
            dialog.dismiss()
        }
        dialog.getView<TextView>(R.id.cancelBut).setOnClickListener {
            dialog.dismiss()
        }
        dialog.show()
    }




}