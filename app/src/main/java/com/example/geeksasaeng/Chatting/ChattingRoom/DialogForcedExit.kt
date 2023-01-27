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
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.geeksasaeng.Chatting.ChattingRoom.Adapter.DialogForcedExitRVAdapter
import com.example.geeksasaeng.Chatting.ChattingRoom.Retrofit.*
import com.example.geeksasaeng.databinding.DialogChattingRoomForcedExitLeaderBinding


class DialogForcedExit: DialogFragment(), ChattingMemberForcedExitView{

    private lateinit var mchattingService : ChattingService
    lateinit var binding: DialogChattingRoomForcedExitLeaderBinding
    private lateinit var dialogForcedExitRVAdapter: DialogForcedExitRVAdapter

    private lateinit var roomId: String
    private lateinit var mForcedExitMemberList: ArrayList<MemberData>
    private lateinit var removedMemberIdList: ArrayList<String>

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
        mForcedExitMemberList = requireArguments().getParcelableArrayList<Parcelable?>("forcedExitList") as ArrayList<MemberData>
        Log.d("DialogForcedExit", "roomID : ${roomId} / 받아온 List : $mForcedExitMemberList")
    }

    private fun initLClickListener() {
        binding.forcedExitCancelBtn.setOnClickListener {
            this.dismiss()
        }

        binding.forcedExitOkBtn.setOnClickListener {
            sendForcedExitMember() //★ 강제퇴장 api 호출
        }
    }

    private fun sendForcedExitMember() { //★ 강제퇴장 api 호출
        mchattingService = ChattingService()
        mchattingService.setChattingMemberForcedExitView(this)

        for(member in mForcedExitMemberList) //강제퇴장 시킬 멤버 id 배열 구성
            removedMemberIdList.add(member.id)

        Log.d("DialogForcedExit", "mForcedExitMemberList : ${mForcedExitMemberList} / removedMemberIdList : $removedMemberIdList")

        val chattingMemberForcedExitRequest = ChattingMemberForcedExitRequest(removedMemberIdList, roomId)
        mchattingService.chattingMemberForcedExit(chattingMemberForcedExitRequest)
    }

    private fun initAdapter() {
        dialogForcedExitRVAdapter = DialogForcedExitRVAdapter(mForcedExitMemberList)
        binding.forcedExitRv.adapter = dialogForcedExitRVAdapter
        binding.forcedExitRv.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)

    }

    override fun chattingMemberForcedExitSuccess(result: ChattingMemberForcedExitResult) {
        Log.d("DialogForcedExit", result.toString())
    }

    override fun chattingMemberForcedExitFailure(code: Int, message: String) {
        Log.d("DialogForcedExit", "code = $code, message = $message")
    }


}


