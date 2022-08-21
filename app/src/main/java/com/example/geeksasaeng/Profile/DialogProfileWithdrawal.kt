package com.example.geeksasaeng.Profile

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.example.geeksasaeng.Login.LoginActivity
import com.example.geeksasaeng.MainActivity
import com.example.geeksasaeng.R
import com.example.geeksasaeng.Signup.Basic.SignUpActivity
import com.example.geeksasaeng.Signup.Basic.StepFiveFragment
import com.example.geeksasaeng.Utils.removeAutoLogin
import com.example.geeksasaeng.databinding.DialogLogoutBinding
import com.example.geeksasaeng.databinding.DialogSignupPhoneSkipBinding
import com.example.geeksasaeng.databinding.DialogWithdralBinding

class DialogProfileWithdrawal: DialogFragment() {

    lateinit var binding: DialogWithdralBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DialogWithdralBinding.inflate(inflater, container, false)
        initListener()
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT)) // 배경 투명하게 만들어줘야 둥근 테두리가 보인다.
        return binding.root
    }

    private fun initListener(){
        binding.withdrawalOkBtn.setOnClickListener {
            this.dismiss()

            // TODO: 임시로 넣어둔 부분 나중에 회원탈퇴 API와 연동하고 성공했을 때 나타나도록 수정하기!
            Toast.makeText(context, "회원탈퇴가 완료되었습니다", Toast.LENGTH_SHORT).show()

            (context as MainActivity).finish()
            removeAutoLogin()
            startActivity(Intent(activity, LoginActivity::class.java))
        }

        binding.withdrawalCancelBtn.setOnClickListener {
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