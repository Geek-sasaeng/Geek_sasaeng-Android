package com.example.geeksasaeng.Signup.Basic

import android.os.Bundle
import android.util.Log
import android.view.View
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

    private var time = 300000 //5분은 300초 = 300*1000
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

        // 재전송버튼
       binding.stepThreeSendBtn.setOnClickListener {
            resetTimer()
            startTimer()

            binding.stepThreeCheckMsgTv.visibility = View.VISIBLE
            binding.stepThreeResultMsgTv.visibility = View.GONE
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
            }
        }
    }

    // 타이머 작동
    private fun startTimer() {
        timerTask = timer(period = 1000) { //1초가 주기
            val timeForm = DecimalFormat("00") //0을 넣은 곳은 빈자리일 경우, 0으로 채워준다.
            val min = timeForm.format(time / 60000) //전체시간 나누기 60초
            val sec = timeForm.format((time % 60000) / 1000)

            activity?.runOnUiThread {
                binding.stepThreeCheckMsgTv.text = "${min}분 ${sec}초 남았어요"

                if (min == "00" && sec == "00"){
                    timerTask?.cancel()
                    binding.stepThreeCheckTv.setTextColor(ContextCompat.getColor(requireContext(),R.color.error))
                    binding.stepThreeCheckTv.text = "인증번호 입력 시간이 만료되었습니다."
                    // 인증번호 입력 시간이 만료 되었으므로 버튼 비활성화 시킴
                    /*binding.stepThreeNextBtn.isEnabled = false*/
                }
            }

            time -= 1000 //1초씩 줄이기
        }
    }

    // 타이머 초기화
    private fun resetTimer() {
        timerTask?.cancel()

        time = 300000
        binding.stepThreeCheckMsgTv.text = "05분 00초 남았어요"
    }

    private fun verifyEmail() {
        val verifyEmailRequest = VerifyEmailRequest("wlals2987@gachon.ac.kr", binding.stepThreeCheckEt.text.toString())
        Log.d("EMAIL-RESPONSE", "wlals2987@gachon.ac.kr " + binding.stepThreeCheckEt.text.toString())
        signUpService.verifyEmailSender(verifyEmailRequest)
    }

    override fun onVerifyEmailSuccess(message: String) {
        // Log.d("EMAIL-RESPONSE", "Success = " + message)
        verifyCheck = 1
    }

    override fun onVerifyEmailFailure(code: Int, message: String) {
        if (verifyCheck == 0) {
            binding.stepThreeCheckMsgTv.visibility = View.GONE
            binding.stepThreeResultMsgTv.visibility = View.VISIBLE
            binding.stepThreeResultMsgTv.text = "인증번호가 틀렸습니다"
            binding.stepThreeResultMsgTv.setTextColor(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.error
                )
            )
        }

        verifyCheck = -1

//        when(code){
//            // 2801 : 유효하지 않은 인증번호
//            2801 -> showToast(message)
//        }
    }
}