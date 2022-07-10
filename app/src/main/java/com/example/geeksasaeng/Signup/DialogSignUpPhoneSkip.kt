package com.example.geeksasaeng.Signup

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.*
import androidx.fragment.app.DialogFragment
import com.example.geeksasaeng.databinding.DialogSignupPhoneSkipBinding


class DialogSignUpPhoneSkip: DialogFragment() {

    lateinit var binding: DialogSignupPhoneSkipBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DialogSignupPhoneSkipBinding.inflate(inflater, container, false)
        initListener()
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT)) // 배경 투명하게 만들어줘야 둥근 테두리가 보인다.
        return binding.root
    }

    private fun initListener(){
        binding.signupOkBtn.setOnClickListener {
            //건너뛰기 수행
        }

        binding.signupCancelBtn.setOnClickListener {
            //건너뛰기 취소
            this.dismiss()
        }
    }
}