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

    var checkPassword: String? = ""
    var loginId: String? = ""
    var nickname: String? = ""
    var password: String? = ""
    var email: String? = ""
    var universityName: String? = ""
    var phoneNumber: String? = ""

    private val progressVM: ProgressViewModel by activityViewModels()
    private var time = 300000 //5분은 300초 = 300*1000
    private var timerTask : Timer? = null

    private lateinit var signUpService :SignupDataService

    override fun onStart() {
        super.onStart()
        signUpService = SignupDataService() //서비스 객체 생성
        signUpService.setSignUpSmsView(this@StepFourFragment)
        signUpService.setVerifySmsView(this@StepFourFragment)
    }


    override fun initAfterBinding() {
        progressVM.increase()

        checkPassword = arguments?.getString("checkPassword")
        loginId = arguments?.getString("loginId")
        nickname = arguments?.getString("nickname")
        password = arguments?.getString("password")
        email = arguments?.getString("email")
        universityName = arguments?.getString("universityName")

        initTextWatcher()
        initClickListener()

    }

    private fun initTextWatcher() {
        //TEXTWACTHER를 이용한 버튼 활성/비활성 작업

        //인증번호 전송 버튼 관련 - 휴대폰 et
        binding.stepFourPhoneEt.addTextChangedListener(object: TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // text가 변경되기 전 호출
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                // text가 바뀔 때마다 호출된다.
            }

            override fun afterTextChanged(s: Editable?) {
                // text가 변경된 후 호출
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

        //인증번호 확인 버튼 관련 - 인증번호 et
        binding.stepFourCheckEt.addTextChangedListener(object: TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // text가 변경된 후 호출
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                // text가 변경되기 전 호출
            }

            override fun afterTextChanged(s: Editable?) {
                // text가 바뀔 때마다 호출된다.
                if (time != 0) { // 인증시간이 만료되지 않았다면 조건 검사하기
                    val codepattern = Pattern.compile("^[0-9]{6}\$") //패턴 생성 (숫자로 이루어진 6자리를 조건으로 둠)
                    val macher = codepattern.matcher(binding.stepFourCheckEt.text.toString())

                    // 조건이 맞으면 확인버튼 활성화, 안맞으면 비활성화 시키기
//                    if (macher.matches()) {
//                        binding.stepFourPhoneCheckBtnO.visibility = View.VISIBLE
//                        binding.stepFourPhoneCheckBtnX.visibility = View.GONE
//                    }
                }
            }

        })
    }


    private fun initClickListener() {
        //인증번호 전송 버튼
        binding.stepFourPhoneReSendBtnO.setOnClickListener {
            binding.stepFourCheckMsgTv.visibility = View.VISIBLE //몇초 남았어요 보이게 만들기
            startTimer() //타이머시작

            binding.stepFourPhoneReSendBtnO.text = "재전송 하기"
            binding.stepFourPhoneReSendBtnX.text = "재전송 하기"

            binding.stepFourPhoneCheckBtnO.visibility = View.VISIBLE
            binding.stepFourPhoneCheckBtnX.visibility = View.GONE

            if (binding.stepFourPhoneReSendBtnO.text == "재전송 하기") {
                //10초 이내에 재전송시 10초 지나고 가능, 하루에 최대 5번까지 가능
                //TODO: 하루에 최대 5번까지 가능하게 만들어야해
                if(time<4900){ //10초가 지났으면
                    resetTimer()
                    startTimer()
                    sendSms() //인증문자 보내는 작업
                }else{
                    showToast("잠시 후에 다시 시도해주세요")
                }
            }

            sendSms() //인증문자 보내는 작업

        }

        //인증번호 확인 버튼
        binding.stepFourPhoneCheckBtnO.setOnClickListener {
            //인증번호 확인 작업
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

            val transaction: FragmentTransaction =
                (context as SignUpActivity).supportFragmentManager.beginTransaction()

            val bundle = Bundle()
            bundle.putString("checkPassword", checkPassword)
            bundle.putString("loginId", loginId)
            bundle.putString("nickname", nickname)
            bundle.putString("password", password)
            bundle.putString("email", email)
            bundle.putString("universityName", universityName)
            bundle.putString("phoneNumber", phoneNumber)

            val stepFiveFragment = StepFiveFragment()
            stepFiveFragment.arguments = bundle

            Log.d("SignupData", bundle.toString())

            (context as SignUpActivity).supportFragmentManager.beginTransaction()
                .replace(R.id.sign_up_vp, stepFiveFragment).commit()

            stepFiveFragment.arguments = bundle

            transaction.replace(R.id.sign_up_vp, stepFiveFragment)
            transaction.commit()
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
                    Log.d("time", time.toString())
                    timerTask?.cancel()
                    binding.stepFourCheckMsgTv.setTextColor(ContextCompat.getColor(requireContext(),R.color.error))
                    binding.stepFourCheckMsgTv.text = "인증번호 입력 시간이 만료되었습니다."
                    // 인증번호 입력 시간이 만료 되었으므로 버튼 비활성화 시킴
                    binding.stepFourPhoneCheckBtnO.visibility = View.GONE
                    binding.stepFourPhoneCheckBtnX.visibility = View.VISIBLE
                }
            }
            if(time!=0) //time이 0이 아니라면
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
        Log.d("sms",phoneNumber.toString()+"/"+getUuid().toString()+"으로 문자 보냄")
        signUpService.signUpSmsSender(signUpSmsRequest) //★인증문자보내기
    }

    //sms인증문자 보내기 성공/실패
    override fun onSignUpSmsSuccess(message: String) {
        Log.d("sms",message)

        ToastMsgSignup.createToast((activity as SignUpActivity), "인증번호가 전송되었습니다.", "#8029ABE2")?.show()

        checkingNext() // 다음버튼 활성화 여부 결정
    }

    override fun onSignUpSmsFailure(code: Int) {
        when(code){
            2015 -> {
                Log.d("sms","일일 최대 전송 횟수를 초과했습니다")
                ToastMsgSignup.createToast((activity as SignUpActivity), "일일 최대 전송 횟수를 초과했습니다", "#80A8A8A8")?.show()
            }
            2016->Log.d("sms","이메일 인증을 하지 못한 유저입니다. 이메일 인증을 해주세요.")
            4001->Log.d("sms","SMS API 연동 오류입니다.")
            4002->Log.d("sms","SMS API 연동 준비 오류입니다.")
        }
    }

    //sms인증번호 확인 성공/실패
    override fun onVerifySmsSuccess(result: VerifySmsResult) {
        //인증번호가 맞은 경우
        Log.d("sms","SMS 인증에 성공했습니다.")
        binding.stepFourCheckMsgTv.text = "성공적으로 인증이 완료되었습니다"
    }

    override fun onVerifySmsFailure(code: Int) {
        when(code){
            2013-> {
                Log.d("sms", "인증 번호가 틀렸습니다.")
                binding.stepFourFailMsgTv.visibility = View.VISIBLE
                binding.stepFourCheckMsgTv.setTextColor(ContextCompat.getColor(requireContext(),R.color.error))
                binding.stepFourCheckMsgTv.text = "인증 번호가 틀렸습니다."
                //TODO: 만약에 시간 만료후, 확인버튼을 누르면 어떻게 되는지 확인하기
            }
        }
    }

    private fun checkingNext(){
        var check : Boolean = (binding.stepFourCheckMsgTv.text.toString() == "성공적으로 인증이 완료되었습니다")
        if(check){
            binding.stepFourNextBtn.isEnabled=true
            binding.stepFourNextBtn.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(requireContext(),R.color.main))
            binding.stepFourNextBtn.setTextColor(ContextCompat.getColor(requireContext(),R.color.white))
        }else{
            binding.stepFourNextBtn.isEnabled=false
            binding.stepFourNextBtn.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(requireContext(),R.color.gray_0))
            binding.stepFourNextBtn.setTextColor(ContextCompat.getColor(requireContext(),R.color.gray_2))
        }
    }
}