package com.example.geeksasaeng.Signup.Naver

import android.content.Intent
import android.util.Log
import androidx.fragment.app.FragmentTransaction
import androidx.fragment.app.activityViewModels
import com.example.geeksasaeng.Base.BaseFragment
import com.example.geeksasaeng.Login.LoginActivity
import com.example.geeksasaeng.databinding.FragmentStepNaverThreeBinding

class StepNaverThreeFragment : BaseFragment<FragmentStepNaverThreeBinding>(FragmentStepNaverThreeBinding::inflate) {

    var nickname: String? = ""
    var email: String? = ""
    var universityName: String? = ""

    private val progressVM: ProgressNaverViewModel by activityViewModels()

    override fun initAfterBinding() {
        progressVM.increase()

        nickname = arguments?.getString("nickname")
        email = arguments?.getString("email")
        universityName = arguments?.getString("universityName")

        initClickListener()
    }

    private fun initClickListener() {
        binding.stepNaverThreeStartBtn.setOnClickListener {
            val transaction: FragmentTransaction =
                (context as SignUpNaverActivity).supportFragmentManager.beginTransaction()

            Log.d("SignupData", "nickname = $nickname / email = $email / universityName = $universityName")

            val intent = Intent(activity, LoginActivity::class.java)
            intent.putExtra("nickname", nickname)
            intent.putExtra("email", email)
            intent.putExtra("universityName", universityName)

            startActivity(intent)
        }
    }
}