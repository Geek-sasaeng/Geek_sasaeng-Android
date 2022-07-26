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
import com.example.geeksasaeng.Home.CreateParty.DialogLink
import com.example.geeksasaeng.Home.Party.CreateParty.DialogCategory
import com.example.geeksasaeng.R
import com.example.geeksasaeng.databinding.DialogCategoryLayoutBinding
import com.example.geeksasaeng.databinding.DialogCategoryUpdateLayoutBinding

class DialogCategoryUpdate : DialogFragment() {
    lateinit var binding: DialogCategoryUpdateLayoutBinding
    private var dialogCategoryUpdateClickListener: DialogCategoryUpdateClickListener? =null
    var categoryString = ""


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DialogCategoryUpdateLayoutBinding.inflate(inflater, container, false)
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT)) //레이아웃배경을 투명하게 해줌?
        initData()
        initClickListener()
        return binding.root
    }

    //frag->Activity 정보전달용 코드 시작
    interface DialogCategoryUpdateClickListener{
        fun onCategoryClicked(text:String)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        dialogCategoryUpdateClickListener = requireParentFragment() as DialogCategoryUpdateClickListener
    }

    override fun onDetach() {
        super.onDetach()
        if(categoryString!=""){ // 입력된 정보가 있으면
            dialogCategoryUpdateClickListener?.onCategoryClicked(categoryString)
        }
        dialogCategoryUpdateClickListener = null
    }
    //frag->Activity 정보전달용 코드 끝

    private fun initData(){
        val category = arguments?.getString("Category")
        when(category){
            "한식"-> binding.categoryUpdateRadio1.isChecked = true
            "중식"-> binding.categoryUpdateRadio3.isChecked = true
            "분식"-> binding.categoryUpdateRadio5.isChecked = true
            "회/돈까스"-> binding.categoryUpdateRadio7.isChecked = true
            "디저트/음료"-> binding.categoryUpdateRadio9.isChecked = true
            "양식"-> binding.categoryUpdateRadio2.isChecked = true
            "일식"-> binding.categoryUpdateRadio4.isChecked = true
            "치킨/피자"-> binding.categoryUpdateRadio6.isChecked = true
            "패스트 푸드"-> binding.categoryUpdateRadio8.isChecked = true
            "기타"-> binding.categoryUpdateRadio10.isChecked = true
            else->{}
        }
    }

    override fun onResume() {
        super.onResume()
        // TODO: 테스트해보고 수정하기 (폰에 따라 크기 다르게 지정?!)
        val width = resources.getDimensionPixelSize(R.dimen.popup_width)
        val height = resources.getDimensionPixelSize(R.dimen.popup_height)
        dialog?.window?.setLayout(width,height)
    }


    private fun initClickListener(){

        // 라디오버튼들은 담은 라디오그룹 이벤트 처리(라디오그룹 2열) => 중복 체크 안되게 처리
        binding.dialogCategoryUpdateRg1.setOnCheckedChangeListener { group, checkedId ->
            if (checkedId != -1) {
                binding.dialogCategoryUpdateRg2.clearCheck()
                binding.dialogCategoryUpdateRg1.check(checkedId)
            }
            when(checkedId){
                R.id.category_update_radio_1 -> categoryString= "한식"
                R.id.category_update_radio_3 -> categoryString= "중식"
                R.id.category_update_radio_5 -> categoryString= "분식"
                R.id.category_update_radio_7 -> categoryString= "회/돈까스"
                R.id.category_update_radio_9 -> categoryString= "디저트/음료"
                else-> {}
            }
        }

        binding.dialogCategoryUpdateRg2.setOnCheckedChangeListener { group, checkedId ->
            if (checkedId != -1) {
                binding.dialogCategoryUpdateRg1.clearCheck() // 1번째열의 check는 지워주기
                binding.dialogCategoryUpdateRg2.check(checkedId)
            }
            when(checkedId){
                R.id.category_update_radio_2 -> categoryString= "양식"
                R.id.category_update_radio_4 -> categoryString= "일식"
                R.id.category_update_radio_6 -> categoryString= "치킨/피자"
                R.id.category_update_radio_8 -> categoryString= "패스트 푸드"
                R.id.category_update_radio_10 -> categoryString= "기타"
                else-> {}
            }
        }

        //완료 버튼
        binding.dialogCategoryUpdateBtn.setOnClickListener {
            if(categoryString!=""){ // 입력된 정보가 있으면
                dialogCategoryUpdateClickListener?.onCategoryClicked(categoryString)
            }
            parentFragmentManager.beginTransaction()
                .remove(this).commit()
        }
    }
}
