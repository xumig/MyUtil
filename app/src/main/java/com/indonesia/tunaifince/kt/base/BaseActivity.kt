package com.indonesia.tunaifince.kt.base

import android.app.Activity
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.lifecycleScope
import androidx.viewbinding.ViewBinding
import com.indonesia.tunaifince.kt.http.HTTPRequest
import com.indonesia.tunaifince.kt.manager.DialogManager
import com.indonesia.tunaifince.kt.manager.MySpan
import com.indonesia.tunaifince.kt.manager.VerifyJump
import com.indonesia.tunaifince.kt.topFun.loge
import com.liys.dialoglib.LDialog
import com.zackratos.ultimatebarx.ultimatebarx.addNavigationBarBottomPadding
import com.zackratos.ultimatebarx.ultimatebarx.addStatusBarTopPadding
import com.zackratos.ultimatebarx.ultimatebarx.statusBarOnly
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch


abstract class BaseActivity<VB : ViewBinding>(private val inflate: (LayoutInflater) -> VB) :
    AppCompatActivity(), IUIActionEventObserver {

    lateinit var vb: VB
    lateinit var rnd: HTTPRequest
    lateinit var mySpan: MySpan
    lateinit var dialogM: DialogManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        vb = inflate(layoutInflater)
        setContentView(vb.root)
        init()
        initView()
        initData()
        initClick()
        initViewMode()

    }

    private fun init() {
        rnd = HTTPRequest(aScope = this)

        statusBarOnly {
            transparent()
            light = true

        }
        vb.root.addStatusBarTopPadding()
        vb.root.addNavigationBarBottomPadding()

        mySpan = MySpan()
        dialogM = DialogManager(this)
    }

    protected inline fun <reified VM> getViewModel(): Lazy<VM> where VM : ViewModel, VM : IUIAction {

        return lazy {
            getViewModelFast(
                viewModelStoreOwner = this,
                lifecycleOwner = this,
                viewModelClass = VM::class.java,
            )
        }
    }

//    protected inline fun <reified VM> getViewModel(): Lazy<VM> where VM : ViewModel, VM : IUIAction {
//        return lazy {
//            ViewModelProvider(this).get(VM::class.java)
//        }
//    }


    abstract fun initView()
    open fun initClick() {}
    open fun initViewMode() {}
    open fun initData() {}

    override val lifecycleSupportedScope: CoroutineScope
        get() = lifecycleScope
//
//    private var loadDialog: ProgressDialog? = null
//
//    override fun showLoading() {
//        dismissLoading()
//        loadDialog = ProgressDialog(this).apply {
//            setCancelable(true)
//            setCanceledOnTouchOutside(false)
//            show()
//        }
//
//    }
//
//    override fun dismissLoading() {
//        loadDialog?.takeIf { it.isShowing }?.dismiss()
//        loadDialog = null
//
//    }
//

    private var loadDialog: LDialog? = null

    override fun showLoading() {
        dismissLoading()
        loadDialog = dialogM.loading()

    }

    override fun dismissLoading() {
        loadDialog?.takeIf { it.isShowing }?.dismiss()
        loadDialog = null
    }


    override fun finishView() {
        finish()
    }

    override fun onDestroy() {
        super.onDestroy()
        dismissLoading()
    }

    override fun jumpActivity(cls: Class<*>, mBundle: Bundle?) {
        startActivity(Intent(this, cls).putExtra("XM_BUNDLE", mBundle))
    }

    protected inline fun <reified T : Activity> startActivity(mBundle: Bundle? = null) {
        val i = Intent(this, T::class.java)
        i.putExtra("XM_BUNDLE", mBundle)
        startActivity(i)
    }

    /**
     * 读取Bundle
     */
    fun getBundle(): Bundle? {
        return intent.extras?.getBundle("XM_BUNDLE")
    }


    var mTime: Long = 0

    /**
     * true
     */
    fun intervalTime(time: Int): Boolean {
        if (System.currentTimeMillis() - mTime >= time) {
            mTime = System.currentTimeMillis()
            return true
        } else {
            // mTime = 0
            return false
        }
    }

}