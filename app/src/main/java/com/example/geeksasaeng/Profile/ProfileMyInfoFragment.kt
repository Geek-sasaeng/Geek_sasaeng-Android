package com.example.geeksasaeng.Profile

import android.content.Intent
import com.example.geeksasaeng.Login.LoginActivity
import com.example.geeksasaeng.MainActivity
import com.example.geeksasaeng.Utils.BaseFragment
import com.example.geeksasaeng.Utils.removeAutoLogin
import com.example.geeksasaeng.databinding.FragmentProfileMyInfoBinding

class ProfileMyInfoFragment: BaseFragment<FragmentProfileMyInfoBinding>(FragmentProfileMyInfoBinding::inflate) {

    override fun initAfterBinding() {
        initClickListener()
    }

    private fun initClickListener(){
        binding.profileMyInfoLogoutCv.setOnClickListener { //로그아웃 버튼
            (context as MainActivity).finish()
            removeAutoLogin()
            startActivity(Intent(activity, LoginActivity::class.java))
       }
    }
}