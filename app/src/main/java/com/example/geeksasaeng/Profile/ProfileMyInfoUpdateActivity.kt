package com.example.geeksasaeng.Profile

import android.content.Intent
import android.content.res.ColorStateList
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.example.geeksasaeng.MainActivity
import com.example.geeksasaeng.R
import com.example.geeksasaeng.Signup.Retrofit.SignUpNickCheckRequest
import com.example.geeksasaeng.Signup.Retrofit.SignUpNickCheckView
import com.example.geeksasaeng.Signup.Retrofit.SignupDataService
import com.example.geeksasaeng.Utils.*
import com.example.geeksasaeng.databinding.ActivityProfileMyInfoUpdateBinding
import java.util.regex.Pattern

class ProfileMyInfoUpdateActivity: BaseActivity<ActivityProfileMyInfoUpdateBinding>(ActivityProfileMyInfoUpdateBinding::inflate),
    SignUpNickCheckView {

    private var dormitoryId = 1 //default 기숙사 아이디
    private lateinit var nickName :String  //기존 닉네임
    private lateinit var loginId :String  //로그인 id -api용
    private lateinit var signUpService : SignupDataService //닉네임 중복확인용

    override fun initAfterBinding() {
        initData()
        initView()
        initClickListener()
        initRadioButton()
        initTextWatcher()
    }

    private fun initData() {
        //기존 정보 불러오기

        //로그인 아이디
        loginId = intent.getStringExtra("loginId").toString()
        //로그인아이디는 getLoginId로 안꺼내 쓰는 이유는 로그인 api 반환값에 로그인아이디가 없어서 sharedPreference에 저장이 안되어있기 때문이다.

        //닉네임
        nickName = getNickname()!!
        binding.profileMyInfoUpdateNicknameEt.hint = nickName

        //기숙사 아이디
        dormitoryId = getDormitoryId()
        when(dormitoryId){
            1-> binding.profileMyInfoUpdateDormitoryRb1.isChecked = true
            2-> binding.profileMyInfoUpdateDormitoryRb2.isChecked = true
            3-> binding.profileMyInfoUpdateDormitoryRb3.isChecked = true
            4-> binding.profileMyInfoUpdateDormitoryRb4.isChecked = true
            5-> binding.profileMyInfoUpdateDormitoryRb5.isChecked = true
            6-> binding.profileMyInfoUpdateDormitoryRb6.isChecked = true
            else->{}
        }

        //사용자 프로필
        Glide.with(this)
            .load(getProfileImgUrl())
            .into(binding.profileMyInfoUpdateUserImgIv)
    }

    private fun initView() {
        signUpService = SignupDataService() // 서비스 객체 생성
        signUpService.setSignUpNickCheckView(this)//닉네임 중복확인 뷰 연결
    }

    private fun initTextWatcher() {
        binding.profileMyInfoUpdateNicknameEt.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                binding.profileMyInfoUpdateNicknameCheckBtn.visibility = View.VISIBLE //중복확인 버튼
                binding.profileMyInfoUpdateNicknameCheckConfirmed.visibility = View.INVISIBLE //확인 완료 버튼
                val nickPattern = Pattern.compile("^[a-zA-Z가-힣ㄱ-ㅎㅏ-ㅣ]{3,8}\$") //패턴 생성 (한글,또는 영문으로 이루어진 3-8자를 조건으로 둠)
                val macher = nickPattern.matcher(binding.profileMyInfoUpdateNicknameEt.text.toString()) //사용자가 입력한 닉네임과 패턴 비교

                if (macher.matches()) { //조건식에 맞는 닉네임이 들어온다면
                    binding.profileMyInfoUpdateNicknameCheckBtn.isEnabled = true
                    binding.profileMyInfoUpdateNicknameCheckBtn.isClickable = true
                    binding.profileMyInfoUpdateNicknameExplainationTv.visibility = View.INVISIBLE
                } else{
                    binding.profileMyInfoUpdateNicknameCheckBtn.isEnabled = false
                    binding.profileMyInfoUpdateNicknameCheckBtn.isClickable = false
                    binding.profileMyInfoUpdateNicknameExplainationTv.visibility = View.VISIBLE
                    binding.profileMyInfoUpdateNicknameExplainationTv.setTextColor(ContextCompat.getColor(applicationContext,R.color.main))
                    binding.profileMyInfoUpdateNicknameExplainationTv.text = "3-8자 영문 혹은 한글로 입력해주세요"
                    if(binding.profileMyInfoUpdateNicknameExplainationTv.visibility == View.INVISIBLE){
                        binding.profileMyInfoUpdateNicknameExplainationTv.visibility = View.VISIBLE // 보이게 만들기
                    }
                }
                checkingModifiability()
            }
        })
    }

    private fun initRadioButton() { // 기숙사 선택 라디오 버튼

        binding.profileMyInfoUpdateDormitoryRg1.setOnCheckedChangeListener { group, checkedId ->
            if (checkedId != -1) { // checkId != -1이 무슨 뜻이더랑..!
                binding.profileMyInfoUpdateDormitoryRg2.clearCheck()
                binding.profileMyInfoUpdateDormitoryRg1.check(checkedId)
            }
            when(checkedId){
                R.id.profile_my_info_update_dormitory_rb1 -> dormitoryId = 1
                R.id.profile_my_info_update_dormitory_rb2 -> dormitoryId = 2
                R.id.profile_my_info_update_dormitory_rb3 -> dormitoryId = 3
                else-> {}
            }
            checkingModifiability()
        }

        binding.profileMyInfoUpdateDormitoryRg2.setOnCheckedChangeListener { group, checkedId ->
            if (checkedId != -1) {
                binding.profileMyInfoUpdateDormitoryRg1.clearCheck() // 1번째열의 check는 지워주기
                binding.profileMyInfoUpdateDormitoryRg2.check(checkedId)
            }
            when(checkedId){
                R.id.profile_my_info_update_dormitory_rb4 -> dormitoryId = 4
                R.id.profile_my_info_update_dormitory_rb5 -> dormitoryId = 5
                R.id.profile_my_info_update_dormitory_rb6 -> dormitoryId = 6
                else-> {}
            }
            checkingModifiability()
        }
    }

    private fun initClickListener() {
        binding.profileMyInfoUpdateBackBtn.setOnClickListener { //뒤로
            finish()
        }

        binding.profileMyInfoUpdateCompleteTv.setOnClickListener { //완료 버튼
            val dialogProfileUpdate = DialogProfileUpdate()
            val bundle = Bundle()

            dialogProfileUpdate.arguments= bundle
            dialogProfileUpdate.show(supportFragmentManager, "DialogProfileUpdate")
        }

        binding.profileMyInfoUpdateNicknameCheckBtn.setOnClickListener {
            //닉네임 중복확인 버튼 - 닉네임 validation 맞을 때만 활성화된다.
            val userNick = binding.profileMyInfoUpdateNicknameEt.text.toString() //사용자가 입력한 닉네임 가져오기
            Log.d("nick", userNick)
            val signUpNickCheckRequest= SignUpNickCheckRequest(userNick)
            signUpService.signUpNickCheckSender(signUpNickCheckRequest) //★닉네임 중복확인하기
        }

        binding.profileMyInfoUpdatePasswordChangeBtn.setOnClickListener { // 비밀번호 변경
            startActivity(Intent(this, ProfileMyInfoUpdatePwdActivity::class.java))
        }
    }

    override fun onSignUpNickCheckSuccess(message: String) {
        // 사용가능한 닉네임일 경우
        if(binding.profileMyInfoUpdateNicknameExplainationTv.visibility == View.VISIBLE){
            binding.profileMyInfoUpdateNicknameExplainationTv.visibility = View.INVISIBLE // edit text 밑에 설명 textview 안 보이게 만들기
        }
        //버튼 이미지 확인 완료로 변경
        binding.profileMyInfoUpdateNicknameCheckBtn.visibility = View.INVISIBLE //중복확인버튼
        binding.profileMyInfoUpdateNicknameCheckConfirmed.visibility = View.VISIBLE //확인 완료 버튼
        checkingModifiability()
    }

    override fun onSignUpNickCheckFailure(message: String) {
        binding.profileMyInfoUpdateNicknameExplainationTv.setTextColor(ContextCompat.getColor(applicationContext,R.color.error))
        binding.profileMyInfoUpdateNicknameExplainationTv.text = "중복된 닉네임입니다"
        if(binding.profileMyInfoUpdateNicknameExplainationTv.visibility == View.INVISIBLE){
            binding.profileMyInfoUpdateNicknameExplainationTv.visibility = View.VISIBLE // 보이게 만들기
        }
        checkingModifiability()
    }

    private fun checkingModifiability() {
        var modifiability = (binding.profileMyInfoUpdateNicknameCheckConfirmed.visibility==View.VISIBLE) ||
                (dormitoryId!= getDormitoryId())
        // 확인완료가 보이거나, 기숙사id가 원래 기숙사id랑 다르면

        if (modifiability) { //수정이 가능하면
            binding.profileMyInfoUpdateCompleteTv.isClickable = true
            binding.profileMyInfoUpdateCompleteTv.setTextColor(ContextCompat.getColor(applicationContext,R.color.main))

        } else{
            binding.profileMyInfoUpdateCompleteTv.isClickable = false
            binding.profileMyInfoUpdateCompleteTv.setTextColor(ContextCompat.getColor(applicationContext,R.color.bababa_color))
        }
    }
}