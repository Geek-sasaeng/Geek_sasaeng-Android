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
import com.example.geeksasaeng.databinding.DialogCategoryLayoutBinding

class DialogCategory : DialogFragment() {
    lateinit var binding: DialogCategoryLayoutBinding
    private var dialogCategoryNextClickListener: DialogCategoryNextClickListener? =null
    var categoryString = ""

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DialogCategoryLayoutBinding.inflate(inflater, container, false)
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT)) //레이아웃배경을 투명하게 해줌?
        initClickListener()
        return binding.root
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
        dialogCategoryNextClickListener?.onCategoryClicked(categoryString)
        dialogCategoryNextClickListener = null
    }
    //frag->Activity 정보전달용 코드 끝

    private fun initClickListener(){

        //다음버튼
        binding.categoryDialogNextBtn.setOnClickListener{
            //frag-> activity 정보전달
            dialogCategoryNextClickListener?.onCategoryClicked(categoryString)
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
                R.id.category_radio_2 -> categoryString= "중식"
                R.id.category_radio_3 -> categoryString= "분식"
                R.id.category_radio_4 -> categoryString= "회/돈까스"
                R.id.category_radio_5 -> categoryString= "디저트/음료"
                else-> {}
            }
        }

        binding.categoryDialogRg2.setOnCheckedChangeListener { group, checkedId ->
            if (checkedId != -1) {
                binding.categoryDialogRg1.clearCheck() // 1번째열의 check는 지워주기
                binding.categoryDialogRg2.check(checkedId)
            }
            when(checkedId){
                R.id.category_radio_6 -> categoryString= "양식"
                R.id.category_radio_7 -> categoryString= "일식"
                R.id.category_radio_8 -> categoryString= "치킨/피자"
                R.id.category_radio_9 -> categoryString= "패스트 푸트"
                R.id.category_radio_10 -> categoryString= "기타"
                else-> {}
            }
        }
    }
}
