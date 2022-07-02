package com.example.geeksasaeng

import android.content.Intent
import android.os.Bundle
import android.util.Log
import com.example.geeksasaeng.Base.BaseActivity
import com.example.geeksasaeng.Data.Signup
import com.example.geeksasaeng.Signup.Retrofit.SignupDataService
import com.example.geeksasaeng.Signup.SignUpView
import com.example.geeksasaeng.databinding.ActivityLoginBinding

class LoginActivity: BaseActivity<ActivityLoginBinding>(ActivityLoginBinding::inflate), SignUpView {

    var checkPassword: String = ""
    var email: String = ""
    var loginId: String = ""
    var nickname: String = ""
    var password: String = ""
    var phoneNumber: String = ""
    var universityName: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (intent != null) {
            checkPassword = intent?.getStringExtra("checkPassword").toString()
            email = intent?.getStringExtra("email").toString()
            loginId = intent?.getStringExtra("loginId").toString()
            nickname = intent?.getStringExtra("nickname").toString()
            password = intent?.getStringExtra("password").toString()
            phoneNumber = intent?.getStringExtra("phoneNumber").toString()
            universityName = intent?.getStringExtra("universityName").toString()

            signup()
        }

        binding.loginLoginBtn.setOnClickListener {
            // 만약 로그인 성공 시
            changeActivity(MainActivity::class.java)
        }
        
        binding.loginSignupBtn.setOnClickListener {
            changeActivity(SignUpActivity::class.java)
        }
    }

    private fun getUser(): Signup {
        Log.d("SIGNUP-RESPONSE", Signup(checkPassword, email, loginId, nickname, password, phoneNumber, universityName).toString())
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