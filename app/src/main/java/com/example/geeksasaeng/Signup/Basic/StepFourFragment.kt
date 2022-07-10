package com.example.geeksasaeng.Signup.Basic

import android.content.Context
import android.content.SharedPreferences
import android.graphics.Color
import android.os.Bundle
import android.telephony.TelephonyManager
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentTransaction
import androidx.fragment.app.activityViewModels
import com.example.geeksasaeng.Base.BaseFragment
import com.example.geeksasaeng.R
import com.example.geeksasaeng.Signup.DialogSignUpPhoneSkip
import com.example.geeksasaeng.Signup.Retrofit.*
import com.example.geeksasaeng.Signup.SignUpSmsView
import com.example.geeksasaeng.Signup.VerifySmsView
import com.example.geeksasaeng.databinding.FragmentStepFourBinding
import com.example.geeksasaeng.util.getUuid
import com.example.geeksasaeng.util.saveUuid
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
    private var time = 5000
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

        initClickListener()

    }
    private fun initClickListener() {

        //TEXTWACTHER를 이용한 버튼 활성/비활성 작업
        binding.stepFourPhoneEt.addTextChangedListener(object: TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // text가 변경된 후 호출
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                // text가 변경되기 전 호출
            }

            override fun afterTextChanged(s: Editable?) {
                // text가 바뀔 때마다 호출된다.
                val phonepattern = Pattern.compile("^[0-9]{11}\$") //패턴 생성 (숫자로 이루어진 11자리를 조건으로 둠)
                val macher = phonepattern.matcher(binding.stepFourPhoneEt.text.toString())
                //조건이 맞으면 인증번호 보내기 버튼 활성화, 안맞으면 비활성화 시키기
                binding.stepFourPhoneSendBtn.isEnabled = macher.matches()
                binding.stepFourPhoneReSendBtn.isEnabled = macher.matches()
                Log.d("btnEnable", macher.matches().toString())
            }

        })

        binding.stepFourCheckEt.addTextChangedListener(object: TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // text가 변경된 후 호출
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                // text가 변경되기 전 호출
            }

            override fun afterTextChanged(s: Editable?) {
                // text가 바뀔 때마다 호출된다.
                if (time != 0) {
                    val codepattern = Pattern.compile("^[0-9]{6}\$") //패턴 생성 (숫자로 이루어진 6자리를 조건으로 둠)
                    val macher = codepattern.matcher(binding.stepFourCheckEt.text.toString())
                    //조건이 맞으면 확인버튼 활성화, 안맞으면 비활성화 시키기
                    binding.stepFourPhoneCheckBtn.isEnabled = macher.matches()
                    Log.d("btnEnable", "확인버튼 : " + macher.matches().toString())
                }
            }

        })

        //인증번호 전송 버튼
        binding.stepFourPhoneSendBtn.setOnClickListener {
            binding.stepFourCheckMsgTv.visibility = View.VISIBLE //몇초 남았어요 보이게 만들기
            startTimer() //타이머시작

            //인증번호 전송버튼은 감추고, 재전송 하기 버튼 보이게 하기
            /*binding.stepFourPhoneSendBtn.isClickable = false //버튼 클릭안되게 설정*/ //TODO:->이제 필요없을듯?
            binding.stepFourPhoneSendBtn.visibility = View.INVISIBLE
            binding.stepFourPhoneReSendBtn.visibility = View.VISIBLE

            //인증문자 보내는 작업
            sendSms()

        }

        //재전송 하기 버튼
        binding.stepFourPhoneReSendBtn.setOnClickListener {
            //10초 이내에 재전송시 10초 지나고 가능, 하루에 최대 5번까지 가능
            if(time<4900){ //10초가 지났으면
                resetTimer()
                startTimer()
                //인증문자 보내는 작업
                sendSms()
            }else{
                showToast("잠시 후에 다시 시도해주세요")
            }
        }


        //인증번호 확인 버튼
        binding.stepFourPhoneCheckBtn.setOnClickListener {

             //인증번호 확인 작업
            val code = binding.stepFourCheckEt.text.toString() //사용자가 입력한 인증번호 가져오기
            val verifySmsRequest= VerifySmsRequest(phoneNumber!!, code)
            signUpService.VerifySmsSender(verifySmsRequest) //★인증번호 맞는지 확인하기
        }

        //건너뛰기 버튼
        binding.stepFourSkipBtn.setOnClickListener {
            val dialog = DialogSignUpPhoneSkip()
            dialog.show(parentFragmentManager, "CustomDialog")
        }

        //다음버튼
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
        timerTask = timer(period = 10) {
            val timeForm = DecimalFormat("00")
            val min = timeForm.format(time / 1000)
            val sec = timeForm.format(time / 100)

            activity?.runOnUiThread {
                binding.stepFourCheckMsgTv.setTextColor(ContextCompat.getColor(requireContext(),R.color.main))
                binding.stepFourCheckMsgTv.text = "${min}분 ${sec}초 남았어요"

                if (min == "00" && sec == "00"){
                    Log.d("time", time.toString())
                    timerTask?.cancel()
                    binding.stepFourCheckMsgTv.setTextColor(ContextCompat.getColor(requireContext(),R.color.error))
                    binding.stepFourCheckMsgTv.text = "인증번호 입력 시간이 만료되었습니다."
                    // 인증번호 입력 시간이 만료 되었으므로 클릭 못하게
                    // TODO: 어떤 걸로 할지 정하기
                    //<1> - 버튼 비활성화 상태로 만드는 방법
                    binding.stepFourPhoneCheckBtn.isEnabled = false
                    //<2> - 버튼 활성화 이미지인데, 클릭만 안되게 막는 방법
                    //binding.stepFourPhoneCheckBtn.isClickable = false
                }
            }

            time--
        }
    }

    // 타이머 초기화
    private fun resetTimer() {
        timerTask?.cancel()

        time = 5000
        binding.stepFourCheckMsgTv.text = "05분 00초 남았어요"
    }

    //sms인증문자 보내기
    override fun onSignUpSmsSuccess(message: String) {
        Log.d("sms",message)
    }

    override fun onSignUpSmsFailure(code: Int) {
        when(code){
            2015->{
                Log.d("sms","일일 최대 전송 횟수를 초과했습니다.")
                showToast("일일 최대 전송 횟수를 초과했습니다.") //TODO:커스텀 토스트 메세지로 바꾸기
            }
            2016->Log.d("sms","이메일 인증을 하지 못한 유저입니다. 이메일 인증을 해주세요.")
            4001->Log.d("sms","SMS API 연동 오류입니다.")
            4002->Log.d("sms","SMS API 연동 준비 오류입니다.")
        }
    }

    //sms인증번호 확인
    override fun onVerifySmsSuccess(result: VerifySmsResult) {
        //인증번호가 맞은 경우
        Log.d("sms","SMS 인증에 성공했습니다.")
        binding.stepFourCheckMsgTv.text = "성공적으로 인증이 완료되었습니다"
    }

    override fun onVerifySmsFailure(code: Int) {
        when(code){
            2013-> {
                Log.d("sms", "인증 번호가 틀렸습니다.")
                binding.stepFourCheckMsgTv.setTextColor(ContextCompat.getColor(requireContext(),R.color.error))
                binding.stepFourCheckMsgTv.text = "인증 번호가 틀렸습니다."
                //TODO: 만약에 시간 만료후, 확인버튼을 누르면 어떻게 되는지 확인하기
            }
        }
    }

    //인증번호 문자 보내는 작업
    private fun sendSms(){
        phoneNumber = binding.stepFourPhoneEt.text.toString() //사용자가 입력한 휴대폰 번호 가져오기
        if(getUuid()==null){ //uuid가 존재하지 않으면,
            val uuid = UUID.randomUUID().toString() //uuid 생성
            saveUuid(uuid) // sharedpref에 저장
        }
        val signUpSmsRequest= SignUpSmsRequest(phoneNumber!!, getUuid().toString())
        Log.d("sms",phoneNumber.toString()+"/"+getUuid().toString()+"으로 문자 보냄")
        signUpService.signUpSmsSender(signUpSmsRequest) //★인증문자보내기
    }


}