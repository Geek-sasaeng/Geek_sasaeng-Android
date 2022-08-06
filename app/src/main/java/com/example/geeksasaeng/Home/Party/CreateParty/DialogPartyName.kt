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
import com.example.geeksasaeng.databinding.DialogPartyNameLayoutBinding

class DialogPartyName : DialogFragment() {
    lateinit var binding: DialogPartyNameLayoutBinding
    private val createPartyVM: CreatePartyViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DialogPartyNameLayoutBinding.inflate(inflater, container, false)
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT)) //레이아웃배경을 투명하게 해줌?
        initData()
        initClickListener()
        return binding.root
    }

    private fun initData(){
        if(createPartyVM.getPartyName().toString()!="null"){
            binding.partyNameDialogEt.setText(createPartyVM.getPartyName().toString())
        }
    }

    override fun onResume() {
        super.onResume()
        val width = resources.getDimensionPixelSize(R.dimen.party_name_popup_width)
        val height = resources.getDimensionPixelSize(R.dimen.party_name_popup_height)
        dialog?.window?.setLayout(width,height)
    }

    override fun onDetach() {
        super.onDetach()
        if(binding.partyNameDialogEt.text.toString()!=""){
            createPartyVM.setPartyName(binding.partyNameDialogEt.text.toString())
        }else{
            createPartyVM.setPartyName(null)
        }
    }

    private fun initClickListener(){
        binding.partyNameDialogBackBtn.setOnClickListener {
            //이전 다이얼로그 실행
            val dialogAccount = DialogAccountNumber()
            dialogAccount.show(parentFragmentManager, "CustomDialog")

            //자기자신(현 다이얼로그)은 종료 => 종료가 안되는 것 같기두..?
            activity?.supportFragmentManager?.beginTransaction()
                ?.remove(this)?.commit()
        }

        binding.partyNameDialogNextBtn.setOnClickListener {
            //자기자신(현 다이얼로그)은 종료
            activity?.supportFragmentManager?.beginTransaction()
                ?.remove(this)?.commit()
        }
    }

}