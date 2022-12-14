package com.indonesia.tunaifince.kt.base

import android.os.Bundle


/**
 * activity vm
 */
sealed class UIActionEvent

object ShowLoadingEvent : UIActionEvent()

object DismissLoadingEvent : UIActionEvent()

object FinishViewEvent : UIActionEvent()

class JumpActivity(val cls: Class<*>,val mBundle: Bundle? = null) : UIActionEvent()

