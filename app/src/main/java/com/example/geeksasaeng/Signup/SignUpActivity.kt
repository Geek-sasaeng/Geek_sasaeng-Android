package com.example.geeksasaeng.Signup

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.geeksasaeng.Base.BaseActivity
import com.example.geeksasaeng.R
import com.example.geeksasaeng.databinding.ActivitySignUpBinding

class SignUpActivity : BaseActivity<ActivitySignUpBinding>(ActivitySignUpBinding::inflate) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setFragment(R.id.sign_up_frm, StepOneFragment())

        binding.signUpNextBtn.setOnClickListener {
            setFragment(R.id.sign_up_frm, StepTwoFragment())
        }
    }
}