package com.example.geeksasaeng.Signup

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.geeksasaeng.Login.LoginActivity
import com.example.geeksasaeng.databinding.FragmentStepFourBinding

class StepFourFragment: Fragment() {


    lateinit var binding: FragmentStepFourBinding

    var checkPassword: String? = ""
    var loginId: String? = ""
    var nickname: String? = ""
    var password: String? = ""
    var email: String? = ""
    var universityName: String? = ""
    var phoneNumber: String? = ""

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)

        binding = FragmentStepFourBinding.inflate(inflater, container, false)

        checkPassword = arguments?.getString("checkPassword")
        loginId = arguments?.getString("loginId")
        nickname = arguments?.getString("nickname")
        password = arguments?.getString("password")
        email = arguments?.getString("email")
        universityName = arguments?.getString("universityName")

        initClickListener()

        return binding.root
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