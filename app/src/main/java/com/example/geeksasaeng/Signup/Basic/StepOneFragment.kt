package com.example.geeksasaeng.Signup.Basic

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import com.example.geeksasaeng.Base.BaseFragment
import com.example.geeksasaeng.R
import com.example.geeksasaeng.databinding.FragmentStepOneBinding

class StepOneFragment: BaseFragment<FragmentStepOneBinding>(FragmentStepOneBinding::inflate) {

    private val progressVM: ProgressViewModel by activityViewModels()

    override fun initAfterBinding() {
        progressVM.increase()

        initClickListener()
    }

    private fun initClickListener() {
        binding.stepOneIdCheckTv.setOnClickListener {
            Toast.makeText(activity, "ID-CHECK-BTN", Toast.LENGTH_SHORT).show()
        }

        binding.stepOneNicknameBtn.setOnClickListener {
            Toast.makeText(activity, "NICKNAME-CHECK-BTN", Toast.LENGTH_LONG).show()
        }

        binding.stepOneNextBtn.setOnClickListener {
            val bundle = Bundle()
            bundle.putString("checkPassword", binding.stepOneCheckPasswordEt.text.toString())
            bundle.putString("loginId", binding.stepOneIdEt.text.toString())
            bundle.putString("nickname", binding.stepOneNicknameEt.text.toString())
            bundle.putString("password", binding.stepOnePasswordEt.text.toString())

            val stepTwoFragment = StepTwoFragment()
            stepTwoFragment.arguments = bundle

            Log.d("SignupData", bundle.toString())

            (context as SignUpActivity).supportFragmentManager.beginTransaction()
                .replace(R.id.sign_up_vp, stepTwoFragment).commit()
        }
    }
}