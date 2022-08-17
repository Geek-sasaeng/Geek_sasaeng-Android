package com.example.geeksasaeng.Home.Party.ReportParty

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.example.geeksasaeng.databinding.DialogReportPartySuccessBinding
import com.example.geeksasaeng.databinding.DialogSignupPhoneSkipBinding

class DialogReportPartySuccess : DialogFragment() {
    lateinit var binding: DialogReportPartySuccessBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DialogReportPartySuccessBinding.inflate(inflater, container, false)
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT)) // 배경 투명하게 만들어줘야 둥근 테두리가 보인다.
        initListener()
        return binding.root
    }

    private fun initListener(){
        binding.reportPartySuccessCancelBtn.setOnClickListener {
            this.dismiss()
        }

        binding.reportPartySuccessOkBtn.setOnClickListener {
            this.dismiss()
        }

    }
}