package com.example.geeksasaeng.Chatting.ChattingRoom

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.geeksasaeng.BuildConfig
import com.example.geeksasaeng.ChatSetting.RabbitMQSetting
import com.example.geeksasaeng.ChatSetting.WebSocketListenerInterface
import com.example.geeksasaeng.ChatSetting.WebSocketManager
import com.example.geeksasaeng.Chatting.ChattingList.*
import com.example.geeksasaeng.Chatting.ChattingRoom.Retrofit.*
import com.example.geeksasaeng.R
import com.example.geeksasaeng.Utils.*
import com.example.geeksasaeng.databinding.ActivityChattingRoomBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.firebase.firestore.*
import com.google.firebase.firestore.ListenerRegistration
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import com.rabbitmq.client.*
import okhttp3.OkHttpClient
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.*
import kotlin.concurrent.thread

class ChattingRoomActivity :
    BaseActivity<ActivityChattingRoomBinding>(ActivityChattingRoomBinding::inflate),
    ChattingMemberLeaveView, MemberOptionView, LeaderOptionView, ChattingLeaderLeaveView,
    WebSocketListenerInterface, SendChattingView, ChattingOrderCompleteView, ChattingRemittanceCompleteView {

    private val TAG = "CHATTING-ROOM-ACTIVITY"

    private var roomName = String()
    private var chattingList: MutableList<Chatting> = ArrayList()
    private var roomId = String()
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

    // Chatting
    // WebSocket
    private var chatClient = OkHttpClient()
    // RabbitMQ
    private val rabbitMQUri = "amqp://" + BuildConfig.RABBITMQ_ID + ":" + BuildConfig.RABBITMQ_PWD + "@" + BuildConfig.RABBITMQ_ADDRESS
    private val factory = ConnectionFactory()
    lateinit var conn: Connection
    lateinit var channel: Channel
    // QUEUE_NAME = MemberID!
    val QUEUE_NAME = "110"

    override fun initAfterBinding() {
        roomName = intent.getStringExtra("roomName").toString()
        roomId = intent.getStringExtra("roomId").toString()
        Log.d("ChatTest", "roomName = $roomName / roomId = $roomId")
        //TODO: room name이랑 roomId는 인텐트로 받아오는데, 이 채팅방에서 주문완료 여부나, 리더여부나 , 송금완료 여부 이런건 어디서 받아와?
        binding.chattingRoomTitleTv.text = roomName

        initClickListener()
        initTextChangedListener()
        initSendChatListener()
        initAdapter()
        initChattingService()
        optionClickListener()
        initTopLayout() // 상단 송금완료/주문완료 부분 레이아웃 조정하는 부분

        // Main Thread에서 Network 관련 작업을 하려고 하면 NetworkOnMainThreadException 발생!!
        // So, 새로운 Thread를 만들어 그 안에서 작동되도록!!!!
        Thread {
            initRabbitMQSetting()
        }.start()

        // initRabbitMQSetting()

//        var rabbitMQSetting = RabbitMQSetting()
//        rabbitMQSetting.main()
    }

    private fun initTopLayout(){
        //TODO: 주문 또는 송금 완료했으면 안보이게 해주기
        //TODO: 그렇지 않았다면~~
        if(leader){ //리더면 상단 바 구성을 다르게 해줘야하므로
            binding.chattingRoomTopLayoutOrderCompleteBtn.visibility = View.VISIBLE
            binding.chattingRoomSectionIv.visibility = View.INVISIBLE
            binding.chattingRoomTopLayoutStatusTv.visibility = View.INVISIBLE
            binding.chattingRoomTopLayoutRemittanceCompleteBtn.visibility = View.INVISIBLE
        }
    }

    fun initRabbitMQSetting() {
        factory.setUri(rabbitMQUri)
        // RabbitMQ 연결
        val conn: Connection = factory.newConnection()
        // Send and Receive 가능하도록 해주는 부분!
        val channel = conn.createChannel()
        // durable = true로 설정!!
        // 참고 : https://teragoon.wordpress.com/2012/01/26/message-durability%EB%A9%94%EC%8B%9C%EC%A7%80-%EC%9E%83%EC%96%B4%EB%B2%84%EB%A6%AC%EC%A7%80-%EC%95%8A%EA%B8%B0-durabletrue-propspersistent_text_plain-2/
        channel.queueDeclare(QUEUE_NAME, true, false, false, null);
        Log.d("RabbitMQ-Test", " [*] Waiting for messages. To exit press CTRL+C")

        val deliverCallback = DeliverCallback { consumerTag: String?, delivery: Delivery ->
            val message = String(delivery.body, Charsets.UTF_8)
            Log.d("RabbitMQ-Test", "")
            println(" [x] Received '$message'")
        }
        channel.basicConsume(
            QUEUE_NAME, true, deliverCallback
        ) { consumerTag: String? -> }
    }

    override fun onResume() {
        super.onResume()
        Log.d("WebSocketListener-Test", "onResume")
        webSocketStart()
    }

    private fun webSocketStart() {
        Log.d("WebSocketListener-Test", "webSocketStart")
        WebSocketManager.init("ws://geeksasaeng.shop:8080/chatting", this)
    }

    // 종료 시 리스너 제거
    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
//        realTimeChatListener.remove()
//        changeParticipantsListener.remove()
        Log.d("WebSocketListener-Test", "END")
        WebSocketManager.close()
    }

    private fun getBankAndAccountNumber() {
        // TODO: 계좌번호 불러오기
    }

    private fun initAdapter() {
        chattingRoomRVAdapter = ChattingRoomRVAdapter(chattingList)
        binding.chattingRoomChattingRv.adapter = chattingRoomRVAdapter
        binding.chattingRoomChattingRv.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

        chattingRoomRVAdapter.setOnUserProfileClickListener(object : ChattingRoomRVAdapter.OnUserProfileClickListener{
            override fun onUserProfileClicked() {
                //사용자 프로필
                Log.d("bottom", "실행됨.")
                val bottomSheetDialogFragment = ChattingUserBottomFragment()
                bottomSheetDialogFragment.setStyle(DialogFragment.STYLE_NORMAL, R.style.AppBottomSheetDialogTheme)
                bottomSheetDialogFragment.show(supportFragmentManager, "bottomSheet")
            }

        })
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
        chattingService.setSendChattingView(this)
        chattingService.setChattingOrderCompleteView(this) //방장용 주문완료 setview
        chattingService.setChattingRemittanceCompleteView(this) //멤버용 송금완료 setView
    }

    private fun optionClickListener() {
        binding.chattingRoomOptionBtn.setOnClickListener {
            if (leader) {
                val optionDialog = LeaderOptionDialog()
                optionDialog.setLeaderOptionView(this)
                val bundle = Bundle()
//                bundle.putString("roomUuid", roomUuid)
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

        binding.chattingRoomTopLayoutOrderCompleteBtn.setOnClickListener { // 주문완료 버튼
            Log.d("orderComplete-request",ChattingOrderCompleteRequest(roomId).toString()  )
            chattingService.chattingOrderComplete(ChattingOrderCompleteRequest(roomId))
        }

        binding.chattingRoomTopLayoutRemittanceCompleteBtn.setOnClickListener { //송금완료 버튼
            Log.d("remittanceComplete-request",ChattingRemittanceCompleteRequest(roomId).toString()  )
            chattingService.chattingRemittanceComplete(ChattingRemittanceCompleteRequest(roomId))
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
            thread {
                kotlin.run {
                    WebSocketManager.connect()
                }
            }

            // TODO: 메시지 전송
            var content = binding.chattingRoomChattingTextEt.text.toString()
            var chatRoomId = roomId
            var isSystemMessage = false
            var memberId = getMemberId()
            var profileImgUrl = getProfileImgUrl()
            var chatType = "publish"
            var chatId = "none"
            var jwt = getJwt()

            // 전송할 데이터를 JSON Type 변수에 저장
            val jsonObject = JSONObject()
            jsonObject.put("content", content)
            jsonObject.put("chatRoomId", chatRoomId)
            jsonObject.put("isSystemMessage", isSystemMessage)
            jsonObject.put("memberId", memberId)
            jsonObject.put("profileImgUrl", profileImgUrl)
            jsonObject.put("chatType", chatType)
            jsonObject.put("chatId", chatId)
            jsonObject.put("jwt", jwt)

            val chatData = JsonParser.parseString(jsonObject.toString()) as JsonObject

            Log.d("WebSocketListener-Test", chatData.toString())
            WebSocketManager.sendMessage(chatData.toString())

            binding.chattingRoomChattingTextEt.setText("")

            if ( WebSocketManager .sendMessage( " Client send " )) {
                Log.d("WebSocketListener-Test", " Send from the client \n " )
            }

            var sendChatData = SendChattingRequest(chatId, chatRoomId, chatType, content, isSystemMessage, jwt, memberId, profileImgUrl)

            chattingService.sendChatting(sendChatData)
        }
    }

    private fun getCurrentDateTime(): String {
        val formatter = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        return formatter.format(Calendar.getInstance().time)
    }

    // 일반 유저 나가기
    override fun MemberExistClick() {
//        val chattingPartyMemberLeaveRequest = ChattingPartyMemberLeaveRequest(roomUuid)
//        chattingService.getChattingPartyMemberLeave(chattingPartyMemberLeaveRequest)
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

    // WebSocketListenerInterface 관련
    override fun onConnectSuccess() {
        Log.d("WebSocketListener-Test", "Connect Success")
    }

    override fun onConnectFailed() {
        Log.d("WebSocketListener-Test", "Connect Failed")
    }

    override fun onClose() {
        Log.d("WebSocketListener-Test", "Connect Closed")
    }

    override fun onMessage(text: String?) {
        Log.d("WebSocketListener-Test", "Message = $text")
    }

    override fun sendChattingSuccess(result: String) {
        Log.d("SEND-CHATTING", "Send Chatting Success")
        Toast.makeText(this, "채팅 전송 성공", Toast.LENGTH_LONG)
    }

    override fun sendChattingFailure(code: Int, message: String) {
        Log.d("SEND-CHATTING", "Send Chatting Failure")
        Toast.makeText(this, "채팅 전송 실패", Toast.LENGTH_LONG)
    }

    //주문 완료 성공/실패
    override fun chattingOrderCompleteSuccess(result: String) {
        binding.chattingRoomTopLayout.visibility = View.GONE
        topLayoutFlag = false
        CustomToastMsg.createToast(this, "주문이 완료되었습니다", "#8029ABE2", 53)?.show()
        Log.d("orderComplete",result)
    }

    override fun chattingOrderCompleteFailure(code: Int, message: String) {
        Log.d("orderComplete","실패 : "+message)
    }

    //채팅 송금완료 성공/실패
    override fun chattingRemittanceCompleteSuccess(result: String) {
        binding.chattingRoomTopLayout.visibility = View.GONE
        topLayoutFlag = false
        CustomToastMsg.createToast(this, "송금이 완료되었습니다", "#8029ABE2", 53)?.show()
        Log.d("remittanceComplete",result)
    }

    override fun chattingRemittanceCompleteFailure(code: Int, message: String) {
        Log.d("remittanceComplete","실패 : "+message)
    }
}