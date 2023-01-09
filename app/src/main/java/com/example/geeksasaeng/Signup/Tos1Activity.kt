package com.example.geeksasaeng.Signup

import android.util.Log
import com.example.geeksasaeng.Signup.Basic.StepFourFragment.Companion.serviceTemrsAgree
import com.example.geeksasaeng.Signup.Naver.StepNaverTwoFragment.Companion.socialServiceTemrsAgree
import com.example.geeksasaeng.Utils.BaseActivity
import com.example.geeksasaeng.databinding.ActivityTos1Binding


class Tos1Activity : BaseActivity<ActivityTos1Binding>(ActivityTos1Binding::inflate){

    private var isSocial : Boolean = false // 네이버 회원가입 유무

    override fun initAfterBinding() {
        isSocial = intent.getBooleanExtra("isSocial", false)
        Log.d("cherry", "social"+intent.getBooleanExtra("isSocial", false).toString())
        initClickListener()
    }

    private fun initClickListener(){
        binding.tos1BackBtn.setOnClickListener { // 뒤로가기버튼
            finish()
        }

        binding.tos1CheckBtn.setOnClickListener {
            Log.d("cherry", isSocial.toString())
            if (isSocial){ // 소셜회원가입일때
                socialServiceTemrsAgree = true
            }else{// 일반 회원가입일때
                serviceTemrsAgree = true
            }
            finish()
        }
    }

}
