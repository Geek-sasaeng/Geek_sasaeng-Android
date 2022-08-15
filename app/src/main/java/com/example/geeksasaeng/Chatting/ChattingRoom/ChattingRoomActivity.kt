package com.example.geeksasaeng.Chatting.ChattingRoom

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.geeksasaeng.Chatting.ChattingList.ChattingRoomRVAdapter
import com.example.geeksasaeng.Chatting.ChattingRoom.Retrofit.ChattingMemberLeaveView
import com.example.geeksasaeng.Chatting.ChattingRoom.Retrofit.ChattingPartyLeaveRequest
import com.example.geeksasaeng.Chatting.ChattingRoom.Retrofit.ChattingService
import com.example.geeksasaeng.R
import com.example.geeksasaeng.Utils.BaseActivity
import com.example.geeksasaeng.Utils.CustomToastMsg
import com.example.geeksasaeng.Utils.getNickname
import com.example.geeksasaeng.databinding.ActivityChattingRoomBinding
import com.google.firebase.firestore.*
import java.text.SimpleDateFormat
import java.util.*

class ChattingRoomActivity: BaseActivity<ActivityChattingRoomBinding>(ActivityChattingRoomBinding::inflate), ChattingMemberLeaveView
, MemberOptionView{

    private var roomName = String()
    private var chattingList: MutableList<Chatting> = ArrayList()
    private var roomUuid = String()
    lateinit var chattingRoomRVAdapter: ChattingRoomRVAdapter
    lateinit var chattingService: ChattingService
    // topLayoutFlag (모든 파티원 X = False / 모든 파티원 O = True)
    var topLayoutFlag = false
    var leader = false
    private var chattingRoomName = String()
    private var nickname = getNickname()
    private var chattingNumber = 0

    // Firebase
    val db = FirebaseFirestore.getInstance()

    override fun initAfterBinding() {
        roomName = intent.getStringExtra("roomName").toString()
        roomUuid = intent.getStringExtra("roomUuid").toString()
        binding.chattingRoomTitleTv.text = roomName

        initTopLayout()
        initClickListener()
        initTextChangedListener()
        initChatListener()
        initSendChatListener()
        initRealTimeChatListener()
        initAdapter()
        initChattingService()
        // binding.chattingRoomChattingRv.smoothScrollToPosition(30)
        optionClickListener()
    }

    private fun initTopLayout() {
        // TODO: 입장한 사람이 채팅방 방장인지 일반 유저인지 구분하기
        // 방장 -> var leader = true     |     일반 유저 -> var leader = false
        topLayoutFlag = true
//      leader = true

        if (topLayoutFlag) {
            binding.chattingRoomTopLayout.visibility = View.INVISIBLE

            if (leader) {
                binding.chattingRoomTopLayoutStatusTv.text = "메뉴 보기"
                binding.chattingRoomTopLayoutBtnTv.text = "주문 완료"
            } else {
                binding.chattingRoomTopLayoutStatusTv.text = "신한 000-000-000000"
                binding.chattingRoomTopLayoutBtnTv.text = "송금 완료"
            }
        } else {
            binding.chattingRoomTopLayout.visibility = View.VISIBLE
        }
    }

    private fun initAdapter() {
        chattingRoomRVAdapter = ChattingRoomRVAdapter(chattingList)
        binding.chattingRoomChattingRv.adapter = chattingRoomRVAdapter
        binding.chattingRoomChattingRv.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
    }

    private fun initClickListener() {
        binding.chattingRoomBackBtn.setOnClickListener {
            finish()
        }

        binding.chattingRoomTopLayoutBtn.setOnClickListener {
            binding.chattingRoomTopLayout.visibility = View.GONE
            topLayoutFlag = false

            if (binding.chattingRoomTopLayoutBtnTv.text == "주문 완료")
                CustomToastMsg.createToast(this, "주문이 완료되었습니다", "#8029ABE2", 53)?.show()
            else if (binding.chattingRoomTopLayoutBtnTv.text == "송금 완료")
                CustomToastMsg.createToast(this, "송금이 완료되었습니다", "#8029ABE2", 53)?.show()
        }
    }

    private fun initChattingService(){
        chattingService = ChattingService()
        chattingService.setChattingMemberLeaveView(this)
    }

    private fun optionClickListener() {
        binding.chattingRoomOptionBtn.setOnClickListener{
            // TODO 사용자, 방장일 경우 구분해서 옵션 보여주기
            if(leader){
                val optionDialog = LeaderOptionDialog()
                val bundle = Bundle()
                bundle.putString("roomUuid", roomUuid)
                optionDialog.arguments = bundle
                optionDialog.show(supportFragmentManager, "chattingLeaderOptionDialog")
            }else{
                val optionDialog = MemberOptionDialog()
                optionDialog.setOptionView(this)
                optionDialog.show(supportFragmentManager, "chattingUserOptionDialog")
            }

        }

        binding.chattingRoomBackBtn.setOnClickListener {
            finish()
        }

        binding.chattingRoomTopLayoutBtn.setOnClickListener {
            binding.chattingRoomTopLayout.visibility = View.GONE
            topLayoutFlag = false

            if (binding.chattingRoomTopLayoutBtnTv.text == "주문 완료")
                CustomToastMsg.createToast(this, "주문이 완료되었습니다", "#8029ABE2", 53)?.show()
            else if (binding.chattingRoomTopLayoutBtnTv.text == "송금 완료")
                CustomToastMsg.createToast(this, "송금이 완료되었습니다", "#8029ABE2", 53)?.show()
        }
    }

    private fun initTextChangedListener() {
        binding.chattingRoomChattingTextEt.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun afterTextChanged(p0: Editable?) {
                if (binding.chattingRoomChattingTextEt.text.isNotEmpty()) {
                    binding.chattingRoomSendTv.setTextColor(ContextCompat.getColor(applicationContext, R.color.main))
                    binding.chattingRoomSendTv.isEnabled = true
                }
                else {
                    binding.chattingRoomSendTv.setTextColor(ContextCompat.getColor(applicationContext, R.color.gray_2))
                    binding.chattingRoomSendTv.isEnabled = false
                }
            }
        })
    }

    private fun initChatListener() {
        chattingRoomName = roomUuid
    }

    private fun initSendChatListener() {
        binding.chattingRoomSendTv.setOnClickListener {
            val uuid = UUID.randomUUID().toString()

            var myChatting = binding.chattingRoomChattingTextEt.text.toString()
            var time = calculateDate()

            var data = hashMapOf(
                "content" to myChatting,
                "nickname" to nickname,
                "time" to time,
                "userImgUrl" to "이미지 링크"
            )

            db.collection("Rooms").document(chattingRoomName).collection("Messages")
                .document(uuid).set(data).addOnSuccessListener {
                    binding.chattingRoomChattingTextEt.setText("")
            }
        }
    }

    private fun calculateDate(): String {
        val now: Long = System.currentTimeMillis()
        val simpleDate = SimpleDateFormat("yyyy-MM-dd hh:mm:ss aa")
        var date: String = simpleDate.format(Date(now)).toString()
        Log.d("ampm", date.toString())
        if (date.substring(20) == "오전" && date.substring(11, 13) == "12")
            date = date.substring(0, 11) + "00" + date.substring(13)
        else if (date.substring(20) == "오후")
            date = date.substring(0, 11) + (Integer.parseInt(date.substring(11, 13)) + 12).toString() + date.substring(13)
        return date
    }

    private fun removeFireBasePartyMember(){
        var participantsId: Int = -1
        /*
                    FireBase
         */
        // 채팅방 참여자 id 가져오기
        db.collection("Rooms")
            .document(roomUuid)
            .get()
            .addOnSuccessListener { document ->
                val roomInfo = document.get("roomInfo") as HashMap<String, Any> //roomInfo 필드 값 정보들을 해시맵 형태로 얻어온다.
                val participants = roomInfo.get("participants") as ArrayList<Any>
                for((idx,map) in participants.withIndex()) {
                    val map = map as HashMap<String, String>
                    val participant = map.get("participant").toString()
                    if (participant.equals(getNickname())) {
                        participantsId = idx;
                        break
                    }
                }
                if(participantsId == -1){
                    // TODO Idx not found 오류 만들기
                    return@addOnSuccessListener
                }

                // 채팅방 참여자 삭제
                db.collection("Rooms")
                    .document(roomUuid)
                    .update("roomInfo.participants", FieldValue.arrayRemove(participantsId))
                    .addOnSuccessListener { Log.d("chatting-member-leave", "파이어베이스 채팅방에서 유저가 삭제됐습니다.") }
                    .addOnFailureListener { e -> Log.w("chatting-member-leave", "파이어베이스 채팅방에서 유저를 삭제하는 도중에 오류가 발생했습니다.", e) }

            }
            .addOnFailureListener { exception ->
                Log.w("firestore", "Error getting documents: ", exception)
            }
    }

    private fun initRealTimeChatListener() {
        db.collection("Rooms").document(roomUuid).collection("Messages").addSnapshotListener { snapshots, _ ->
            for (dc in snapshots?.documentChanges!!) {
                if (dc.type == DocumentChange.Type.ADDED) {
                    var item: Chatting

                    if (nickname == dc.document["nickname"]) {
                        item = Chatting(1, nickname, dc.document["time"].toString(), R.drawable.ic_default_profile, dc.document["content"].toString(), 0)
                    } else {
                        item = Chatting(2, nickname, dc.document["time"].toString(), R.drawable.ic_default_profile2, dc.document["content"].toString(), 0)
                    }

                    chattingRoomRVAdapter.addItem(item)
                }
            }

            chattingRoomRVAdapter.itemSort()
            var scrollSize = chattingRoomRVAdapter.returnPosition() - 1
            binding.chattingRoomChattingRv.scrollToPosition(scrollSize)
        }
    }

    // 일반 유저가 나가기를 눌렀을 경우
    override fun MemberExistClick() {
        removeFireBasePartyMember()
//        val chattingPartyLeaveRequest = ChattingPartyLeaveRequest(roomUuid)
//        chattingService.getChattingPartyMemberLeave(chattingPartyLeaveRequest)
    }


    override fun chattingMemberLeaveSuccess(result: String) {
//        removeFireBasePartyMember()
    }

    override fun chattingMemberLeaveFailure(code: Int, message: String) {
        TODO("Not yet implemented")
    }
}