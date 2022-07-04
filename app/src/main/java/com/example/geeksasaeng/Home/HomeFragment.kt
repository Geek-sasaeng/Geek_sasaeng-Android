package com.example.geeksasaeng.Home

import com.example.geeksasaeng.Base.BaseFragment
import com.example.geeksasaeng.databinding.FragmentHomeBinding
import com.google.android.material.tabs.TabLayoutMediator

class HomeFragment: BaseFragment<FragmentHomeBinding>(FragmentHomeBinding::inflate) {

    private val information = arrayListOf("배달파티", "마켓", "헬퍼")

    override fun initAfterBinding() {
        /* TabLayout, ViewPager 전환 */
        val homeVPAdapter = HomeVPAdapter(this)
        binding.homeVp.adapter = homeVPAdapter
        TabLayoutMediator(binding.homeTab, binding.homeVp) {
            tab, position -> tab.text = information[position]
        }.attach()
    }
}