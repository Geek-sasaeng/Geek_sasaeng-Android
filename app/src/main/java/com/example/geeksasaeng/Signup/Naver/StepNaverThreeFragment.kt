package com.example.geeksasaeng.Signup.Naver

import android.content.Intent
import androidx.fragment.app.activityViewModels
import com.example.geeksasaeng.Utils.BaseFragment
import com.example.geeksasaeng.Login.LoginActivity
import com.example.geeksasaeng.databinding.FragmentStepNaverThreeBinding

class StepNaverThreeFragment : BaseFragment<FragmentStepNaverThreeBinding>(FragmentStepNaverThreeBinding::inflate) {

    private val progressVM: ProgressNaverViewModel by activityViewModels()
    private val signUpNaverVM: SignUpNaverViewModel by activityViewModels()

    override fun initAfterBinding() {
        progressVM.increase()

        initClickListener()
    }

    private fun initClickListener() {
        binding.stepNaverThreeStartBtn.setOnClickListener {
            val intent = Intent(activity, LoginActivity::class.java)
            // TODO: 나중에 토큰 형식으로 바뀌면 수정해주기!!
            intent.putExtra("emailId", signUpNaverVM.getEmailId())
            intent.putExtra("loginId", signUpNaverVM.getLoginId())
            intent.putExtra("nickname", signUpNaverVM.getNickname())
            intent.putExtra("universityName", signUpNaverVM.getUniversityName())
            startActivity(intent)
        }
    }
}