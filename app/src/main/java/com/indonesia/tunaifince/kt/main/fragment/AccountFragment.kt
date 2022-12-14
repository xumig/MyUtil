package com.indonesia.tunaifince.kt.main.fragment

import com.indonesia.tunaifince.kt.base.BaseFragment
import com.indonesia.tunaifince.kt.databinding.FragmentAccountBinding

class AccountFragment : BaseFragment<FragmentAccountBinding>(FragmentAccountBinding::inflate) {

    private val vm by getViewModel<MainFragmentVM>()
    override fun initView() {
        vm.queryCashUser()
    }


    override fun initViewMode() {
        super.initViewMode()


    }

    override fun initClick() {
        super.initClick()


    }


}