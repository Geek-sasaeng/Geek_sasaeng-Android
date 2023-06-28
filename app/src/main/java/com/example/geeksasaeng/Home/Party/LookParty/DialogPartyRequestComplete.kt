package com.example.geeksasaeng.Home.Party.LookParty

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.*
import androidx.fragment.app.DialogFragment
import com.example.geeksasaeng.Chatting.ChattingList.ChattingListFragment
import com.example.geeksasaeng.MainActivity
import com.example.geeksasaeng.MyBottomNaviCallback
import com.example.geeksasaeng.R
import com.example.geeksasaeng.databinding.DialogPartyRequestCompleteBinding

interface DialogPartyRequestCompleteView{
    fun showPartyChattingRoom()
}

class DialogPartyRequestComplete: DialogFragment() {
    lateinit var binding: DialogPartyRequestCompleteBinding
    lateinit var dialogPartyRequestCompleteView: DialogPartyRequestCompleteView
    lateinit var bottomNaviCallback: MyBottomNaviCallback

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DialogPartyRequestCompleteBinding.inflate(inflater, container, false)
        initListener()
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT)) // 배경 투명하게 만들어줘야 둥근 테두리가 보인다.
        dialog?.window?.setGravity(Gravity.TOP)
        var params: WindowManager.LayoutParams = dialog?.window?.attributes!!
        params.y = 200
        dialog?.window?.attributes = params

        return binding.root
    }

    private fun initListener(){
        binding.partyRequestCompleteCancelBtn.setOnClickListener {
            // 신청하기 취소
            this.dismiss()
        }

        binding.partyRequestCompleteGoBtn.setOnClickListener {
            this.dismiss()
            (context as MainActivity).supportFragmentManager.beginTransaction().replace(R.id.main_frm, ChattingListFragment()).commit()
            //바텀내비 바꾸기
            bottomNaviCallback = activity as MyBottomNaviCallback
            bottomNaviCallback.changeBottomIcon(R.id.chattingFragment)
        }
    }
}