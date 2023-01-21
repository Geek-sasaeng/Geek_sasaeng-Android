package com.example.geeksasaeng.Chatting.ChattingRoom

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.DialogFragment
import com.example.geeksasaeng.databinding.DialogChattingRoomOptionNotLeaderPopupBinding


class MemberOptionDialog: DialogFragment() {
    lateinit var binding: DialogChattingRoomOptionNotLeaderPopupBinding
    lateinit var memberOptionView: MemberOptionView

    fun setOptionView(memberOptionView: MemberOptionView){
        this.memberOptionView = memberOptionView
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DialogChattingRoomOptionNotLeaderPopupBinding.inflate(inflater, container, false)
        dialog?.window?.setGravity(Gravity.TOP or Gravity.RIGHT)
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT)) // 배경 투명하게 만들어줘야 둥근 테두리가 보인다.
        dialog?.window?.setWindowAnimations(com.example.geeksasaeng.R.style.AnimationPopupStyle)
        initListener()
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        val params = dialog!!.window!!.attributes
        params.width = WindowManager.LayoutParams.WRAP_CONTENT
        params.height = WindowManager.LayoutParams.WRAP_CONTENT
        dialog!!.window!!.attributes = params as WindowManager.LayoutParams
    }

    private fun initListener() {
        initExistClickListener()
    }

    private fun initExistClickListener(){
        binding.dialogNotLeaderPopupOptionExitTv.setOnClickListener { //파티나가기
            Log.d("exit", "멤버 파티 나가기 클릭됨")
            //memberOptionView.MemberExistClick()
            val dialogChattingExit = DialogChattingExit()
            val bundle = Bundle()
            bundle.putBoolean("leader",true)
            dialogChattingExit.arguments = bundle
            dialogChattingExit.show(parentFragmentManager, "DialogChattingExit")
        }
    }
}
