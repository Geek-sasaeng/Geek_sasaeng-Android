package com.example.geeksasaeng.Chatting.ChattingRoom

import android.os.Bundle
import android.os.Handler
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
import com.google.android.gms.tasks.Task
import com.google.android.gms.tasks.Tasks
import com.google.firebase.Timestamp
import com.google.firebase.firestore.*
import java.text.SimpleDateFormat
import java.util.*

class ChattingRoomActivity: BaseActivity<ActivityChattingRoomBinding>(ActivityChattingRoomBinding::inflate) {

    private var roomName = String()
    private var chattingList: MutableList<Chatting> = ArrayList()
    private var roomUuid = String()
    lateinit var chattingRoomRVAdapter: ChattingRoomRVAdapter
    // topLayoutFlag (모든 파티원 X = False / 모든 파티원 O = True)
    var topLayoutFlag = false // TODO: topLayoutFlag
    var leader = false
    private var chattingRoomName = String()
    private var nickname = getNickname()
    lateinit var bank: String
    lateinit var accountNumber: String

    // Firebase
    val db = FirebaseFirestore.getInstance()

    override fun initAfterBinding() {
        roomName = intent.getStringExtra("roomName").toString()
        roomUuid = intent.getStringExtra("roomUuid").toString()
        chattingRoomName = roomUuid
        binding.chattingRoomTitleTv.text = roomName

        initTopLayout()
        initClickListener()
        initTextChangedListener()
        initSendChatListener()
        initRealTimeChatListener()
        initAdapter()
        optionClickListener()
    }

    private fun initTopLayout() {
        // TODO: 입장한 사람이 채팅방 방장인지 일반 유저인지 구분하기
        // 방장 -> var leader = true     |     일반 유저 -> var leader = false
        topLayoutFlag = true
        // leader = true

        if (topLayoutFlag) {
            binding.chattingRoomTopLayout.visibility = View.VISIBLE

            if (leader) {
                binding.chattingRoomTopLayoutStatusTv.text = "메뉴 보기"
                binding.chattingRoomTopLayoutBtnTv.text = "주문 완료"
            } else {
//                Log.d("FIREBASE-RESPONSE", "ORDER = 1")
//                getBankAndAccountNumber()
//                Log.d("FIREBASE-RESPONSE", "ORDER = 8")
//                binding.chattingRoomTopLayoutStatusTv.text = "$bank $accountNumber"
//                Log.d("FIREBASE-RESPONSE", "ORDER = 9")
//                Log.d("FIREBASE-RESPONSE", "getBankAndAccountNumber = $bank $accountNumber")
//                Log.d("FIREBASE-RESPONSE", "ORDER = 10")
                binding.chattingRoomTopLayoutStatusTv.text = "은행 12345678900"
                binding.chattingRoomTopLayoutBtnTv.text = "송금 완료"
            }
        } else {
            binding.chattingRoomTopLayout.visibility = View.INVISIBLE
        }
    }

//    private fun getBankAndAccountNumber() {
//        Log.d("FIREBASE-RESPONSE", "ORDER = 2")
//        val getBankAndAccountNumberTask: Task<DocumentSnapshot> =
//        db.collection("Rooms").document(chattingRoomName).get().addOnSuccessListener { result ->
//            bank = result.get("roomInfo.bank").toString()
//            accountNumber = result.get("roomInfo.accountNumber").toString()
//            // Log.d("FIREBASE-RESPONSE", "1 BANK = ${result.get("roomInfo.bank").toString()} / ACCOUNT-NUMBER = ${result.get("roomInfo.accountNumber").toString()}")
//            Log.d("FIREBASE-RESPONSE", "ORDER = 3")
//        }.addOnFailureListener { exception ->
//            Log.e("firestore", "ERROR getting account number: ", exception)
//            bank = "은행 및 "
//            accountNumber = "계좌번호 불러오기 실패"
//        }
//        Log.d("FIREBASE-RESPONSE", "ORDER = 4")
//
//        try {
//            Log.d("FIREBASE-RESPONSE", "ORDER = 5")
//            Tasks.await(getBankAndAccountNumberTask)
//            Log.d("FIREBASE-RESPONSE", "ORDER = 6")
//        } catch (e: Exception) {
//            Log.e("FIRESTORE", "FAIL = ${e.toString()}")
//        }
//        Log.d("FIREBASE-RESPONSE", "ORDER = 7")
//    }

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

    private fun optionClickListener() {
        binding.chattingRoomOptionBtn.setOnClickListener{
            // TODO 사용자, 방장일 경우 구분해서 옵션 보여주기
            if(leader){
                val optionDialog = ChattingLeaderOptionDialog()
                val bundle = Bundle()
                bundle.putString("roomUuid", roomUuid)
                optionDialog.arguments = bundle
                optionDialog.show(supportFragmentManager, "chattingLeaderOptionDialog")
            }else{
                val optionDialog = ChattingNotLeaderOptionDialog()
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

    private fun initRealTimeChatListener() {
        db.collection("Rooms").document(roomUuid).collection("Messages").addSnapshotListener { snapshots, _ ->
            for (dc in snapshots?.documentChanges!!) {
                if (dc.type == DocumentChange.Type.ADDED) {
                    var item: Chatting

                    Log.d("FIREBASE-RESPONSE", "nickname = $nickname")
                    if (nickname == dc.document["nickname"]) {
                        item = Chatting(1, nickname, dc.document["time"].toString(), R.drawable.ic_default_profile, dc.document["content"].toString(), 0)
                    } else if (dc.document["nickname"] != null) {
                        item = Chatting(2, dc.document["nickname"].toString(), dc.document["time"].toString(), R.drawable.ic_default_profile2, dc.document["content"].toString(), 0)
                    } else {
                        item = Chatting(3, null, dc.document["time"].toString(), null, dc.document["content"].toString(), null)
                    }

                    chattingRoomRVAdapter.addItem(item)
                }
            }

            chattingRoomRVAdapter.itemSort()
            var scrollSize = chattingRoomRVAdapter.returnPosition() - 1
            binding.chattingRoomChattingRv.scrollToPosition(scrollSize)
        }
    }
}