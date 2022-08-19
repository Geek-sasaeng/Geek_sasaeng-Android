package com.example.geeksasaeng.Signup

import android.view.View
import androidx.transition.Visibility
import com.example.geeksasaeng.Signup.Basic.StepFiveFragment
import com.example.geeksasaeng.Signup.Basic.StepFiveFragment.Companion.privacyTemrsAgree
import com.example.geeksasaeng.Signup.Basic.StepFiveFragment.Companion.serviceTemrsAgree
import com.example.geeksasaeng.Signup.Naver.StepNaverThreeFragment.Companion.socialPrivacyTemrsAgree
import com.example.geeksasaeng.Utils.BaseActivity
import com.example.geeksasaeng.databinding.ActivityTos2Binding

class Tos2Activity : BaseActivity<ActivityTos2Binding>(ActivityTos2Binding::inflate) {

    private var isSocial : Boolean = false // 네이버 회원가입 유무
    var status: String? = null

    override fun initAfterBinding() {
        status = intent.getStringExtra("status")

        if(status!="signUp"){ // 내정보뷰에서 보는 경우가 있다.
            binding.tos2BottomBar.visibility = View.GONE
            binding.tos2TitleTv.text = "서비스 이용 약관 보기"
        }else{ //회원가입창에서 들어온 경우
            isSocial = intent.getBooleanExtra("isSocial", false)
        }
        initClickListener()
    }

    private fun initClickListener(){
        binding.tos2BackBtn.setOnClickListener { // 뒤로가기버튼
            finish()
        }

        binding.tos2CheckBtn.setOnClickListener {
            if (isSocial){ // 소셜회원가입일때
                socialPrivacyTemrsAgree = true
            }else{// 일반 회원가입일때
                privacyTemrsAgree = true
            }
            finish()
        }
    }

}