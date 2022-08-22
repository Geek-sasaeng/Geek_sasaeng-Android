package com.example.geeksasaeng.Home.Party.ReportParty

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.example.geeksasaeng.databinding.DialogReportPartyBinding


class DialogReportParty : DialogFragment() { // TODO:근데 이건 특별한 기능이 없어서 공통으로 해도 될듯?
    lateinit var binding: DialogReportPartyBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DialogReportPartyBinding.inflate(inflater, container, false)
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT)) // 배경 투명하게 만들어줘야 둥근 테두리가 보인다.
        initListener()
        return binding.root
    }

    override fun onStart() {
        super.onStart()
        binding.reportPartyContentTv.text = arguments?.getString("msg")
    }

    private fun initListener (){
        binding.reportPartyCancelBtn.setOnClickListener {
            this.dismiss()
        }
        binding.reportPartyOkBtn.setOnClickListener {
            this.dismiss()
            requireActivity().finish()
        }
    }
}