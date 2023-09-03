package com.example.geeksasaeng.Login

import android.content.*
import android.text.*
import android.graphics.Color
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.example.geeksasaeng.BuildConfig
import com.example.geeksasaeng.Config.Secret.Secret.OAUTH_CLIENT_ID
import com.example.geeksasaeng.Config.Secret.Secret.OAUTH_CLIENT_NAME
import com.example.geeksasaeng.Config.Secret.Secret.OAUTH_CLIENT_SECRET
import com.example.geeksasaeng.Home.HomeFragment
// import com.example.geeksasaeng.Secret.OAUTH_CLIENT_ID
// import com.example.geeksasaeng.Secret.OAUTH_CLIENT_NAME
// import com.example.geeksasaeng.Secret.OAUTH_CLIENT_SECRET
import com.example.geeksasaeng.databinding.ActivityLoginBinding
import com.example.geeksasaeng.Login.Retrofit.*
import com.example.geeksasaeng.MainActivity
import com.example.geeksasaeng.R
import com.example.geeksasaeng.Signup.Basic.SignUpActivity
import com.example.geeksasaeng.Signup.Naver.SignUpNaverActivity
import com.example.geeksasaeng.Signup.Naver.SignUpNaverViewModel
import com.example.geeksasaeng.Signup.Retrofit.*
import com.example.geeksasaeng.Utils.*
import com.google.firebase.messaging.FirebaseMessaging
import com.navercorp.nid.NaverIdLoginSDK
import com.navercorp.nid.oauth.*
import com.navercorp.nid.profile.NidProfileCallback
import com.navercorp.nid.profile.data.NidProfileResponse

class LoginActivity: BaseActivity<ActivityLoginBinding>(ActivityLoginBinding::inflate), SignUpSocialView, LoginView, SocialLoginView {
    var loginId: String? = ""
    var nickname: String? = ""
    var phoneNumber: String? = ""
    private lateinit var fcmToken: String

    private lateinit var signUpNaverVM: SignUpNaverViewModel

    override fun initAfterBinding() {
        signUpNaverVM = ViewModelProvider(this).get(SignUpNaverViewModel::class.java)
        getFireBasetoken()
        setTextChangedListener()
        initClickListener()
    }

    private fun initClickListener() {
        binding.loginLoginBtn.setOnClickListener {
            login()
        }

        // 네이버 로그인
        binding.loginNaverBtn.setOnClickListener {
            NaverIdLoginSDK.initialize(this, OAUTH_CLIENT_ID, OAUTH_CLIENT_SECRET, OAUTH_CLIENT_NAME)
            Log.d("API-TEST", "OAUTH_CLIENT_ID = ${OAUTH_CLIENT_ID}\nOAUTH_CLIENT_SECRET = ${OAUTH_CLIENT_SECRET}\nOAUTH_CLIENT_NAME = ${OAUTH_CLIENT_NAME}")

            val profileCallback = object : NidProfileCallback<NidProfileResponse> {
                override fun onSuccess(response: NidProfileResponse) {
                    Log.d("API-TEST", "profileCallback onSuccess")
                    loginId = response.profile?.email.toString()
                    phoneNumber = response.profile?.mobile.toString()
                }
                override fun onFailure(httpStatus: Int, message: String) {
                    Log.d("API-TEST", "profileCallback onFailure")
                    val errorCode = NaverIdLoginSDK.getLastErrorCode().code
                    val errorDescription = NaverIdLoginSDK.getLastErrorDescription()
                    Toast.makeText(this@LoginActivity, "errorCode: ${errorCode}\n" + "errorDescription: ${errorDescription}", Toast.LENGTH_SHORT).show()
                    Log.d("API-TEST", "profileCallback errorCode = $errorCode / errorDescription = $errorDescription")
                }
                override fun onError(errorCode: Int, message: String) {
                    onFailure(errorCode, message)
                    Log.d("API-TEST", "profileCallback onError")
                }
            }

            val oauthLoginCallback = object : OAuthLoginCallback {
                override fun onSuccess() {
                    Log.d("API-TEST", "oauthLoginCallback onSuccess")
                    // 로그인 유저 정보 가져오기
                    NidOAuthLogin().callProfileApi(profileCallback)
                    // removeSP()
                    // saveSP(NaverIdLoginSDK.getAccessToken().toString())

                    // accessToken 서버로 보냄
                    val accessToken = NaverIdLoginSDK.getAccessToken().toString()
                    Log.d("API-TEST", "accessToken = $accessToken")
                    socialLogin(accessToken)
                }
                override fun onFailure(httpStatus: Int, message: String) {
                    val errorCode = NaverIdLoginSDK.getLastErrorCode().code
                    val errorDescription = NaverIdLoginSDK.getLastErrorDescription()
                    // removeSP()
                    Log.d("API-TEST", "oauthLoginCallback onFailure")
                    Log.d("API-TEST", "oauthLoginCallback errorCode = $errorCode / errorDescription = $errorDescription")
                }
                override fun onError(errorCode: Int, message: String) {
                    Log.d("API-TEST", "oauthLoginCallback onError")
                    onFailure(errorCode, message)
                }
            }

            NaverIdLoginSDK.authenticate(this, oauthLoginCallback)
        }

        binding.loginSignupBtn.setOnClickListener {
            changeActivity(SignUpActivity::class.java)
        }
    }

    private fun login() {
        val loginDataService = LoginDataService()
        loginDataService.setLoginView(this)
        loginDataService.login(getLoginUser())
    }

    private fun socialLogin(accessToken: String) {
        val socialLoginDataService = LoginDataService()
        socialLoginDataService.setSocialLoginView(this)
        socialLoginDataService.socialLogin(SocialLogin(accessToken, fcmToken))
    }

    private fun getLoginUser(): Login {
        val loginId = binding.loginIdEt.text.toString()
        val password = binding.loginPwdEt.text.toString()
        return Login(loginId, password, fcmToken)
    }

    override fun onLoginSuccess(code : Int , result: LoginResult) {
        val jwt = result.jwt
        saveIsSocial(false)
        // 자동 로그인
        if (binding.loginAutologinCb.isChecked) {
            setAutoLogin(jwt)
        }
        saveJwt(jwt) //TODO: 얜 왜 필요한거지?
        if(result.loginStatus=="NEVER"){ //첫 로그인이면
            val intent = Intent(this, DormitoryActivity::class.java)
            intent.putExtra("nickName", result.nickName) // DormitoryActivity로 닉네임 넘겨줌
            startActivity(intent)
        }else{
            Log.d("cherry", "첫 로그인이 아님")
            saveNickname(result.nickName)
            saveMemberId(result.memberId)
            saveProfileImgUrl(result.profileImgUrl)
            saveDormitory("제"+result.dormitoryName)
            saveDormitoryId(result.dormitoryId)
            changeActivity(MainActivity::class.java)
            finish()
        }
    }

    override fun onLoginFailure(message: String) {
        // 2011 : 비밀번호가 틀립니다
        // 2012 : 탈퇴한 회원
        // 2400 : 존재하지 않는 아이디
        CustomToastMsg.createToast(this, "로그인 실패! 다시 시도해주세요", "#80A8A8A8", 26)?.show() //gray_2색상 //80붙여서 투명도 50%줌
        Log.d("API-TEST", "API onLoginFailure")
/*        showToast(message)*/
    }

    // 소셜 로그인 성공
    override fun onSocialLoginSuccess(code: Int, result: SocialLoginResult) {
        Log.d("API-TEST", "onSocialLoginSuccess")
        // 네이버 자동 로그인 자동 적용
        val jwt = result.jwt
        setAutoLogin(jwt)
        saveIsSocial(true)
        if(result.loginStatus=="NEVER"){ //첫 로그인이면
            val intent = Intent(this, DormitoryActivity::class.java)
            intent.putExtra("nickName", result.nickName)
            startActivity(intent)
        }else{
            Log.d("cherry", "첫 로그인이 아님")
            saveNickname(result.nickName)
            saveProfileImgUrl(result.profileImgUrl)
            saveDormitory("제"+result.dormitoryName)
            saveDormitoryId(result.dormitoryId)
            finish()
            changeActivity(MainActivity::class.java)
        }
    }

    // 소셜 로그인 회원가입 창으로 이동
    override fun onSocialLoginToRegister(message: String, accessToken: String) {
        Log.d("API-TEST", "onSocialLoginToRegister")
        val signUpNaverIntent = Intent(this, SignUpNaverActivity::class.java)
        signUpNaverIntent.putExtra("accessToken", accessToken)
        startActivity(signUpNaverIntent)
        showToast("회원가입이 되지않아 회원가입 창으로 이동합니다.")
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
    private fun getFireBasetoken(){
        Log.d("API-TEST", "getFireBaseToken")
        FirebaseMessaging.getInstance().token.addOnCompleteListener {
                task ->
            if(task.isSuccessful) {
                fcmToken = task.result?:""
                Log.d("API-TEST", "getFireBaseToken = $fcmToken")
                Log.d("FCM-TOKEN-RESPONSE", fcmToken.toString())
            }
        }
    }

    override fun onSignUpSocialSuccess() {
        showToast("성공")
        Log.d("API-TEST", "onSignUpSocialSuccess")
    }

    override fun onSignUpSocialFailure(message: String) {
        showToast("실패 $message")
        Log.d("API-TEST", "onSignUpSocialFailure")
    }
}