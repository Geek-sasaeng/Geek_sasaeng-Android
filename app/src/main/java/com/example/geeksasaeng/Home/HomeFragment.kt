package com.example.geeksasaeng.Home

import com.example.geeksasaeng.Utils.BaseFragment
import com.example.geeksasaeng.databinding.FragmentHomeBinding
import com.google.android.material.tabs.TabLayoutMediator

class HomeFragment: BaseFragment<FragmentHomeBinding>(FragmentHomeBinding::inflate) {

    private val information = arrayListOf("배달파티", "마켓", "헬퍼")

    override fun initAfterBinding() {
        val homeVPAdapter = HomeVPAdapter(this)
        binding.homeVp.adapter = homeVPAdapter
        TabLayoutMediator(binding.homeTab, binding.homeVp) {
            tab, position -> tab.text = information[position]
        }.attach()
        // 뷰페이저 스와이프 막기
        binding.homeVp.setUserInputEnabled(false)
    }
}