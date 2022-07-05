package com.example.geeksasaeng.Signup

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.*
import androidx.fragment.app.DialogFragment
import com.example.geeksasaeng.Login.LoginActivity
import com.example.geeksasaeng.databinding.DialogSignupEmailCheckBinding


class DialogSignUpEmailCheck: DialogFragment() {

    lateinit var binding: DialogSignupEmailCheckBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DialogSignupEmailCheckBinding.inflate(inflater, container, false)

        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog?.window?.setGravity(Gravity.TOP)

        // Dialog 위치 조정
        val params: WindowManager.LayoutParams? = dialog?.window?.attributes
        params?.y = 200
        dialog?.window?.attributes = params

        // Dialog 주변 배경 투명하게
        dialog?.window?.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)

        return binding.root
   }
}