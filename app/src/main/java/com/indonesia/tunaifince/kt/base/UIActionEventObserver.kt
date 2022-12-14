package com.indonesia.tunaifince.kt.base

import android.os.Bundle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch


interface IUIAction {

    val scope: CoroutineScope

    val uiActionEventFlow: Flow<UIActionEvent>?

    fun showLoading() {
        dispatchUIActionEvent(ShowLoadingEvent)
    }

    fun dismissLoading() {
        dispatchUIActionEvent(DismissLoadingEvent)
    }

    fun finishView() {
        dispatchUIActionEvent(FinishViewEvent)
    }

    fun jumpActivity(cls: Class<*>,mBundle: Bundle? = null) {
        dispatchUIActionEvent(JumpActivity(cls,mBundle))
    }

    fun dispatchUIActionEvent(actionEvent: UIActionEvent) {

    }

}

interface IUIActionEventObserver {

    val lifecycleSupportedScope: CoroutineScope

    fun showLoading()

    fun dismissLoading()

    fun finishView()

    fun jumpActivity(cls: Class<*>,mBundle: Bundle? = null)

    fun <VM> getViewModelFast(
        viewModelStoreOwner: ViewModelStoreOwner,
        lifecycleOwner: LifecycleOwner,
        viewModelClass: Class<VM>,
    ): VM where VM : ViewModel, VM : IUIAction {
        return ViewModelProvider(viewModelStoreOwner).get(viewModelClass)
            .apply {
                collectUiActionIfNeed(this)
            }
    }


    fun <VM> collectUiActionIfNeed(viewModel: VM) where VM : ViewModel, VM : IUIAction {
        val uiActionEventFlow = viewModel.uiActionEventFlow
        if (uiActionEventFlow != null) {
            lifecycleSupportedScope.launch(Dispatchers.Main.immediate) {
                uiActionEventFlow.collect {
                    when (it) {
                        is ShowLoadingEvent -> {
                            showLoading()
                        }
                        is DismissLoadingEvent -> {
                            dismissLoading()
                        }
                        is FinishViewEvent -> {
                            finishView()
                        }
                        is JumpActivity -> {
                            jumpActivity(it.cls,it.mBundle)
                        }
                    }
                }
            }
        }
    }

}