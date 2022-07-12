package com.example.geeksasaeng.Login

import android.app.Activity
import android.content.SharedPreferences
import android.graphics.Color
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.Toast
import com.example.geeksasaeng.Base.BaseActivity
import com.example.geeksasaeng.Data.Login
import com.example.geeksasaeng.Login.Retrofit.LoginDataService
import com.example.geeksasaeng.Login.Retrofit.LoginResult
import com.example.geeksasaeng.Login.Retrofit.LoginView
import com.example.geeksasaeng.MainActivity
import com.example.geeksasaeng.R
import com.example.geeksasaeng.Signup.Basic.SignUpActivity
import com.example.geeksasaeng.Signup.Naver.SignUpNaverActivity
import com.example.geeksasaeng.Signup.Retrofit.SignUpRequest
import com.example.geeksasaeng.Signup.Retrofit.SignupDataService
import com.example.geeksasaeng.Signup.Retrofit.SignUpView
import com.example.geeksasaeng.databinding.ActivityLoginBinding
import com.navercorp.nid.NaverIdLoginSDK
import com.navercorp.nid.oauth.OAuthLoginCallback

class LoginActivity: BaseActivity<ActivityLoginBinding>(ActivityLoginBinding::inflate), SignUpView, LoginView {

    var checkPassword: String = ""
    var email: String = ""
    var loginId: String = ""
    var informationAgreeStatus: String = "" //약관동의
    var nickname: String = ""
    var password: String = ""
    var phoneNumber: String = ""
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
        Log.d("LOGIN-RESPONSE", "AUTO ID = " + autoLoginid + " / AUTO PWD = " + autoPassword)

        if (autoLoginid != null && autoPassword != null) {
            login(true)
        }

        if (intent != null) {
            checkPassword = intent?.getStringExtra("checkPassword").toString()
            email = intent?.getStringExtra("email").toString()
            informationAgreeStatus = intent?.getStringExtra("informationAgreeStatus").toString() //약관동의 정보
            loginId = intent?.getStringExtra("loginId").toString()
            nickname = intent?.getStringExtra("nickname").toString()
            password = intent?.getStringExtra("password").toString()
            phoneNumber = intent?.getStringExtra("phoneNumber").toString()
            universityName = intent?.getStringExtra("universityName").toString()

            signup() //회원가입 진행
        }

        setTextChangedListener()
        initClickListener()
    }

    private fun login(auto: Boolean) {
        val loginDataService = LoginDataService()
        loginDataService.setLoginView(this)

        if (auto) {
            loginDataService.login(Login(autoLoginid, autoPassword))
        } else {
            loginDataService.login(getLoginUser())
        }

        Log.d("LOGIN-RESPONSE", "LoginActivity-login : Login Check")
    }

    private fun getLoginUser(): Login {
        val loginId = binding.loginIdEt.text.toString()
        val password = binding.loginPwdEt.text.toString()
        Log.d("LOGIN-RESPONSE", Login(loginId, password).toString())

        return Login(loginId, password)
    }

    private fun saveSP(jwt: String) {
        val spf = getSharedPreferences("autoLogin" , MODE_PRIVATE)
        val editor = spf.edit()

        editor.putString("jwt", jwt)
        editor?.putString("loginid", binding.loginIdEt.text.toString())
        editor?.putString("password", binding.loginPwdEt.text.toString())

        editor.apply()

        Log.d("LOGIN-RESPONSE", "AUTO-ID = " + binding.loginIdEt.text.toString() + "AUTO-PWD = " + binding.loginPwdEt.text.toString())
    }

    private fun removeSP() {
        val spf = getSharedPreferences("autoLogin" , MODE_PRIVATE)
        val editor = spf.edit()

        editor.clear()
        editor.commit()
    }

    override fun onLoginSuccess(code : Int , result: LoginResult) {
        when(code) {
            1000 -> {
                if (binding.loginAutologinCb.isChecked && binding.loginIdEt.text.isNotEmpty() && binding.loginPwdEt.text.isNotEmpty()) {
                    Log.d("LOGIN-RESPONSE", "IF CHECK")
                    removeSP()
                    saveSP(result.jwt)
                } else if (binding.loginAutologinCb.isChecked == false) {
                    removeSP()
                }
                finish()
                changeActivity(MainActivity::class.java)
            } else -> {
                Toast.makeText(this, "LoginActivity-onLoginSuccess : Fail", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onLoginFailure(code: Int, message: String) {
        Log.d("LOGIN-RESPONSE", "LoginActivity-onLoginFailure : Fail Code = $code")

        if (code == 2011) showToast(message)
        else if (code == 2012) showToast(message)
        else if (code == 2400) showToast(message)
    }

    //회원가입하려는 유저를 SignUpRequest형태로 돌려주는 함수
    private fun getSignupUser(): SignUpRequest {
        //TODO: 일단은 약관동의에 다 "Y"로 넣어둠 (StepFive의 StepFiveStartBtn 클릭리스너 부분 참조)
        Log.d("SIGNUP-RESPONSE", SignUpRequest(checkPassword, email, informationAgreeStatus, loginId, nickname, password, phoneNumber, universityName).toString())
        return SignUpRequest(checkPassword, email, informationAgreeStatus, loginId, nickname, password, phoneNumber, universityName)
    }

    //회원가입하는 함수
    private fun signup() {
        val signupDataService = SignupDataService()  //회원가입 서비스 객체 생성
        signupDataService.setSignUpView(this) // 뷰연결
        signupDataService.signUpSender(getSignupUser()) //★회원가입 진행

        Log.d("SIGNUP-RESPONSE", "LoginActivity-signup : Signup Check")
    }


    private fun setTextChangedListener() {
        binding.loginIdEt.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}
            override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}
            override fun afterTextChanged(editable: Editable) {
                Log.d("LOGIN-TEXT-RESPONSE", binding.loginIdEt.text.toString())
                Log.d("LOGIN-TEXT-RESPONSE", binding.loginPwdEt.text.toString())
                if (binding.loginIdEt.text.length >= 6 && binding.loginPwdEt.text.length >= 8) {
                    binding.loginLoginBtn.isClickable = true;
                    binding.loginLoginBtn.setBackgroundResource(R.drawable.round_border_button);
                    binding.loginLoginBtn.setTextColor(Color.parseColor("#ffffff"))
                } else {
                    binding.loginLoginBtn.isClickable = false;
                    binding.loginLoginBtn.isClickable = true;
                    binding.loginLoginBtn.setBackgroundResource(R.drawable.round_border_button_gray0);
                    binding.loginLoginBtn.setTextColor(Color.parseColor("#A8A8A8"))
                }
            }
        })

        binding.loginPwdEt.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}
            override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}
            override fun afterTextChanged(editable: Editable) {
                Log.d("LOGIN-TEXT-RESPONSE", binding.loginIdEt.text.toString())
                Log.d("LOGIN-TEXT-RESPONSE", binding.loginPwdEt.text.toString())

                if (binding.loginIdEt.text.length >= 6 && binding.loginPwdEt.text.length >= 8) {
                    binding.loginLoginBtn.isClickable = true;
                    binding.loginLoginBtn.setBackgroundResource(R.drawable.round_border_button);
                    binding.loginLoginBtn.setTextColor(Color.parseColor("#ffffff"))
                } else {
                    binding.loginLoginBtn.isClickable = false;
                    binding.loginLoginBtn.isClickable = true;
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
            NaverIdLoginSDK.initialize(this, OAUTH_CLIENT_ID, OAUTH_CLIENT_SECRET, OAUTH_CLIENT_NAME)

            val oauthLoginCallback = object : OAuthLoginCallback {
                override fun onSuccess() {
                    // 네이버 로그인 인증이 성공했을 때 수행할 코드 추가
                    Log.d("NAVER-LOGIN", "SUCCESS : " + NaverIdLoginSDK.getAccessToken() + " " + NaverIdLoginSDK.getRefreshToken() + " " + NaverIdLoginSDK.getExpiresAt().toString() + " " + NaverIdLoginSDK.getTokenType() + " " + NaverIdLoginSDK.getState().toString())
                    removeSP()
                    saveSP(NaverIdLoginSDK.getAccessToken().toString())
                    changeActivity(SignUpNaverActivity::class.java)
                }
                override fun onFailure(httpStatus: Int, message: String) {
                    val errorCode = NaverIdLoginSDK.getLastErrorCode().code
                    val errorDescription = NaverIdLoginSDK.getLastErrorDescription()
                    removeSP()
                    Log.d("NAVER-LOGIN", "FAILED : " + errorCode + " " + errorDescription)
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
        Log.d("signup", "회원가입에 성공하였습니다.")
    }

    override fun onSignUpFailure(code:Int) {
        when(code){
            2006-> Log.d("signup", "중복되는 유저 아이디입니다")
            2007-> Log.d("signup", "중복되는 유저 이메일입니다")
            2008-> Log.d("signup", "존재하지 않는 학교 이름입니다")
            2201-> Log.d("signup", "회원 정보동의 status가 Y가 아닙니다.")
            2205-> Log.d("signup", "존재하지 않는 회원 id 입니다.")
            4000-> Log.d("signup", "서버 오류입니다.")
        }
    }

}