package com.example.geeksasaeng.Profile

import android.os.Bundle
import com.example.geeksasaeng.Utils.BaseActivity
import com.example.geeksasaeng.databinding.ActivityProfileMyInfoUpdatePwdBinding

class ProfileMyInfoUpdatePwdActivity: BaseActivity<ActivityProfileMyInfoUpdatePwdBinding>(ActivityProfileMyInfoUpdatePwdBinding::inflate) {
    override fun initAfterBinding() {
        initClickListener()
    }

    private fun initClickListener() {
        binding.profileMyInfoUpdatePwdBackBtn.setOnClickListener { //뒤로가기
            finish()
        }

        binding.profileMyInfoUpdatePwdCompleteTv.setOnClickListener { //완료
            val dialogProfileUpdate = DialogProfileUpdate()
            val bundle = Bundle()

            dialogProfileUpdate.arguments= bundle
            dialogProfileUpdate.show(supportFragmentManager, "DialogProfileUpdate")
        }
    }
}