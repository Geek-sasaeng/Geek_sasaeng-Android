package com.example.geeksasaeng.Signup.Naver

import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentTransaction
import androidx.fragment.app.activityViewModels
import com.example.geeksasaeng.Utils.BaseFragment
import com.example.geeksasaeng.R
import com.example.geeksasaeng.Signup.Basic.SignUpActivity
import com.example.geeksasaeng.Signup.Retrofit.*
import com.example.geeksasaeng.Signup.ToastMsgSignup
import com.example.geeksasaeng.databinding.FragmentStepNaverTwoBinding
import com.example.geeksasaeng.Utils.getUuid
import java.text.DecimalFormat
import java.util.*
import kotlin.concurrent.timer

class StepNaverTwoFragment : BaseFragment<FragmentStepNaverTwoBinding>(FragmentStepNaverTwoBinding::inflate), SignUpEmailView, VerifyEmailView {

    private val progressVM: ProgressNaverViewModel by activityViewModels()
    private val signUpNaverVM: SignUpNaverViewModel by activityViewModels()

    private lateinit var signUpService : SignupDataService

    private var time = 300000 //5분은 300초 = 300*1000
    private var timerTask : Timer? = null

    var verifyCheck = 0

    override fun initAfterBinding() {
        progressVM.increase()
        
        startTimer()

        signUpService = SignupDataService() //서비스 객체 생성
        signUpService.setVerifyEmailView(this@StepNaverTwoFragment)
        signUpService.setSignUpEmailView(this@StepNaverTwoFragment)

        initClickListener()
    }

    private fun initClickListener() {

        // 재전송버튼
        binding.stepNaverTwoSendBtn.setOnClickListener {
            sendEmail()
            binding.stepNaverTwoCheckMsgTv.visibility = View.VISIBLE
            binding.stepNaverTwoResultMsgTv.visibility = View.GONE
        }

        binding.stepNaverTwoNextBtn.setOnClickListener {
            // 이메일 번호 인증
            verifyEmail()
        }
    }

    // 타이머 작동
    private fun startTimer() {
        timerTask = timer(period = 1000) { //1초가 주기
            val timeForm = DecimalFormat("00") //0을 넣은 곳은 빈자리일 경우, 0으로 채워준다.
            val min = timeForm.format(time / 60000) //전체시간 나누기 60초
            val sec = timeForm.format((time % 60000) / 1000)

            activity?.runOnUiThread {
                binding.stepNaverTwoCheckMsgTv.text = "${min}분 ${sec}초 남았어요"

                if (min == "00" && sec == "00")
                    timerTask?.cancel()
            }

            time -= 1000 //1초씩 줄이기
        }
    }

    // 타이머 초기화
    private fun resetTimer() {
        timerTask?.cancel()

        time = 300000
        binding.stepNaverTwoCheckMsgTv.text = "05분 00초 남았어요"
    }

    private fun verifyEmail() {
        val verifyEmailRequest = VerifyEmailRequest(signUpNaverVM.getEmail(), binding.stepNaverTwoCheckEt.text.toString())
        signUpService.verifyEmailSender(verifyEmailRequest)
    }

    override fun onVerifyEmailSuccess(result: VerifyEmailResult) {
        (context as SignUpNaverActivity).supportFragmentManager.beginTransaction().replace(R.id.sign_up_naver_vp, StepNaverThreeFragment()).commit()
    }

    override fun onVerifyEmailFailure(message: String) {
        showToast(message)

        if (verifyCheck == 0) {
            binding.stepNaverTwoCheckMsgTv.visibility = View.GONE
            binding.stepNaverTwoResultMsgTv.visibility = View.VISIBLE
            binding.stepNaverTwoResultMsgTv.text = "인증번호가 틀렸습니다"
            binding.stepNaverTwoResultMsgTv.setTextColor(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.error
                )
            )
        }

        verifyCheck = -1
    }

    private fun sendEmail() {
        val uuid = getUuid().toString()
        val signUpEmailRequest = SignUpEmailRequest(signUpNaverVM.getEmail(), signUpNaverVM.getUniversityName(), uuid)
        signUpService.signUpEmailSender(signUpEmailRequest)
    }

    override fun onSignUpEmailSuccess(message: String) {
        ToastMsgSignup.createToast((activity as SignUpActivity), "인증번호가 전송되었습니다.", "#8029ABE2")?.show()

        resetTimer()
        startTimer()
    }

    override fun onSignUpEmailFailure(code: Int, message: String) {
        when(code){
            2803 -> showToast(message)
            2804 -> {
                ToastMsgSignup.createToast((activity as SignUpActivity), "일일 최대 전송 횟수를 초과했습니다", "#80A8A8A8")?.show()
            }
            2805 -> {
                ToastMsgSignup.createToast((activity as SignUpActivity), "잠시 후에 다시 시도해주세요", "#80A8A8A8")?.show()
            }
        }
    }
}