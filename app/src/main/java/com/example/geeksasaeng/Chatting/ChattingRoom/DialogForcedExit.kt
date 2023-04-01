package com.example.geeksasaeng.Chatting.ChattingRoom

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.os.Parcelable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.geeksasaeng.Chatting.ChattingRoom.Adapter.DialogForcedExitRVAdapter
import com.example.geeksasaeng.Chatting.ChattingRoom.Retrofit.*
import com.example.geeksasaeng.Utils.CustomToastMsg
import com.example.geeksasaeng.databinding.DialogChattingRoomForcedExitLeaderBinding
import kotlin.properties.Delegates


class DialogForcedExit: DialogFragment(), ChattingMemberForcedExitView, DeliveryPartyMemberForcedExitView{

    private lateinit var mchattingService : ChattingService
    lateinit var binding: DialogChattingRoomForcedExitLeaderBinding
    private lateinit var dialogForcedExitRVAdapter: DialogForcedExitRVAdapter

    private lateinit var roomId: String
    private var partyId by Delegates.notNull<Int>()
    private lateinit var mForcedExitMemberList: ArrayList<MemberData>
    private var removedChatMemberIdList: ArrayList<String> = ArrayList()
    private var membersIdList: ArrayList<Int> = ArrayList()

    override fun onResume() {
        super.onResume()
        val params = dialog!!.window!!.attributes
        params.width = WindowManager.LayoutParams.WRAP_CONTENT
        params.height = WindowManager.LayoutParams.WRAP_CONTENT
        dialog!!.window!!.attributes = params as WindowManager.LayoutParams

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DialogChattingRoomForcedExitLeaderBinding.inflate(inflater, container, false)
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT)) // 배경 투명하게 만들어줘야 둥근 테두리가 보인다.
        initData()
        initAdapter()
        initLClickListener()
        return binding.root
    }

    private fun initData() {
        roomId = requireArguments().getString("roomId").toString()
        partyId = requireArguments().getInt("partyId")
        mForcedExitMemberList = requireArguments().getParcelableArrayList<Parcelable?>("forcedExitList") as ArrayList<MemberData>
        Log.d("DialogForcedExit", "roomID : ${roomId} / partyId : $partyId / 받아온 List : $mForcedExitMemberList")
    }

    private fun initLClickListener() {
        binding.forcedExitCancelBtn.setOnClickListener {
            this.dismiss()
        }

        binding.forcedExitOkBtn.setOnClickListener {
            sendForcedExitMember() //★ 강제퇴장 api for 채팅방 호출
        }
    }

    private fun sendForcedExitMember() { //★ 강제퇴장 api for 채팅방 호출
        mchattingService = ChattingService()
        mchattingService.setChattingMemberForcedExitView(this)
        mchattingService.setDeliveryPartyMemberForcedExitView(this)

        for (member in mForcedExitMemberList){ //강제퇴장 시킬 멤버 id 배열 구성{
            Log.d("DialogForcedExit-member", member.toString())
            removedChatMemberIdList.add(member.chatMemberId) //for 채팅방 강제퇴장
            membersIdList.add(member.memberId) //for 배달파티 강제퇴장
        }

        Log.d("DialogForcedExit", "mForcedExitMemberList : ${mForcedExitMemberList} / removedChatMemberIdList : $removedChatMemberIdList / membersIdList : $membersIdList")

        val chattingMemberForcedExitRequest = ChattingMemberForcedExitRequest(removedChatMemberIdList, roomId)
        mchattingService.chattingMemberForcedExit(chattingMemberForcedExitRequest)
    }

    private fun initAdapter() {
        dialogForcedExitRVAdapter = DialogForcedExitRVAdapter(mForcedExitMemberList)
        binding.forcedExitRv.adapter = dialogForcedExitRVAdapter
        binding.forcedExitRv.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)

    }

    override fun chattingMemberForcedExitSuccess(result: ChattingMemberForcedExitResult) { //강제퇴장 api for 채팅방이 성공 시,
        Log.d("DialogForcedExit for 채팅방", result.toString())
        val deliveryPartyMemberForcedExitRequest = DeliveryPartyMemberForcedExitRequest(membersIdList, partyId)
        mchattingService.deliveryPartyMemberForcedExit(deliveryPartyMemberForcedExitRequest) //★ 강제퇴장 api for 배달파티 호출
    }

    override fun chattingMemberForcedExitFailure(code: Int, message: String) {
        Log.d("DialogForcedExit for 채팅방", "code = $code, message = $message")
    }

    override fun deliveryPartyMemberForcedExitSuccess(result: DeliveryPartyMemberForcedExitResult) {
        Log.d("DialogForcedExit for 배달파티", result.toString())
        requireActivity().finish()
        CustomToastMsg.createToast(requireContext(), "강제 퇴장이 완료되었습니다", "#8029ABE2", 53)?.show()
    }

    override fun deliveryPartyMemberForcedExitFailure(code: Int, message: String) {
        Log.d("DialogForcedExit for 배달파티", "code = $code, message = $message")
    }


}


