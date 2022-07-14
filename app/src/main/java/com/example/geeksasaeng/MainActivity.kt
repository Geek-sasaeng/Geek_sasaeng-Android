package com.example.geeksasaeng

import android.os.Bundle
import com.example.geeksasaeng.Base.BaseActivity
import com.example.geeksasaeng.Chatting.ChattingFragment
import com.example.geeksasaeng.Community.CommunityFragment
import com.example.geeksasaeng.Home.HomeFragment
import com.example.geeksasaeng.Profile.ProfileFragment
import com.example.geeksasaeng.databinding.ActivityMainBinding

class MainActivity : BaseActivity<ActivityMainBinding>(ActivityMainBinding::inflate) {

    override fun initAfterBinding() {
        setFragment(R.id.main_frm, HomeFragment())
        setBottomNavi()
    }

    private fun setBottomNavi() {
        binding.mainBottomNavi.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.homeFrament -> {
                    setFragment(R.id.main_frm, HomeFragment())
                    return@setOnItemSelectedListener true
                }
                R.id.communityFragment -> {
                    setFragment(R.id.main_frm, CommunityFragment())
                    return@setOnItemSelectedListener true
                }
                R.id.chattingFragment -> {
                    setFragment(R.id.main_frm, ChattingFragment())
                    return@setOnItemSelectedListener true
                }
                R.id.profileFragment -> {
                    setFragment(R.id.main_frm, ProfileFragment())
                    return@setOnItemSelectedListener true
                }
            }
            false
        }

        binding.mainBottomNavi.itemIconTintList = null
    }


}