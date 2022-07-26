package com.example.geeksasaeng.Home.Party.UpdateParty

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import com.example.geeksasaeng.Home.CreateParty.CreatePartyViewModel
import com.example.geeksasaeng.R
import com.example.geeksasaeng.databinding.DialogNumLayoutBinding
import com.example.geeksasaeng.databinding.DialogNumUpdateLayoutBinding

class DialogNumUpdate: DialogFragment() {

    lateinit var binding: DialogNumUpdateLayoutBinding
    private var dialogNumNextClickListener: DialogNumNextClickListener? =null
    var numString = "2"

    private val createPartyVM: CreatePartyViewModel by activityViewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?
    {
        binding = DialogNumUpdateLayoutBinding.inflate(inflater, container, false)
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT)) //레이아웃배경을 투명하게 해줌?
        initData()
        initNumberPicker()
        initClickListener()
        return binding.root
    }

    private fun initData(){
        //사용자가 입력해둔 정보 있으면, 그걸 default값으로 설정해주기 위함 (사용자가 입력해둔 정보 유무는 ViewModel에 저장되어 있는지 여부로 결정)
        Log.d("dialogNum", createPartyVM.getMaxMatching().toString())
        if(createPartyVM.getMaxMatching().toString()!="null"){
            numString = createPartyVM.getMaxMatching().toString() //numString값에 원래 저장되어있던 값 지정
            Log.d("dialogNum", createPartyVM.getMaxMatching()!!.toInt().toString())
        }
    }

    override fun onResume() {
        super.onResume()
        val width = resources.getDimensionPixelSize(R.dimen.popup_width)
        val height = resources.getDimensionPixelSize(R.dimen.popup_height)
        dialog?.window?.setLayout(width,height)
    }

    //frag->Activity 정보전달용 코드 시작
    interface DialogNumNextClickListener{
        fun onNumClicked(text:String)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        dialogNumNextClickListener = activity as DialogNumNextClickListener
    }

    override fun onDetach() {
        super.onDetach()
        //frag-> activity 정보전달
        dialogNumNextClickListener?.onNumClicked(numString+"명")
        dialogNumNextClickListener = null
    }
    //frag->Activity 정보전달용 코드 끝

    private fun initNumberPicker(){
        val numArray = arrayOf("2명","3명","4명","5명","6명","7명","8명","9명","10명")
        binding.numDialogPicker.minValue = 2
        binding.numDialogPicker.maxValue = 10
        binding.numDialogPicker.wrapSelectorWheel = false
        binding.numDialogPicker.displayedValues = numArray
        binding.numDialogPicker.value = numString.toInt() // numberPicker 초기값 설정하기
    }

    private fun initClickListener(){
        //다음버튼
        binding.numDialogNextBtn.setOnClickListener {
            //다음 다이얼로그 띄우기
            val dialogCategory = DialogCategoryUpdate()
            dialogCategory.show(parentFragmentManager, "CustomDialog")
            //자기는 종료
            activity?.supportFragmentManager?.beginTransaction()
                ?.remove(this)?.commit()
        }

        //넘버픽커 값
        binding.numDialogPicker.setOnValueChangedListener { picker, oldVal, newVal ->
            //oldVal 변동전 값, newVal이 변동 후의 값
            Log.d("test", "oldVal : ${oldVal}, newVal : $newVal")
            numString = newVal.toString()
            createPartyVM.setMaxMatching(newVal) // VM에 저장
        }

        //뒤로가기 버튼
        binding.numDialogBackBtn.setOnClickListener {

            //이전 다이얼로그 실행
            val dialogDt = DialogDtUpdate()
            dialogDt.show(parentFragmentManager, "CustomDialog")

            //자기는 종료
            activity?.supportFragmentManager?.beginTransaction()
                ?.remove(this)?.commit()
        }
    }
}