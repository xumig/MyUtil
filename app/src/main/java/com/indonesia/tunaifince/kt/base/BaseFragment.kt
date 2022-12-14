package com.indonesia.tunaifince.kt.base

import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModel
import androidx.lifecycle.lifecycleScope
import androidx.viewbinding.ViewBinding
import com.indonesia.tunaifince.kt.http.HTTPRequest
import com.indonesia.tunaifince.kt.manager.DialogManager
import com.indonesia.tunaifince.kt.manager.MySpan
import com.indonesia.tunaifince.kt.manager.VerifyJump
import com.liys.dialoglib.LDialog
import com.zackratos.ultimatebarx.ultimatebarx.addNavigationBarBottomPadding
import com.zackratos.ultimatebarx.ultimatebarx.addStatusBarTopPadding
import com.zackratos.ultimatebarx.ultimatebarx.statusBarOnly
import kotlinx.coroutines.CoroutineScope

abstract class BaseFragment<VB : ViewBinding>(private val mInflate: (LayoutInflater, ViewGroup?, Boolean) -> VB) :
    Fragment(), IUIActionEventObserver {


    var contentView: View? = null
    lateinit var vb: VB
    lateinit var rnd: HTTPRequest
    lateinit var mySpan: MySpan
    lateinit var dialogM: DialogManager

    @Suppress("UNCHECKED_CAST")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        if (null == contentView) {
            vb = mInflate(inflater, container, false)
            contentView = vb.root
        }

        return contentView
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
        initView()
        initData()
        initClick()
        initViewMode()
    }

    private fun init() {
        rnd = HTTPRequest(aScope = this)
        mySpan = MySpan()
        dialogM = DialogManager(requireActivity())
    }


    abstract fun initView()
    open fun initClick() {}
    open fun initViewMode() {}
    open fun initData() {}


    protected inline fun <reified VM> getViewModel(): Lazy<VM> where VM : ViewModel, VM : IUIAction {

        return lazy {
            getViewModelFast(
                viewModelStoreOwner = this,
                lifecycleOwner = viewLifecycleOwner,
                viewModelClass = VM::class.java,
            )
        }
    }

    override val lifecycleSupportedScope: CoroutineScope
        get() = viewLifecycleOwner.lifecycleScope

    private var loadDialog: LDialog? = null

    override fun showLoading() {
        dismissLoading()
        activity?.let { act ->
            loadDialog = dialogM.loading()
        }
    }

    override fun dismissLoading() {
        loadDialog?.takeIf { it.isShowing }?.dismiss()
        loadDialog = null
    }

    override fun onDestroyView() {
        super.onDestroyView()
        dismissLoading()
    }

    override fun finishView() {
        requireActivity().finish()
    }

    override fun jumpActivity(cls: Class<*>, mBundle: Bundle?) {
        startActivity(Intent(requireActivity(), cls).putExtra("XM_BUNDLE", mBundle))
    }


    protected inline fun <reified T : Activity> startActivity(mBundle: Bundle? = null) {
        val i = Intent(requireActivity(), T::class.java)
        i.putExtra("XM_BUNDLE", mBundle)
        startActivity(i)
    }


}