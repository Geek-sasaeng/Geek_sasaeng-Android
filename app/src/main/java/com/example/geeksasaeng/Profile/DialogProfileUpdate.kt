package com.example.geeksasaeng.Profile

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.fragment.app.DialogFragment
import com.example.geeksasaeng.databinding.DialogProfileUpdateBinding

class DialogProfileUpdate: DialogFragment() {
    lateinit var binding: DialogProfileUpdateBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DialogProfileUpdateBinding.inflate(inflater, container, false)
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT)) // 배경 투명하게 만들어줘야 둥근 테두리가 보인다.
        // Dialog 사이즈 조절 하기
        val params = dialog!!.window!!.attributes
        params.width = WindowManager.LayoutParams.WRAP_CONTENT
        params.height = WindowManager.LayoutParams.WRAP_CONTENT
        dialog!!.window!!.attributes = params as WindowManager.LayoutParams

        initLClickListener()
        return binding.root
    }

    private fun initLClickListener() {
        binding.dialogProfileUpdateCancelBtn.setOnClickListener {
            this.dismiss()
        }

        binding.dialogProfileUpdateOkBtn.setOnClickListener {
            //★ 나의 정보 수정 api 호출
        }
    }
}