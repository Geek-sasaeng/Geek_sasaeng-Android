package com.example.geeksasaeng.Home.CreateParty

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.example.geeksasaeng.R
import com.example.geeksasaeng.databinding.DialogLinkLayoutBinding

class DialogLink : DialogFragment() {
    lateinit var binding: DialogLinkLayoutBinding
    private var dialogLinkNextClickListener: DialogLinkNextClickListener? =null
    var flagNext : Boolean = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View?
    {
        binding = DialogLinkLayoutBinding.inflate(inflater, container, false)
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT)) //레이아웃배경을 투명하게 해줌
        initClickListener()
        return binding.root
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
        if(flagNext==false){ //다음버튼을 안클릭하고 빠져나오는 경우(다이얼로그 바깥부분 터치시)
            //frag-> activity 정보전달
            dialogLinkNextClickListener?.onLinkClicked("링크를 입력해주세요", flagNext)
        }
        dialogLinkNextClickListener = null
    }
    //frag->Activity 정보전달용 코드 끝

    private fun initClickListener(){

        binding.linkDialogNextBtn.setOnClickListener { //다음버튼

            flagNext = true // next버튼 클릭했다고 표시
            //frag-> activity 정보전달
            if (binding.locationDialogSearchEt.text.toString()!=""){
                dialogLinkNextClickListener?.onLinkClicked(binding.locationDialogSearchEt.text.toString(), flagNext)
            }else{ //아무것도 입력 안했을 때
                dialogLinkNextClickListener?.onLinkClicked("링크를 입력해주세요", flagNext)
            }

            //다음 다이얼로그 띄우기
            val dialogLocation = DialogLocation()
            dialogLocation.show(parentFragmentManager, "CustomDialog")

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

        binding.stepFourSkipBtn.setOnClickListener {

            //frag-> activity 정보전달
            flagNext = true
            dialogLinkNextClickListener?.onLinkClicked("링크를 입력해주세요", flagNext)

            //다음 다이얼로그 띄우기
            val dialogLocation = DialogLocation()
            dialogLocation.show(parentFragmentManager, "CustomDialog")

            //자기는 종료
            activity?.supportFragmentManager?.beginTransaction()
                ?.remove(this)?.commit()
        }
    }
}