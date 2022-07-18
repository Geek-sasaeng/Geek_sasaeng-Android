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

    var checkPassword: String? = ""
    var loginId: String? = ""
    var nickname: String? = ""
    var password: String? = ""
    var email: String? = ""
    var universityName: String? = ""
    var phoneNumber: String? = ""

    private val progressVM: ProgressViewModel by activityViewModels()
    private lateinit var signUpService : SignupDataService

    override fun onStart() {
        super.onStart()
        signUpService = SignupDataService() //서비스 객체 생성
        signUpService.setSignUpView(this)
    }

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
            val intent = Intent(activity, LoginActivity::class.java)
            intent.putExtra("checkPassword", checkPassword)
            intent.putExtra("email", email)
            // TODO: 약관페이지는 디자인이 안나와서 구현X 일단 DEFAULT값으로 Y줌
            intent.putExtra("informationAgreeStatus", "Y")
            intent.putExtra("loginId", loginId)
            intent.putExtra("nickname", nickname)
            intent.putExtra("password", password)
            intent.putExtra("phoneNumber", phoneNumber)
            intent.putExtra("universityName", universityName)

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