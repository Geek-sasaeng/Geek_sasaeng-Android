package com.example.geeksasaeng.Chatting.ChattingRoom


import android.content.Intent
import android.net.Uri
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
import androidx.recyclerview.widget.LinearSmoothScroller
import androidx.recyclerview.widget.RecyclerView
import com.example.geeksasaeng.BuildConfig
import com.example.geeksasaeng.ChatSetting.*
import com.example.geeksasaeng.ChatSetting.ChatRoomDB.ChatDatabase
import com.example.geeksasaeng.Chatting.ChattingList.*
import com.example.geeksasaeng.Chatting.ChattingRoom.Retrofit.*
import com.example.geeksasaeng.R
import com.example.geeksasaeng.Utils.*
import com.example.geeksasaeng.databinding.ActivityChattingRoomBinding
import com.google.firebase.firestore.ListenerRegistration
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import com.rabbitmq.client.Connection
import com.rabbitmq.client.ConnectionFactory
import com.rabbitmq.client.DeliverCallback
import com.rabbitmq.client.Delivery
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.*
import kotlin.concurrent.thread
import kotlin.properties.Delegates


class ChattingRoomActivity :
    BaseActivity<ActivityChattingRoomBinding>(ActivityChattingRoomBinding::inflate),
    WebSocketListenerInterface, SendChattingView, ChattingOrderCompleteView, ChattingRemittanceCompleteView,
    ChattingDetailView, DialogMatchingEnd.MatchingEndClickListener {

    private val TAG = "CHATTING-ROOM-ACTIVITY"

    // 채팅 리스트에서 받아오는 값들
    private lateinit var roomName: String
    private lateinit var roomId: String

    //상세보기 api로 부터 받아올 정보들
    private lateinit var accountNumber: String
    private lateinit var bank: String
    private var chiefId by Delegates.notNull<Int>()
    private lateinit var enterTime: String
    private var isChief: Boolean = false
    private var isMatchingFinish: Boolean = false
    private var isOrderFinish: Boolean = false
    private var isRemittanceFinish: Boolean = false
    private var partyId by Delegates.notNull<Int>()

    private lateinit var participants: ArrayList<Any>
    private lateinit var chattingRoomRVAdapter: ChattingRoomRVAdapter
    private lateinit var chattingService: ChattingService
    private lateinit var realTimeChatListener: ListenerRegistration
    private lateinit var changeParticipantsListener: ListenerRegistration

    // topLayoutFlag (모든 파티원 X = False / 모든 파티원 O = True)
    private var topLayoutFlag = false

    // Chatting
    // WebSocket
    private var chatClient = OkHttpClient()
    // RabbitMQ
    private val rabbitMQUri = "amqp://" + BuildConfig.RABBITMQ_ID + ":" + BuildConfig.RABBITMQ_PWD + "@" + BuildConfig.RABBITMQ_ADDRESS
    private val factory = ConnectionFactory()
    // QUEUE_NAME = MemberID!
    var QUEUE_NAME = getMemberId().toString()
    private val chattingList = arrayListOf<Chat>()
    // RoomDB
    private lateinit var chatDB: ChatDatabase

    // Album
    private lateinit var resultLauncher: ActivityResultLauncher<Intent>
    // 이미지의 uri를 담을 ArrayList 객체
    var albumImageList: ArrayList<Uri> = ArrayList()

    override fun initAfterBinding() {
        chatDB = ChatDatabase.getDBInstance(applicationContext)!!
        roomName = intent.getStringExtra("roomName").toString()
        roomId = intent.getStringExtra("roomId").toString()
        binding.chattingRoomTitleTv.text = roomName

        initChattingService()
        chattingService.getChattingDetail(roomId) //채팅방 상세 조회 api 호출!!
        initClickListener()
        initTextChangedListener()
        initSendChatListener()
        initAdapter()
        optionClickListener()

        // Main Thread에서 Network 관련 작업을 하려고 하면 NetworkOnMainThreadException 발생!!
        // So, 새로운 Thread를 만들어 그 안에서 작동되도록!!!!
        Thread {
            initRabbitMQSetting()
        }.start()

        // 이전 채팅을 가져오기 위한 초기 작업
        // DB 비동기 처리를 위해 코루틴 사용!
        CoroutineScope(Dispatchers.IO).launch {
            var allChatting = chatDB.chatDao().getAllChats()
            var chattingSize = allChatting.size

            for (i in 0 until chattingSize) {
                // roomId가 같은 경우에만 가져오도록 설정!
                if (roomId == allChatting[i].chatRoomId)
                    chattingList.add(allChatting[i])
            }

            // 채팅 제일 밑으로 가는 방법!
            val smoothScroller: RecyclerView.SmoothScroller by lazy {
                object : LinearSmoothScroller(this@ChattingRoomActivity) {
                    override fun getVerticalSnapPreference() = SNAP_TO_START
                }
            }

            smoothScroller.targetPosition = chattingSize
            binding.chattingRoomChattingRv.layoutManager?.startSmoothScroll(smoothScroller)
        }
    }

    private fun initRabbitMQSetting() {
        factory.setUri(rabbitMQUri)
        // RabbitMQ 연결
        val conn: Connection = factory.newConnection()
        // Send and Receive 가능하도록 해주는 부분!
        val channel = conn.createChannel()
        // durable = true로 설정!!
        // 참고 : https://teragoon.wordpress.com/2012/01/26/message-durability%EB%A9%94%EC%8B%9C%EC%A7%80-%EC%9E%83%EC%96%B4%EB%B2%84%EB%A6%AC%EC%A7%80-%EC%95%8A%EA%B8%B0-durabletrue-propspersistent_text_plain-2/
        channel.queueDeclare(QUEUE_NAME, true, false, false, null)
        Log.d("CHATTING-SYSTEM-TEST", "Waiting for messages")

        lateinit var originalMessage: String
        lateinit var chatResponseMessage: Chat

        val deliverCallback = DeliverCallback { consumerTag: String?, delivery: Delivery ->
            originalMessage = String(delivery.body, Charsets.UTF_8)
            chatResponseMessage = getJSONtoChatting(originalMessage)
            chatDB.chatDao().insert(chatResponseMessage)

            chattingList.add(chatResponseMessage)
        }

        channel.basicConsume(QUEUE_NAME, true, deliverCallback) { consumerTag: String? -> }
    }


    private fun getJSONtoChatting(message: String): Chat {
        var chatting = JSONObject(message)
        var chatId = chatting.getString("chatId")
        var content = chatting.getString("content")
        var chatRoomId = chatting.getString("chatRoomId")
        var isSystemMessage = chatting.getBoolean("isSystemMessage")
        var memberId = chatting.getInt("memberId")
        var nickName = chatting.getString("nickName")
        var profileImgUrl = chatting.getString("profileImgUrl")

        // JsonArray를 ArrayList로 바꾸기 위한 과정!
        // RoomDB를 위해 Int Type을 String Type으로 변경해줌!!
        var readMembers = ArrayList<String>()
        var readMembersTemp = chatting.getJSONArray("readMembers")
        if (readMembersTemp != null) {
            for (i in 0 until readMembersTemp.length()) {
                readMembers.add(readMembersTemp.getInt(i).toString())
            }
        }

        var createdAt = chatting.getString("createdAt")
        var chatType = chatting.getString("chatType")
        var unreadMemberCnt = chatting.getInt("unreadMemberCnt")
        var isImageMessage = chatting.getBoolean("isImageMessage")

        // ViewType 설정 부분
        var viewType: Int = if (isSystemMessage.toString() == "true") systemChatting
        else if (isImageMessage.toString() == "true") imageChatting
        else if (memberId.toString() == QUEUE_NAME) myChatting
        else yourChatting

        var isLeader: Boolean = chiefId == memberId

        return Chat(chatId, content, chatRoomId, isSystemMessage, memberId, nickName, profileImgUrl, readMembers, createdAt, chatType, unreadMemberCnt, isImageMessage, viewType, isLeader)
    }

    override fun onResume() {
        super.onResume()
        webSocketStart()
    }

    private fun webSocketStart() {
        Log.d("CHATTING-SYSTEM-TEST", "webSocketStart")
        WebSocketManager.init("ws://geeksasaeng.shop:8080/chatting", this)
    }

    // 종료 시 리스너 제거
    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
//        realTimeChatListener.remove()
//        changeParticipantsListener.remove()
        Log.d("CHATTING-SYSTEM-TEST", "END")
        WebSocketManager.close()
    }

    private fun initAdapter() {
        chattingRoomRVAdapter = ChattingRoomRVAdapter(chattingList)
        binding.chattingRoomChattingRv.adapter = chattingRoomRVAdapter
        binding.chattingRoomChattingRv.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

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

        binding.chattingRoomAlbumIv.setOnClickListener(View.OnClickListener {
//            val intent = Intent(Intent.ACTION_PICK)
//            intent.type = MediaStore.Images.Media.CONTENT_TYPE
//            intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
//            intent.data = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
            
            // startActivityForResult -> registerForActivityResult로 변경하기
            // startActivityForResult(intent, 2222)
//            resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
//                if (result.resultCode == Activity.RESULT_OK) {
//                    getImageFromAlbum(result.data)
//                }
//            }

            // resultLauncher.launch(intent)
            // var launcher = registerForActivityResult<String, Uri>(ActivityResultContracts.GetContent()) { uri -> setImage(uri) }
            // launcher.launch("image/*")
        })
    }

    private fun getImageFromAlbum(data: Intent?) {
        // 어떤 이미지도 선택하지 않은 경우
        if (data == null) {
            Toast.makeText(getApplicationContext(), "이미지를 선택하지 않았습니다.", Toast.LENGTH_LONG).show();
        }
        // 이미지를 하나라도 선택한 경우
        else {
            var clipData = data.clipData

            if (clipData == null) {
                var imageUri = data.data
                if (imageUri != null) {
                    albumImageList.add(imageUri)
                }
            } else {
                for (i in 0 until clipData.itemCount) {
                    var imageUri = clipData.getItemAt(i).uri
                    try {
                        albumImageList.add(imageUri)
                    } catch (e: Exception) {
                        Log.e("ALBUM-IMAGE-ACCESS", "Files select error", e)
                    }
                }
            }
            Log.d("ALBUM-IMAGE-ACCESS", "Image = ${clipData!!.itemCount}")
            Log.d("ALBUM-IMAGE-ACCESS", "ImageList = $albumImageList")
        }
    }

    private fun initChattingService() {
        chattingService = ChattingService()
        chattingService.setSendChattingView(this)
        chattingService.setChattingOrderCompleteView(this) //방장용 주문완료 setview
        chattingService.setChattingRemittanceCompleteView(this) //멤버용 송금완료 setView
        chattingService.setChattingDetailView(this) //채팅방 상세 조회용
    }

    private fun optionClickListener() {
        binding.chattingRoomOptionBtn.setOnClickListener {
            if (isChief) {
                val optionDialog = LeaderOptionDialog()
                val bundle = Bundle()
                bundle.putInt("partyId", partyId)
                bundle.putString("roomId", roomId)
                bundle.putBoolean("isMatchingFinish", isMatchingFinish)
                optionDialog.arguments = bundle
                optionDialog.show(supportFragmentManager, "chattingLeaderOptionDialog")
            } else {
                val optionDialog = MemberOptionDialog()
                val bundle = Bundle()
                bundle.putString("roomId", roomId)
                optionDialog.arguments = bundle
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

            Log.d("CHATTING-SYSTEM-TEST", chatData.toString())
            WebSocketManager.sendMessage(chatData.toString())

            binding.chattingRoomChattingTextEt.setText("")

            if ( WebSocketManager .sendMessage( " Client send " )) {
                Log.d("CHATTING-SYSTEM-TEST", " Send from the client \n " )
            }

            var sendChatData = SendChattingRequest(chatId, chatRoomId, chatType, content, isSystemMessage, jwt, memberId, profileImgUrl)

            chattingService.sendChatting(sendChatData)
        }
    }

    private fun getCurrentDateTime(): String {
        val formatter = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        return formatter.format(Calendar.getInstance().time)
    }

    private fun calculateToday(): String {
        val nowTime = System.currentTimeMillis();
        val date = Date(nowTime)
        var dateFormat: SimpleDateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        return dateFormat.format(date)
    }

    // WebSocketListenerInterface 관련
    override fun onConnectSuccess() {
        Log.d("CHATTING-SYSTEM-TEST", "Connect Success")
    }

    override fun onConnectFailed() {
        Log.d("CHATTING-SYSTEM-TEST", "Connect Failed")
    }

    override fun onClose() {
        Log.d("CHATTING-SYSTEM-TEST", "Connect Closed")
    }

    override fun onMessage(text: String?) {
        Log.d("CHATTING-SYSTEM-TEST", "Message = $text")
    }

    override fun sendChattingSuccess(result: String) {
        Log.d("CHATTING-SYSTEM-TEST", "Send Chatting Success")
    }

    override fun sendChattingFailure(code: Int, message: String) {
        Log.d("CHATTING-SYSTEM-TEST", "Send Chatting Failure")
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

    //채팅방 상세조회
    override fun getChattingDetailSuccess(result: ChattingDetailResult) {
        Log.d("chatDetail", "채팅방 디테일 불러오기 성공 :${result.toString()}")
        accountNumber = result.accountNumber
        bank = result.bank
        chiefId = result.chiefId
        enterTime = result.enterTime
        isChief = result.isChief
        isMatchingFinish = result.isMatchingFinish
        isOrderFinish = result.isOrderFinish
        isRemittanceFinish = result.isRemittanceFinish
        partyId = result.partyId

        initTopLayout() // 받아온 정보로 상단바 설정
    }

    private fun initTopLayout(){ //채팅방 상단 바 설정

        if(isOrderFinish||isRemittanceFinish){ //주문 또는 송금 완료했으면 안보이게 해주기
            binding.chattingRoomTopLayout.visibility = View.INVISIBLE
        }else{
            if(isChief){ //리더면 상단 바 구성을 다르게 해줘야하므로
                binding.chattingRoomTopLayoutOrderCompleteBtn.visibility = View.VISIBLE //주문완료 버튼 보이게 하기
                binding.chattingRoomSectionIv.visibility = View.INVISIBLE // 계좌 아이콘
                binding.chattingRoomTopLayoutAccountNumberTv.visibility = View.INVISIBLE //계좌번호 textView
                binding.chattingRoomTopLayoutRemittanceCompleteBtn.visibility = View.INVISIBLE // 송금완료 버튼
            }else{
                binding.chattingRoomTopLayoutAccountNumberTv.text = bank + "  " + accountNumber
            }
        }
    }

    override fun getChattingDetailFailure(code: Int, msg: String) {
        Log.d("chatDetail", "채팅방 디테일 불러오기 실패 :$msg")
        //finish()
    }

    override fun onMatchingEndClicked() {
        isMatchingFinish = true
    }
}