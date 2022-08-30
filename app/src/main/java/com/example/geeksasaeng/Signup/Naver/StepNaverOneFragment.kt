package com.example.geeksasaeng.Signup.Naver

import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
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
import com.example.geeksasaeng.Signup.Basic.SignUpActivity
import com.example.geeksasaeng.Signup.Basic.StepThreeFragment
import com.example.geeksasaeng.Signup.UniversitySpinnerAdapter
import com.example.geeksasaeng.Signup.Retrofit.*
import com.example.geeksasaeng.Signup.ToastMsgSignup
import com.example.geeksasaeng.Utils.CustomToastMsg
import com.example.geeksasaeng.databinding.FragmentStepNaverOneBinding
import com.example.geeksasaeng.Utils.getUuid
import java.util.*
import java.util.regex.Pattern
import kotlin.concurrent.timer

class StepNaverOneFragment: BaseFragment<FragmentStepNaverOneBinding>(FragmentStepNaverOneBinding::inflate), SignUpNickCheckView, SignUpEmailView {

    var loginId: String? = ""
    var phoneNumber: String? = ""
    var nickname: String? = ""
    var universityName: String? = ""
    var email: String? = ""

    private val progressVM: ProgressNaverViewModel by activityViewModels()
    private val signUpNaverVM: SignUpNaverViewModel by activityViewModels()
    private lateinit var signUpService : SignupDataService // 닉네임 중복확인용

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
            binding.stepNaverOneNicknameSuccessTv.visibility=View.VISIBLE

            binding.stepNaverOneNicknameBtnO.visibility = View.VISIBLE
            binding.stepNaverOneNicknameBtnX.visibility = View.INVISIBLE

            binding.stepNaverOneEmailCheckBtnX.visibility = View.GONE
            binding.stepNaverOneEmailCheckBtnO.visibility = View.VISIBLE
            isSendEmail = false
            checkingNext()
        }
        signUpService = SignupDataService() // 서비스 객체 생성
        signUpService.setSignUpNickCheckView(this@StepNaverOneFragment) // 닉네임 중복확인 뷰 연결
        signUpService.setSignUpEmailView(this@StepNaverOneFragment) // 이메일 인증 뷰 연결
    }

    // 타이머 작동
    private fun startTimer() {
        timerTask = timer(period = 1000) { //1초가 주기
            if (time != 0) // time이 0이 아니라면
                time -= 1000 //1초씩 줄이기
        }
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
                    binding.stepNaverOneEmailCheckBtnX.visibility = View.GONE
                    binding.stepNaverOneEmailCheckBtnO.visibility = View.VISIBLE
                } else {
                    binding.stepNaverOneEmailCheckBtnX.visibility = View.VISIBLE
                    binding.stepNaverOneEmailCheckBtnO.visibility = View.GONE
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
                    binding.stepNaverOneEmailCheckBtnX.visibility = View.GONE
                    binding.stepNaverOneEmailCheckBtnO.visibility = View.VISIBLE
                } else {
                    binding.stepNaverOneEmailCheckBtnX.visibility = View.VISIBLE
                    binding.stepNaverOneEmailCheckBtnO.visibility = View.GONE
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
        binding.stepNaverOneEmailCheckBtnO.setOnClickListener {
            sendEmail()
        }

        // 다음
        binding.stepNaverOneNextBtn.setOnClickListener {

            isNotFirst = true
            signUpNaverVM.setLoginId(loginId)
            signUpNaverVM.setPhoneNumber(phoneNumber)
            signUpNaverVM.setNickname(binding.stepNaverOneNicknameEt.text.toString())
            signUpNaverVM.setUniversityName(universityName)
            signUpNaverVM.setEmail(binding.stepNaverOneEmailEt.text.toString() + binding.stepNaverOneEmail2Et.text.toString())

            val bundle = Bundle()
            bundle.putInt("time",time)
            val frag = StepNaverTwoFragment()
            frag.arguments = bundle

            (context as SignUpNaverActivity).supportFragmentManager.beginTransaction().replace(R.id.sign_up_naver_vp, frag).addToBackStack(null).commit()
        }
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
        //ToastMsgSignup.createToast((activity as SignUpNaverActivity), "인증번호가 전송되었습니다.", "#8029ABE2")?.show()
        isSendEmail = true
        //이메일이 성공적으로 진행되었을때 버튼 활성화
        checkingNext()

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
        var check = (binding.stepNaverOneNicknameSuccessTv.visibility==View.VISIBLE) && isSendEmail

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