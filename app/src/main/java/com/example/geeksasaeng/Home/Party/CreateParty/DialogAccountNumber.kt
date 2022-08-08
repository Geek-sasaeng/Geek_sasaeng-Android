package com.example.geeksasaeng.Home.Party.CreateParty

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil.setContentView
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import com.example.geeksasaeng.Home.CreateParty.CreatePartyViewModel
import com.example.geeksasaeng.R
import com.example.geeksasaeng.databinding.DialogAccountNumberLayoutBinding

class DialogAccountNumber : DialogFragment() {
    lateinit var binding: DialogAccountNumberLayoutBinding
    private val createPartyVM: CreatePartyViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DialogAccountNumberLayoutBinding.inflate(inflater, container, false)
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        initData()
        initClickListener()
        return binding.root
    }

    private fun initData(){
        if(createPartyVM.getAccount().toString()!="null"){
            binding.accountDialogBankEt.setText(createPartyVM.getAccount().toString())
        }
        if(createPartyVM.getAccountNumber().toString()!="null"){
            binding.accountDialogBankNumberEt.setText(createPartyVM.getAccountNumber().toString())
        }
    }

    override fun onDetach() {
        super.onDetach()
    }

    override fun onResume() {
        super.onResume()
        val width = resources.getDimensionPixelSize(R.dimen.account_popup_width)
        val height = resources.getDimensionPixelSize(R.dimen.account_popup_height)
        dialog?.window?.setLayout(width,height)
    }

    private fun initClickListener(){

        binding.accountDialogBackBtn.setOnClickListener {
            activity?.supportFragmentManager?.beginTransaction()
                ?.remove(this)?.commit()
        }

        binding.accountDialogNextBtn.setOnClickListener {

            if(binding.accountDialogBankEt.text.toString()!=""){//뭔가가 입력되었다면
                createPartyVM.setAccount(binding.accountDialogBankEt.text.toString())
            }else{//아무것도 없다면
                createPartyVM.setAccount(null)
            }

            if(binding.accountDialogBankNumberEt.text.toString()!=""){//뭔가가 입력되었다면
                createPartyVM.setAccountNumber(binding.accountDialogBankNumberEt.text.toString())
            }else{//아무것도 없다면
                createPartyVM.setAccountNumber(null)
            }

            val dialogPartyName = DialogPartyName()
            dialogPartyName.show(parentFragmentManager, "CustomDialog")

            //자기자신(현 다이얼로그)은 종료
            activity?.supportFragmentManager?.beginTransaction()
                ?.remove(this)?.commit()
        }
    }
}