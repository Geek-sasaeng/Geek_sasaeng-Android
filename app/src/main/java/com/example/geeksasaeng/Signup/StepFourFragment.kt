package com.example.geeksasaeng.Signup

import android.content.Intent
import android.util.Log
import com.example.geeksasaeng.Base.BaseFragment
import com.example.geeksasaeng.Login.LoginActivity
import com.example.geeksasaeng.databinding.FragmentStepFourBinding

class StepFourFragment: BaseFragment<FragmentStepFourBinding>(FragmentStepFourBinding::inflate) {

    var checkPassword: String? = ""
    var loginId: String? = ""
    var nickname: String? = ""
    var password: String? = ""
    var email: String? = ""
    var universityName: String? = ""
    var phoneNumber: String? = ""

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
        binding.stepFourNextBtn.setOnClickListener {
            phoneNumber = binding.signupPhoneNumberEt.text.toString()

            Log.d("SignupData", "checkPassword = $checkPassword / loginId = $loginId / nickname = $nickname / password = $password / email = $email / universityName = $universityName / phoneNumber = $phoneNumber")

            val intent = Intent(activity, LoginActivity::class.java)
            intent.putExtra("checkPassword", checkPassword)
            intent.putExtra("loginId", loginId)
            intent.putExtra("nickname", nickname)
            intent.putExtra("password", password)
            intent.putExtra("email", email)
            intent.putExtra("universityName", universityName)
            intent.putExtra("phoneNumber", phoneNumber)
            startActivity(intent)
        }
    }
}