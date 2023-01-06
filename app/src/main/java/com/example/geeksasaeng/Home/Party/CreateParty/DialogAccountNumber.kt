package com.example.geeksasaeng.Home.Party.CreateParty

import android.content.Context
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
    private var dialogAccountNumberClickListener: DialogAccountNumberClickListener? =null

    interface DialogAccountNumberClickListener{
        //TODO: 뷰모델 이용하면서 사실 여기서 매개변수로 안넘겨줘도 ACTIVITY에서 값 알 수 있어..
        //TODO: 근데 이걸 하는 이유는 정보 갱신을 위함.
        fun onCompleteClicked()
    }

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

    override fun onAttach(context: Context) {
        super.onAttach(context)
        dialogAccountNumberClickListener = activity as DialogAccountNumber.DialogAccountNumberClickListener
    }

    override fun onDetach() {
        super.onDetach()
        dialogAccountNumberClickListener = null
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

        binding.accountDialogCompleteBtn.setOnClickListener { // 완료 버튼 누르면

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
            //아무것도 없을 때 null로 설정하는거 왜 해줬지? 필요 없을 것 같은데

            if((binding.accountDialogBankEt.text.toString()!="")&&(binding.accountDialogBankNumberEt.text.toString()!="")){
                dialogAccountNumberClickListener?.onCompleteClicked() // frag-> activity 정보전달
                //자기자신(현 다이얼로그)은 종료
                activity?.supportFragmentManager?.beginTransaction()
                    ?.remove(this)?.commit()

            }
        }
    }
}