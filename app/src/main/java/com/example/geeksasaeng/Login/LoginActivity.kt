package com.example.geeksasaeng.Login

import android.app.Activity
import android.content.*
import android.text.*
import android.graphics.Color
import android.util.Log
import android.widget.Toast
import com.example.geeksasaeng.databinding.ActivityLoginBinding
import com.example.geeksasaeng.Utils.BaseActivity
import com.example.geeksasaeng.Login.Retrofit.*
import com.example.geeksasaeng.MainActivity
import com.example.geeksasaeng.R
import com.example.geeksasaeng.Signup.Basic.SignUpActivity
import com.example.geeksasaeng.Signup.Naver.SignUpNaverActivity
import com.example.geeksasaeng.Signup.Retrofit.*
import com.navercorp.nid.NaverIdLoginSDK
import com.navercorp.nid.oauth.*
import com.navercorp.nid.profile.NidProfileCallback
import com.navercorp.nid.profile.data.NidProfileResponse

class LoginActivity: BaseActivity<ActivityLoginBinding>(ActivityLoginBinding::inflate), SignUpView, LoginView {

    var checkPassword: String = ""
    var emailId: Int? = null
    var loginId: String = ""
    var informationAgreeStatus: String = "" //약관동의
    var nickname: String = ""
    var password: String = ""
    var phoneNumber: String = ""
    var phoneNumberId: Int? = null
    var universityName: String = ""

    private var OAUTH_CLIENT_ID: String = "czrW_igO4TDdAeE5WhGW"
    private var OAUTH_CLIENT_SECRET = "syoXsVLKN1"
    private var OAUTH_CLIENT_NAME = "긱사생"

    private var autoLogin: SharedPreferences? = null
    private var autoLoginEditor: SharedPreferences.Editor? = null
    private var getAutoLogin: SharedPreferences? = null

    private lateinit var signUpService : SignupDataService

    var autoJwt: String? = null
    var autoLoginid: String? = null
    var autoPassword: String? = null

    override fun initAfterBinding() {
        getAutoLogin = getSharedPreferences("autoLogin", Activity.MODE_PRIVATE)
        autoJwt = getAutoLogin?.getString("jwt", null).toString()
        autoLoginid = getAutoLogin?.getString("loginid", null).toString()
        autoPassword = getAutoLogin?.getString("password", null).toString()

        if (autoLoginid != null && autoPassword != null) {
            login(true)
        }

        if (intent != null) {
            checkPassword = intent?.getStringExtra("checkPassword").toString()
            emailId = intent?.getIntExtra("email", -1)
            informationAgreeStatus = intent?.getStringExtra("informationAgreeStatus").toString() //약관동의 정보
            loginId = intent?.getStringExtra("loginId").toString()
            nickname = intent?.getStringExtra("nickname").toString()
            password = intent?.getStringExtra("password").toString()
            phoneNumberId = intent?.getIntExtra("phoneNumberId", -1)
            universityName = intent?.getStringExtra("universityName").toString()

            showToast("check = $checkPassword / emailId = $emailId / info = $informationAgreeStatus / loginId = $loginId / nickname = $nickname / password = $password / phoneNumberId = $phoneNumberId")
            signup() //위의 정보를 바탕으로 회원가입 진행
        }

        setTextChangedListener()
        initClickListener()
    }

    private fun login(auto: Boolean) {
        val loginDataService = LoginDataService()
        loginDataService.setLoginView(this)

//        if (auto) {
//            loginDataService.login(Login(autoLoginid, autoPassword))
//        } else {
//            loginDataService.login(getLoginUser())
//        }

        loginDataService.login(getLoginUser())
    }

    private fun getLoginUser(): Login {
        val loginId = binding.loginIdEt.text.toString()
        val password = binding.loginPwdEt.text.toString()

        return Login(loginId, password)
    }

    private fun saveSP(jwt: String) {
        val spf = getSharedPreferences("autoLogin" , MODE_PRIVATE)
        val editor = spf.edit()

        editor.putString("jwt", jwt)
        editor?.putString("loginid", binding.loginIdEt.text.toString())
        editor?.putString("password", binding.loginPwdEt.text.toString())

        editor.apply()
    }

    private fun removeSP() {
        val spf = getSharedPreferences("autoLogin" , MODE_PRIVATE)
        val editor = spf.edit()
        editor.clear()
        editor.commit()
    }

    override fun onLoginSuccess(code : Int , result: LoginResult) {
        // 자동 로그인 수정 필요
        if (binding.loginAutologinCb.isChecked && binding.loginIdEt.text.isNotEmpty() && binding.loginPwdEt.text.isNotEmpty()) {
            removeSP()
            saveSP(result.jwt)
        } else if (!binding.loginAutologinCb.isChecked) {
            removeSP()
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
        return SignUpRequest(checkPassword, emailId, informationAgreeStatus, loginId, nickname, password, phoneNumberId, universityName)
    }

    //회원가입하는 함수
    private fun signup() {
        val signupDataService = SignupDataService()  //회원가입 서비스 객체 생성
        signupDataService.setSignUpView(this) // 뷰연결
        signupDataService.signUpSender(getSignupUser()) //★회원가입 진행
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
            login(false)
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
                    removeSP()
                    saveSP(NaverIdLoginSDK.getAccessToken().toString())

                    val intent = Intent(this@LoginActivity, SignUpNaverActivity::class.java)
                    Log.d("NAVER-LOGIN", "LOGIN-ACTIVITY : LOGIN-CALLBACK : loginId = $loginId / phone = $phoneNumber")
                    intent.putExtra("loginId", loginId)
                    intent.putExtra("phoneNumber", phoneNumber)
                    startActivity(intent)
                }
                override fun onFailure(httpStatus: Int, message: String) {
                    val errorCode = NaverIdLoginSDK.getLastErrorCode().code
                    val errorDescription = NaverIdLoginSDK.getLastErrorDescription()
                    removeSP()
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
}