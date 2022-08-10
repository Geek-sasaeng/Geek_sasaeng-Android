package com.example.geeksasaeng.Profile

import android.content.Intent
import com.example.geeksasaeng.Login.LoginActivity
import com.example.geeksasaeng.MainActivity
import com.example.geeksasaeng.Utils.BaseFragment
import com.example.geeksasaeng.Utils.removeAutoLogin
import com.example.geeksasaeng.databinding.FragmentProfileBinding

class ProfileFragment: BaseFragment<FragmentProfileBinding>(FragmentProfileBinding::inflate) {
    override fun initAfterBinding() {
        initClickListener()
    }

    private fun initClickListener() {
        binding.profileLogoutBtn.setOnClickListener {
            (context as MainActivity).finish()
            removeAutoLogin()
            startActivity(Intent(activity, LoginActivity::class.java))
        }
    }
}