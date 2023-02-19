package com.example.geeksasaeng.Profile

import android.os.Bundle
import com.example.geeksasaeng.Profile.Retrofit.ProfileDataService
import com.example.geeksasaeng.Profile.Retrofit.ProfilePasswordChangeRequest
import com.example.geeksasaeng.Profile.Retrofit.ProfilePasswordChangeView
import com.example.geeksasaeng.Utils.BaseActivity
import com.example.geeksasaeng.databinding.ActivityProfileMyInfoUpdatePwdBinding

class ProfileMyInfoUpdatePwdActivity: BaseActivity<ActivityProfileMyInfoUpdatePwdBinding>(ActivityProfileMyInfoUpdatePwdBinding::inflate), ProfilePasswordChangeView {

    lateinit var profiledataService: ProfileDataService

    override fun initAfterBinding() {
        profiledataService = ProfileDataService()
        profiledataService.setProfilePasswordChangeView(this)
        initClickListener()
    }

    private fun initClickListener() {
        binding.profileMyInfoUpdatePwdBackBtn.setOnClickListener { //뒤로가기
            finish()
        }

        binding.profileMyInfoUpdatePwdCompleteTv.setOnClickListener { //완료
            val profilePasswordChangeRequest = ProfilePasswordChangeRequest(binding.profileMyInfoUpdatePwdCheckEt.text.toString(), binding.profileMyInfoUpdatePwdEt.text.toString())
            profiledataService.profilePasswordChangeSender(profilePasswordChangeRequest)
        }
    }

    override fun onProfilePasswordChangeSuccess() {

    }

    override fun onProfilePasswordChangeFailure(message: String) {

    }
}