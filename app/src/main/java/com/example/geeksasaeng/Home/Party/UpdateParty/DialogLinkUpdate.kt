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
import com.example.geeksasaeng.Home.Party.CreateParty.DialogCategory
import com.example.geeksasaeng.Home.Party.CreateParty.DialogLocation
import com.example.geeksasaeng.R
import com.example.geeksasaeng.databinding.DialogLinkLayoutBinding
import com.example.geeksasaeng.databinding.DialogLinkUpdateLayoutBinding

class DialogLinkUpdate : DialogFragment() {
    lateinit var binding: DialogLinkUpdateLayoutBinding
    private var dialogLinkNextClickListener: DialogLinkNextClickListener? =null
    var flagNext : Boolean = false

    private val createPartyVM: CreatePartyViewModel by activityViewModels()

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

    private fun initData(){
        if(createPartyVM.getStoreUrl().toString()!="null"){ // 이미 입력되어있는 url이 있으면 띄워주기
            binding.locationDialogSearchEt.setText(createPartyVM.getStoreUrl().toString()) //String을 Editable로 못 바꾸므로 setText함수 이용해주기
        }
    }

    override fun onResume() {
        super.onResume()
        //<방법2: 지정해둔 dp크기로로 다이얼로그 기 조정>
        val width = resources.getDimensionPixelSize(R.dimen.popup_width)
        val height = resources.getDimensionPixelSize(R.dimen.popup_height)
        dialog?.window?.setLayout(width,height)
    }

    //frag->Activity 정보전달용 코드 시작
    interface DialogLinkNextClickListener{
        fun onLinkClicked(link:String, flagNext:Boolean)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        dialogLinkNextClickListener = activity as DialogLinkNextClickListener
    }

    override fun onDetach() {
        super.onDetach()

        //다음 다이얼로그 띄우기전에 이 작업 필요
        if(binding.locationDialogSearchEt.text.toString()!=""){ //뭔가가 입력되었다면
            dialogLinkNextClickListener?.onLinkClicked(binding.locationDialogSearchEt.text.toString(), flagNext)
            createPartyVM.setStoreUrl(binding.locationDialogSearchEt.text.toString())
        }else{ //아무것도 없다면
            dialogLinkNextClickListener?.onLinkClicked("링크를 입력해주세요", flagNext)
            createPartyVM.setStoreUrl(null)
        }

        if(flagNext){
            //다음 다이얼로그 띄우기
            val dialogLocation = DialogLocation()
            dialogLocation.show(parentFragmentManager, "CustomDialog")
        }
        dialogLinkNextClickListener = null
    }
    //frag->Activity 정보전달용 코드 끝

    private fun initClickListener(){

        binding.linkDialogNextBtn.setOnClickListener { //다음버튼

            flagNext = true // next버튼 클릭했다고 표시
            //자기는 종료
            activity?.supportFragmentManager?.beginTransaction()
                ?.remove(this)?.commit()
        }

        binding.linkDialogBackBtn.setOnClickListener { //뒤로가기 버튼

            //이전 다이얼로그 실행
            val dialogCategory = DialogCategory()
            dialogCategory.show(parentFragmentManager, "CustomDialog")

            //자기자신(현 다이얼로그)은 종료 => 종료가 안되는 것 같기두..?
            activity?.supportFragmentManager?.beginTransaction()
                ?.remove(this)?.commit()
        }

        binding.stepFourSkipBtn.setOnClickListener { //건너뛰기 버튼 (수행하는게 다음이랑 똑같다.)

            flagNext = true // next버튼 클릭했다고 표시
            //자기는 종료
            activity?.supportFragmentManager?.beginTransaction()
                ?.remove(this)?.commit()
        }
    }
}