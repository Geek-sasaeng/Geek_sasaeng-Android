package com.example.geeksasaeng.Signup.Naver

import android.content.Intent
import androidx.fragment.app.activityViewModels
import com.example.geeksasaeng.Utils.BaseFragment
import com.example.geeksasaeng.Login.LoginActivity
import com.example.geeksasaeng.databinding.FragmentStepNaverThreeBinding

class StepNaverThreeFragment : BaseFragment<FragmentStepNaverThreeBinding>(FragmentStepNaverThreeBinding::inflate) {

    var email: String? = ""
    var loginId: String? = ""
    var nickname: String? = ""
    var phoneNumber: String? = ""
    var universityName: String? = ""

    private val progressVM: ProgressNaverViewModel by activityViewModels()

    override fun initAfterBinding() {
        progressVM.increase()

        email = arguments?.getString("email")
        loginId = arguments?.getString("loginId")
        nickname = arguments?.getString("nickname")
        phoneNumber = arguments?.getString("phoneNumber")
        universityName = arguments?.getString("universityName")

        initClickListener()
    }

    private fun initClickListener() {
        binding.stepNaverThreeStartBtn.setOnClickListener {
            val intent = Intent(activity, LoginActivity::class.java)
            intent.putExtra("email", email)
            intent.putExtra("loginId", loginId)
            intent.putExtra("nickname", nickname)
            intent.putExtra("phoneNumber", phoneNumber)
            intent.putExtra("universityName", universityName)
            intent.putExtra("status", "social")
            startActivity(intent)
        }
    }
}