package com.example.geeksasaeng.Home.Party.LookParty

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.navigation.fragment.findNavController
import com.example.geeksasaeng.Home.Party.Retrofit.JoinPartyRequest
import com.example.geeksasaeng.Home.Party.Retrofit.JoinPartyView
import com.example.geeksasaeng.Home.Party.Retrofit.PartyDataService
import com.example.geeksasaeng.R
import com.example.geeksasaeng.Signup.Basic.SignUpActivity
import com.example.geeksasaeng.Signup.Basic.StepFiveFragment
import com.example.geeksasaeng.databinding.DialogPartyRequestBinding
import com.example.geeksasaeng.databinding.DialogSignupPhoneSkipBinding
interface ChattingRoomJoinView{
    fun showPartyRequestSuccess()
    fun showPartyRequestFail()
}

class DialogPartyRequest(val partyId: Int): DialogFragment(), JoinPartyView {
    private lateinit var partyDataService : PartyDataService
    private lateinit var binding: DialogPartyRequestBinding
    private lateinit var chattingRoomJoinView : ChattingRoomJoinView

    fun setChattingRoonJoinView(chattingRoomJoinView: ChattingRoomJoinView){
        this.chattingRoomJoinView = chattingRoomJoinView
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DialogPartyRequestBinding.inflate(inflater, container, false)
        initListener()
        initDeliveryPartyData()
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT)) // 배경 투명하게 만들어줘야 둥근 테두리가 보인다.
        return binding.root
    }

    private fun initListener(){
        binding.partyRequestOkBtn.setOnClickListener {
            // 신청하기 수행
            val joinPartyRequest = JoinPartyRequest(partyId)
            partyDataService.joinPartySender(joinPartyRequest)
            this.dismiss()
        }

        binding.partyRequestCancelBtn.setOnClickListener {
            // 신청하기 취소
            this.dismiss()
        }
    }

    override fun onResume() {
        super.onResume()
        val width = resources.getDimensionPixelSize(R.dimen.party_request_width)
        val height = resources.getDimensionPixelSize(R.dimen.party_request_height)
        dialog?.window?.setLayout(width,height)
    }
    private fun initDeliveryPartyData() {
        partyDataService = PartyDataService()
        partyDataService.setJoinPartyView(this)
    }

    override fun joinPartyViewSuccess(code: Int) {
        Log.d("JoinParty", "SUCCESS")
        chattingRoomJoinView.showPartyRequestSuccess()
    }

    override fun joinPartyViewFailure(message: String) {
        Log.d("JoinParty", "FAIL")
        chattingRoomJoinView.showPartyRequestFail()
    }
}