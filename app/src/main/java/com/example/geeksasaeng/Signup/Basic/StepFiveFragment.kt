package com.example.geeksasaeng.Signup.Basic

import android.content.Intent
import android.util.Log
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentTransaction
import androidx.fragment.app.activityViewModels
import com.example.geeksasaeng.Base.BaseFragment
import com.example.geeksasaeng.Login.LoginActivity
import com.example.geeksasaeng.R
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


    //회원가입 성공/실패
    override fun onSignUpSuccess() {
        Log.d("signup", "회원가입에 성공하였습니다.")
    }

    override fun onSignUpFailure(code:Int) {
        when(code){
            2006-> Log.d("signup", "중복되는 유저 아이디입니다")
            2007-> Log.d("signup", "중복되는 유저 이메일입니다")
            2008-> Log.d("signup", "존재하지 않는 학교 이름입니다")
            2201-> Log.d("signup", "회원 정보동의 status가 Y가 아닙니다.")
            2205-> Log.d("signup", "존재하지 않는 회원 id 입니다.")
            4000-> Log.d("signup", "서버 오류입니다.")
        }
    }
}