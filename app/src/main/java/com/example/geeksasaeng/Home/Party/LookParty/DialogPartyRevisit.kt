package com.example.geeksasaeng.Home.Party.LookParty

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.fragment.app.DialogFragment
import com.example.geeksasaeng.databinding.DialogPartyRevisitBinding

class DialogPartyRevisit: DialogFragment() {
    lateinit var binding: DialogPartyRevisitBinding


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DialogPartyRevisitBinding.inflate(inflater, container, false)
        initViewSetting()
        initClickListener()
        return binding.root
    }

    private fun initClickListener() {
        binding.partyRevisitCancelBtn.setOnClickListener {
            requireActivity().finish() // 메인화면으로 이동
        }

        binding.partyRevisitOkBtn.setOnClickListener {
            requireActivity().finish() // 메인화면으로 이동
        }
    }

    private fun initViewSetting() {
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT)) // 배경 투명하게 만들어줘야 둥근 테두리가 보인다.
        //다이얼로그! 동적인 화면 크기 구성
        val params = dialog!!.window!!.attributes
        params.width = WindowManager.LayoutParams.WRAP_CONTENT
        params.height = WindowManager.LayoutParams.WRAP_CONTENT
        dialog!!.window!!.attributes = params as WindowManager.LayoutParams
    }
}