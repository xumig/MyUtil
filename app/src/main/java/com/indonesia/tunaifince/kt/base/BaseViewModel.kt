package com.indonesia.tunaifince.kt.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.indonesia.tunaifince.kt.http.HTTPRequest
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch


open class BaseViewModel : ViewModel(), IUIAction {

    // protected open val remoteDataSource = AppRemoteDataSource(this)

    private val _uiActionEventFlow = MutableSharedFlow<UIActionEvent>(
        replay = 0,
        extraBufferCapacity = 4,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )

    override val scope: CoroutineScope
        get() = viewModelScope

    override val uiActionEventFlow: Flow<UIActionEvent>?
        get() = _uiActionEventFlow

    override fun dispatchUIActionEvent(actionEvent: UIActionEvent) {
        _uiActionEventFlow.let {
            scope.launch(Dispatchers.Main.immediate) {
                it.emit(actionEvent)
            }
        }
    }

    protected val rnd: HTTPRequest = HTTPRequest(vmScope = this)


}