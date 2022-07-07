package com.example.geeksasaeng.Login

import android.content.Intent
import android.util.Log
import android.widget.Toast
import com.example.geeksasaeng.Base.BaseActivity
import com.example.geeksasaeng.Data.Login
import com.example.geeksasaeng.Data.Signup
import com.example.geeksasaeng.Login.Retrofit.LoginDataService
import com.example.geeksasaeng.Login.Retrofit.LoginResult
import com.example.geeksasaeng.MainActivity
import com.example.geeksasaeng.SignUpActivity
import com.example.geeksasaeng.SignUpNaverActivity
import com.example.geeksasaeng.Signup.Retrofit.SignupDataService
import com.example.geeksasaeng.Signup.SignUpView
import com.example.geeksasaeng.Signup.StepNaverOneFragment
import com.example.geeksasaeng.databinding.ActivityLoginBinding
import com.navercorp.nid.NaverIdLoginSDK
import com.navercorp.nid.oauth.OAuthLoginCallback
import com.nhn.android.naverlogin.OAuthLogin

// import com.navercorp.nid.NaverIdLoginSDK
// import com.navercorp.nid.oauth.NidOAuthLogin
// import com.navercorp.nid.oauth.OAuthLoginCallback
// import com.navercorp.nid.profile.NidProfileCallback
// import com.navercorp.nid.profile.data.NidProfileResponse

class LoginActivity: BaseActivity<ActivityLoginBinding>(ActivityLoginBinding::inflate), SignUpView, LoginView {

    var checkPassword: String = ""
    var email: String = ""
    var loginId: String = ""
    var nickname: String = ""
    var password: String = ""
    var phoneNumber: String = ""
    var universityName: String = ""

    private var OAUTH_CLIENT_ID: String = "czrW_igO4TDdAeE5WhGW"
    private var OAUTH_CLIENT_SECRET = "syoXsVLKN1"
    private var OAUTH_CLIENT_NAME = "긱사생"

    private var mOAuthLoginInstance: OAuthLogin? = null;

    override fun initAfterBinding() {
        if (intent != null) {
            checkPassword = intent?.getStringExtra("checkPassword").toString()
            email = intent?.getStringExtra("email").toString()
            loginId = intent?.getStringExtra("loginId").toString()
            nickname = intent?.getStringExtra("nickname").toString()
            password = intent?.getStringExtra("password").toString()
            phoneNumber = intent?.getStringExtra("phoneNumber").toString()
            universityName = intent?.getStringExtra("universityName").toString()

            signup()
        }

        binding.loginLoginBtn.setOnClickListener {
            // login()
            changeActivity(MainActivity::class.java)
        }

        binding.loginNaverBtn.setOnClickListener {
            NaverIdLoginSDK.initialize(this, OAUTH_CLIENT_ID, OAUTH_CLIENT_SECRET, OAUTH_CLIENT_NAME)

            val oauthLoginCallback = object : OAuthLoginCallback {
                override fun onSuccess() {
                    // 네이버 로그인 인증이 성공했을 때 수행할 코드 추가
                    Log.d("NAVER-LOGIN", "SUCCESS : " + NaverIdLoginSDK.getAccessToken() + " " + NaverIdLoginSDK.getRefreshToken() + " " + NaverIdLoginSDK.getExpiresAt().toString() + " " + NaverIdLoginSDK.getTokenType() + " " + NaverIdLoginSDK.getState().toString())
                    changeActivity(SignUpNaverActivity::class.java)
                }
                override fun onFailure(httpStatus: Int, message: String) {
                    val errorCode = NaverIdLoginSDK.getLastErrorCode().code
                    val errorDescription = NaverIdLoginSDK.getLastErrorDescription()
                    Log.d("NAVER-LOGIN", "FAILED : " + errorCode + " " + errorDescription)
                }
                override fun onError(errorCode: Int, message: String) {
                    onFailure(errorCode, message)
                }
            }

            NaverIdLoginSDK.authenticate(this, oauthLoginCallback)

            // changeActivity(SignUpNaverActivity::class.java)
        }

        binding.loginSignupBtn.setOnClickListener {
            changeActivity(SignUpActivity::class.java)
        }
    }

    private fun login() {
        val loginDataService = LoginDataService()
        loginDataService.setLoginView(this)
        loginDataService.login(getLoginUser())

        Log.d("LOGIN-RESPONSE", "LoginActivity-login : Login Check")
    }

    private fun getLoginUser(): Login {
        val loginId = binding.loginIdTv.text.toString()
        val password = binding.loginPwdTv.text.toString()
        Log.d("LOGIN-RESPONSE", Login(loginId, password).toString())

        return Login(loginId, password)
    }

    private fun saveJwt(jwt: String) {
        val spf = getSharedPreferences("auth2" , MODE_PRIVATE)
        val editor = spf.edit()

        editor.putString("jwt", jwt)
        editor.apply()
    }

    override fun onLoginSuccess(code : Int , result: LoginResult) {
        when(code) {
            1000 -> {
                saveJwt(result.jwt)
                changeActivity(MainActivity::class.java)
            } else -> {
                Toast.makeText(this, "LoginActivity-onLoginSuccess : Fail", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onLoginFailure() {
        Log.d("LOGIN-RESPONSE", "LoginActivity-onLoginFailure : Login Check")
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

    override fun onSignUpSuccess() {
        Log.d("SIGNUP-RESPONSE", "LoginActivity-onSignUpSuccess : Signup Check")
        startActivity(Intent(this, LoginActivity::class.java))
    }

    override fun onSignUpFailure() {
        Log.d("SIGNUP-RESPONSE", "LoginActivity-onSignUpFailure : Signup Check")
    }
}