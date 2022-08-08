package com.example.geeksasaeng.Home.Party.LookParty

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.os.Handler
import android.view.*
import androidx.fragment.app.DialogFragment
import com.example.geeksasaeng.R
import com.example.geeksasaeng.Signup.Basic.SignUpActivity
import com.example.geeksasaeng.Signup.Basic.StepFiveFragment
import com.example.geeksasaeng.databinding.DialogPartyRequestBinding
import com.example.geeksasaeng.databinding.DialogSignupPhoneSkipBinding

class DialogPartyRequest: DialogFragment() {

    lateinit var binding: DialogPartyRequestBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DialogPartyRequestBinding.inflate(inflater, container, false)
        initListener()
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT)) // 배경 투명하게 만들어줘야 둥근 테두리가 보인다.
        return binding.root
    }

    private fun initListener(){
        binding.partyRequestOkBtn.setOnClickListener {
            // 신청하기 수행
            this.dismiss()
            // TODO: 채팅방이 생성되었습니다 Dialog 구현
            val dialog = DialogPartyRequestComplete()
            dialog.show(parentFragmentManager, "partyRequestComplete")
            val handler = Handler()
            handler.postDelayed({
                dialog.dismiss()
            }, 3000)
        }

        binding.partyRequestCancelBtn.setOnClickListener {
            // 신청하기 취소
            this.dismiss()
        }
    }

    override fun onResume() {
        super.onResume()
        val width = resources.getDimensionPixelSize(R.dimen.party_request_width)
        val height = resources.getDimensionPixelSize(R.dimen.party_request_height)
        dialog?.window?.setLayout(width,height)
    }
}