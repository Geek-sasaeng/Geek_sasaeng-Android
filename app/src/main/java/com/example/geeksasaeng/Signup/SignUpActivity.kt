package com.example.geeksasaeng

import android.os.Bundle
import com.example.geeksasaeng.Base.BaseActivity
import com.example.geeksasaeng.Signup.SignUpVPAdapter
import com.example.geeksasaeng.databinding.ActivitySignUpBinding

class SignUpActivity : BaseActivity<ActivitySignUpBinding>(ActivitySignUpBinding::inflate) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val signUpAdapter = SignUpVPAdapter(this)
        binding.signUpVp.setUserInputEnabled(false) // 뷰페이저 스와이프 막기
        binding.signUpVp.adapter = signUpAdapter //어댑터 연결

        initClickListener()
    }

    private fun initClickListener(){

        binding.signUpNextBtn.setOnClickListener { //다음버튼의 클릭이벤트 처리
            var currentPos= binding.signUpVp.currentItem

            if (currentPos==2){ //마지막 페이지
                //회원가입 진행
            }else{ //마지막 페이지가 아니면 다음 페이지로 넘겨주기
                if(currentPos==1){ //이때 대마지막 전페이지면
                    binding.signUpNextBtn.text = "시작하기"
                    binding.signUpNextBtn.setOnClickListener {
                        finish()
                    }
                }
                binding.signUpVp.setCurrentItem(currentPos+1, false)
            }
            binding.signUpProgressbar.setProgress(currentPos+1) // 프로그레스바 진행률 올리기
        }
    }
}