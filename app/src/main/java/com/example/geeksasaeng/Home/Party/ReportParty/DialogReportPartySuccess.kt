package com.example.geeksasaeng.Home.Party.ReportParty

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.example.geeksasaeng.Home.CreateParty.DialogLink
import com.example.geeksasaeng.Home.Party.LookParty.LookPartyFragment
import com.example.geeksasaeng.MainActivity
import com.example.geeksasaeng.R
import com.example.geeksasaeng.databinding.DialogReportPartySuccessBinding
import com.example.geeksasaeng.databinding.DialogSignupPhoneSkipBinding

class DialogReportPartySuccess : DialogFragment() {
    lateinit var binding: DialogReportPartySuccessBinding
    private var dialogDismissListener: SuccessDialogDismissListener? =null

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

    //frag->Activity 정보전달용 코드 시작
    interface SuccessDialogDismissListener{
        fun onSuccessDialogDismissed()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        dialogDismissListener = activity as SuccessDialogDismissListener
    }

    override fun onDetach() {
        super.onDetach()
        dialogDismissListener?.onSuccessDialogDismissed() // 다이얼로그 창이 닫힌걸 PartyReportOptionActivity에 알려주기 위함
        dialogDismissListener = null
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