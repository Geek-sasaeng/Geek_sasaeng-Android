package com.example.geeksasaeng.Signup.Basic

import android.content.Intent
import android.util.Log
import androidx.fragment.app.FragmentTransaction
import androidx.fragment.app.activityViewModels
import com.example.geeksasaeng.Utils.BaseFragment
import com.example.geeksasaeng.Login.LoginActivity
import com.example.geeksasaeng.Signup.Retrofit.SignUpView
import com.example.geeksasaeng.Signup.Retrofit.SignupDataService
import com.example.geeksasaeng.databinding.FragmentStepFiveBinding

class StepFiveFragment : BaseFragment<FragmentStepFiveBinding>(FragmentStepFiveBinding::inflate), SignUpView {

    private val progressVM: ProgressViewModel by activityViewModels()
    private val signUpVM: SignUpViewModel by activityViewModels()

    private lateinit var signUpService : SignupDataService

    override fun onStart() {
        super.onStart()
        signUpService = SignupDataService() //서비스 객체 생성
        signUpService.setSignUpView(this)
    }

    override fun initAfterBinding() {
        progressVM.increase()

        // showToast("checkPassword = ${signUpVM.getCheckPassword()} / loginId = ${signUpVM.getLoginId()} / nickname = ${signUpVM.getNickname()} / password = ${signUpVM.getPassword()} / email = ${signUpVM.getEmail()} / universityName = ${signUpVM.getUniversityName()} / phoneNumber = ${signUpVM.getPhoneNumberId()}")

        initClickListener()
    }

    private fun initClickListener() {
        binding.stepFiveStartBtn.setOnClickListener {
            signUpVM.setInformationAgreeStatus("Y")

            val intent = Intent(activity, LoginActivity::class.java)
            intent.putExtra("checkPassword", signUpVM.getCheckPassword())
            intent.putExtra("emailId", signUpVM.getEmailId())
            // TODO: 약관페이지는 디자인이 안나와서 구현X 일단 DEFAULT값으로 Y줌
            intent.putExtra("informationAgreeStatus", "Y")
            intent.putExtra("loginId", signUpVM.getLoginId())
            intent.putExtra("nickname", signUpVM.getNickname())
            intent.putExtra("password", signUpVM.getPassword())
            intent.putExtra("phoneNumberId", signUpVM.getPhoneNumberId())
            intent.putExtra("universityName", signUpVM.getUniversityName())
            startActivity(intent)
        }
    }

    //회원가입 성공/실패
    override fun onSignUpSuccess() {
        Log.d("signup", "회원가입에 성공하였습니다.")
    }

    override fun onSignUpFailure(message:String) {
        showToast(message)
    }
}