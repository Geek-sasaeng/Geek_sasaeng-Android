package com.example.geeksasaeng.Signup

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.FragmentTransaction
import com.example.geeksasaeng.Base.BaseFragment
import com.example.geeksasaeng.R
import com.example.geeksasaeng.SignUpActivity
import com.example.geeksasaeng.databinding.FragmentStepThreeBinding

class StepThreeFragment : BaseFragment<FragmentStepThreeBinding>(FragmentStepThreeBinding::inflate) {

    var checkPassword: String? = ""
    var loginId: String? = ""
    var nickname: String? = ""
    var password: String? = ""
    var email: String? = ""
    var universityName: String? = ""

    override fun initAfterBinding() {

        checkPassword = arguments?.getString("checkPassword")
        loginId = arguments?.getString("loginId")
        nickname = arguments?.getString("nickname")
        password = arguments?.getString("password")
        email = arguments?.getString("email")
        universityName = arguments?.getString("universityName")

        initClickListener()
    }

    private fun initClickListener() {
        binding.stepThreeNextBtn.setOnClickListener {
            val transaction: FragmentTransaction =
                (context as SignUpActivity).supportFragmentManager.beginTransaction()

            val bundle = Bundle()
            bundle.putString("checkPassword", checkPassword)
            bundle.putString("loginId", loginId)
            bundle.putString("nickname", nickname)
            bundle.putString("password", password)
            bundle.putString("email", email)
            bundle.putString("universityName", universityName)

            val stepFourFragment = StepFourFragment()
            stepFourFragment.arguments = bundle

            Log.d("SignupData", bundle.toString())

            (context as SignUpActivity).supportFragmentManager.beginTransaction()
                .replace(R.id.sign_up_vp, stepFourFragment).commit()

            stepFourFragment.arguments = bundle

            transaction.replace(R.id.sign_up_vp, stepFourFragment)
            transaction.commit()
        }
    }
}
