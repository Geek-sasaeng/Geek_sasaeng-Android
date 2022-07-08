package com.example.geeksasaeng.Signup.Naver

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import com.example.geeksasaeng.Base.BaseFragment
import com.example.geeksasaeng.R
import com.example.geeksasaeng.databinding.FragmentStepNaverOneBinding

class StepNaverOneFragment: BaseFragment<FragmentStepNaverOneBinding>(FragmentStepNaverOneBinding::inflate) {

    private val progressVM: ProgressNaverViewModel by activityViewModels()

    override fun initAfterBinding() {
        progressVM.increase()

        initClickListener()
    }

    private fun initClickListener() {
        binding.stepNaverOneNicknameBtn.setOnClickListener {
            Toast.makeText(activity, "NICKNAME-CHECK-BTN", Toast.LENGTH_SHORT).show()
        }

        binding.stepNaverOneEmailCheckBtn.setOnClickListener {
            Toast.makeText(activity, "EMAIL-CHECK-BTN", Toast.LENGTH_SHORT).show()
        }

        binding.stepNaverOneNextBtn.setOnClickListener {
            val bundle = Bundle()
            bundle.putString("nickname", binding.stepNaverOneNicknameEt.text.toString())
            bundle.putString("universityName", binding.stepNaverOneSchoolEt.text.toString())
            bundle.putString("email", binding.stepNaverOneEmailEt.text.toString() + "@" + binding.stepNaverOneEmail2Et.text.toString())

            val stepNaverOneFragment = StepNaverTwoFragment()
            stepNaverOneFragment.arguments = bundle

            Log.d("SignupData", bundle.toString())

            (context as SignUpNaverActivity).supportFragmentManager.beginTransaction()
                .replace(R.id.sign_up_naver_vp, stepNaverOneFragment).commit()
        }
    }
}