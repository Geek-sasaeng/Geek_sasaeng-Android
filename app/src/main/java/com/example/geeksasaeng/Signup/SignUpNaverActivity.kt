package com.example.geeksasaeng

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.geeksasaeng.Base.BaseActivity
import com.example.geeksasaeng.Data.Signup
import com.example.geeksasaeng.Signup.*
import com.example.geeksasaeng.Signup.Retrofit.SignupDataService
import com.example.geeksasaeng.databinding.ActivitySignUpBinding
import com.example.geeksasaeng.databinding.ActivitySignUpNaverBinding
import com.nhn.android.naverlogin.OAuthLogin

class SignUpNaverActivity : BaseActivity<ActivitySignUpNaverBinding>(ActivitySignUpNaverBinding::inflate) {

    private lateinit var progressVM: ProgressNaverViewModel

    override fun initAfterBinding() {

        progressVM = ViewModelProvider(this).get(ProgressNaverViewModel::class.java)

        supportFragmentManager.beginTransaction()
            .replace(R.id.sign_up_naver_vp, StepNaverOneFragment())
            .commit()

        progressVM.currentPro.observe(this, Observer {
            Log.d("PROGRESS-STATUS", "SIGNUP-PROGRESS = ${progressVM.currentPro.value.toString()}")

            binding.signUpNaverProgressbar.setProgress(progressVM.currentPro.value!!.toInt())
        })

        initClickListener()
    }

    private fun initClickListener() {

    }
}