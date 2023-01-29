package com.example.geeksasaeng.Home.Party.CreateParty

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
import com.example.geeksasaeng.Home.CreateParty.DialogLink
import com.example.geeksasaeng.R
import com.example.geeksasaeng.databinding.DialogCategoryLayoutBinding

class DialogCategory : DialogFragment() {
    lateinit var binding: DialogCategoryLayoutBinding
    private var dialogCategoryNextClickListener: DialogCategoryNextClickListener? =null
    var categoryString = ""

    private val createPartyVM: CreatePartyViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DialogCategoryLayoutBinding.inflate(inflater, container, false)
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT)) //레이아웃배경을 투명하게 해줌?
        initData()
        initClickListener()
        return binding.root
    }

    private fun initData(){
        Log.d("dialogCate", createPartyVM.getCategory().toString())
        if(createPartyVM.getCategory().toString()!="null"){
            categoryString = createPartyVM.getCategory().toString() //categoryString값에 원래 저장되어있던 값 지정

            //default 체크 작업
            when(categoryString){
                "한식"-> binding.categoryRadio1.isChecked = true
                "중식"-> binding.categoryRadio3.isChecked = true
                "분식"-> binding.categoryRadio5.isChecked = true
                "회/돈까스"-> binding.categoryRadio7.isChecked = true
                "디저트/음료"-> binding.categoryRadio9.isChecked = true
                "양식"-> binding.categoryRadio2.isChecked = true
                "일식"-> binding.categoryRadio4.isChecked = true
                "치킨/피자"-> binding.categoryRadio6.isChecked = true
                "패스트 푸드"-> binding.categoryRadio8.isChecked = true
                "기타"-> binding.categoryRadio10.isChecked = true
                else->{}
            }
        }else{ // 설정된 값이 없을 때, 한식을 default값으로 지정!
            categoryString = "한식"
            binding.categoryRadio1.isChecked = true
            createPartyVM.setCategory(categoryString)
        }
    }

    override fun onResume() {
        super.onResume()
        // TODO: 테스트해보고 수정하기 (폰에 따라 크기 다르게 지정?!)
        val width = resources.getDimensionPixelSize(R.dimen.popup_width)
        val height = resources.getDimensionPixelSize(R.dimen.popup_height)
        dialog?.window?.setLayout(width,height)
    }

    //frag->Activity 정보전달용 코드 시작
    interface DialogCategoryNextClickListener{
        fun onCategoryClicked(text:String)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        dialogCategoryNextClickListener = activity as DialogCategoryNextClickListener
    }

    override fun onDetach() {
        super.onDetach()
        if(categoryString!=""){ // 입력된 정보가 있으면
            dialogCategoryNextClickListener?.onCategoryClicked(categoryString)
        }
        dialogCategoryNextClickListener = null
    }
    //frag->Activity 정보전달용 코드 끝

    private fun initClickListener(){

        //다음버튼
        binding.categoryDialogNextBtn.setOnClickListener{
            //다음 다이얼로그 띄우기
            val dialogLink = DialogLink()
            dialogLink.show(parentFragmentManager, "CustomDialog")
            //자기는 종료
            activity?.supportFragmentManager?.beginTransaction()?.remove(this)?.commit()
        }

        //뒤로가기 버튼
        binding.categoryDialogBackBtn.setOnClickListener {

            //이전 다이얼로그 실행
            val dialogNum = DialogNum()
            dialogNum.show(parentFragmentManager, "CustomDialog")

            //자기자신(현 다이얼로그)은 종료 => 종료가 안되는 것 같기두..?
            activity?.supportFragmentManager?.beginTransaction()
                ?.remove(this)?.commit()
        }

        // 라디오버튼들은 담은 라디오그룹 이벤트 처리(라디오그룹 2열) => 중복 체크 안되게 처리
        binding.categoryDialogRg1.setOnCheckedChangeListener { group, checkedId ->
            if (checkedId != -1) {
                binding.categoryDialogRg2.clearCheck()
                binding.categoryDialogRg1.check(checkedId)
            }
            when(checkedId){
                R.id.category_radio_1 -> categoryString= "한식"
                R.id.category_radio_3 -> categoryString= "중식"
                R.id.category_radio_5 -> categoryString= "분식"
                R.id.category_radio_7 -> categoryString= "회/돈까스"
                R.id.category_radio_9 -> categoryString= "디저트/음료"
                else-> {}
            }
            createPartyVM.setCategory(categoryString)
        }

        binding.categoryDialogRg2.setOnCheckedChangeListener { group, checkedId ->
            if (checkedId != -1) {
                binding.categoryDialogRg1.clearCheck() // 1번째열의 check는 지워주기
                binding.categoryDialogRg2.check(checkedId)
            }
            when(checkedId){
                R.id.category_radio_2 -> categoryString= "양식"
                R.id.category_radio_4 -> categoryString= "일식"
                R.id.category_radio_6 -> categoryString= "치킨/피자"
                R.id.category_radio_8 -> categoryString= "패스트 푸드"
                R.id.category_radio_10 -> categoryString= "기타"
                else-> {}
            }
            createPartyVM.setCategory(categoryString)
        }

    }
}
