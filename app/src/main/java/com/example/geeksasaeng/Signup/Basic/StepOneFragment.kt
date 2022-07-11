package com.example.geeksasaeng.Signup.Basic

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import com.example.geeksasaeng.Base.BaseFragment
import com.example.geeksasaeng.R
import com.example.geeksasaeng.Signup.Retrofit.SignUpIdCheckRequest
import com.example.geeksasaeng.Signup.Retrofit.SignupDataService
import com.example.geeksasaeng.Signup.Retrofit.VerifySmsRequest
import com.example.geeksasaeng.Signup.SignUpIdCheckView
import com.example.geeksasaeng.databinding.FragmentStepOneBinding
import com.navercorp.nid.oauth.NidOAuthPreferencesManager.code
import java.util.regex.Pattern

class StepOneFragment: BaseFragment<FragmentStepOneBinding>(FragmentStepOneBinding::inflate), SignUpIdCheckView {

    private val progressVM: ProgressViewModel by activityViewModels()
    private lateinit var signUpService : SignupDataService //아이디, 닉네임 중복확인용

    override fun onStart() {
        super.onStart()
        signUpService = SignupDataService() // 서비스 객체 생성
        signUpService.setSignUpIdCheckView(this@StepOneFragment)//뷰 연결
    }
    override fun initAfterBinding() {
        progressVM.increase()

        initListener()
    }

    private fun initListener() {

        //TEXTWACTHER를 이용한 버튼 활성/비활성 작업
        //아이디 TEXTWATCHER
        binding.stepOneIdEt.addTextChangedListener(object :TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // text가 변경된 후 호출
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                // text가 변경되기 전 호출
            }

            override fun afterTextChanged(s: Editable?) {
                // text가 바뀔 때마다 호출된다.
                var textLength = binding.stepOneIdEt.text.toString().length
                //조건이 맞으면 인증번호 보내기 버튼 활성화, 안맞으면 비활성화 시키기
                binding.stepOneIdCheckBtn.isEnabled = textLength>=6 //6자이 이상이면 버튼 활성화 시키기
            }
        })

        //비밀번호 TEXTWATCHER
        binding.stepOnePasswordEt.addTextChangedListener(object :TextWatcher{
            //TODO: 비밀번호에 한글 안쳐지게 막아야할 듯?
            //TODO: 일단 TEXT바뀔때마다 VALIDATION검사하게했는데 , IOS는 TEXT입력 완료했을 때만 검사하기로 해두었대
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // text가 변경된 후 호출

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                // text가 변경되기 전 호출
            }

            override fun afterTextChanged(s: Editable?) {
                // text가 바뀔 때마다 호출된다.
                // 조건: 숫자, 영어, 특수문자의 조합(하나 이상 포함), 공백 포함 불가
                val pwRegex = """^(?=.*[0-9])(?=.*[a-zA-Z])(?=.*[!@#$%^+\-=])(?=\S+$)[A-Za-z\d!@#$%^+\-=]{8,}$"""
                val pwPattern = Pattern.compile(pwRegex)
                val macher = pwPattern.matcher(binding.stepOnePasswordEt.text.toString())
                binding.stepOnePwMsgTv.visibility = View.VISIBLE // 비밀번호 밑에 안내창 보이게하기
                if(macher.matches()){ //조건이 맞으면
                    binding.stepOnePwMsgTv.setTextColor(ContextCompat.getColor(requireContext(),R.color.main))
                    binding.stepOnePwMsgTv.text = "사용가능한 비밀번호입니다"
                }else{ //조건이 맞지 않으면
                    binding.stepOnePwMsgTv.setTextColor(ContextCompat.getColor(requireContext(),R.color.error))
                    binding.stepOnePwMsgTv.text = "문자, 숫자 및 특수문자 포함 8자 이상으로 입력해주세요"
                }
                Log.d("pw", binding.stepOnePasswordEt.text.toString()+"는 조건 :"+macher.matches().toString())
            }
        })

        //비밀번호 확인 TEXTWATCHER
        binding.stepOneCheckPasswordEt.addTextChangedListener(object :TextWatcher{
            //TODO: 일단 TEXT바뀔때마다 VALIDATION검사하게했는데 , IOS는 TEXT입력 완료했을 때만 검사하기로 해두었대
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // text가 변경된 후 호출

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                // text가 변경되기 전 호출
            }

            override fun afterTextChanged(s: Editable?) {
                Log.d("pw", binding.stepOnePasswordEt.text.toString() +":"+binding.stepOneCheckPasswordEt.text.toString())
                // text가 바뀔 때마다 호출된다.
                if(binding.stepOnePasswordEt.text.toString()!=binding.stepOneCheckPasswordEt.text.toString()){ //일치하지 않으면,
                    binding.stepOneCheckPwMsgTv.visibility = View.VISIBLE // 비밀번호 밑에 안내창 보이게하기
                }else{ // 비밀번호 일치하면,
                    binding.stepOneCheckPwMsgTv.visibility = View.INVISIBLE // 비밀번호 밑에 안내창 안 보이게하기
                }
            }
        })


        //아이디 중복확인 버튼
        binding.stepOneIdCheckBtn.setOnClickListener {
            Toast.makeText(activity, "ID-CHECK-BTN", Toast.LENGTH_SHORT).show()

            val userId = binding.stepOneIdEt.text.toString() //사용자가 입력한 아이디 가져오기
            val signUpIdCheckRequest= SignUpIdCheckRequest(userId)
            signUpService.signUpIdCheckSender(signUpIdCheckRequest) //★아이디 중복확인하기
            Log.d("CheckId", "중복확인 리퀘스트 보냄")
        }

        binding.stepOneNicknameBtn.setOnClickListener {
            Toast.makeText(activity, "NICKNAME-CHECK-BTN", Toast.LENGTH_LONG).show()
        }

        binding.stepOneNextBtn.setOnClickListener {
            val bundle = Bundle()
            bundle.putString("checkPassword", binding.stepOneCheckPasswordEt.text.toString())
            bundle.putString("loginId", binding.stepOneIdEt.text.toString())
            bundle.putString("nickname", binding.stepOneNicknameEt.text.toString())
            bundle.putString("password", binding.stepOnePasswordEt.text.toString())

            val stepTwoFragment = StepTwoFragment()
            stepTwoFragment.arguments = bundle

            Log.d("SignupData", bundle.toString())

            (context as SignUpActivity).supportFragmentManager.beginTransaction()
                .replace(R.id.sign_up_vp, stepTwoFragment).commit()
        }
    }

    //아이디 중복확인
    override fun onSignUpIdCheckSuccess(message: String) {
        //사용가능한 아이디일 경우
        Log.d("CheckId", "사용가능한 아이디입니다")
        binding.stepOneIdMsgTv.setTextColor(ContextCompat.getColor(requireContext(),R.color.main))
        binding.stepOneIdMsgTv.text = message
        binding.stepOneIdMsgTv.visibility = View.VISIBLE // 보이게 만들기
        Log.d(
            "CheckId",
            binding.stepOneIdMsgTv.text.toString() + "/" + binding.stepOneIdMsgTv.visibility.toString()
        )
    }
    override fun onSignUpIdCheckFailure(code: Int) {
        when(code){
            2603->{ //존재하는 아이디일 경우
                Log.d("CheckId", "존재하는 아이디입니다")
                binding.stepOneIdMsgTv.setTextColor(ContextCompat.getColor(requireContext(),R.color.error))
                binding.stepOneIdMsgTv.text = "존재하는 아이디입니다"
                binding.stepOneIdMsgTv.visibility = View.VISIBLE
            }
            4000->{ //서버오류
                Log.d("CheckId", "4000-서버오류입니다.")
            }
        }
    }
}