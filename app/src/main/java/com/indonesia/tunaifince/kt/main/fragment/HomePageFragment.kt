package com.indonesia.tunaifince.kt.main.fragment

import androidx.recyclerview.widget.LinearLayoutManager
import com.indonesia.tunaifince.kt.base.BaseFragment
import com.indonesia.tunaifince.kt.databinding.FragmentHomeBinding
import com.indonesia.tunaifince.kt.http.model.HomeList

class HomePageFragment : BaseFragment<FragmentHomeBinding>(FragmentHomeBinding::inflate) {

    private val vm by getViewModel<MainFragmentVM>()

    private var list = ArrayList<HomeList>()
    private lateinit var madapter: HomeListAdapter
    override fun initView() {

        madapter = HomeListAdapter(requireActivity(), list)
        vb.frontRv.apply {
            adapter = madapter
            layoutManager = LinearLayoutManager(requireActivity())
        }
    }


    override fun initData() {
        super.initData()

    }

    override fun initClick() {
        super.initClick()
        madapter.itemClick { i, homeList ->

        }

    }

    override fun initViewMode() {
        super.initViewMode()

    }


}