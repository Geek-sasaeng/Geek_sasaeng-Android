package com.example.geeksasaeng.Home.Party.ReportParty

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.example.geeksasaeng.databinding.DialogReportPartyFailBinding
import com.example.geeksasaeng.databinding.DialogReportPartySuccessBinding

class DialogReportPartyFail : DialogFragment() { //TODO:근데 이건 특별한 기능이 없어서 공통으로 해도 될듯?
    lateinit var binding: DialogReportPartyFailBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DialogReportPartyFailBinding.inflate(inflater, container, false)
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT)) // 배경 투명하게 만들어줘야 둥근 테두리가 보인다.
        initListener()
        return binding.root
    }

    private fun initListener(){
        binding.reportPartyFailCancelBtn.setOnClickListener {
            this.dismiss()
        }
        binding.reportPartyFailOkBtn.setOnClickListener {
            this.dismiss()
        }
    }


}