package com.example.geeksasaeng.Signup

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import com.example.geeksasaeng.Home.Delivery.BannerFragment
import com.example.geeksasaeng.Home.Party.CreateParty.DialogDt
import com.example.geeksasaeng.R
import com.example.geeksasaeng.Signup.Basic.SignUpActivity
import com.example.geeksasaeng.Signup.Basic.SignUpViewModel
import com.example.geeksasaeng.Signup.Basic.StepFiveFragment
import com.example.geeksasaeng.Signup.Basic.StepFiveFragment.Companion.serviceTemrsAgree
import com.example.geeksasaeng.Signup.Naver.StepNaverThreeFragment
import com.example.geeksasaeng.Utils.BaseActivity
import com.example.geeksasaeng.databinding.ActivityTos1Binding


class Tos1Activity : BaseActivity<ActivityTos1Binding>(ActivityTos1Binding::inflate){

    private var isSocial : Boolean = false // 네이버 회원가입 유무

    override fun initAfterBinding() {
        isSocial = intent.getBooleanExtra("isSocial", false)
        initClickListener()
    }

    private fun initClickListener(){
        binding.tos1BackBtn.setOnClickListener { // 뒤로가기버튼
            finish()
        }

        binding.tos1CheckBtn.setOnClickListener {
            if (isSocial){ // 소셜회원가입일때
                //
            }else{// 일반 회원가입일때
                serviceTemrsAgree = true
            }
            finish()
        }
    }

}
