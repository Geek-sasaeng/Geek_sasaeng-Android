package com.example.geeksasaeng.Home

import android.content.Intent
import android.util.Log
import com.bumptech.glide.Glide
import com.example.geeksasaeng.Home.Search.SearchActivity
import com.example.geeksasaeng.Utils.BaseFragment
import com.example.geeksasaeng.Utils.getDormitory
import com.example.geeksasaeng.Utils.getProfileImgUrl
import com.example.geeksasaeng.databinding.FragmentHomeBinding
import com.google.android.material.tabs.TabLayoutMediator

class HomeFragment: BaseFragment<FragmentHomeBinding>(FragmentHomeBinding::inflate) {

    private val information = arrayListOf("배달파티", "마켓", "헬퍼")

    override fun initAfterBinding() {
        initClickListener()
        initData()

        val homeVPAdapter = HomeVPAdapter(this)
        binding.homeVp.adapter = homeVPAdapter
        TabLayoutMediator(binding.homeTab, binding.homeVp) {
            tab, position -> tab.text = information[position]
        }.attach()
        // 뷰페이저 스와이프 막기
        binding.homeVp.setUserInputEnabled(false)
    }

    private fun initData(){
        Glide.with(this)
            .load(getProfileImgUrl())
            .into(binding.homeUnivLogo) // 프로필 띄워주기
        binding.homeDormitoryNameTv.text = getDormitory().toString() // 저장되어있는 기숙사 정보 가져와서 설정해주기
    }

    fun initClickListener() {
        binding.homeSearchBtn.setOnClickListener {
            startActivity(Intent(activity, SearchActivity::class.java))
        }
    }
}