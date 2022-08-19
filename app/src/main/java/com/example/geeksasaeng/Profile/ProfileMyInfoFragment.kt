package com.example.geeksasaeng.Profile

import android.content.Intent
import com.example.geeksasaeng.Login.LoginActivity
import com.example.geeksasaeng.MainActivity
import com.example.geeksasaeng.R
import com.example.geeksasaeng.Signup.DialogSignUpPhoneSkip
import com.example.geeksasaeng.Utils.BaseFragment
import com.example.geeksasaeng.Utils.removeAutoLogin
import com.example.geeksasaeng.databinding.FragmentProfileMyInfoBinding

class ProfileMyInfoFragment: BaseFragment<FragmentProfileMyInfoBinding>(FragmentProfileMyInfoBinding::inflate) {

    override fun initAfterBinding() {
        initClickListener()
    }

    private fun initClickListener() {
        binding.profileMyInfoBackBtn.setOnClickListener {
            parentFragmentManager.beginTransaction().remove(this).commit()
        }

        binding.profilePencilIv.setOnClickListener {
            (context as MainActivity).supportFragmentManager.beginTransaction().addToBackStack("profile_my_info_update").replace(R.id.main_frm, ProfileMyInfoUpdateFragment()).commit()
        }

        binding.profileUserImgCv.setOnClickListener {
            // TODO: 프로필 사진 수정 부분 앨범과 연결해 넣어주기
        }

        binding.profileMyInfoLogoutCv.setOnClickListener {
            val dialog = DialogProfileLogout()
            dialog.show(parentFragmentManager, "ProfileLogoutDialog")
        }
        
        binding.profileMyInfoWithdrawalTv.setOnClickListener {
            val dialog = DialogProfileWithdrawal()
            dialog.show(parentFragmentManager, "ProfileWithdrawalDialog")
        }
    }
}