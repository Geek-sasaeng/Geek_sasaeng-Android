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
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import com.example.geeksasaeng.Base.BaseFragment
import com.example.geeksasaeng.R
import com.example.geeksasaeng.Signup.Basic.SignUpActivity
import com.example.geeksasaeng.Signup.Basic.UniversitySpinnerAdapter
import com.example.geeksasaeng.Signup.Retrofit.*
import com.example.geeksasaeng.Signup.ToastMsgSignup
import com.example.geeksasaeng.databinding.FragmentStepNaverOneBinding
import com.example.geeksasaeng.util.getUuid
import java.util.regex.Pattern

class StepNaverOneFragment: BaseFragment<FragmentStepNaverOneBinding>(FragmentStepNaverOneBinding::inflate), SignUpNickCheckView, SignUpEmailView {

    var loginId: String? = ""
    var phoneNumber: String? = ""
    var nickname: String? = ""
    var universityName: String? = ""
    var email: String? = ""

    var verifyBtnClick: Int = 0

    private val progressVM: ProgressNaverViewModel by activityViewModels()
    private lateinit var signUpService : SignupDataService // 닉네임 중복확인용

    var universityList: Array<String> = arrayOf("자신의 학교를 선택해주세요", "ㄱ", "가천대학교", "ㄴ", "나천대학교", "ㄷ", "다천대학교", "ㄹ", "라천대학교", "ㅁ", "마천대학교")

    override fun onStart() {
        super.onStart()
        signUpService = SignupDataService() // 서비스 객체 생성
        signUpService.setSignUpNickCheckView(this@StepNaverOneFragment) // 닉네임 중복확인 뷰 연결
        signUpService.setSignUpEmailView(this@StepNaverOneFragment) // 이메일 인증 뷰 연결
    }

    override fun initAfterBinding() {
        progressVM.increase()

        loginId = arguments?.getString("loginId")
        phoneNumber = arguments?.getString("phoneNumber")

        Log.d("NAVER-LOGIN", "loginid = ${loginId} / phoneNumber = ${phoneNumber}")

        initSpinner()
        initTextChangedListener()
        initClickListener()
    }

    //스피너 관련 작업
    private fun initSpinner(){
        val spinnerAdapter = UniversitySpinnerAdapter(requireContext(), universityList)
        binding.stepNaverOneSchoolSp.adapter = spinnerAdapter
        binding.stepNaverOneSchoolSp.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                // 축소된 스피너화면에 맞게 아이템 색상, 화살표 변경
                val image: ImageView = view!!.findViewById(R.id.university_arrow_iv)
                image.setImageResource(R.drawable.ic_spinner_up)
                image.visibility = View.VISIBLE

                universityList[0] = universityList[position] // items[0]은 현재 선택된 아이템 저장용
                val textName: TextView = view!!.findViewById(R.id.spinner_university_text)
                textName.text = universityList[position]
                universityName = textName.text.toString()

                if (universityName == "자신의 학교를 선택해주세요") {
                    universityName = null
                }

                // 임의로 넣어놓은 부분!!
                if (universityName == "가천대학교") {
                    binding.stepNaverOneEmail2Et.setText("@gachon.ac.kr")
                }
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
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // text가 변경되기 전 호출
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                // text가 바뀔 때마다 호출
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

                checkingNext()
            }

            override fun afterTextChanged(s: Editable?) {
                // text가 변경된 후 호출
            }
        })

        binding.stepNaverOneEmailEt.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}
            override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {
                checkingNext()
            }
            override fun afterTextChanged(editable: Editable) {
                if (universityName != null && binding.stepNaverOneEmailEt.text.isNotEmpty()) {
                    binding.stepNaverOneEmailCheckBtnX.visibility = View.GONE
                    binding.stepNaverOneEmailCheckBtnO.visibility = View.VISIBLE
                } else {
                    binding.stepNaverOneEmailCheckBtnX.visibility = View.VISIBLE
                    binding.stepNaverOneEmailCheckBtnO.visibility = View.GONE
                }
            }
        })

        binding.stepNaverOneEmail2Et.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}
            override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {
                checkingNext()
            }
            override fun afterTextChanged(editable: Editable) {
                if (binding.stepNaverOneEmail2Et.text.isNotEmpty() && binding.stepNaverOneEmailEt.text.isNotEmpty()) {
                    binding.stepNaverOneEmailCheckBtnX.visibility = View.GONE
                    binding.stepNaverOneEmailCheckBtnO.visibility = View.VISIBLE
                } else {
                    binding.stepNaverOneEmailCheckBtnX.visibility = View.VISIBLE
                    binding.stepNaverOneEmailCheckBtnO.visibility = View.GONE
                }
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
            Log.d("EMAIL-RESPONSE", "인증번호 전송 버튼 클릭")
            sendEmail()
        }

        binding.stepNaverOneNextBtn.setOnClickListener {
            val bundle = Bundle()
            bundle.putString("loginId", loginId)
            bundle.putString("phoneNumber", phoneNumber)
            bundle.putString("nickname", binding.stepNaverOneNicknameEt.text.toString())
            bundle.putString("universityName", universityName)
            bundle.putString("email", binding.stepNaverOneEmailEt.text.toString() + binding.stepNaverOneEmail2Et.text.toString())

            Log.d("NAVER-LOGIN", "STEP-NAVER-ONE : loginId = $loginId / phoneNumber = $phoneNumber")

            val stepNaverTwoFragment = StepNaverTwoFragment()
            stepNaverTwoFragment.arguments = bundle

            Log.d("SignupData", bundle.toString())

            (context as SignUpNaverActivity).supportFragmentManager.beginTransaction()
                .replace(R.id.sign_up_naver_vp, stepNaverTwoFragment).commit()
        }
    }

    //닉네임 중복확인 결과
    override fun onSignUpNickCheckSuccess(message: String) {
        if (binding.stepNaverOneNicknameSuccessTv.visibility == View.GONE) {
            binding.stepNaverOneNicknameSuccessTv.visibility = View.VISIBLE
            binding.stepNaverOneNicknameFailTv.visibility = View.GONE
        }

        // 테스트용
        binding.stepNaverOneNextBtn.isEnabled = true;

        checkingNext()
    }

    override fun onSignUpNickCheckFailure(code: Int) {
        when (code) {
            2600 -> { // 존재하는 닉네임일 경우
                if (binding.stepNaverOneNicknameFailTv.visibility == View.GONE) {
                    binding.stepNaverOneNicknameFailTv.visibility = View.VISIBLE
                    binding.stepNaverOneNicknameSuccessTv.visibility = View.GONE
                }
            }
            4000 -> { //서버오류
                Log.d("CheckNick", "4000-서버오류입니다.")
            }
        }
    }

    private fun sendEmail() {
        email = binding.stepNaverOneEmailEt.text.toString() + binding.stepNaverOneEmail2Et.text.toString()
        val uuid = getUuid().toString()
        Log.d("EMAIL-RESPONSE", "STEP-NAVER-ONE-SEND-EMAIL : EMAIL = ${email} / UNIVERSITY = ${universityName} / UUID = ${uuid}")
        val signUpEmailRequest = SignUpEmailRequest(email, universityName, uuid)
        signUpService.signUpEmailSender(signUpEmailRequest)
    }

    override fun onSignUpEmailSuccess(message: String) {
        Log.d("EMAIL-RESPONSE", "SUCCESS")
        Log.d("EMAIL-RESPONSE", message)

        ToastMsgSignup.createToast((activity as SignUpActivity), "인증번호가 전송되었습니다.", "#8029ABE2")?.show()

        // showToast("SUCCESS")
        //이메일이 성공적으로 진행되었을때 버튼 활성화
        binding.stepNaverOneNextBtn.isEnabled = true;
        binding.stepNaverOneNextBtn.setBackgroundResource(R.drawable.round_border_button);
        binding.stepNaverOneNextBtn.setTextColor(Color.parseColor("#ffffff"))
        verifyBtnClick = 1
    }

    override fun onSignUpEmailFailure(code: Int, message: String) {
        Log.d("EMAIL-RESPONSE", "FAILED")
        Log.d("EMAIL-RESPONSE", message)
        verifyBtnClick = 1
        showToast(message)

        when (code) {
            2803 -> showToast(message)
            2804 -> {
                ToastMsgSignup.createToast((activity as SignUpActivity), "일일 최대 전송 횟수를 초과했습니다", "#80A8A8A8")?.show()
            }
            2805 -> {
                ToastMsgSignup.createToast((activity as SignUpActivity), "잠시 후에 다시 시도해주세요", "#80A8A8A8")?.show()
            }
        }
    }

    private fun checkingNext() {
        // if (binding.stepNaverOneNicknameSuccessTv.visibility == View.VISIBLE && )
    }
}