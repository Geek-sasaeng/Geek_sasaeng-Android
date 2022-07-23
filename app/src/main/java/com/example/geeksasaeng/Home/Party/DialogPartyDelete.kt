package com.example.geeksasaeng.Home.Party

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.*
import androidx.fragment.app.DialogFragment
import com.example.geeksasaeng.MainActivity
import com.example.geeksasaeng.R
import com.example.geeksasaeng.Signup.Basic.SignUpActivity
import com.example.geeksasaeng.Signup.Basic.StepFiveFragment
import com.example.geeksasaeng.databinding.DialogDeliveryDeleteBinding
import com.example.geeksasaeng.databinding.DialogSignupPhoneSkipBinding

class DialogPartyDelete: DialogFragment() {

    lateinit var binding: DialogDeliveryDeleteBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DialogDeliveryDeleteBinding.inflate(inflater, container, false)
        initListener()
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT)) // 배경 투명하게 만들어줘야 둥근 테두리가 보인다.
        return binding.root
    }

    private fun initListener(){
        binding.deleteCancelBtn.setOnClickListener {
            //건너뛰기 취소
            this.dismiss()
        }
    }

    override fun onResume() {
        super.onResume()
        // TODO: 테스트해보고 수정하기 (폰에 따라 크기 다르게 지정?!)
        val width = resources.getDimensionPixelSize(R.dimen.party_delete_popup_width)
        val height = resources.getDimensionPixelSize(R.dimen.party_delete_popup_height)
        dialog?.window?.setLayout(width,height)
    }
}