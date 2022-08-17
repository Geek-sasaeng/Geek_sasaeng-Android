package com.example.geeksasaeng.Home.Party.LookParty

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.*
import androidx.fragment.app.DialogFragment
import com.example.geeksasaeng.Chatting.ChattingList.ChattingListFragment
import com.example.geeksasaeng.MainActivity
import com.example.geeksasaeng.R
import com.example.geeksasaeng.databinding.DialogPartyRequestCompleteBinding

interface DialogPartyRequestCompleteView{
    fun showPartyChattingRoom()
}
class DialogPartyRequestComplete: DialogFragment() {
    lateinit var binding: DialogPartyRequestCompleteBinding
    lateinit var dialogPartyRequestCompleteView: DialogPartyRequestCompleteView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DialogPartyRequestCompleteBinding.inflate(inflater, container, false)
        initListener()
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT)) // 배경 투명하게 만들어줘야 둥근 테두리가 보인다.
        // TODO: dialog 위치 변경해주기
        dialog?.window?.setGravity(Gravity.TOP)

        return binding.root
    }

    private fun initListener(){
        binding.partyRequestCompleteCancelBtn.setOnClickListener {
            // 신청하기 취소
            this.dismiss()
        }

        binding.partyRequestCompleteGoBtn.setOnClickListener {
            // 채팅방으로 이동하기
            dialogPartyRequestCompleteView.showPartyChattingRoom()
            this.dismiss()
            (context as MainActivity).supportFragmentManager.beginTransaction().replace(R.id.main_frm, ChattingListFragment()).commit()
            // TODO: Bottom_navi 아이콘 채팅으로 변경해주기
        }
    }

    override fun onResume() {
        super.onResume()
        val width = resources.getDimensionPixelSize(R.dimen.party_request_complete_width)
        val height = resources.getDimensionPixelSize(R.dimen.party_request_complete_height)
        dialog?.window?.setLayout(width,height)
    }
}