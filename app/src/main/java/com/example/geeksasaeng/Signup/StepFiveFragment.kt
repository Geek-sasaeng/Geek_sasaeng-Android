package com.example.geeksasaeng.Signup

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.FragmentTransaction
import androidx.fragment.app.activityViewModels
import com.example.geeksasaeng.Base.BaseFragment
import com.example.geeksasaeng.Login.LoginActivity
import com.example.geeksasaeng.R
import com.example.geeksasaeng.SignUpActivity
import com.example.geeksasaeng.databinding.FragmentStepFiveBinding
import com.example.geeksasaeng.databinding.FragmentStepThreeBinding
import java.text.DecimalFormat
import java.util.*
import kotlin.concurrent.timer

class StepFiveFragment : BaseFragment<FragmentStepFiveBinding>(FragmentStepFiveBinding::inflate) {

    var checkPassword: String? = ""
    var loginId: String? = ""
    var nickname: String? = ""
    var password: String? = ""
    var email: String? = ""
    var universityName: String? = ""
    var phoneNumber: String? = ""

    private val progressVM: ProgressViewModel by activityViewModels()

    override fun initAfterBinding() {
        progressVM.increase()

        checkPassword = arguments?.getString("checkPassword")
        loginId = arguments?.getString("loginId")
        nickname = arguments?.getString("nickname")
        password = arguments?.getString("password")
        email = arguments?.getString("email")
        universityName = arguments?.getString("universityName")
        phoneNumber = arguments?.getString("phoneNumber")

        initClickListener()
    }

    private fun initClickListener() {
        binding.stepFiveStartBtn.setOnClickListener {
            val transaction: FragmentTransaction =
                (context as SignUpActivity).supportFragmentManager.beginTransaction()

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