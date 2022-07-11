package com.example.geeksasaeng.Signup.Basic

import android.os.Bundle
import android.os.Handler
import android.util.Log
import androidx.fragment.app.FragmentTransaction
import androidx.fragment.app.activityViewModels
import com.example.geeksasaeng.Base.BaseFragment
import com.example.geeksasaeng.R
import com.example.geeksasaeng.Signup.DialogSignUpEmailCheck
import com.example.geeksasaeng.Signup.Retrofit.*
import com.example.geeksasaeng.databinding.FragmentStepTwoBinding
import com.example.geeksasaeng.util.getUuid

class StepTwoFragment : BaseFragment<FragmentStepTwoBinding>(FragmentStepTwoBinding::inflate), SignUpEmailView {

    var checkPassword: String? = ""
    var loginId: String? = ""
    var nickname: String? = ""
    var password: String? = ""
    var email: String? = ""
    var university: String? = ""

    var uuid: String? = null

    private lateinit var signUpService : SignupDataService

    private val progressVM: ProgressViewModel by activityViewModels()

    override fun initAfterBinding() {
        Log.d("EMAIL-RESPONSE", "StepTwoFragment : InitAfterBinding")

        progressVM.increase()

        checkPassword = arguments?.getString("checkPassword")
        loginId = arguments?.getString("loginId")
        nickname = arguments?.getString("nickname")
        password = arguments?.getString("password")

        signUpService = SignupDataService() //서비스 객체 생성
        signUpService.setSignUpEmailView(this@StepTwoFragment)

        initClickListener()
    }

    private fun initClickListener() {
        // 이메일 인증 전송 버튼
        binding.stepTwoEmailCheckBtn.setOnClickListener {
            sendEmail()
        }

        binding.stepTwoNextBtn.setOnClickListener {
            val transaction: FragmentTransaction =
                (context as SignUpActivity).supportFragmentManager.beginTransaction()

            val bundle = Bundle()
            bundle.putString("checkPassword", checkPassword)
            bundle.putString("loginId", loginId)
            bundle.putString("nickname", nickname)
            bundle.putString("password", password)

            email = binding.stepTwoEmailEt.text.toString() + "@" + binding.stepTwoEmail2Et.text.toString()
            // university = binding.stepTwoSchoolEt.text.toString()

            bundle.putString("email", email)
            // bundle.putString("universityName", university)

            val stepThreeFragment = StepThreeFragment()
            stepThreeFragment.arguments = bundle

            Log.d("SignupData", bundle.toString())

            (context as SignUpActivity).supportFragmentManager.beginTransaction()
                .replace(R.id.sign_up_vp, stepThreeFragment).commit()

            stepThreeFragment.arguments = bundle

            transaction.replace(R.id.sign_up_vp, stepThreeFragment)
            transaction.commit()
        }
    }

    private fun sendEmail() {
        email = binding.stepTwoEmailEt.text.toString() + "@" + binding.stepTwoEmail2Et.text.toString()

        // val signUpEmailRequest = SignUpEmailRequest(email, university, getUuid().toString())
        val uuid = getUuid().toString()
        // val signUpEmailRequest = SignUpEmailRequest("wlals2987" + "@" + "gachon.ac.kr", "Gachon University", uuid)
        val signUpEmailRequest = SignUpEmailRequest("wlals2987@gachon.ac.kr", "가천대학교", uuid)
        Log.d("EMAIL-RESPONSE", "wlals2987@gachon.ac.kr 가천대학교 uuid = $uuid")
        signUpService.signUpEmailSender(signUpEmailRequest)
    }

    override fun onSignUpEmailSuccess(message: String) {
        Log.d("EMAIL-RESPONSE", message)
//        val dialog = DialogSignUpEmailCheck()
//        dialog.show(parentFragmentManager, "CustomDialog")
//
//        // 1.5초 뒤에 Dialog 자동 종료
//        Handler().postDelayed({
//            dialog.dismiss()
//        }, 1500)
    }

    override fun onSignUpEmailFailure(code: Int, message: String) {
        Log.d("EMAIL-RESPONSE", "StepTwoFragment : onSignUpEmailFailure : Fail Code = $code")

        when(code){
            2803 -> showToast(message)
            2804 -> showToast(message)
            2400 -> showToast(message)
        }
    }
}