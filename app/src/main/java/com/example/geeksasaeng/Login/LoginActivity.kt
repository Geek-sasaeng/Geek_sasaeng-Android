package com.example.geeksasaeng.Login

import android.content.*
import android.text.*
import android.graphics.Color
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.example.geeksasaeng.databinding.ActivityLoginBinding
import com.example.geeksasaeng.Login.Retrofit.*
import com.example.geeksasaeng.MainActivity
import com.example.geeksasaeng.R
import com.example.geeksasaeng.Signup.Basic.SignUpActivity
import com.example.geeksasaeng.Signup.Basic.SignUpViewModel
import com.example.geeksasaeng.Signup.Naver.SignUpNaverActivity
import com.example.geeksasaeng.Signup.Naver.SignUpNaverViewModel
import com.example.geeksasaeng.Signup.Retrofit.*
import com.example.geeksasaeng.Utils.*
import com.google.gson.annotations.SerializedName
import com.navercorp.nid.NaverIdLoginSDK
import com.navercorp.nid.oauth.*
import com.navercorp.nid.profile.NidProfileCallback
import com.navercorp.nid.profile.data.NidProfileResponse

class LoginActivity: BaseActivity<ActivityLoginBinding>(ActivityLoginBinding::inflate), SignUpView, SignUpSocialView, LoginView {

    var checkPassword: String? = ""
    var emailId: Int? = null
    var informationAgreeStatus: String? = ""
    var loginId: String? = ""
    var nickname: String? = ""
    var password: String? = ""
    var phoneNumber: String? = ""
    var phoneNumberId: Int? = null
    var universityName: String? = ""

    private var OAUTH_CLIENT_ID: String = "Kvyd6swFfWHQJgjWaHr1"
    private var OAUTH_CLIENT_SECRET = "pq8VgJC5sn"
    private var OAUTH_CLIENT_NAME = "긱사생"

    private var autoLogin: SharedPreferences? = null
    private var autoLoginEditor: SharedPreferences.Editor? = null
    private var getAutoLogin: SharedPreferences? = null

    private lateinit var signUpNaverVM: SignUpNaverViewModel

    var autoJwt: String? = null
    var autoLoginid: String? = null
    var autoPassword: String? = null

    override fun initAfterBinding() {
        signUpNaverVM = ViewModelProvider(this).get(SignUpNaverViewModel::class.java)

        checkPassword = intent.getStringExtra("checkPassword")
        emailId = intent.getIntExtra("emailId", -1)
        informationAgreeStatus = intent.getStringExtra("informationAgreeStatus")
        loginId = intent.getStringExtra("loginId")
        nickname = intent.getStringExtra("nickname")
        password = intent.getStringExtra("password")
        phoneNumberId = intent.getIntExtra("phoneNumberId", -1)
        universityName = intent.getStringExtra("universityName")

//        showToast("checkpassword = $checkPassword / emailId = $emailId / informationAgreeStatus = $informationAgreeStatus / loginId = $loginId / nickname = $nickname / " +
//                "password = $password / phoneNumberId = $phoneNumberId / universityName = $universityName")

        Log.d("SIGNUP-RESPONSE", "checkpassword = $checkPassword / emailId = $emailId / informationAgreeStatus = $informationAgreeStatus / loginId = $loginId / nickname = $nickname / " +
                "password = $password / phoneNumberId = $phoneNumberId / universityName = $universityName")

        signup()

        /*
        네이버 회원가입 추가해주기
         */

        setTextChangedListener()
        initClickListener()
    }

    private fun login() {
        val loginDataService = LoginDataService()
        loginDataService.setLoginView(this)
        loginDataService.login(getLoginUser())
    }

    private fun getLoginUser(): Login {
        val loginId = binding.loginIdEt.text.toString()
        val password = binding.loginPwdEt.text.toString()

        return Login(loginId, password)
    }

    override fun onLoginSuccess(code : Int , result: LoginResult) {
        // 자동 로그인 수정 필요
        if (binding.loginAutologinCb.isChecked) {
            saveAutoLogin(result.jwt, binding.loginIdEt.text.toString(), binding.loginPwdEt.text.toString())
        }

        finish()
        changeActivity(MainActivity::class.java)
    }

    override fun onLoginFailure(message: String) {
        // 2011 : 비밀번호가 틀립니다
        // 2012 : 탈퇴한 회원
        // 2400 : 존재하지 않는 아이디
        showToast(message)
    }

    //회원가입하려는 유저를 SignUpRequest형태로 돌려주는 함수
    private fun getSignupUser(): SignUpRequest {
        // TODO: 일단은 약관동의에 다 "Y"로 넣어둠 (StepFive의 StepFiveStartBtn 클릭리스너 부분 참조)
        return SignUpRequest(checkPassword.toString(), emailId, informationAgreeStatus.toString(), loginId.toString(), nickname.toString(), password.toString(), phoneNumberId, universityName.toString())
    }

    //회원가입하는 함수
    private fun signup() {
        val signupDataService = SignupDataService()  //회원가입 서비스 객체 생성
        signupDataService.setSignUpView(this) // 뷰연결
        signupDataService.signUpSender(getSignupUser()) //★회원가입 진행
    }

    private fun signupSocial() {
        val signupDataService = SignupDataService()  //회원가입 서비스 객체 생성
        signupDataService.setSignUpView(this) // 뷰연결
        signupDataService.signUpSocialSender(getSignupUser()) //★회원가입 진행
    }

    private fun setTextChangedListener() {
        binding.loginIdEt.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}
            override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}
            override fun afterTextChanged(editable: Editable) {
                if (binding.loginIdEt.text.length >= 6 && binding.loginPwdEt.text.length >= 8) {
                    binding.loginLoginBtn.isEnabled = true;
                    binding.loginLoginBtn.setBackgroundResource(R.drawable.round_border_button);
                    binding.loginLoginBtn.setTextColor(Color.parseColor("#ffffff"))
                } else {
                    binding.loginLoginBtn.isEnabled = false;
                    binding.loginLoginBtn.setBackgroundResource(R.drawable.round_border_button_gray0);
                    binding.loginLoginBtn.setTextColor(Color.parseColor("#A8A8A8"))
                }
            }
        })

        binding.loginPwdEt.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}
            override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}
            override fun afterTextChanged(editable: Editable) {
                if (binding.loginIdEt.text.length >= 6 && binding.loginPwdEt.text.length >= 8) {
                    binding.loginLoginBtn.isEnabled = true;
                    binding.loginLoginBtn.setBackgroundResource(R.drawable.round_border_button);
                    binding.loginLoginBtn.setTextColor(Color.parseColor("#ffffff"))
                } else {
                    binding.loginLoginBtn.isEnabled = false;
                    binding.loginLoginBtn.setBackgroundResource(R.drawable.round_border_button_gray0);
                    binding.loginLoginBtn.setTextColor(Color.parseColor("#A8A8A8"))
                }
            }
        })
    }

    private fun initClickListener() {
        binding.loginLoginBtn.setOnClickListener {
            login()
            // login(false)
            // changeActivity(MainActivity::class.java)
        }

        binding.loginNaverBtn.setOnClickListener {
            changeActivity(SignUpNaverActivity::class.java)

            NaverIdLoginSDK.initialize(this, OAUTH_CLIENT_ID, OAUTH_CLIENT_SECRET, OAUTH_CLIENT_NAME)

            val profileCallback = object : NidProfileCallback<NidProfileResponse> {
                override fun onSuccess(response: NidProfileResponse) {
                    loginId = response.profile?.email.toString()
                    phoneNumber = response.profile?.mobile.toString()

                    Toast.makeText(this@LoginActivity, "네이버 아이디 로그인 성공!", Toast.LENGTH_SHORT).show()
                }
                override fun onFailure(httpStatus: Int, message: String) {
                    val errorCode = NaverIdLoginSDK.getLastErrorCode().code
                    val errorDescription = NaverIdLoginSDK.getLastErrorDescription()
                    Toast.makeText(this@LoginActivity, "errorCode: ${errorCode}\n" + "errorDescription: ${errorDescription}", Toast.LENGTH_SHORT).show()
                }
                override fun onError(errorCode: Int, message: String) {
                    onFailure(errorCode, message)
                }
            }

            val oauthLoginCallback = object : OAuthLoginCallback {
                override fun onSuccess() {
                    // 로그인 유저 정보 가져오기
                    NidOAuthLogin().callProfileApi(profileCallback)
                    // removeSP()
                    // saveSP(NaverIdLoginSDK.getAccessToken().toString())

                    signUpNaverVM.setLoginId(loginId)
                    signUpNaverVM.setPhoneNumber(phoneNumber)

                    changeActivity(SignUpNaverActivity::class.java)
                }
                override fun onFailure(httpStatus: Int, message: String) {
                    val errorCode = NaverIdLoginSDK.getLastErrorCode().code
                    val errorDescription = NaverIdLoginSDK.getLastErrorDescription()
                    // removeSP()
                    Log.d("NAVER-LOGIN", "FAILED : $errorCode $errorDescription")
                }
                override fun onError(errorCode: Int, message: String) {
                    onFailure(errorCode, message)
                }
            }

            NaverIdLoginSDK.authenticate(this, oauthLoginCallback)
        }

        binding.loginSignupBtn.setOnClickListener {
            changeActivity(SignUpActivity::class.java)
        }
    }

    //회원가입 성공/실패
    override fun onSignUpSuccess() {
        // TODO: 성공하면 나오는 화면 생기면 넣어주기
        showToast("성공")
        Log.d("signup", "회원가입에 성공하였습니다.")
    }

    override fun onSignUpFailure(message: String) {
        // 2006 : 중복되는 유저 아이디
        // 2007 : 중복되는 유저 이메일
        // 2008 : 존재하지 않는 학교 이름
        // 2201 : 회원 정보 동의 Status가 Y가 아닌 경우
        // 2205 : 존재하지 않는 회원 ID
        // 4000 : 서버 오류
        showToast("실패 $message")
        Log.d("signup", "회원가입에 실패하였습니다.\n$message")
    }

    override fun onSignUpSocialSuccess() {
        // TODO: 성공하면 나오는 화면 생기면 넣어주기
        showToast("성공")
        Log.d("signup", "회원가입에 성공하였습니다.")
    }

    override fun onSignUpSocialFailure(message: String) {
        showToast("실패 $message")
        Log.d("signup", "회원가입에 실패하였습니다.\n$message")
    }
}