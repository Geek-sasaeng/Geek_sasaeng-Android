package com.example.geeksasaeng.Signup.Basic

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentTransaction
import androidx.fragment.app.activityViewModels
import com.example.geeksasaeng.Utils.BaseFragment
import com.example.geeksasaeng.R
import com.example.geeksasaeng.Signup.Retrofit.*
import com.example.geeksasaeng.Signup.ToastMsgSignup
import com.example.geeksasaeng.databinding.FragmentStepThreeBinding
import com.example.geeksasaeng.Utils.getUuid
import java.text.DecimalFormat
import java.util.*
import kotlin.concurrent.timer

class StepThreeFragment : BaseFragment<FragmentStepThreeBinding>(FragmentStepThreeBinding::inflate), VerifyEmailView, SignUpEmailView {

    var email: String? = ""
    var emailId: Int? = null
    var universityName: String? = ""

    private val progressVM: ProgressViewModel by activityViewModels()
    private val signUpVM: SignUpViewModel by activityViewModels()

    private lateinit var signUpService : SignupDataService

    private var time = 300000 //5분은 300초 = 300*1000
    private var timerTask : Timer? = null

    var verifyCheck = 0

    override fun initAfterBinding() {
        progressVM.increase()

        email = signUpVM.getEmail()
        universityName = signUpVM.getUniversityName()

        startTimer()

        signUpService = SignupDataService() //서비스 객체 생성
        signUpService.setVerifyEmailView(this@StepThreeFragment)
        signUpService.setSignUpEmailView(this@StepThreeFragment)

        initClickListener()
    }

    private fun initClickListener() {
        // 재전송버튼
        binding.stepThreeSendBtn.setOnClickListener {
            sendEmail()
            binding.stepThreeCheckMsgTv.visibility = View.VISIBLE
            binding.stepThreeResultMsgTv.visibility = View.GONE
        }

        binding.stepThreeNextBtn.setOnClickListener {
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
                binding.stepThreeCheckMsgTv.setTextColor(ContextCompat.getColor(requireContext(),R.color.main))
                binding.stepThreeCheckMsgTv.text = "${min}분 ${sec}초 남았어요"

                if (min == "00" && sec == "00"){
                    timerTask?.cancel()
                    binding.stepThreeCheckMsgTv.setTextColor(ContextCompat.getColor(requireContext(),R.color.error))
                    binding.stepThreeCheckMsgTv.text = "인증번호 입력 시간이 만료되었습니다."
                    // TODO: 인증번호 입력 시간이 만료 되었으므로 다음버튼 비활성화 시켜야하나?

                }
            }

            if (time != 0) // time이 0이 아니라면
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
        val verifyEmailRequest = VerifyEmailRequest(email.toString(), binding.stepThreeCheckEt.text.toString())
        signUpService.verifyEmailSender(verifyEmailRequest)
    }

    override fun onVerifyEmailSuccess(result: VerifyEmailResult) {
        signUpVM.setEmailId(result.emailId)
        (context as SignUpActivity).supportFragmentManager.beginTransaction().replace(R.id.sign_up_vp, StepFourFragment()).commit()
    }

    override fun onVerifyEmailFailure(message: String) {
        verifyCheck = -1

        ToastMsgSignup.createToast((activity as SignUpActivity), "인증번호가 틀렸습니다", "#80A8A8A8")?.show()
    }

    private fun sendEmail() {
        val uuid = getUuid().toString()
        val signUpEmailRequest = SignUpEmailRequest(email, universityName, uuid)
        signUpService.signUpEmailSender(signUpEmailRequest)
    }

    override fun onSignUpEmailSuccess(message: String) {
        ToastMsgSignup.createToast((activity as SignUpActivity), "인증번호가 전송되었습니다", "#8029ABE2")?.show()
        
        resetTimer()
        startTimer()
    }

    override fun onSignUpEmailFailure(code: Int, message: String) {
        when (code) {
            2803 -> ToastMsgSignup.createToast((activity as SignUpActivity), "유효하지 않은 인증번호입니다", "#80A8A8A8")?.show()
            2804 -> ToastMsgSignup.createToast((activity as SignUpActivity), "일일 최대 전송 횟수를 초과했습니다", "#80A8A8A8")?.show()
            2805 -> ToastMsgSignup.createToast((activity as SignUpActivity), "잠시 후에 다시 시도해주세요", "#80A8A8A8")?.show()
        }
    }
}