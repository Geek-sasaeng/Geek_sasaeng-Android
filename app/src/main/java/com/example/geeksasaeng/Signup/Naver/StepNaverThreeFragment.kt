package com.example.geeksasaeng.Signup.Naver

import android.content.Intent
import android.util.Log
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

        Log.d("NAVER-LOGIN", "STEP-NAVER-THREE-1 : email = $email / loginId = $loginId / nickname = $nickname / phoneNumber = $phoneNumber / universityName = $universityName")

        initClickListener()
    }

    private fun initClickListener() {
        binding.stepNaverThreeStartBtn.setOnClickListener {
            Log.d("NAVER-LOGIN", "email = $email / loginId = $loginId / nickname = $nickname / phoneNumber = $phoneNumber / universityName = $universityName")

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