package com.example.geeksasaeng.Chatting.ChattingRoom

import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.geeksasaeng.Chatting.ChattingList.ChattingRoomRVAdapter
import com.example.geeksasaeng.R
import com.example.geeksasaeng.Utils.BaseActivity
import com.example.geeksasaeng.Utils.CustomToastMsg
import com.example.geeksasaeng.Utils.getNickname
import com.example.geeksasaeng.databinding.ActivityChattingRoomBinding
import com.google.firebase.firestore.FirebaseFirestore
import java.text.SimpleDateFormat
import java.util.*

class ChattingRoomActivity: BaseActivity<ActivityChattingRoomBinding>(ActivityChattingRoomBinding::inflate) {

    private var roomName = String()
    private var chattingList: MutableList<Chatting> = mutableListOf()
    lateinit var chattingRoomRVAdapter: ChattingRoomRVAdapter
    var topLayoutFlag = false // topLayoutFlag (모든 파티원 X = False / 모든 파티원 O = True)
    var leader = false
    private var chattingRoomName = String()
    private var nickname = getNickname()

    // Firebase
    val db = FirebaseFirestore.getInstance()

    override fun initAfterBinding() {
        roomName = intent.getStringExtra("roomName").toString()

        binding.chattingRoomTitleTv.text = roomName

        initTopLayout()
        initAdapter()
        initClickListener()
        initTextChangedListener()
        initChatListener()
        initReceiveChatListener()
        initSendChatListener()
    }

    private fun initTopLayout() {
        // TODO: 입장한 사람이 채팅방 방장인지 일반 유저인지 구분하기
        // 방장 -> var leader = true     |     일반 유저 -> var leader = false
        topLayoutFlag = true
        leader = true

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
        // TODO: 채팅방 리스트 API와 연동
        chattingRoomName = "TestRoom3"
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
                    // 채팅 Adapter에 적용
                    // TODO: 보내는 시간 넣어주기
                    val item = Chatting(1, nickname, "test", R.drawable.ic_default_profile, myChatting, null)
                    chattingRoomRVAdapter.addItem(item)
                    chattingRoomRVAdapter.notifyDataSetChanged()
            }
        }
    }

    private fun initReceiveChatListener() {
        db.collection("Rooms").document(chattingRoomName).collection("Messages").get().addOnSuccessListener { result ->
            var item: Chatting

            for (document in result) {
                if (document["nickname"].toString() == nickname) // 자신의 채팅
                    item = Chatting(1, document["nickname"].toString(), document["time"].toString(), R.drawable.ic_default_profile, document["content"].toString(), null)
                else // 상대의 채팅
                    item = Chatting(2, document["nickname"].toString(), document["time"].toString(), R.drawable.ic_default_profile2, document["content"].toString(), null)
                
                chattingList.add(item)
            }

            chattingList = chattingList.sortedBy { it.time } as MutableList<Chatting>
            // Log.d("FIREBASE-RESPONSE", chattingList.toString())

            chattingRoomRVAdapter.addAllItems(chattingList)
        }
    }

        private fun calculateDate(): String {
            val now: Long = System.currentTimeMillis()
            val simpleDate = SimpleDateFormat("yyyy-MM-dd hh:mm:ss")
            return simpleDate.format(Date(now))
        }
    }