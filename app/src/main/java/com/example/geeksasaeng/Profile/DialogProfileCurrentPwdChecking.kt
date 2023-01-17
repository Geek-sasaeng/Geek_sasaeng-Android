package com.example.geeksasaeng.Profile

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.fragment.app.DialogFragment
import com.example.geeksasaeng.databinding.DialogProfileCurrentPwdCheckingBinding

class DialogProfileCurrentPwdChecking: DialogFragment()  {

    lateinit var binding: DialogProfileCurrentPwdCheckingBinding

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
        initClickListener()
        return binding.root
    }

    private fun initClickListener() {
        binding.dialogProfileCurrentPwdCheckingNextBtn.setOnClickListener {
            //비밀번호 맞는지 안 맞는 지 체크하는 API
            //그게 성공시 아래 코드 수행
            startActivity(Intent(activity, ProfileMyInfoUpdatePwdActivity::class.java))
        }
    }
}