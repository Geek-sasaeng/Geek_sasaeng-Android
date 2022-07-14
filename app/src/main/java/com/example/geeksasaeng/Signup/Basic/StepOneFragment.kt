package com.example.geeksasaeng.Signup.Basic

import android.content.res.ColorStateList
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
import com.example.geeksasaeng.Signup.Retrofit.*
import com.example.geeksasaeng.databinding.FragmentStepOneBinding
import java.util.regex.Pattern

class StepOneFragment: BaseFragment<FragmentStepOneBinding>(FragmentStepOneBinding::inflate), SignUpIdCheckView, SignUpNickCheckView {

    private val progressVM: ProgressViewModel by activityViewModels()
    private lateinit var signUpService : SignupDataService //아이디, 닉네임 중복확인용

    override fun onStart() {
        super.onStart()
        signUpService = SignupDataService() // 서비스 객체 생성
        signUpService.setSignUpIdCheckView(this@StepOneFragment)//아이디 중복확인 뷰 연결
        signUpService.setSignUpNickCheckView(this@StepOneFragment)//닉네임 중복확인 뷰 연결
    }

    override fun initAfterBinding() {
        progressVM.increase()
        initTextWatcher()
        initClickListener()
    }

    //<텍스트 와쳐>
    private fun initTextWatcher(){
        //TEXTWACTHER를 이용한 버튼 활성/비활성 작업
        //아이디 TEXTWATCHER
        binding.stepOneIdEt.addTextChangedListener(object :TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // text가 변경되기 전 호출

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                // text가 바뀔 때마다 호출된다.

            }

            override fun afterTextChanged(s: Editable?) {
                // text가 변경 된 후 호출
                // TODO: 닉네임 한글 막아주기
                var textLength = binding.stepOneIdEt.text.toString().length
                //조건이 맞으면 인증번호 보내기 버튼 활성화, 안맞으면 비활성화 시키기

                if (binding.stepOneIdEt.length() >= 6) {
                    binding.stepOneIdBtnO.visibility = View.VISIBLE
                    binding.stepOneIdBtnX.visibility = View.GONE
                } else {
                    binding.stepOneIdBtnO.visibility = View.GONE
                    binding.stepOneIdBtnX.visibility = View.VISIBLE
                }

//                binding.stepOneIdMsgTv.text=""
                checkingNext()
            }
        })

        // 비밀번호 TEXTWATCHER
        binding.stepOnePasswordEt.addTextChangedListener(object :TextWatcher{
            //TODO: 비밀번호에 한글 안쳐지게 막아야할 듯?
            //TODO: 일단 TEXT바뀔때마다 VALIDATION검사하게했는데 , IOS는 TEXT입력 완료했을 때만 검사하기로 해두었대
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // text가 변경되기 전 호출
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                // text가 바뀔 때마다 호출된다.
            }

            override fun afterTextChanged(s: Editable?) {
                // text가 변경 된 후 호출
                // 조건: 숫자, 영어, 특수문자의 조합(하나 이상 포함), 공백 포함 불가
                val pwRegex = """^(?=.*[0-9])(?=.*[a-zA-Z])(?=.*[!@#$%^+\-=])(?=\S+$)[A-Za-z\d!@#$%^+\-=]{8,}$"""
                val pwPattern = Pattern.compile(pwRegex)
                val macher = pwPattern.matcher(binding.stepOnePasswordEt.text.toString())
                binding.stepOnePwMsgTv.visibility = View.VISIBLE // 비밀번호 밑에 안내창 보이게하기
                if(macher.matches()){ //조건이 맞으면
                    binding.stepOnePwMsgTv.setTextColor(ContextCompat.getColor(requireContext(),R.color.main))
                    binding.stepOnePwMsgTv.text = "사용 가능한 비밀번호입니다"
                }else{ //조건이 맞지 않으면
                    binding.stepOnePwMsgTv.setTextColor(ContextCompat.getColor(requireContext(),R.color.error))
                    binding.stepOnePwMsgTv.text = "문자, 숫자 및 특수문자 포함 8자 이상으로 입력해주세요"
                }
                Log.d("pw", binding.stepOnePasswordEt.text.toString()+"는 조건 :"+macher.matches().toString())


                //비밀번호확인 부분도 수정 필요함
                if (binding.stepOneCheckPasswordEt.text.toString()!=""){ // 비밀번호확인칸에 뭐가 써져있으면
                    //TODO: 아래랑 코드 중복이긴한데,, 함수를 만들어야할까??
                    if(binding.stepOnePasswordEt.text.toString()!=binding.stepOneCheckPasswordEt.text.toString()){ //일치하지 않으면,
                        binding.stepOneCheckPwMsgTv.setTextColor(ContextCompat.getColor(requireContext(),R.color.error))
                        binding.stepOneCheckPwMsgTv.text = "비밀번호를 다시 확인해주세요"
                        binding.stepOneCheckPwMsgTv.visibility = View.VISIBLE // 비밀번호 밑에 안내창 보이게하기
                    }else{ // 비밀번호 일치하면,
                        binding.stepOneCheckPwMsgTv.setTextColor(ContextCompat.getColor(requireContext(),R.color.main))
                        binding.stepOneCheckPwMsgTv.text = "비밀번호가 일치합니다"
                        binding.stepOneCheckPwMsgTv.visibility = View.VISIBLE // 비밀번호 밑에 안내창 보이게하기
                    }
                }

                checkingNext()
            }
        })

        // 비밀번호 확인 TEXTWATCHER
        binding.stepOneCheckPasswordEt.addTextChangedListener(object :TextWatcher{
            //TODO: 일단 TEXT바뀔때마다 VALIDATION검사하게했는데 , IOS는 TEXT입력 완료했을 때만 검사하기로 해두었대
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // text가 변경되기 전 호출

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                // text가 바뀔 때마다 호출된다.
            }

            override fun afterTextChanged(s: Editable?) {
                // text가 변경된 후 호출
                Log.d("pw", binding.stepOnePasswordEt.text.toString() +":"+binding.stepOneCheckPasswordEt.text.toString())
                if (binding.stepOnePasswordEt.text.toString()!=binding.stepOneCheckPasswordEt.text.toString()){ //일치하지 않으면,
                    binding.stepOneCheckPwMsgTv.setTextColor(ContextCompat.getColor(requireContext(),R.color.error))
                    binding.stepOneCheckPwMsgTv.text = "비밀번호를 다시 확인해주세요"
                    binding.stepOneCheckPwMsgTv.visibility = View.VISIBLE // 비밀번호 밑에 안내창 보이게하기
                } else{ // 비밀번호 일치하면,
                    binding.stepOneCheckPwMsgTv.setTextColor(ContextCompat.getColor(requireContext(),R.color.main))
                    binding.stepOneCheckPwMsgTv.text = "비밀번호가 일치합니다"
                    binding.stepOneCheckPwMsgTv.visibility = View.VISIBLE // 비밀번호 밑에 안내창 보이게하기
                }
                checkingNext()
            }
        })

        // 닉네임 TEXTWATCHER
        binding.stepOneNicknameEt.addTextChangedListener(object :TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // text가 변경되기 전 호출
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                // text가 바뀔 때마다 호출
                val nickPattern = Pattern.compile("^[a-zA-Z가-힣ㄱ-ㅎㅏ-ㅣ]{3,15}\$") //패턴 생성 (한글,또는 영문으로 이루어진 3-8자를 조건으로 둠)
                val macher = nickPattern.matcher(binding.stepOneNicknameEt.text.toString()) //사용자가 입력한 닉네임과 패턴 비교

                if(!macher.matches()){ //조건식에 맞지 않는 닉네임이 들어온다면
                    Log.d("Nick", "조건에 맞지 않는 닉네임 들어옴")
                    binding.stepOneNicknameMsgTv.setTextColor(ContextCompat.getColor(requireContext(),R.color.error))
                    binding.stepOneNicknameMsgTv.text = "3-15자 영문 혹은 한글로 입력해주세요"
                    binding.stepOneNicknameMsgTv.visibility = View.VISIBLE
                }else{ //조건에 맞는 닉네임이 들어오면 안내문 숨기기
                    binding.stepOneNicknameMsgTv.visibility = View.INVISIBLE
                }

                if (macher.matches()) {
                    binding.stepOneNicknameBtnO.visibility = View.VISIBLE
                    binding.stepOneNicknameBtnX.visibility = View.GONE
                } else {
                    binding.stepOneNicknameBtnO.visibility = View.GONE
                    binding.stepOneNicknameBtnX.visibility = View.VISIBLE
                }

                binding.stepOneNicknameMsgTv.text=""
                checkingNext()
            }

            override fun afterTextChanged(s: Editable?) {
                // text가 변경된 후 호출

            }

        })
    }


    //<클릭리스너>
    private fun initClickListener() {

        //아이디 중복확인 버튼
        binding.stepOneIdBtnO.setOnClickListener {
            val userId = binding.stepOneIdEt.text.toString() //사용자가 입력한 아이디 가져오기
            val signUpIdCheckRequest= SignUpIdCheckRequest(userId)
            signUpService.signUpIdCheckSender(signUpIdCheckRequest) //★아이디 중복확인하기
            Log.d("CheckId", "아이디 중복확인 리퀘스트 보냄")
        }

        //닉네임 중복확인 버튼
        binding.stepOneNicknameBtnO.setOnClickListener {
            val userNick = binding.stepOneNicknameEt.text.toString() //사용자가 입력한 닉네임 가져오기
            val signUpNickCheckRequest= SignUpNickCheckRequest(userNick)
            signUpService.signUpNickCheckSender(signUpNickCheckRequest) //★아이디 중복확인하기
            Log.d("CheckNick", "닉네임 중복확인 리퀘스트 보냄")
        }

        //다음 버튼
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

    //아이디 중복확인 결과
    override fun onSignUpIdCheckSuccess(message: String) {
        //사용가능한 아이디일 경우
        Log.d("CheckId", "사용 가능한 아이디입니다")
        binding.stepOneIdMsgTv.setTextColor(ContextCompat.getColor(requireContext(),R.color.main))
        binding.stepOneIdMsgTv.text = "사용 가능한 아이디입니다"
        binding.stepOneIdMsgTv.visibility = View.VISIBLE // 보이게 만들기

        Log.d(
            "CheckId",
            binding.stepOneIdMsgTv.text.toString() + "/" + binding.stepOneIdMsgTv.visibility.toString()
        )
        checkingNext()

    }

    override fun onSignUpIdCheckFailure(code: Int) {
        when(code){
            2603->{ //존재하는 아이디일 경우
                Log.d("CheckId", "중복된 아이디입니다")
                binding.stepOneIdMsgTv.setTextColor(ContextCompat.getColor(requireContext(),R.color.error))
                binding.stepOneIdMsgTv.text = "중복된 아이디입니다"
                binding.stepOneIdMsgTv.visibility = View.VISIBLE
            }
            4000->{ //서버오류
                Log.d("CheckId", "4000-서버오류입니다.")
            }
        }
    }

    //닉네임 중복확인 결과
    override fun onSignUpNickCheckSuccess(message: String) {
        //사용가능한 닉네임일 경우
        Log.d("CheckNick", "사용 가능한 닉네임입니다")
        binding.stepOneNicknameMsgTv.setTextColor(ContextCompat.getColor(requireContext(),R.color.main))
        /*binding.stepOneNicknameMsgTv.text = message*/
        binding.stepOneNicknameMsgTv.text = "사용 가능한 닉네임입니다"
        if(binding.stepOneNicknameMsgTv.visibility==View.INVISIBLE){
            binding.stepOneNicknameMsgTv.visibility = View.VISIBLE // 보이게 만들기
        }
        checkingNext()
    }

    override fun onSignUpNickCheckFailure(code: Int) {
        when(code){
            2600->{ //존재하는 닉네임일 경우
                Log.d("CheckNick", "중복된 닉네임입니다")
                binding.stepOneNicknameMsgTv.setTextColor(ContextCompat.getColor(requireContext(),R.color.error))
                binding.stepOneNicknameMsgTv.text = "중복된 닉네임입니다"
                if(binding.stepOneNicknameMsgTv.visibility==View.INVISIBLE){
                    binding.stepOneNicknameMsgTv.visibility = View.VISIBLE // 보이게 만들기
                }
            }
            4000->{ //서버오류
                Log.d("CheckNick", "4000-서버오류입니다.")
            }
        }
    }

    private fun checkingNext() {
        var check = (binding.stepOneIdMsgTv.text.toString()=="사용 가능한 아이디입니다")&&
                (binding.stepOnePwMsgTv.text.toString() == "사용 가능한 비밀번호입니다")&&
                (binding.stepOneCheckPwMsgTv.text.toString() == "비밀번호가 일치합니다") &&
                (binding.stepOneNicknameMsgTv.text.toString() == "사용 가능한 닉네임입니다")
        Log.d("Nextable", check.toString())
        Log.d("Nextable", binding.stepOneIdMsgTv.text.toString()+"/"+binding.stepOnePwMsgTv.text.toString()+"/"+binding.stepOneCheckPwMsgTv.text.toString()+"/"+binding.stepOneNicknameMsgTv.text.toString())

        if(check){ //다음으로 넘어갈 수 있으면
            Log.d("checkingNext: ok",check.toString())
            binding.stepOneNextBtn.isEnabled=true
            binding.stepOneNextBtn.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(requireContext(),R.color.main))
            binding.stepOneNextBtn.setTextColor(ContextCompat.getColor(requireContext(),R.color.white))
        }else{
            Log.d("checkingNext: nk",check.toString())
            /*binding.stepOneNextBtn.isEnabled=false*/ //디버깅용: 활성화 해제 안해놓음
            binding.stepOneNextBtn.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(requireContext(),R.color.gray_0))
            binding.stepOneNextBtn.setTextColor(ContextCompat.getColor(requireContext(),R.color.gray_2))
        }
    }
}