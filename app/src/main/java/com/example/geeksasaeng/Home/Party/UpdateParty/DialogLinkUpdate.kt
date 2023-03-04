package com.example.geeksasaeng.Home.Party.UpdateParty

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.Editable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import com.example.geeksasaeng.Home.CreateParty.CreatePartyViewModel
import com.example.geeksasaeng.Home.CreateParty.DialogLink
import com.example.geeksasaeng.Home.Party.CreateParty.DialogCategory
import com.example.geeksasaeng.Home.Party.CreateParty.DialogLocation
import com.example.geeksasaeng.R
import com.example.geeksasaeng.databinding.DialogLinkLayoutBinding
import com.example.geeksasaeng.databinding.DialogLinkUpdateLayoutBinding

class DialogLinkUpdate : DialogFragment() {
    lateinit var binding: DialogLinkUpdateLayoutBinding
    private var dialogLinkUpdateClickListener: DialogLinkUpdateClickListener? =null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View?
    {
        binding = DialogLinkUpdateLayoutBinding.inflate(inflater, container, false)
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT)) //레이아웃배경을 투명하게 해줌
        initData()
        initClickListener()
        return binding.root
    }

    //frag->Activity 정보전달용 코드 시작
    interface DialogLinkUpdateClickListener{
        fun onLinkClicked(link:String)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        dialogLinkUpdateClickListener = requireParentFragment() as DialogLinkUpdateClickListener
    }

    override fun onDetach() {
        super.onDetach()
        dialogLinkUpdateClickListener = null
    }
    //frag->Activity 정보전달용 코드 끝

    private fun initData(){
        val storeUrl = arguments?.getString("Link")
        if(storeUrl!="null"){ // 이미 입력되어있는 url이 있으면 띄워주기
            binding.dialogLinkUpdateSearchEt.setText(storeUrl) //String을 Editable로 못 바꾸므로 setText함수 이용해주기
        }
    }

    override fun onResume() {
        super.onResume()
        //<방법2: 지정해둔 dp크기로로 다이얼로그 기 조정>
        val width = resources.getDimensionPixelSize(R.dimen.popup_width)
        val height = resources.getDimensionPixelSize(R.dimen.popup_height)
        dialog?.window?.setLayout(width,height)
    }

    private fun initClickListener(){

        binding.dialogLinkUpdateBtn.setOnClickListener { //다음버튼

            if(binding.dialogLinkUpdateSearchEt.text.toString()!=""){ //뭔가가 입력되었다면
                dialogLinkUpdateClickListener?.onLinkClicked(binding.dialogLinkUpdateSearchEt.text.toString())
            }else{ //아무것도 없다면
                dialogLinkUpdateClickListener?.onLinkClicked("링크를 입력해주세요")
            }
            //자기는 종료
            parentFragmentManager.beginTransaction()
                .remove(this).commit()
        }

        binding.dialogLinkUpdateSkipBtn.setOnClickListener {
            dismiss()
        }

    }
}