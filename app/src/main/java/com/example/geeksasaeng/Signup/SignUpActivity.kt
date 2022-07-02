package com.example.geeksasaeng

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.geeksasaeng.Base.BaseActivity
import com.example.geeksasaeng.Data.Signup
import com.example.geeksasaeng.Data.SignupDataService
import com.example.geeksasaeng.Signup.SignUpVPAdapter
import com.example.geeksasaeng.Signup.SignUpView
import com.example.geeksasaeng.databinding.ActivitySignUpBinding

class SignUpActivity : BaseActivity<ActivitySignUpBinding>(ActivitySignUpBinding::inflate),
    SignUpView {

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
                if(currentPos==0){
                    binding.signUpNextBtn
                }

                if(currentPos==1){ //이때 대마지막 전페이지면
                    binding.signUpNextBtn.text = "시작하기"
                    binding.signUpNextBtn.setOnClickListener {
                        signup()
                        // finish()
                    }
                }
                binding.signUpVp.setCurrentItem(currentPos+1, false)
            }
            binding.signUpProgressbar.setProgress(currentPos+1) // 프로그레스바 진행률 올리기
        }
    }

    private fun getUser(): Signup {
        val checkPassword: String = "00000000"
        val email: String = "a@a.com"
        val loginId: String = "luna1234"
        val nickname: String = "luna1234"
        val password: String = "00000000"
        val phoneNumber: String = "01012341234"
        val universityName: String = "Gachon University"

        Log.d("SIGNUP-RESPONSE", "SignupActivity-getUser : " + Signup(checkPassword, email, loginId, nickname, password, phoneNumber, universityName).toString())
        return Signup(checkPassword, email, loginId, nickname, password, phoneNumber, universityName)
    }

    private fun signup() {
        val signupDataService = SignupDataService()
        signupDataService.setSignUpView(this)
        signupDataService.signUp(getUser())

        Log.d("SIGNUP-RESPONSE", "SignupActivity-signup : Signup Check")
    }

    override fun onSignUpSuccess() {
        Log.d("SIGNUP-RESPONSE", "SignupActivity-onSignUpSuccess : Signup Check")
        startActivity(Intent(this, LoginActivity::class.java))
    }

    override fun onSignUpFailure() {
        Log.d("SIGNUP-RESPONSE", "SignupActivity-onSignUpFailure : Signup Check")
    }
}