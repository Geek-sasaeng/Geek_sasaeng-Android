package com.example.geeksasaeng.Profile

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.*
import androidx.fragment.app.DialogFragment
import com.example.geeksasaeng.Login.LoginActivity
import com.example.geeksasaeng.MainActivity
import com.example.geeksasaeng.R
import com.example.geeksasaeng.Utils.removeAutoLogin
import com.example.geeksasaeng.Utils.removeIsSocial
import com.example.geeksasaeng.databinding.DialogLogoutBinding

class DialogProfileLogout: DialogFragment() {

    lateinit var binding: DialogLogoutBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DialogLogoutBinding.inflate(inflater, container, false)
        initListener()
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT)) // 배경 투명하게 만들어줘야 둥근 테두리가 보인다.
        return binding.root
    }

    private fun initListener(){
        binding.logoutOkBtn.setOnClickListener {
            this.dismiss()
            (context as MainActivity).finish()
            removeAutoLogin()
            removeIsSocial()
            startActivity(Intent(activity, LoginActivity::class.java))
        }

        binding.logoutCancelBtn.setOnClickListener {
            this.dismiss()
        }
    }

    override fun onResume() {
        super.onResume()
        val width = resources.getDimensionPixelSize(R.dimen.party_request_width)
        val height = resources.getDimensionPixelSize(R.dimen.chatting_exit_popup_height)
        dialog?.window?.setLayout(width,height)
    }
}