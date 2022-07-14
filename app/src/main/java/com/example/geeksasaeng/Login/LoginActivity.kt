package com.example.geeksasaeng.Login

import android.app.Activity
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.Toast
import com.example.geeksasaeng.Base.BaseActivity
import com.example.geeksasaeng.Data.Login
import com.example.geeksasaeng.Data.Signup
import com.example.geeksasaeng.Login.Retrofit.LoginDataService
import com.example.geeksasaeng.Login.Retrofit.LoginResult
import com.example.geeksasaeng.Login.Retrofit.LoginView
import com.example.geeksasaeng.MainActivity
import com.example.geeksasaeng.R
import com.example.geeksasaeng.Signup.Basic.SignUpActivity
import com.example.geeksasaeng.Signup.Naver.SignUpNaverActivity
import com.example.geeksasaeng.Signup.Retrofit.SignUpView
import com.example.geeksasaeng.Signup.Retrofit.SignupDataService
import com.example.geeksasaeng.databinding.ActivityLoginBinding
import com.navercorp.nid.NaverIdLoginSDK
import com.navercorp.nid.oauth.NidOAuthLogin
import com.navercorp.nid.oauth.OAuthLoginCallback
import com.navercorp.nid.profile.NidProfileCallback
import com.navercorp.nid.profile.data.NidProfileResponse


class LoginActivity: BaseActivity<ActivityLoginBinding>(ActivityLoginBinding::inflate), SignUpView, LoginView {

    var checkPassword: String = ""
    var email: String = ""
    var loginId: String = ""
    var nickname: String = ""
    var password: String = ""
    var phoneNumber: String = ""
    var universityName: String = ""
    var status: String? = ""

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

    override fun onStart() {
        super.onStart()
        signUpService = SignupDataService() //서비스 객체 생성
        signUpService.setSignUpView(this)
    }

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
            loginId = intent?.getStringExtra("loginId").toString()
            nickname = intent?.getStringExtra("nickname").toString()
            password = intent?.getStringExtra("password").toString()
            phoneNumber = intent?.getStringExtra("phoneNumber").toString()
            universityName = intent?.getStringExtra("universityName").toString()

            if (intent?.getStringExtra("status") == "social")
                signupSocial()
            else
                signup()
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

        when (code) {
            2011 -> showToast(message)
            2012 -> showToast(message)
            2400 -> showToast(message)
        }
    }

    private fun getSignupUser(): Signup {
        Log.d("SIGNUP-RESPONSE", Signup(checkPassword, email, loginId, nickname, password, phoneNumber, universityName).toString())
        return Signup(checkPassword, email, loginId, nickname, password, phoneNumber, universityName)
    }

    private fun signup() {
        val signupDataService = SignupDataService()
        signupDataService.setSignUpView(this)
        signupDataService.signUp(getSignupUser())

        Log.d("SIGNUP-RESPONSE", "LoginActivity-signup : Signup Check")
    }

    private fun signupSocial() {
        val signupDataService = SignupDataService()
        signupDataService.setSignUpView(this)
        signupDataService.signUp(getSignupUser())

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

            val profileCallback = object : NidProfileCallback<NidProfileResponse> {
                override fun onSuccess(response: NidProfileResponse) {
                    loginId = response.profile?.email.toString()
                    phoneNumber = response.profile?.mobile.toString()

                    Log.d("NAVER-LOGIN", "LOGIN-ACTIVITY : BTN-CLICK1 : id = ${response.profile?.email} / phone = ${response.profile?.mobile}")
                    Log.d("NAVER-LOGIN", "LOGIN-ACTIVITY : BTN-CLICK2 : id = ${loginId} / phone = ${phoneNumber}")

                    Toast.makeText(this@LoginActivity, "네이버 아이디 로그인 성공!", Toast.LENGTH_SHORT).show()

                    val intent = Intent(this@LoginActivity, SignUpNaverActivity::class.java)
                    Log.d("NAVER-LOGIN", "1 LOGIN-ACTIVITY : LOGIN-CALLBACK : loginId = $loginId / phone = $phoneNumber")
                    intent.putExtra("loginId", loginId)
                    intent.putExtra("phoneNumber", phoneNumber)
                    startActivity(intent)
                }
                override fun onFailure(httpStatus: Int, message: String) {
                    val errorCode = NaverIdLoginSDK.getLastErrorCode().code
                    val errorDescription = NaverIdLoginSDK.getLastErrorDescription()
                    Toast.makeText(this@LoginActivity, "errorCode: ${errorCode}\n" +
                            "errorDescription: ${errorDescription}", Toast.LENGTH_SHORT).show()
                }
                override fun onError(errorCode: Int, message: String) {
                    onFailure(errorCode, message)
                }
            }

            val oauthLoginCallback = object : OAuthLoginCallback {
                override fun onSuccess() {
                    // 네이버 로그인 인증이 성공했을 때 수행할 코드 추가
                    // Log.d("NAVER-LOGIN", "SUCCESS : " + NaverIdLoginSDK.getAccessToken() + " " + NaverIdLoginSDK.getRefreshToken() + " " + NaverIdLoginSDK.getExpiresAt().toString() + " " + NaverIdLoginSDK.getTokenType() + " " + NaverIdLoginSDK.getState().toString())

                    NidOAuthLogin().callProfileApi(profileCallback)
                    removeSP()
                    saveSP(NaverIdLoginSDK.getAccessToken().toString())
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
            Log.d("NAVER-LOGIN", "NaverIdLoginSDK = ${NaverIdLoginSDK.authenticate(this, oauthLoginCallback).toString()}")
        }

        binding.loginSignupBtn.setOnClickListener {
            changeActivity(SignUpActivity::class.java)
        }
    }

    //회원가입 성공/실패
    override fun onSignUpSuccess() {
        Log.d("SIGNUP-RESPONSE", "SIGNUP $status : 회원가입에 성공하였습니다.")
    }

    override fun onSignUpFailure(code:Int) {
        Log.d("SIGNUP-RESPONSE", "SIGNUP $status : 회원가입에 실패하였습니다.")
        
        when(code){
            2006-> Log.d("signup", "중복되는 유저 아이디입니다")
            2007-> Log.d("signup", "중복되는 유저 이메일입니다")
            2008-> Log.d("signup", "존재하지 않는 학교 이름입니다")
            2201-> Log.d("signup", "회원 정보동의 status가 Y가 아닙니다.")
            2205-> Log.d("signup", "존재하지 않는 회원 id 입니다.")
            4000-> Log.d("signup", "서버 오류입니다.")
        }
    }

    /**
     * 로그인
     * authenticate() 메서드를 이용한 로그인 */
    private fun startNaverLogin(){
    }
}