package com.example.geeksasaeng.Chatting.ChattingRoom

import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import androidx.annotation.NonNull
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.geeksasaeng.Chatting.ChattingList.ChattingRoomRVAdapter
import com.example.geeksasaeng.Chatting.ChattingRoom.Retrofit.*
import com.example.geeksasaeng.R
import com.example.geeksasaeng.Utils.*
import com.example.geeksasaeng.databinding.ActivityChattingRoomBinding
import com.google.firebase.firestore.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.HashMap

class ChattingRoomActivity :
    BaseActivity<ActivityChattingRoomBinding>(ActivityChattingRoomBinding::inflate),
    ChattingMemberLeaveView, MemberOptionView, LeaderOptionView, ChattingLeaderLeaveView {

    private var roomName = String()
    private var chattingList: MutableList<Chatting> = ArrayList()
    private var roomUuid = String()
    private lateinit var participants: ArrayList<Any>
    private lateinit var chattingRoomRVAdapter: ChattingRoomRVAdapter
    private lateinit var chattingService: ChattingService

    // topLayoutFlag (모든 파티원 X = False / 모든 파티원 O = True)
    private var topLayoutFlag = false
    private var leader = false
    private var nickname = getNickname()
    private var chattingNumber = 0

    // Firebase
    val db = FirebaseFirestore.getInstance()

    override fun initAfterBinding() {
        roomName = intent.getStringExtra("roomName").toString()
        roomUuid = intent.getStringExtra("roomUuid").toString()
        binding.chattingRoomTitleTv.text = roomName

        initFirestore()
        initClickListener()

        initTextChangedListener()
        initSendChatListener()
        initAdapter()
        initChattingService()
        optionClickListener()
    }

    private fun initFirestore() {
        // 현재 파티원 정보 불러오기, 방장인지 아닌지 확인하기
        db.collection("Rooms")
            .document(roomUuid)
            .get()
            .addOnSuccessListener { document ->
                val roomInfo =
                    document.get("roomInfo") as java.util.HashMap<String, Any> //roomInfo 필드 값 정보들을 해시맵 형태로 얻어온다.
                participants = roomInfo.get("participants") as ArrayList<Any>
                var participantIdx: Int = -1
                for ((idx, map) in participants!!.withIndex()) {
                    val map = map as HashMap<String, String>
                    val participantName = map.get("participant").toString()
                    if (participantName.equals(getNickname())) {
                        participantIdx = idx
                        break
                    }
                }

                // Idx == 0 이면 방장임
                leader = participantIdx == 0

                topLayoutFlag = true
                // TopLayout 설정
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

                // 파이어스토어 Listener 설정
                initRealTimeChatListener()
                initChangeParticipantsListener()
            }
            .addOnFailureListener { e ->
                Log.w(
                    "chatting-member",
                    "파이어베이스 채팅방에서 유저 정보를 가져오는 도중에 오류가 발생했습니다."
                )
            }

    }

    private fun initAdapter() {
        chattingRoomRVAdapter = ChattingRoomRVAdapter(chattingList)
        binding.chattingRoomChattingRv.adapter = chattingRoomRVAdapter
        binding.chattingRoomChattingRv.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
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

    private fun initChattingService() {
        chattingService = ChattingService()
        chattingService.setChattingMemberLeaveView(this)
    }

    private fun optionClickListener() {
        binding.chattingRoomOptionBtn.setOnClickListener {
            if (leader) {
                val optionDialog = LeaderOptionDialog()
                optionDialog.setLeaderOptionView(this)
                optionDialog.show(supportFragmentManager, "chattingLeaderOptionDialog")
            } else {
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
        binding.chattingRoomChattingTextEt.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun afterTextChanged(p0: Editable?) {
                if (binding.chattingRoomChattingTextEt.text.isNotEmpty()) {
                    binding.chattingRoomSendTv.setTextColor(
                        ContextCompat.getColor(
                            applicationContext,
                            R.color.main
                        )
                    )
                    binding.chattingRoomSendTv.isEnabled = true
                } else {
                    binding.chattingRoomSendTv.setTextColor(
                        ContextCompat.getColor(
                            applicationContext,
                            R.color.gray_2
                        )
                    )
                    binding.chattingRoomSendTv.isEnabled = false
                }
            }
        })
    }

    private fun initChangeParticipantsListener() {
        db.collection("Rooms").document(roomUuid)
            .addSnapshotListener { document, error ->
                val roomInfo =
                    document!!.get("roomInfo") as java.util.HashMap<String, Any> //roomInfo 필드 값 정보들을 해시맵 형태로 얻어온다.
                participants = roomInfo.get("participants") as ArrayList<Any>
            }
    }

    private fun initRealTimeChatListener() {
        db.collection("Rooms").document(roomUuid).collection("Messages")
            .orderBy("time", Query.Direction.ASCENDING)
            .addSnapshotListener { snapshots, _ ->
                for ((idx, dc) in snapshots?.documentChanges!!.withIndex()) {
                    if(dc.document["readUsers"] == null)
                        continue

                    val readUsers = dc.document["readUsers"] as ArrayList<Any>

                    // readUsers에 자신의 닉네임이 없다면 추가해서 업데이트 하기
                    if(!readUsers.contains(getNickname()!!)){
                        readUsers.add(getNickname()!!)

                        val messageId = dc.document.id
                        db.collection("Rooms").document(roomUuid).collection("Messages")
                            .document(messageId).update("readUsers", readUsers)
                        readUsers.add(getNickname()!!)
                    }
                    val notReadCnt = participants.size - readUsers.size

                    // 새로운 메시지가 추가되었을 경우
                    if (dc.type == DocumentChange.Type.ADDED) {
                        var item: Chatting
                        if (nickname == dc.document["nickname"]) {
                            item = Chatting(
                                1,
                                nickname,
                                dc.document["time"].toString(),
                                R.drawable.ic_default_profile,
                                dc.document["content"].toString(),
                                notReadCnt
                            )
                        } else {
                            val msgNickname = dc.document["nickname"] as String
                            item = Chatting(
                                2,
                                msgNickname,
                                dc.document["time"].toString(),
                                R.drawable.ic_default_profile2,
                                dc.document["content"].toString(),
                                notReadCnt
                            )
                        }
                        chattingRoomRVAdapter.addItem(item)

                     // readUsers가 업데이트 되었을 경우
                    }else if(dc.type == DocumentChange.Type.MODIFIED){
                        chattingRoomRVAdapter.setUnreadCount(idx, notReadCnt)
                    }
                }

                var scrollSize = chattingRoomRVAdapter.returnPosition() - 1
                binding.chattingRoomChattingRv.scrollToPosition(scrollSize)
            }
    }

    // 메시지 전송
    private fun initSendChatListener() {
        binding.chattingRoomSendTv.setOnClickListener { //메세지 전송버튼
            val uuid = UUID.randomUUID().toString()
            var myChatting = binding.chattingRoomChattingTextEt.text.toString()
            var time = getCurrentDateTime()
            val readUsers = ArrayList<String>();
            readUsers.add(getNickname()!!)

            var data = hashMapOf(
                "content" to myChatting,
                "nickname" to nickname,
                "time" to time,
                "userImgUrl" to "이미지 링크",
                "readUsers" to readUsers,
            )

            // 파이어 스토어에 추가
            db.collection("Rooms").document(roomUuid).collection("Messages")
                .document(uuid).set(data).addOnSuccessListener {
                    binding.chattingRoomChattingTextEt.setText("")
            }
        }
    }

    fun getCurrentDateTime(): String{
        val formatter = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        return formatter.format(Calendar.getInstance().time)
    }

    private fun calculateDate(): String {
        val now: Long = System.currentTimeMillis()
        val simpleDate = SimpleDateFormat("yyyy-MM-dd hh:mm:ss aa")
        var date: String = simpleDate.format(Date(now)).toString()
        Log.d("ampm", date.toString())
        if (date.substring(20) == "오전" && date.substring(11, 13) == "12")
            date = date.substring(0, 11) + "00" + date.substring(13)
        else if (date.substring(20) == "오후")
            date = date.substring(0, 11) + (Integer.parseInt(
                date.substring(
                    11,
                    13
                )
            ) + 12).toString() + date.substring(13)
        return date
    }

    // 일반 유저 나가기
    override fun MemberExistClick() {
        val chattingPartyMemberLeaveRequest = ChattingPartyMemberLeaveRequest(roomUuid)
        chattingService.getChattingPartyMemberLeave(chattingPartyMemberLeaveRequest)
    }

    // 방장 나가기
    override fun LeaderExistClick() {
        val db = FirebaseFirestore.getInstance()

        // 방장 나가기
        var participants: ArrayList<Any>? = null
        db.collection("Rooms")
            .document(roomUuid)
            .get()
            .addOnSuccessListener { document ->
                val roomInfo =
                    document.get("roomInfo") as java.util.HashMap<String, Any> //roomInfo 필드 값 정보들을 해시맵 형태로 얻어온다.
                participants = roomInfo.get("participants") as ArrayList<Any>
                var leaderMap: HashMap<String, String>? = null
                var nextLeader: String? = null
                // 방장 후보군 닉네임 가져오기
                for ((idx, map) in participants!!.withIndex()) {
                    val map = map as HashMap<String, String>
                    if (idx == 0) {
                        leaderMap = map
                    } else if (idx == 1) {
                        nextLeader = map.get("participant").toString()
                        break
                    }
                }

                // 방장 삭제하기 API 호출
                chattingService.setChattingLeaderLeaveView(this)
                val chattingPartyLeaderLeaveRequest =
                    ChattingPartyLeaderLeaveRequest(nextLeader, roomUuid)
                chattingService.getChattingPartyLeaderLeave(
                    chattingPartyLeaderLeaveRequest,
                    leaderMap!!
                )
            }
    }


    override fun chattingMemberLeaveSuccess(result: String) {
        // 파이어베이스 멤버 삭제
        var participants: ArrayList<Any>? = null

        // 나의 participantMap 가져오기
        db.collection("Rooms")
            .document(roomUuid)
            .get()
            .addOnSuccessListener { document ->
                val roomInfo =
                    document.get("roomInfo") as HashMap<String, Any> //roomInfo 필드 값 정보들을 해시맵 형태로 얻어온다.
                participants = roomInfo.get("participants") as ArrayList<Any>
                var participantMap: HashMap<String, String>? = null
                for ((idx, map) in participants!!.withIndex()) {
                    val map = map as HashMap<String, String>
                    if (map.get("participant").equals(getNickname())) {

                        // participants 에서 제거하기
                        db.collection("Rooms")
                            .document(roomUuid)
                            .update("roomInfo.participants", FieldValue.arrayRemove(map))
                            .addOnSuccessListener {
                                finish()
                                Log.d("chatting-member-leave", "파이어베이스 채팅방에서 유저가 삭제됐습니다.")
                            }
                            .addOnFailureListener { e ->
                                Log.w(
                                    "chatting-member-leave",
                                    "파이어베이스 채팅방에서 유저를 삭제하는 도중에 오류가 발생했습니다."
                                )
                            }
                        break

                    }
                }
            }.addOnFailureListener { e ->
                Log.w(
                    "chatting-member",
                    "파이어베이스 채팅방에서 유저 정보를 가져오는 과정 중에 오류가 발생했습니다."
                )
            }
    }

    override fun chattingMemberLeaveFailure(code: Int, message: String) {
        TODO("Not yet implemented")
    }

    override fun chattingLeaderLeaveSuccess(result: String, leaderMap: HashMap<String, String>) {
        // 방장 삭제하기 파이어스토어 호출
        db.collection("Rooms")
            .document(roomUuid)
            .update("roomInfo.participants", FieldValue.arrayRemove(leaderMap))
            .addOnSuccessListener {
                Log.d(
                    "chatting-leader-leave",
                    "파이어베이스 채팅방에서 방장이 삭제됐습니다."
                )
            }
            .addOnFailureListener { e ->
                Log.w(
                    "chatting-leader-leave",
                    "파이어베이스 채팅방에서 방장이 삭제하는 도중에 오류가 발생했습니다."
                )
            }
        finish()
    }

    override fun chattingLeaderLeaveFailure(code: Int, message: String) {
        TODO("Not yet implemented")
    }
}