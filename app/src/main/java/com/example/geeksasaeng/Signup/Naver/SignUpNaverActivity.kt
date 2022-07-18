package com.example.geeksasaeng.Signup.Naver

import android.os.Bundle
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.geeksasaeng.Utils.BaseActivity
import com.example.geeksasaeng.R
import com.example.geeksasaeng.databinding.ActivitySignUpNaverBinding

class SignUpNaverActivity : BaseActivity<ActivitySignUpNaverBinding>(ActivitySignUpNaverBinding::inflate) {

    private lateinit var progressVM: ProgressNaverViewModel

    var loginId: String? = ""
    var phoneNumber: String? = ""

    override fun initAfterBinding() {
        progressVM = ViewModelProvider(this).get(ProgressNaverViewModel::class.java)

        loginId = intent.getStringExtra("loginId")
        phoneNumber = intent.getStringExtra("phoneNumber")

        val transaction: FragmentTransaction = this@SignUpNaverActivity.supportFragmentManager.beginTransaction()

        val bundle = Bundle()
        bundle.putString("loginId", loginId)
        bundle.putString("phoneNumber", phoneNumber)

        val stepNaverOneFragment = StepNaverOneFragment()
        stepNaverOneFragment.arguments = bundle

        transaction.replace(R.id.sign_up_naver_vp, stepNaverOneFragment)
        transaction.commit()

        progressVM.currentPro.observe(this, Observer {
            binding.signUpNaverProgressbar.setProgress(progressVM.currentPro.value!!.toInt())
        })
    }
}