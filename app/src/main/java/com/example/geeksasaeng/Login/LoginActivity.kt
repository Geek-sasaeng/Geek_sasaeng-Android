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
import com.example.geeksasaeng.MainActivity
import com.example.geeksasaeng.R
import com.example.geeksasaeng.Signup.Basic.SignUpActivity
import com.example.geeksasaeng.Signup.Naver.SignUpNaverActivity
import com.example.geeksasaeng.Signup.Retrofit.SignupDataService
import com.example.geeksasaeng.Signup.SignUpView
import com.example.geeksasaeng.databinding.ActivityLoginBinding
import com.navercorp.nid.NaverIdLoginSDK
import com.navercorp.nid.oauth.OAuthLoginCallback

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

    private var autoLogin: SharedPreferences? = null
    private var autoLoginEditor: SharedPreferences.Editor? = null
    private var getAutoLogin: SharedPreferences? = null

    var autoLoginid: String? = null
    var autoPassword: String? = null

    override fun initAfterBinding() {
        getAutoLogin = getSharedPreferences("autoLogin", Activity.MODE_PRIVATE)
        var autoJwt = getAutoLogin?.getString("jwt", null).toString()

        Log.d("LOGIN-RESPONSE", "JWT = $autoJwt")

        if (autoLoginid != null && autoPassword != null) {
            // login(true)
        }

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
        editor.apply()

        Log.d("LOGIN-RESPONSE", "jwt = $jwt")
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
                if (binding.loginAutologinCb.isChecked)
                    saveSP(result.jwt)
                else removeSP()

                changeActivity(MainActivity::class.java)
            } else -> {
                Toast.makeText(this, "LoginActivity-onLoginSuccess : Fail", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onLoginFailure(code: Int, message: String) {
        Log.d("LOGIN-RESPONSE", "LoginActivity-onLoginFailure : Fail Code = $code")

        if (code == 2011) showToast(message)
        else if (code == 2400) showToast(message)
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
                    binding.loginLoginBtn.setBackgroundResource(R.drawable.round_border_button_gray);
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
                    binding.loginLoginBtn.setBackgroundResource(R.drawable.round_border_button_gray);
                    binding.loginLoginBtn.setTextColor(Color.parseColor("#A8A8A8"))
                }
            }
        })
    }

    private fun initClickListener() {

        binding.loginLoginBtn.setOnClickListener {
            if (binding.loginAutologinCb.isChecked) {
                // SharedPreferences 설정
                autoLogin = getSharedPreferences("autoLogin", Activity.MODE_PRIVATE)
                autoLoginEditor = autoLogin?.edit()

                autoLoginEditor?.putString("loginid", binding.loginIdEt.text.toString())
                autoLoginEditor?.putString("password", binding.loginPwdEt.text.toString())
                autoLoginEditor?.commit()

                Log.d("LOGIN-RESPONSE", "AUTO-ID = " + binding.loginIdEt.text.toString() + "AUTO-PWD = " + binding.loginPwdEt.text.toString())
            }

            login(false)
            // changeActivity(MainActivity::class.java)
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
        }

        binding.loginSignupBtn.setOnClickListener {
            changeActivity(SignUpActivity::class.java)
        }
    }
}