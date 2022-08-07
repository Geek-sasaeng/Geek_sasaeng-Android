package com.example.geeksasaeng.Signup.Basic

import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.geeksasaeng.Utils.BaseActivity
import com.example.geeksasaeng.R
import com.example.geeksasaeng.databinding.ActivitySignUpBinding

class SignUpActivity : BaseActivity<ActivitySignUpBinding>(ActivitySignUpBinding::inflate) {

    private lateinit var progressVM: ProgressViewModel
    private lateinit var signUpVM: SignUpViewModel

    override fun initAfterBinding() {
        progressVM = ViewModelProvider(this).get(ProgressViewModel::class.java)
        signUpVM = ViewModelProvider(this).get(SignUpViewModel::class.java)

        supportFragmentManager.beginTransaction()
            .replace(R.id.sign_up_vp, StepOneFragment())
            .addToBackStack("stepOne")
            .commit()

        progressVM.currentPro.observe(this, Observer {
            binding.signUpProgressbar.setProgress(progressVM.currentPro.value!!.toInt())
        })
    }
}