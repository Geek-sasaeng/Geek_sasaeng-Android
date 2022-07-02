package com.example.geeksasaeng

import android.content.Intent
import android.os.Bundle
import android.util.Log
import com.example.geeksasaeng.Base.BaseActivity
import com.example.geeksasaeng.Data.Signup
import com.example.geeksasaeng.Signup.*
import com.example.geeksasaeng.Signup.Retrofit.SignupDataService
import com.example.geeksasaeng.databinding.ActivitySignUpBinding

class SignUpActivity : BaseActivity<ActivitySignUpBinding>(ActivitySignUpBinding::inflate) {

    var checkPassword: String = ""
    var email: String = ""
    var loginId: String = ""
    var nickname: String = ""
    var password: String = ""
    var phoneNumber: String = ""
    var universityName: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        supportFragmentManager.beginTransaction()
            .replace(R.id.sign_up_vp, StepOneFragment())
            .commit()
    }
}