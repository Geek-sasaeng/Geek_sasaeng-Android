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


    override fun onResume() {
        super.onResume()
        // Dialog 사이즈 조절 하기
        // 와! 획기적이야..! 이때까지 dp로 했는데 이렇게 할 수 있다니.. 동적으로 크기도 변경할 수 있겠다!
        // => 더 알아보고, 다른 코드도 이렇게 바꾸자
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
        initAdapter()
        initLClickListener()
        return binding.root
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
        // TODO: 리스트에 있는 멤버하나하나 별로 강제퇴장 API 호출해줘야해?? (memberId,roomId필요) - TEST 필요
        val chattingMemberForcedExitRequest = ChattingMemberForcedExitRequest("string","string")
        mchattingService.chattingMemberForcedExit(chattingMemberForcedExitRequest)
    }

    private fun initAdapter() {
        val mMemberList = requireArguments().getParcelableArrayList<Parcelable?>("forcedExitList") as MutableList<MemberData>
        Log.d("forcedExitDialog", "dialog 받아온 List : $mMemberList")
        dialogForcedExitRVAdapter = DialogForcedExitRVAdapter(mMemberList)
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


