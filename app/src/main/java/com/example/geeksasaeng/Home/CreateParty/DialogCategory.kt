package com.example.geeksasaeng.Home.CreateParty

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.example.geeksasaeng.R
import com.example.geeksasaeng.databinding.DialogCategoryLayoutBinding


class DialogCategory : DialogFragment() {
    lateinit var binding: DialogCategoryLayoutBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View?
    {
        binding = DialogCategoryLayoutBinding.inflate(inflater, container, false)
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT)) //레이아웃배경을 투명하게 해줌?
        initClickListener()
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        val width = resources.getDimensionPixelSize(R.dimen.popup_width)
        val height = resources.getDimensionPixelSize(R.dimen.popup_height)
        dialog?.window?.setLayout(width,height)
    }

    private fun initClickListener(){
        binding.categoryDialogNextBtn.setOnClickListener{
            //다음 다이얼로그 띄우기
            val dialogLocation = DialogLocation()
            dialogLocation.show(parentFragmentManager, "CustomDialog")
            //자기는 종료
            activity?.supportFragmentManager?.beginTransaction()
                ?.remove(this)?.commit()
        }

        binding.categoryDialogBackBtn.setOnClickListener {

            //이전 다이얼로그 실행
            val dialogNum = DialogNum()
            dialogNum.show(parentFragmentManager, "CustomDialog")

            //자기자신(현 다이얼로그)은 종료 => 종료가 안되는 것 같기두..?
            activity?.supportFragmentManager?.beginTransaction()
                ?.remove(this)?.commit()
        }

        //라디오버튼들은 담은 라디오그룹 이벤트 처리(라디오그룹 2열) => 중복 체크 안되게 처리
        binding.categoryDialogRg1.setOnCheckedChangeListener { _, checkedId ->
            if(checkedId!=-1){
                Log.d("click_Category1", "click_Category1")
                binding.categoryDialogRg2.clearCheck() // 2번째열의 check는 지워주기
                binding.categoryDialogRg1.check(checkedId)
            }
        }

        binding.categoryDialogRg2.setOnCheckedChangeListener { _, checkedId ->
            if (checkedId != -1) {
                //초기화
                Log.d("click_Category2", "click_Category2")
                binding.categoryDialogRg1.clearCheck() // 1번째열의 check는 지워주기
                binding.categoryDialogRg2.check(checkedId)
            }
        }
    }
}
