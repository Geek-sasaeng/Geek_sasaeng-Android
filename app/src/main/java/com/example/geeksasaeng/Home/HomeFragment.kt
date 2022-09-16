package com.example.geeksasaeng.Home

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.FragmentManager
import com.bumptech.glide.Glide
import com.example.geeksasaeng.Home.Search.SearchActivity
import com.example.geeksasaeng.MainActivity
import com.example.geeksasaeng.R
import com.example.geeksasaeng.Utils.BaseFragment
import com.example.geeksasaeng.Utils.getDormitory
import com.example.geeksasaeng.Utils.getProfileImgUrl
import com.example.geeksasaeng.databinding.FragmentHomeBinding
import com.google.android.material.tabs.TabLayoutMediator

class HomeFragment: BaseFragment<FragmentHomeBinding>(FragmentHomeBinding::inflate) {

    private val information = arrayListOf("배달파티", "마켓", "헬퍼")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("Stack-Log", "onCreate1")
        clearBackStack()
        Log.d("Stack-Log", "onCreate2")
    }

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
        /*Glide.with(this)
            .load("https://w1652956794-fa4293447.slack.com/files/U03MCQJF01F/F03U3P27LMB/gachonlogo.jpeg")  //TODO:각 학교 로고 불러와서 띄워주기
            .into(binding.homeUnivLogoIv) // 프로필 띄워주기*/
        binding.homeUnivLogoIv.setImageResource(R.drawable.gachon_logo) //가천대 로고 (일단  url이 아니라 이렇게 해둠)
        binding.homeDormitoryNameTv.text = getDormitory().toString() // 저장되어있는 기숙사 정보 가져와서 설정해주기
    }

    fun initClickListener() {
        binding.homeSearchBtn.setOnClickListener {
                startActivity(Intent(activity, SearchActivity::class.java))
        }
    }

    private fun clearBackStack() {
//        val fragmentManager: FragmentManager = (context as MainActivity).supportFragmentManager
//        fragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)

        val fm = requireActivity().supportFragmentManager
        Log.d("Stack-Log", "clearBackStack")
        for (i in 0 until fm.backStackEntryCount) {
            Log.d("Stack-Log", "stack $i")
            fm.popBackStack()
        }
        Log.d("Stack-Log", "clearBackStack2")
    }
}