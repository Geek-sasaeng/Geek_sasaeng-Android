package com.example.geeksasaeng.Signup.Naver

import android.content.res.ColorStateList
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import com.example.geeksasaeng.Utils.BaseFragment
import com.example.geeksasaeng.R
import com.example.geeksasaeng.Signup.UniversitySpinnerAdapter
import com.example.geeksasaeng.Signup.Retrofit.*
import com.example.geeksasaeng.Utils.CustomToastMsg
import com.example.geeksasaeng.databinding.FragmentStepNaverOneBinding
import com.example.geeksasaeng.Utils.getUuid
import java.text.DecimalFormat
import java.util.*
import java.util.regex.Pattern
import kotlin.concurrent.timer


class StepNaverOneFragment: BaseFragment<FragmentStepNaverOneBinding>(FragmentStepNaverOneBinding::inflate),
    SignUpNickCheckView, SignUpEmailView, VerifyEmailView {

    var loginId: String? = ""
    var phoneNumber: String? = ""
    var nickname: String? = ""
    var universityName: String? = ""
    var email: String? = ""

    private val progressVM: ProgressNaverViewModel by activityViewModels()
    private val signUpNaverVM: SignUpNaverViewModel by activityViewModels()
    private lateinit var signUpService : SignupDataService // 닉네임 중복확인, 이메일전송, 이메일 인증용

    // TODO: 학교 리스트 API 연결
    var universityList: Array<String> = arrayOf("자신의 학교를 선택해주세요", "ㄱ", "가천대학교","자신의 학교를 선택해주세요")
    //var universityList: Array<String> = arrayOf("자신의 학교를 선택해주세요", "ㄱ", "가천대학교", "ㄴ", "나천대학교", "ㄷ", "다천대학교", "ㄹ", "라천대학교", "ㅁ", "마천대학교")

    private var isNotFirst: Boolean = false
    private var isSendEmail: Boolean = false
    private var time = 300000 //5분은 300초 = 300*1000
    private var timerTask : Timer? = null

    override fun onStart() {
        super.onStart()
        if(isNotFirst){
            binding.stepNaverOneNicknameSuccessTv.visibility= View.VISIBLE

            binding.stepNaverOneNicknameBtnO.visibility = View.VISIBLE
            binding.stepNaverOneNicknameBtnX.visibility = View.INVISIBLE

            binding.stepNaverOneEmailSendBtnX.visibility = View.GONE
            binding.stepNaverOneEmailSendBtnO.visibility = View.VISIBLE
            isSendEmail = false
            checkingNext()
        }
        signUpService = SignupDataService() // 서비스 객체 생성
        signUpService.setSignUpNickCheckView(this@StepNaverOneFragment) // 닉네임 중복확인 뷰 연결
        signUpService.setSignUpEmailView(this@StepNaverOneFragment) // 이메일 보내기 뷰 연결
        signUpService.setVerifyEmailView(this@StepNaverOneFragment) // 이메일 인증 뷰 연결
    }

    // 타이머 작동
    private fun startTimer() {
        timerTask = timer(period = 1000) { //1초가 주기
            val timeForm = DecimalFormat("00") //0을 넣은 곳은 빈자리일 경우, 0으로 채워준다.
            val min = timeForm.format(time / 60000) //전체시간 나누기 60초
            val sec = timeForm.format((time % 60000) / 1000)

            activity?.runOnUiThread {
//                binding.stepNaverOneCheckMsgTv.setTextColor(ContextCompat.getColor(requireContext(),R.color.main))
//                binding.stepNaverOneCheckMsgTv.text = "${min}분 ${sec}초 남았어요"
                Log.d("time", "${min}분 ${sec}초 남았어요")

                if (min == "00" && sec == "00"){
                    timerTask?.cancel()
//                    binding.stepNaverOneCheckMsgTv.setTextColor(ContextCompat.getColor(requireContext(),R.color.error))
//                    binding.stepNaverOneCheckMsgTv.text = "인증번호 입력 시간이 만료되었습니다."
//                    // 인증번호 입력 시간이 만료 되었으므로 버튼 비활성화 시킴
//                    binding.stepNaverOneCheckMsgTv.visibility = View.GONE
//                    binding.stepNaverOneCheckMsgTv.visibility = View.VISIBLE
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
//        binding.stepNaverOneCheckMsgTv.visibility=View.VISIBLE
//        binding.stepNaverOneCheckMsgTv.text = "05분 00초 남았어요"
    }

    override fun onStop() {
        super.onStop()
        timerTask?.cancel() //화면 꺼질때
    }

    override fun initAfterBinding() {
        progressVM.setValue(1)
        initSpinner()
        initTextChangedListener()
        initClickListener()
    }

    //스피너 관련 작업
    private fun initSpinner(){
        val spinnerAdapter = UniversitySpinnerAdapter(requireContext(), universityList)
        binding.stepNaverOneSchoolSp.adapter = spinnerAdapter
        binding.stepNaverOneSchoolSp.setSelection(0) //첫 아이템을 스피너 초기값으로 설정해준다.

        if(signUpNaverVM.getUniversityName()!="null"){ // 선택했던 대학교를 띄워준다.
            binding.stepNaverOneSchoolSp.setSelection(universityList.indexOf(signUpNaverVM.getUniversityName()))
        }

        binding.stepNaverOneSchoolSp.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                // 축소된 스피너화면에 맞게 아이템 색상, 화살표 변경
                val image: ImageView? = view?.findViewById(R.id.university_arrow_iv)
                image?.setImageResource(R.drawable.ic_spinner_up)
                image?.visibility = View.VISIBLE

                if(universityList[position].length!=1){ // 자음을 하나짜리가 아니면
                    universityList[universityList.size-1] = universityList[position] // 마지막 아이템은 현재 선택된 아이템 저장용
                }

                val textName: TextView? = view?.findViewById(R.id.spinner_university_text)
                textName?.text = universityList[universityList.size-1]
                universityName = textName?.text.toString()

                if (universityName == "자신의 학교를 선택해주세요") {
                    universityName = null
                    binding.stepNaverOneEmail2Et.setText("")
                }

                // TODO: 학교 리스트 API 연결하기
                // 임의로 넣어놓은 부분!!
                if (universityName == "가천대학교")
                    binding.stepNaverOneEmail2Et.setText("@gachon.ac.kr")
                else if (universityName == "나천대학교")
                    binding.stepNaverOneEmail2Et.setText("@nachon.ac.kr")
                else if (universityName == "다천대학교")
                    binding.stepNaverOneEmail2Et.setText("@dachon.ac.kr")
                else if (universityName == "라천대학교")
                    binding.stepNaverOneEmail2Et.setText("@rachon.ac.kr")
                else if (universityName == "마천대학교")
                    binding.stepNaverOneEmail2Et.setText("@machon.ac.kr")
            }

            override fun onNothingSelected(parent: AdapterView<*>?) { }
        }
    }

    private fun initTextChangedListener() {
        // 닉네임 TEXTWATCHER
        binding.stepNaverOneNicknameEt.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val nickPattern = Pattern.compile("^[a-zA-Z가-힣ㄱ-ㅎㅏ-ㅣ]{3,15}\$") //패턴 생성 (한글,또는 영문으로 이루어진 3-8자를 조건으로 둠)
                val macher = nickPattern.matcher(binding.stepNaverOneNicknameEt.text.toString()) //사용자가 입력한 닉네임과 패턴 비교

                //조건이 맞으면 중복확인 버튼 활성화, 안맞으면 비활성화 시키기
                if (macher.matches()) {
                    binding.stepNaverOneNicknameBtnO.visibility = View.VISIBLE
                    binding.stepNaverOneNicknameBtnX.visibility = View.INVISIBLE
                } else {
                    binding.stepNaverOneNicknameBtnO.visibility = View.INVISIBLE
                    binding.stepNaverOneNicknameBtnX.visibility = View.VISIBLE
                }
                binding.stepNaverOneNicknameSuccessTv.visibility = View.GONE
                binding.stepNaverOneNicknameFailTv.visibility = View.GONE

                checkingNext()
            }
            override fun afterTextChanged(s: Editable?) {}
        })

        binding.stepNaverOneEmailEt.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}
            override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}
            override fun afterTextChanged(editable: Editable) {
                if (universityName != null && binding.stepNaverOneEmailEt.text.isNotEmpty()) {
                    binding.stepNaverOneEmailSendBtnX.visibility = View.GONE
                    binding.stepNaverOneEmailSendBtnO.visibility = View.VISIBLE
                } else {
                    binding.stepNaverOneEmailSendBtnX.visibility = View.VISIBLE
                    binding.stepNaverOneEmailSendBtnO.visibility = View.GONE
                }

                isSendEmail = false
                checkingNext()
            }
        })

        binding.stepNaverOneEmail2Et.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}
            override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}
            override fun afterTextChanged(editable: Editable) {
                if (binding.stepNaverOneEmail2Et.text.isNotEmpty() && binding.stepNaverOneEmailEt.text.isNotEmpty()) {
                    binding.stepNaverOneEmailSendBtnX.visibility = View.GONE
                    binding.stepNaverOneEmailSendBtnO.visibility = View.VISIBLE
                } else {
                    binding.stepNaverOneEmailSendBtnX.visibility = View.VISIBLE
                    binding.stepNaverOneEmailSendBtnO.visibility = View.GONE
                }
                isSendEmail = false
                checkingNext()
            }
        })
    }

    private fun initClickListener() {
        //닉네임 중복확인 버튼
        binding.stepNaverOneNicknameBtnO.setOnClickListener {
            val userNick = binding.stepNaverOneNicknameEt.text.toString() // 사용자가 입력한 닉네임 가져오기
            val signUpNickCheckRequest= SignUpNickCheckRequest(userNick)
            signUpService.signUpNickCheckSender(signUpNickCheckRequest) // 아이디 중복확인하기
        }

        // 이메일 인증 전송 버튼
        binding.stepNaverOneEmailSendBtnO.setOnClickListener {
            sendEmail()
        }

        // 재전송버튼
        binding.stepNaverOneResendBtn.setOnClickListener {
            sendEmail()
            binding.stepNaverOneCheckMsgTv.visibility = View.VISIBLE
            binding.stepNaverOneCheckMsgTv.visibility = View.GONE
        }


        // 다음
        binding.stepNaverOneNextBtn.setOnClickListener {

            isNotFirst = true
            signUpNaverVM.setLoginId(loginId)
            signUpNaverVM.setPhoneNumber(phoneNumber)
            signUpNaverVM.setNickname(binding.stepNaverOneNicknameEt.text.toString())
            signUpNaverVM.setUniversityName(universityName)
            signUpNaverVM.setEmail(binding.stepNaverOneEmailEt.text.toString() + binding.stepNaverOneEmail2Et.text.toString())

            // 이메일 번호 인증
            verifyEmail()
        }
    }

    //이메일 인증
    private fun verifyEmail() {
        val verifyEmailRequest = VerifyEmailRequest(signUpNaverVM.getEmail(), binding.stepNaverOneCheckEt.text.toString())
        signUpService.verifyEmailSender(verifyEmailRequest)
    }

    override fun onVerifyEmailSuccess(result: VerifyEmailResult) {
        signUpNaverVM.setEmailId(result.emailId)
        (context as SignUpNaverActivity).supportFragmentManager.beginTransaction().replace(R.id.sign_up_naver_vp, StepNaverTwoFragment()).addToBackStack(null).commit()
    }

    override fun onVerifyEmailFailure(message: String) {
        CustomToastMsg.createToast((activity as SignUpNaverActivity), "인증번호가 틀렸습니다.", "#80A8A8A8", 53)?.show()
    }

    //닉네임 중복확인 결과
    override fun onSignUpNickCheckSuccess(message: String) {
        if (binding.stepNaverOneNicknameSuccessTv.visibility == View.GONE) {
            binding.stepNaverOneNicknameSuccessTv.visibility = View.VISIBLE
            binding.stepNaverOneNicknameFailTv.visibility = View.GONE
        }
        checkingNext()
    }

    override fun onSignUpNickCheckFailure(message: String) {
        showToast(message)
        if (binding.stepNaverOneNicknameFailTv.visibility == View.GONE) {
            binding.stepNaverOneNicknameSuccessTv.visibility = View.GONE
            binding.stepNaverOneNicknameFailTv.visibility = View.VISIBLE
        }
    }

    private fun sendEmail() {
        Log.d("cherry","sendEmail")
        email = binding.stepNaverOneEmailEt.text.toString() + binding.stepNaverOneEmail2Et.text.toString()
        val uuid = getUuid().toString()
        val signUpEmailRequest = SignUpEmailRequest(email, universityName, uuid)
        Log.d("cherry",email.toString()+"/"+universityName.toString()+"/"+uuid)
        signUpService.signUpEmailSender(signUpEmailRequest)
    }

    override fun onSignUpEmailSuccess(message: String) {
        CustomToastMsg.createToast((activity as SignUpNaverActivity), "인증번호가 전송되었습니다.", "#8029ABE2", 53)?.show()
        isSendEmail = true
        //이메일이 성공적으로 진행되었을때 버튼 활성화
/*        checkingNext()*/

        resetTimer()
        startTimer()
        Log.d("cherry","Signup-success")
    }

    override fun onSignUpEmailFailure(code: Int, message: String) {
        when (code) {
            2803 -> CustomToastMsg.createToast((activity as SignUpNaverActivity), "유효하지 않은 인증번호입니다", "#80A8A8A8", 53)?.show()//ToastMsgSignup.createToast((activity as SignUpActivity), "유효하지 않은 인증번호입니다", "#80A8A8A8")?.show()
            2804 -> CustomToastMsg.createToast((activity as SignUpNaverActivity), "일일 최대 전송 횟수를 초과했습니다", "#80A8A8A8", 53)?.show() //ToastMsgSignup.createToast((activity as SignUpActivity), "일일 최대 전송 횟수를 초과했습니다", "#80A8A8A8")?.show()
            2805 -> CustomToastMsg.createToast((activity as SignUpNaverActivity), "잠시 후에 다시 시도해주세요", "#80A8A8A8", 53)?.show() //ToastMsgSignup.createToast((activity as SignUpActivity), "잠시 후에 다시 시도해주세요", "#80A8A8A8")?.show()
        }
    }

    private fun checkingNext(){
        var check = (binding.stepNaverOneNicknameSuccessTv.visibility==View.VISIBLE) && isSendEmail //TODO: 이메일 보낸 여부가 아니라 이메일 검증 유무로 바꿔야함

        if(check){
            binding.stepNaverOneNextBtn.isEnabled = true;
            binding.stepNaverOneNextBtn.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(requireContext(),R.color.main))
            binding.stepNaverOneNextBtn.setTextColor(ContextCompat.getColor(requireContext(),R.color.white))
        }else{
            binding.stepNaverOneNextBtn.isEnabled = false;
            binding.stepNaverOneNextBtn.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(requireContext(),R.color.gray_0))
            binding.stepNaverOneNextBtn.setTextColor(ContextCompat.getColor(requireContext(),R.color.gray_2))
        }
    }
}