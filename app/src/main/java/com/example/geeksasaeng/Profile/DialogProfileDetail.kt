package com.example.geeksasaeng.Profile

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.telephony.PhoneNumberUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.fragment.app.DialogFragment
import com.bumptech.glide.Glide
import com.example.geeksasaeng.Utils.getProfileImgUrl
import com.example.geeksasaeng.databinding.DialogProfileDetailLayoutBinding
import java.util.Locale

class DialogProfileDetail : DialogFragment()  {

    lateinit var binding: DialogProfileDetailLayoutBinding

    var nickName: String? = null
    var universityName: String? = null
    var dormitoryName: String? = null
    var loginId: String? = null
    var emailAddress: String? = null
    var phoneNumber: String? = null
    var signUpDate: String? = null

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
        binding = DialogProfileDetailLayoutBinding.inflate(inflater, container, false)
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT)) // 배경 투명하게 만들어줘야 둥근 테두리가 보인다.
        initData()
        return binding.root
    }

    private fun initData() {
        nickName =requireArguments().getString("nickName")
        universityName =requireArguments().getString("universityName")
        dormitoryName = requireArguments().getString("dormitoryName")
        loginId =requireArguments().getString("loginId")
        emailAddress =requireArguments().getString("emailAddress")
        phoneNumber =requireArguments().getString("phoneNumber") //이미 포맷팅된 전화번호가 넘어옴
        signUpDate =requireArguments().getString("signUpDate") //이미 포맷팅된 가입일이 넘어옴

        binding.dialogProfileDetailNickNameTv.text = nickName
        binding.dialogProfileDetailUnivTv.text = universityName
        binding.dialogProfileDetailDormitoryNameTv.text = dormitoryName
        binding.dialogProfileDetailIdTv.text = loginId
        binding.dialogProfileDetailEmailTv.text = emailAddress
        binding.dialogProfileDetailPhoneTv.text = phoneNumber
        binding.dialogProfileDetailSignUpDateTv.text = signUpDate
        Glide.with(this)
            .load(getProfileImgUrl())
            .into(binding.dialogProfileDetailImgIv)
    }

}