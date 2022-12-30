package com.example.geeksasaeng.Chatting.ChattingRoom

import android.Manifest
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.Gallery
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.geeksasaeng.Chatting.ChattingList.*
import com.example.geeksasaeng.Chatting.ChattingRoom.Retrofit.*
import com.example.geeksasaeng.R
import com.example.geeksasaeng.Utils.*
import com.example.geeksasaeng.databinding.ActivityChattingRoomBinding
import com.google.firebase.firestore.ListenerRegistration
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.HashMap

class ChattingRoomActivity :
    BaseActivity<ActivityChattingRoomBinding>(ActivityChattingRoomBinding::inflate),
    ChattingMemberLeaveView, MemberOptionView, LeaderOptionView, ChattingLeaderLeaveView {

    private val TAG = "CHATTING-ROOM-ACTIVITY"

    private var roomName = String()
    private var chattingList: MutableList<Chatting> = ArrayList()
    private var roomUuid = String()
    private var checkRemittance: Boolean = false
    private lateinit var participants: ArrayList<Any>
    private lateinit var chattingRoomRVAdapter: ChattingRoomRVAdapter
    private lateinit var chattingService: ChattingService
    private lateinit var realTimeChatListener: ListenerRegistration
    private lateinit var changeParticipantsListener: ListenerRegistration

    // topLayoutFlag (모든 파티원 X = False / 모든 파티원 O = True)
    private var topLayoutFlag = false
    private var leader = false
    private var leaderName = String()
    private var chattingRoomName = String()
    private var nickname = getNickname()
    lateinit var bank: String
    lateinit var accountNumber: String
    var preChatNickname: String = ""

    // Album
    val PERMISSION_Album = 101 // 앨범 권한 처리
    lateinit var activityResultLauncher: ActivityResultLauncher<Intent>
    
    override fun initAfterBinding() {
        roomName = intent.getStringExtra("roomName").toString()
//        roomUuid = intent.getStringExtra("roomUuid").toString()
//        chattingRoomName = roomUuid
        binding.chattingRoomTitleTv.text = roomName

        initClickListener()

        initTextChangedListener()
        initSendChatListener()
        initAdapter()
        initChattingService()
//        optionClickListener()
    }

    // 종료 시 리스너 제거
    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
//        realTimeChatListener.remove()
//        changeParticipantsListener.remove()
    }

    private fun getBankAndAccountNumber() {
        // TODO: 계좌번호 불러오기
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

        binding.chattingRoomAlbumIv.setOnClickListener {
            val intent = Intent()
            intent.type = "image/*"
            intent.action = Intent.ACTION_GET_CONTENT

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
                val bundle = Bundle()
                bundle.putString("roomUuid", roomUuid)
                optionDialog.arguments = bundle
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
            else if (binding.chattingRoomTopLayoutBtnTv.text == "송금 완료") {
                // TODO: 여기에 성공했을 때 toast message 뜨도록 아래 코드 넣어주기
            }
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
        // TODO : 참가자 확인
    }

    private fun initRealTimeChatListener() {
        // TODO: 실시간 채팅 시간 확인
    }

    // 메시지 전송
    private fun initSendChatListener() {
        binding.chattingRoomSendTv.setOnClickListener {
            // TODO: 메시지 전송
        }
    }

    private fun getCurrentDateTime(): String {
        val formatter = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        return formatter.format(Calendar.getInstance().time)
    }

    // 일반 유저 나가기
    override fun MemberExistClick() {
        val chattingPartyMemberLeaveRequest = ChattingPartyMemberLeaveRequest(roomUuid)
        chattingService.getChattingPartyMemberLeave(chattingPartyMemberLeaveRequest)
    }

    // 방장 나가기
    override fun LeaderExistClick() {
        // TODO: FIREBASE 버전에서 수정하기
    }

    override fun chattingMemberLeaveSuccess(result: String) {
        // TODO: 채팅방 나가기 성공
    }

    override fun chattingMemberLeaveFailure(code: Int, message: String) {
        // TODO: 채팅방 나가기 실패
    }

    override fun chattingLeaderLeaveSuccess(result: String, leaderMap: HashMap<String, String>) {
        // TODO: 방장 나가기
    }

    override fun chattingLeaderLeaveFailure(code: Int, message: String) {
        TODO("Not yet implemented")
    }

    private fun calculateToday(): String {
        val nowTime = System.currentTimeMillis();
        val date = Date(nowTime)
        var dateFormat: SimpleDateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        return dateFormat.format(date)
    }
}