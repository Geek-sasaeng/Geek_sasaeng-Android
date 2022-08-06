package com.example.geeksasaeng.Signup.Basic

import android.content.res.ColorStateList
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentTransaction
import androidx.fragment.app.activityViewModels
import com.example.geeksasaeng.Utils.BaseFragment
import com.example.geeksasaeng.R
import com.example.geeksasaeng.Signup.DialogSignUpPhoneSkip
import com.example.geeksasaeng.Signup.Retrofit.*
import com.example.geeksasaeng.Signup.ToastMsgSignup
import com.example.geeksasaeng.databinding.FragmentStepFourBinding
import com.example.geeksasaeng.Utils.getUuid
import java.text.DecimalFormat
import java.util.*
import java.util.regex.Pattern
import kotlin.concurrent.timer

class StepFourFragment: BaseFragment<FragmentStepFourBinding>(FragmentStepFourBinding::inflate), SignUpSmsView, VerifySmsView {

    var phoneNumber: String? = ""
    var phoneNumberId: Int? = null

    private val progressVM: ProgressViewModel by activityViewModels()
    private val signUpVM: SignUpViewModel by activityViewModels()

    private var time = 300000 //5분은 300초 = 300*1000
    private var timerTask : Timer? = null

    private lateinit var signUpService :SignupDataService

    override fun onStart() {
        super.onStart()
        signUpService = SignupDataService() //서비스 객체 생성
        signUpService.setSignUpSmsView(this@StepFourFragment)
        signUpService.setVerifySmsView(this@StepFourFragment)
    }

    override fun onStop() {
        super.onStop()
        timerTask?.cancel() //화면 꺼질때
    }

    override fun initAfterBinding() {
        progressVM.increase()

        initTextWatcher()
        initClickListener()
        binding.stepFourNextBtn.isEnabled = true //디버깅용
    }

    private fun initTextWatcher() {
        //인증번호 전송 버튼 관련 - 휴대폰 et
        binding.stepFourPhoneEt.addTextChangedListener(object: TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                val phonepattern = Pattern.compile("^[0-9]{11}\$") //패턴 생성 (숫자로 이루어진 11자리를 조건으로 둠)
                val macher = phonepattern.matcher(binding.stepFourPhoneEt.text.toString())

                //조건이 맞으면 인증번호 보내기 버튼 활성화, 안맞으면 비활성화
                if (macher.matches()) {
                    binding.stepFourPhoneReSendBtnO.visibility = View.VISIBLE
                    binding.stepFourPhoneReSendBtnX.visibility = View.GONE
                } else {
                    binding.stepFourPhoneReSendBtnO.visibility = View.GONE
                    binding.stepFourPhoneReSendBtnX.visibility = View.VISIBLE
                }
                checkingNext() // 휴대폰 번호 바꾸면 다시 다음버튼 활성화/비활성화 조건 체크 위함
            }

        })
    }

    private fun initClickListener() {
        //인증번호 전송 버튼
        binding.stepFourPhoneReSendBtnO.setOnClickListener {
            binding.stepFourCheckMsgTv.visibility = View.VISIBLE //몇초 남았어요 보이게 만들기
            startTimer()

            binding.stepFourPhoneReSendBtnO.text = "재전송 하기"
            binding.stepFourPhoneReSendBtnX.text = "재전송 하기"

            binding.stepFourPhoneCheckBtnO.visibility = View.VISIBLE
            binding.stepFourPhoneCheckBtnX.visibility = View.GONE

            if (binding.stepFourPhoneReSendBtnO.text == "재전송 하기") {
                //10초 이내에 재전송시 10초 지나고 가능, 하루에 최대 5번까지 가능
                // TODO: 하루에 최대 5번까지 가능하게 만들어야해
                if (time < 4900) { //10초가 지났으면
                    resetTimer()
                    startTimer()
                    sendSms() //인증문자 보내는 작업
                }
            }

            sendSms() //인증문자 보내는 작업
        }

        //인증번호 확인 버튼
        binding.stepFourPhoneCheckBtnO.setOnClickListener {
            val code = binding.stepFourCheckEt.text.toString() //사용자가 입력한 인증번호 가져오기
            val verifySmsRequest= VerifySmsRequest(phoneNumber!!, code)
            signUpService.verifySmsSender(verifySmsRequest) //★인증번호 맞는지 확인하기
        }

        //건너뛰기 버튼
        binding.stepFourSkipBtn.setOnClickListener {
            val dialog = DialogSignUpPhoneSkip()
            dialog.show(parentFragmentManager, "CustomDialog")
        }

        //다음 버튼
        binding.stepFourNextBtn.setOnClickListener {
            timerTask?.cancel()

            phoneNumber = binding.stepFourPhoneEt.text.toString()
            signUpVM.setPhoneNumber(phoneNumber)
            signUpVM.setPhoneNumberId(phoneNumberId)

            (context as SignUpActivity).supportFragmentManager.beginTransaction().replace(R.id.sign_up_vp, StepFiveFragment()).commit()
        }
    }

    // 타이머 작동
    private fun startTimer() {
        timerTask = timer(period = 1000) { //1초가 주기
            val timeForm = DecimalFormat("00") //0을 넣은 곳은 빈자리일 경우, 0으로 채워준다.
            val min = timeForm.format(time / 60000) //전체시간 나누기 60초
            val sec = timeForm.format((time % 60000) / 1000)

            activity?.runOnUiThread {
                binding.stepFourCheckMsgTv.setTextColor(ContextCompat.getColor(requireContext(),R.color.main))
                binding.stepFourCheckMsgTv.text = "${min}분 ${sec}초 남았어요"

                if (min == "00" && sec == "00"){
                    timerTask?.cancel()
                    binding.stepFourCheckMsgTv.setTextColor(ContextCompat.getColor(requireContext(),R.color.error))
                    binding.stepFourCheckMsgTv.text = "인증번호 입력 시간이 만료되었습니다."
                    // 인증번호 입력 시간이 만료 되었으므로 버튼 비활성화 시킴
                    binding.stepFourPhoneCheckBtnO.visibility = View.GONE
                    binding.stepFourPhoneCheckBtnX.visibility = View.VISIBLE
                }
            }
            if (time != 0) //time이 0이 아니라면
                time -= 1000 //1초씩 줄이기
        }
    }

    // 타이머 초기화
    private fun resetTimer() {
        timerTask?.cancel()

        time = 300000
        binding.stepFourCheckMsgTv.text = "05분 00초 남았어요"
    }

    //인증번호 문자 보내는 작업
    private fun sendSms(){
        phoneNumber = binding.stepFourPhoneEt.text.toString() //사용자가 입력한 휴대폰 번호 가져오기
        val signUpSmsRequest= SignUpSmsRequest(phoneNumber!!, getUuid().toString())
        signUpService.signUpSmsSender(signUpSmsRequest) //★인증문자보내기
    }

    // sms인증문자 보내기 성공/실패
    override fun onSignUpSmsSuccess(message: String) {
        ToastMsgSignup.createToast((activity as SignUpActivity), "인증번호가 전송되었습니다.", "#8029ABE2")?.show()
    }

    override fun onSignUpSmsFailure(code: Int, message: String) {
        when (code) {
            2015 -> ToastMsgSignup.createToast((activity as SignUpActivity), "일일 최대 전송 횟수를 초과했습니다", "#80A8A8A8")?.show()
            else -> Log.d("SMS-RESPONSE", "Error code = $code / Error msg = $message")
        }
    }

    // sms인증번호 확인 성공/실패
    override fun onVerifySmsSuccess(result: VerifySmsResult) {
        timerTask?.cancel()
        phoneNumberId = result.phoneNumberId
        binding.stepFourCheckMsgTv.text = "성공적으로 인증이 완료되었습니다"
        checkingNext()
    }

    override fun onVerifySmsFailure(message: String) {
        //TODO: 만약에 시간 만료후, 확인버튼을 누르면 어떻게 되는지 확인하기
        ToastMsgSignup.createToast((activity as SignUpActivity), message, "#80A8A8A8")?.show()
    }

    private fun checkingNext(){
        if (binding.stepFourCheckMsgTv.text.toString() == "성공적으로 인증이 완료되었습니다") {
            binding.stepFourNextBtn.isEnabled = true
            binding.stepFourNextBtn.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(requireContext(),R.color.main))
            binding.stepFourNextBtn.setTextColor(ContextCompat.getColor(requireContext(),R.color.white))
        } else {
            binding.stepFourNextBtn.isEnabled = false
            binding.stepFourNextBtn.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(requireContext(),R.color.gray_0))
            binding.stepFourNextBtn.setTextColor(ContextCompat.getColor(requireContext(),R.color.gray_2))
        }
    }
}