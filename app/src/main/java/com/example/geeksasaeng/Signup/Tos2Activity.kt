package com.example.geeksasaeng.Signup

import com.example.geeksasaeng.Signup.Basic.StepFiveFragment
import com.example.geeksasaeng.Signup.Basic.StepFiveFragment.Companion.privacytemrsAgree
import com.example.geeksasaeng.Signup.Basic.StepFiveFragment.Companion.serviceTemrsAgree
import com.example.geeksasaeng.Utils.BaseActivity
import com.example.geeksasaeng.databinding.ActivityTos2Binding

class Tos2Activity : BaseActivity<ActivityTos2Binding>(ActivityTos2Binding::inflate) {
    private var isSocial : Boolean = false // 네이버 회원가입 유무

    override fun initAfterBinding() {
        isSocial = intent.getBooleanExtra("isSocial", false)
        initClickListener()
    }

    private fun initClickListener(){
        binding.tos2BackBtn.setOnClickListener { // 뒤로가기버튼
            finish()
        }
        binding.tos2CheckBtn.setOnClickListener {
            if (isSocial){ // 소셜회원가입일때
                //
            }else{// 일반 회원가입일때
                privacytemrsAgree = true
            }
            finish()
        }
    }

}