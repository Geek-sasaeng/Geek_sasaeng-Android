package com.example.geeksasaeng

import android.os.Bundle
import com.example.geeksasaeng.Base.BaseActivity
import com.example.geeksasaeng.Signup.SignUpActivity
import com.example.geeksasaeng.databinding.ActivityLoginBinding

class LoginActivity: BaseActivity<ActivityLoginBinding>(ActivityLoginBinding::inflate) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding.loginLoginBtn.setOnClickListener {
            // 만약 로그인 성공 시
            changeActivity(MainActivity::class.java)
        }
        
        binding.loginSignupBtn.setOnClickListener {
            changeActivity(SignUpActivity::class.java)
        }
    }
}