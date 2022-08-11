package com.example.geeksasaeng.Login

import android.content.*
import android.text.*
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.example.geeksasaeng.Home.HomeFragment
import com.example.geeksasaeng.Config.Secret.Secret.OAUTH_CLIENT_ID
import com.example.geeksasaeng.Config.Secret.Secret.OAUTH_CLIENT_NAME
import com.example.geeksasaeng.Config.Secret.Secret.OAUTH_CLIENT_SECRET
import com.example.geeksasaeng.databinding.ActivityLoginBinding
import com.example.geeksasaeng.Login.Retrofit.*
import com.example.geeksasaeng.MainActivity
import com.example.geeksasaeng.R
import com.example.geeksasaeng.Signup.Basic.SignUpActivity
import com.example.geeksasaeng.Signup.Naver.SignUpNaverActivity
import com.example.geeksasaeng.Signup.Naver.SignUpNaverViewModel
import com.example.geeksasaeng.Signup.Retrofit.*
import com.example.geeksasaeng.Utils.*
import com.navercorp.nid.NaverIdLoginSDK
import com.navercorp.nid.oauth.*
import com.navercorp.nid.profile.NidProfileCallback
import com.navercorp.nid.profile.data.NidProfileResponse

class LoginActivity: BaseActivity<ActivityLoginBinding>(ActivityLoginBinding::inflate), SignUpSocialView, LoginView, SocialLoginView {
    var loginId: String? = ""
    var nickname: String? = ""
    var phoneNumber: String? = ""


    private lateinit var signUpNaverVM: SignUpNaverViewModel

    override fun initAfterBinding() {
        signUpNaverVM = ViewModelProvider(this).get(SignUpNaverViewModel::class.java)
        setTextChangedListener()
        initClickListener()
    }

    private fun initClickListener() {
        binding.loginLoginBtn.setOnClickListener {
            Log.d("login","로그인버튼 클릭됨")
            login()
        }

        // 네이버 로그인
        binding.loginNaverBtn.setOnClickListener {
            NaverIdLoginSDK.initialize(this, OAUTH_CLIENT_ID, OAUTH_CLIENT_SECRET, OAUTH_CLIENT_NAME)

            val profileCallback = object : NidProfileCallback<NidProfileResponse> {
                override fun onSuccess(response: NidProfileResponse) {
                    loginId = response.profile?.email.toString()
                    phoneNumber = response.profile?.mobile.toString()
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

                    // accessToken 서버로 보냄
                    val accessToken = NaverIdLoginSDK.getAccessToken().toString()
                    socialLogin(accessToken)
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

    private fun login() {
        val loginDataService = LoginDataService()
        loginDataService.setLoginView(this)
        loginDataService.login(getLoginUser())
    }

    private fun socialLogin(accessToken: String) {
        val socialLoginDataService = SocialLoginDataService()
        socialLoginDataService.setSocialLoginView(this)
        socialLoginDataService.socialLogin(SocialLogin(accessToken))
    }

    private fun getLoginUser(): Login {
        val loginId = binding.loginIdEt.text.toString()
        val password = binding.loginPwdEt.text.toString()
        Log.d("login", loginId.toString()+"/"+password)
        return Login(loginId, password)
    }

    override fun onLoginSuccess(code : Int , result: LoginResult) {
        val jwt = result.jwt
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
            //TODO: 자동로그인할때는 DORMITORY, PROFILEIMG어디서 불러오지..?
            saveNickname(result.nickName)
            saveProfileImgUrl(result.profileImgUrl)
            saveDormitory("제"+result.dormitoryName)
            saveDormitoryId(result.dormitoryId)
            changeActivity(MainActivity::class.java)
            finish()
        }
    }

    // 소셜 로그인 성공
    override fun onSocialLoginSuccess(code: Int, result: SocialLoginResult) {
        // 네이버 자동 로그인 자동 적용
        val jwt = result.jwt
        setAutoLogin(jwt)
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
        val signUpNaverIntent = Intent(this, SignUpNaverActivity::class.java)
        signUpNaverIntent.putExtra("accessToken", accessToken)
        startActivity(signUpNaverIntent)
        showToast("회원가입이 되지않아 회원가입 창으로 이동합니다.")
    }

    override fun onLoginFailure(message: String) {
        // 2011 : 비밀번호가 틀립니다
        // 2012 : 탈퇴한 회원
        // 2400 : 존재하지 않는 아이디
        showToast(message)
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