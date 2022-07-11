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
        binding.stepOneIdSuccessTv.setTextColor(ContextCompat.getColor(requireContext(),R.color.main))
        binding.stepOneIdSuccessTv.text = message
        binding.stepOneIdSuccessTv.visibility = View.VISIBLE // 보이게 만들기
        Log.d(
            "CheckId",
            binding.stepOneIdSuccessTv.text.toString() + "/" + binding.stepOneIdSuccessTv.visibility.toString()
        )
    }
    override fun onSignUpIdCheckFailure(code: Int) {
        when(code){
            2603->{ //존재하는 아이디일 경우
                Log.d("CheckId", "존재하는 아이디입니다")
                binding.stepOneIdSuccessTv.setTextColor(ContextCompat.getColor(requireContext(),R.color.error))
                binding.stepOneIdSuccessTv.text = "존재하는 아이디입니다"
                binding.stepOneIdSuccessTv.visibility = View.VISIBLE
            }
            4000->{ //서버오류
                Log.d("CheckId", "4000-서버오류입니다.")
            }
        }
    }
}