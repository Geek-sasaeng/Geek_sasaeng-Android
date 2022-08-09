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
    private var chattingArray = ArrayList<Chatting>()
    lateinit var chattingRoomRVAdapter: ChattingRoomRVAdapter
    // topLayoutFlag (모든 파티원 X = False / 모든 파티원 O = True)
    var topLayoutFlag = false
    var leader = false
    private var chattingRoomName = String()
    private var nickname = getNickname()

    // Firebase
    val db = FirebaseFirestore.getInstance()

    override fun initAfterBinding() {
        roomName = intent.getStringExtra("roomName").toString()

        binding.chattingRoomTitleTv.text = roomName

        initChatArray()
        initTopLayout()
        initAdapter()
        initClickListener()
        initTextChangedListener()
        initChatListener()
        initReceiveChatListener()
        initSendChatListener()
    }

    private fun initChatArray() {
        /*
        chattingArray.apply {
            add(Chatting(3, null, null, "네오님이 입장하셨습니다", null))
            add(Chatting(3, null, null, "루나님이 입장하셨습니다", null))
            add(Chatting(1, true, R.drawable.ic_default_profile, "안녕하세요안녕하세요안녕하세요안녕하세요안녕하세요안녕하세요안녕하세요안녕하세요안녕하세요", null))
            add(Chatting(4, null, null, null, null))
            add(Chatting(2, false, R.drawable.ic_default_profile2, "안녕하세요!", null))
            add(Chatting(3, null, null, "제로님이 입장하셨습니다", null))
            add(Chatting(3, null, null, "모든 파티원이 입장을 마쳤습니다!\n안내에 따라 메뉴를 입력해주세요", null))
            add(Chatting(1, true, R.drawable.ic_default_profile, "안녕하세요~", 1))
            add(Chatting(2, false, R.drawable.ic_default_profile2, "안녕안녕안녕안녕안녕안녕안녕안녕안녕안녕안녕안녕안녕안녕안녕안녕안녕안녕안녕안녕안녕안녕안녕안녕", 1))
            add(Chatting(1, true, R.drawable.ic_default_profile, "뭐 먹을까요", 1))
            add(Chatting(2, false, R.drawable.ic_default_profile2, "저는 짜장면이요!", 1))
            add(Chatting(1, true, R.drawable.ic_default_profile, "좋아요", 2))
        }
        */
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
        chattingRoomRVAdapter = ChattingRoomRVAdapter(chattingArray)
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
                    val item = Chatting(1, nickname, R.drawable.ic_default_profile, myChatting, null)
                    chattingRoomRVAdapter.addItem(item)
                    chattingRoomRVAdapter.notifyDataSetChanged()
            }
        }
    }

    private fun initReceiveChatListener() {
        db.collection("Rooms").document(chattingRoomName).collection("Messages").get().addOnSuccessListener { result ->
            for (document in result) {
                var item: Chatting

                if (document["nickname"].toString() == nickname) // 자신의 채팅
                    item = Chatting(1, document["nickname"].toString(), R.drawable.ic_default_profile, document["content"].toString(), null)
                else // 상대의 채팅 
                    item = Chatting(2, document["nickname"].toString(), R.drawable.ic_default_profile2, document["content"].toString(), null)
                
                chattingRoomRVAdapter.addItem(item)
                chattingRoomRVAdapter.notifyDataSetChanged()
            }
        }
    }

    private fun calculateDate(): String {
        val now: Long = System.currentTimeMillis()
        val simpleDate = SimpleDateFormat("yyyy-MM-dd hh:mm:ss")
        return simpleDate.format(Date(now))
    }
}