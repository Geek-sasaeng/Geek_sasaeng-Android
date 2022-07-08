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

    override fun initAfterBinding() {
        supportFragmentManager.beginTransaction()
            .replace(R.id.sign_up_vp, StepOneFragment())
            .commit()
    }
}