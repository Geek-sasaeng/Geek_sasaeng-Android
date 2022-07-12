package com.example.geeksasaeng.Signup.Basic

import android.os.Bundle
import android.util.Log
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentTransaction
import androidx.fragment.app.activityViewModels
import com.example.geeksasaeng.Base.BaseFragment
import com.example.geeksasaeng.R
import com.example.geeksasaeng.Signup.Retrofit.SignUpEmailRequest
import com.example.geeksasaeng.Signup.Retrofit.SignupDataService
import com.example.geeksasaeng.Signup.Retrofit.VerifyEmailRequest
import com.example.geeksasaeng.Signup.Retrofit.VerifyEmailView
import com.example.geeksasaeng.databinding.FragmentStepThreeBinding
import com.example.geeksasaeng.util.getUuid
import java.text.DecimalFormat
import java.util.*
import kotlin.concurrent.timer

class StepThreeFragment : BaseFragment<FragmentStepThreeBinding>(FragmentStepThreeBinding::inflate), VerifyEmailView {

    var checkPassword: String? = ""
    var loginId: String? = ""
    var nickname: String? = ""
    var password: String? = ""
    var email: String? = ""
    var universityName: String? = ""

    private val progressVM: ProgressViewModel by activityViewModels()

    private lateinit var signUpService : SignupDataService

    private var time = 5000
    private var timerTask : Timer? = null

    var verifyCheck = 0

    override fun initAfterBinding() {
        progressVM.increase()
        
        startTimer()

        checkPassword = arguments?.getString("checkPassword")
        loginId = arguments?.getString("loginId")
        nickname = arguments?.getString("nickname")
        password = arguments?.getString("password")
        email = arguments?.getString("email")
        universityName = arguments?.getString("universityName")

        signUpService = SignupDataService() //서비스 객체 생성
        signUpService.setVerifyEmailView(this@StepThreeFragment)

        initClickListener()
    }

    private fun initClickListener() {
        binding.stepThreeSendBtn.setOnClickListener {
            resetTimer()
            startTimer()
        }

        binding.stepThreeNextBtn.setOnClickListener {
            // 이메일 번호 인증
            verifyEmail()

            if (verifyCheck == 1) {
                timerTask?.cancel()

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
            } else {

            }
        }
    }

    // 타이머 작동
    private fun startTimer() {
        timerTask = timer(period = 10) {
            val timeForm = DecimalFormat("00")
            val min = timeForm.format(time / 1000)
            val sec = timeForm.format(time / 100)

            activity?.runOnUiThread {
                binding.stepThreeCheckMsgTv.text = "${min}분 ${sec}초 남았어요"

                if (min == "00" && sec == "00")
                    timerTask?.cancel()
            }

            time--
        }
    }

    // 타이머 초기화
    private fun resetTimer() {
        timerTask?.cancel()

        time = 5000
        binding.stepThreeCheckMsgTv.text = "05분 00초 남았어요"
    }

    private fun verifyEmail() {
        val verifyEmailRequest = VerifyEmailRequest("wlals2987@gachon.ac.kr", binding.stepThreeCheckEt.text.toString())
        Log.d("EMAIL-RESPONSE", "wlals2987@gachon.ac.kr " + binding.stepThreeCheckEt.text.toString())
        signUpService.verifyEmailSender(verifyEmailRequest)
    }

    override fun onVerifyEmailSuccess(message: String) {
        Log.d("EMAIL-RESPONSE", "Success = " + message)
        verifyCheck = 1
    }

    override fun onVerifyEmailFailure(code: Int, message: String) {
        Log.d("EMAIL-RESPONSE", "StepTwoFragment : onSignUpEmailFailure : Fail Code = $code")
        Log.d("EMAIL-RESPONSE", "StepTwoFragment : onSignUpEmailFailure : Fail Message = $message")

        showToast("code = ${code} / message = ${message}")

        binding.stepThreeCheckMsgTv.setTextColor(ContextCompat.getColor(requireContext(),R.color.error))
        binding.stepThreeCheckMsgTv.text = "인증번호가 틀렸습니다"

        verifyCheck = -1

//        when(code){
//            // 2801 : 유효하지 않은 인증번호
//            2801 -> showToast(message)
//        }
    }
}