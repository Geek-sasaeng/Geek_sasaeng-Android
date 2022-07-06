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
import com.example.geeksasaeng.Signup.Retrofit.SignupDataService
import com.example.geeksasaeng.Signup.SignUpView
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
            Toast.makeText(this, "LOGIN-BTN-RESPONSE", Toast.LENGTH_SHORT).show()
            // login()
            changeActivity(MainActivity::class.java)
        }

        binding.loginNaverBtn.setOnClickListener {
            val oAuthLoginCallback = object : OAuthLoginCallback {
                override fun onSuccess() {
                    // 네이버 로그인 API 호출 성공 시 유저 정보를 가져온다
                    NidOAuthLogin().callProfileApi(object : NidProfileCallback<NidProfileResponse> {
                        override fun onSuccess(result: NidProfileResponse) {
                            name = result.profile?.name.toString()
                            email = result.profile?.email.toString()
                            gender = result.profile?.gender.toString()
                            Log.e(TAG, "네이버 로그인한 유저 정보 - 이름 : $name")
                            Log.e(TAG, "네이버 로그인한 유저 정보 - 이메일 : $email")
                            Log.e(TAG, "네이버 로그인한 유저 정보 - 성별 : $gender")
                        }

                        override fun onError(errorCode: Int, message: String) {
                            //
                        }

                        override fun onFailure(httpStatus: Int, message: String) {
                            //
                        }
                    })
                }

                override fun onError(errorCode: Int, message: String) {
                    val naverAccessToken = NaverIdLoginSDK.getAccessToken()
                    Log.e(TAG, "naverAccessToken : $naverAccessToken")
                }

                override fun onFailure(httpStatus: Int, message: String) {
                    //
                }
            }

            NaverIdLoginSDK.initialize(this@MainActivity, getString(R.string.naver_client_id), getString(R.string.naver_client_secret), "앱 이름")
            NaverIdLoginSDK.authenticate(this@MainActivity, oAuthLoginCallback)
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