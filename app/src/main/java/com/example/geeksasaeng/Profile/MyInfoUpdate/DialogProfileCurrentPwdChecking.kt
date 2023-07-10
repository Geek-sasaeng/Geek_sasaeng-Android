package com.example.geeksasaeng.Profile.MyInfoUpdate

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.fragment.app.DialogFragment
import com.example.geeksasaeng.Profile.Retrofit.ProfileDataService
import com.example.geeksasaeng.Profile.Retrofit.ProfilePasswordCheckingRequest
import com.example.geeksasaeng.Profile.Retrofit.ProfilePasswordCheckingView
import com.example.geeksasaeng.databinding.DialogProfileCurrentPwdCheckingBinding
import java.util.regex.Pattern

class DialogProfileCurrentPwdChecking: DialogFragment(), ProfilePasswordCheckingView  {

    lateinit var binding: DialogProfileCurrentPwdCheckingBinding
    lateinit var profileDataService: ProfileDataService

    override fun onResume() {
        super.onResume()
        //동적인 화면 크기 구성
        val params = dialog!!.window!!.attributes
        params.width = WindowManager.LayoutParams.WRAP_CONTENT
        params.height = WindowManager.LayoutParams.WRAP_CONTENT
        dialog!!.window!!.attributes = params as WindowManager.LayoutParams

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DialogProfileCurrentPwdCheckingBinding.inflate(inflater, container, false)
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT)) // 배경 투명하게 만들어줘야 둥근 테두리가 보인다.
        initView()
        initClickListener()
        return binding.root
    }

    private fun initView() {
        profileDataService = ProfileDataService()
        profileDataService.setProfilePasswordCheckingView(this)
    }

    private fun initClickListener() {

        binding.dialogProfileCurrentPwdCheckingBackIv.setOnClickListener {
            dismiss()
        }

        binding.dialogProfileCurrentPwdCheckingNextBtn.setOnClickListener {
            val pwRegex = """^(?=.*[0-9])(?=.*[a-zA-Z])(?=.*[!@#$%^+\-=])(?=\S+$)[A-Za-z\d!@#$%^+\-=]{8,}$"""
            val pwPattern = Pattern.compile(pwRegex)
            val macher = pwPattern.matcher(binding.dialogProfileCurrentPwdCheckingEt.text.toString())

            if (macher.matches()) { // 조건이 맞을 때만 api로 확인
                val password = binding.dialogProfileCurrentPwdCheckingEt.text.toString()
                profileDataService.profilePasswordCheckingSender(ProfilePasswordCheckingRequest(password))  //비밀번호 일치 확인 API ★
            }
            else{ //조건이 안 맞으면 무조건 틀린거
               binding.dialogProfileCurrentPwdCheckingExplanationTv.visibility = View.VISIBLE
            }
        }
    }

    override fun onProfilePasswordCheckingSuccess() {
        binding.dialogProfileCurrentPwdCheckingExplanationTv.visibility = View.INVISIBLE
        startActivity(Intent(activity, ProfileMyInfoUpdatePwdActivity::class.java))
        dismiss()
    }

    override fun onProfilePasswordCheckingFailure(message: String) {
        binding.dialogProfileCurrentPwdCheckingExplanationTv.visibility = View.VISIBLE
    }
}