package com.example.geeksasaeng.Home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.geeksasaeng.Base.BaseFragment
import com.example.geeksasaeng.MainActivity
import com.example.geeksasaeng.R
import com.example.geeksasaeng.databinding.FragmentHomeBinding
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class HomeFragment: Fragment() {
    lateinit var binding: FragmentHomeBinding

    private val information = arrayListOf("배달파티", "마켓", "헬퍼")

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater, container, false)

        /* TabLayout, ViewPager 전환 */
        val homeVPAdapter = HomeVPAdapter(this)
        binding.homeVp.adapter = homeVPAdapter
        TabLayoutMediator(binding.homeTab, binding.homeVp){
                tab, position ->
            tab.text = information[position]
        }.attach()

        return binding.root
    }
}