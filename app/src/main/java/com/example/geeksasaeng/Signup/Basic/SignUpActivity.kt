package com.example.geeksasaeng.Signup.Basic

import android.util.Log
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.geeksasaeng.Utils.BaseActivity
import com.example.geeksasaeng.R
import com.example.geeksasaeng.databinding.ActivitySignUpBinding

class SignUpActivity : BaseActivity<ActivitySignUpBinding>(ActivitySignUpBinding::inflate) {

    private lateinit var progressVM: ProgressViewModel

    override fun initAfterBinding() {
        progressVM = ViewModelProvider(this).get(ProgressViewModel::class.java)

        supportFragmentManager.beginTransaction()
            .replace(R.id.sign_up_vp, StepOneFragment())
            .commit()

        progressVM.currentPro.observe(this, Observer {
            Log.d("PROGRESS-STATUS", "SIGNUP-PROGRESS = ${progressVM.currentPro.value.toString()}")

            binding.signUpProgressbar.setProgress(progressVM.currentPro.value!!.toInt())
        })

        initClickListener()
    }

    private fun initClickListener() {

    }
}