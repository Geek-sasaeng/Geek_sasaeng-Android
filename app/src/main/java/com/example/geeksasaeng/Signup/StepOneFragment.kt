package com.example.geeksasaeng.Signup

import android.os.Bundle
import android.util.Log
import com.example.geeksasaeng.Base.BaseFragment
import com.example.geeksasaeng.R
import com.example.geeksasaeng.SignUpActivity
import com.example.geeksasaeng.databinding.FragmentStepOneBinding

class StepOneFragment: BaseFragment<FragmentStepOneBinding>(FragmentStepOneBinding::inflate) {

    override fun initAfterBinding() {
        initClickListener()
    }

    private fun initClickListener() {
        binding.stepOneNextBtn.setOnClickListener {
            val bundle = Bundle()
            bundle.putString("checkPassword", binding.stepOnePasswordCheckEt.text.toString())
            bundle.putString("loginId", binding.stepOneIdEt.text.toString())
            bundle.putString("nickname", binding.stepOneNickEt.text.toString())
            bundle.putString("password", binding.stepOnePasswordEt.text.toString())

            val stepTwoFragment = StepTwoFragment()
            stepTwoFragment.arguments = bundle

            Log.d("SignupData", bundle.toString())

            (context as SignUpActivity).supportFragmentManager.beginTransaction()
                .replace(R.id.sign_up_vp, stepTwoFragment).commit()
        }
    }
}