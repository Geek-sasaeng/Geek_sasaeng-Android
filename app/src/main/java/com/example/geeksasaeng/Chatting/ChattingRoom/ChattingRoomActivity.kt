package com.example.geeksasaeng.Chatting.ChattingRoom


import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.provider.MediaStore
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSmoothScroller
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
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
import com.rabbitmq.client.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import java.io.File
import java.text.SimpleDateFormat
import java.util.*
import kotlin.properties.Delegates


class ChattingRoomActivity :
    BaseActivity<ActivityChattingRoomBinding>(ActivityChattingRoomBinding::inflate),
    WebSocketListenerInterface, SendChattingView, SendImageChattingView, ChattingOrderCompleteView, ChattingRemittanceCompleteView,
    ChattingDetailView, DialogMatchingEnd.MatchingEndClickListener, ChattingUserProfileView {

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
    private val chattingList = arrayListOf<Chat>()
    // RabbitMQ
    private val rabbitMQUri = "amqp://" + BuildConfig.RABBITMQ_ID + ":" + BuildConfig.RABBITMQ_PWD + "@" + BuildConfig.RABBITMQ_ADDRESS
    private val factory = ConnectionFactory()
    // QUEUE_NAME = MemberID!
    var QUEUE_NAME = getMemberId().toString()
    // RoomDB
    private lateinit var chatDB: ChatDatabase

    // Album
    private lateinit var resultLauncher: ActivityResultLauncher<Intent>
    // 이미지의 uri를 담을 ArrayList 객체
    var albumImageList: ArrayList<Uri> = ArrayList()

    private var imageUri: Uri? = null

    companion object{
        // 갤러리 권한 요청
        const val REQ_GALLERY = 1
    }

    private val imageResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == RESULT_OK) {
            imageUri = result.data?.data
            imageUri?.let{
                Log.d("API-TEST", "imageUri = $imageUri")
                CoroutineScope(Dispatchers.Main).launch {
                    withContext(Dispatchers.IO) {
                        WebSocketManager.connect()
                        var content = binding.chattingRoomChattingTextEt.text.toString()
                        var chatRoomId = roomId
                        var isSystemMessage = false
                        var email = getEmail()
                        var profileImgUrl = getProfileImgUrl()
                        var chatType = "publish"
                        var chatId = "none"
                        var images = arrayListOf(imageUri)
                        var isImageMessage = true

                        var file: File
                        var requestFile: RequestBody
                        var multipartFile = mutableListOf<MultipartBody.Part?>()

                        if (imageUri != null) {
                            file = File(ImageTranslator.optimizeBitmap(this@ChattingRoomActivity, imageUri!!)!!)
                            requestFile = file.asRequestBody("image/*".toMediaTypeOrNull())
                            multipartFile.add(MultipartBody.Part.createFormData("images", file.name, requestFile))
                        }

                        // chatId, chatRoomId, chatType, content, images(file), isImageMessage, isSystemMessage
                        val mapData = HashMap<String, RequestBody>()
                        mapData.put("chatId", RequestBody.create("text/plain".toMediaTypeOrNull(), chatId))
                        mapData.put("chatRoomId", RequestBody.create("text/plain".toMediaTypeOrNull(), chatRoomId))
                        mapData.put("chatType", RequestBody.create("text/plain".toMediaTypeOrNull(), chatType))
                        mapData.put("content", RequestBody.create("text/plain".toMediaTypeOrNull(), content))
                        mapData.put("isImageMessage", RequestBody.create("text/plain".toMediaTypeOrNull(), isImageMessage.toString()))
                        mapData.put("isSystemMessage", RequestBody.create("text/plain".toMediaTypeOrNull(), isSystemMessage.toString()))

                        // 전송할 데이터를 JSON Type 변수에 저장
                        val jsonObject = JSONObject()
                        jsonObject.put("content", content)
                        jsonObject.put("chatRoomId", chatRoomId)
                        jsonObject.put("isSystemMessage", isSystemMessage)
                        jsonObject.put("email", email)
                        jsonObject.put("profileImgUrl", profileImgUrl)
                        jsonObject.put("chatType", chatType)
                        jsonObject.put("chatId", chatId)
                        jsonObject.put("images", images)
                        jsonObject.put("isImageMessage", isImageMessage)

                        val chatData = JsonParser.parseString(jsonObject.toString()) as JsonObject
                        WebSocketManager.sendMessage(chatData.toString())

                        if ( WebSocketManager .sendMessage( " Client send " )) {
                            Log.d("CHATTING-SYSTEM-TEST", " Send from the client \n " )
                        }

                        chattingService.sendImgChatting(multipartFile, mapData)

                        // 보낸 채팅을 다시 받아오기 위함
                        Thread {
                            initRabbitMQSetting()
                        }.start()
                    }
                }
            }
        }
    }

    // 유저의 프로필 정보
    lateinit var member: String
    lateinit var grade: String
    lateinit var userNickname: String
    lateinit var userProfileImgUrl: String
    lateinit var userMemberId: String
    val bottomSheetDialogFragment = ChattingUserBottomFragment()

    override fun initAfterBinding() {
        chatDB = ChatDatabase.getDBInstance(applicationContext)!!

        accountNumber = intent.getStringExtra("accountNumber").toString()
        bank = intent.getStringExtra("bank").toString()
        chiefId = intent.getIntExtra("chiefId", 0)
        enterTime = intent.getStringExtra("enterTime").toString()
        isChief = intent.getBooleanExtra("isChief", false)
        isOrderFinish = intent.getBooleanExtra("isOrderFinish", false)
        isRemittanceFinish = intent.getBooleanExtra("isRemittanceFinish", false)
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

        // 이전 채팅을 가져오기 위한 초기 작업
        // DB 비동기 처리를 위해 코루틴 사용!
        CoroutineScope(Dispatchers.IO).launch {
            var allChatting = chatDB.chatDao().getAllChats()
            var chattingSize = allChatting.size

            for (i in 0 until chattingSize) {
                // roomId가 같은 경우에만 가져오도록 설정!
                if (roomId == allChatting[i].chatRoomId) {
                    withContext(Dispatchers.Main) {
                        // Main Thread 에서만 View 관련 작업 가능!
                        chattingRoomRVAdapter.addItem(allChatting[i])
                        Log.d("CHATTING-SYSTEM-TEST", "allChatting = ${allChatting[i]}")
                    }
                }
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

        Log.d("CHATTING-SYSTEM-TEST", "chattingList = ${chattingRoomRVAdapter.getAllItems()}")
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
            Log.d("CHATTING-SYSTEM-TEST", "originalMessage = $originalMessage")
            chatResponseMessage = getJSONtoChatting(originalMessage)
            chatDB.chatDao().insert(chatResponseMessage)
            var result = chatDB.chatDao().getAllChats()
            var resultTemp = result[result.size - 1]
            chattingRoomRVAdapter.addItem(chatResponseMessage)
            Log.d("CHATTING-SYSTEM-TEST", "chattingRoom chatResponseMessage = $chatResponseMessage")
        }
        Log.d("CHATTING-SYSTEM-TEST", "deliverCallback = ${deliverCallback}")

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
        WebSocketManager.close()
    }

    private fun initAdapter() {
        chattingRoomRVAdapter = ChattingRoomRVAdapter(chattingList)
        binding.chattingRoomChattingRv.adapter = chattingRoomRVAdapter
        binding.chattingRoomChattingRv.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

        chattingRoomRVAdapter.setOnUserProfileClickListener(object : ChattingRoomRVAdapter.OnUserProfileClickListener{
            override fun onUserProfileClicked(profileImgUrl: String?, memberId: String) {
                chattingService.getChattingUserProfile(roomId, memberId.toInt())
                userProfileImgUrl = profileImgUrl.toString()
                userMemberId = memberId
            }
        })
    }

    override fun getChattingUserProfileSuccess(result: ChattingUserProfileResult) {
        var isChief = !result.isChief
        member = if (isChief) "파티원" else "파티장"
        member = result.grade + " | " + member
        userNickname = result.userName

        var bundle = Bundle(2)
        bundle.putString("chatRoomId", roomId)
        bundle.putString("nickname", userNickname)
        bundle.putString("profileImgUrl", userProfileImgUrl)
        bundle.putString("memberId", userMemberId)
        bundle.putString("member", member)

        bottomSheetDialogFragment.arguments = bundle
        bottomSheetDialogFragment.setStyle(DialogFragment.STYLE_NORMAL, R.style.AppBottomSheetDialogTheme)
        bottomSheetDialogFragment.show(supportFragmentManager, "bottomSheet")
    }

    override fun getChattingUserProfileFailure(code: Int, msg: String) {
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show()
    }

    private fun initClickListener() {
        binding.chattingRoomBackBtn.setOnClickListener {
            finish()
        }

        binding.chattingRoomAlbumIv.setOnClickListener(View.OnClickListener {
            val writePermission = ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            )
            val readPermission = ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE
            )
            
            Log.d("API-TEST", "writePermission = $writePermission\nreadPermission = $readPermission")

            // 권한 확인
            if (writePermission == PackageManager.PERMISSION_DENIED || readPermission == PackageManager.PERMISSION_DENIED) {
                // 권한 요청
                Log.d("API-TEST", "권한 요청")
                ActivityCompat.requestPermissions(this, arrayOf(
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.READ_EXTERNAL_STORAGE
                ), REQ_GALLERY
                )
            } else {
                Log.d("API-TEST", "권한 있음")
                // 권한이 있는 경우 갤러리 실행
                val intent = Intent(Intent.ACTION_PICK)
                intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*")
                imageResult.launch(intent)
            }

            Log.d("API-TEST", "AlbumClick")

//            val intent = Intent(Intent.ACTION_PICK)
//            intent.type = MediaStore.Images.Media.CONTENT_TYPE
//            intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
//            intent.data = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
//
//             startActivityForResult -> registerForActivityResult로 변경하기
//             startActivityForResult(intent, 2222)
//            resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
//                if (result.resultCode == Activity.RESULT_OK) {
//                    getImageFromAlbum(result.data)
//                }
//            }
//
//             resultLauncher.launch(intent)
//             var launcher = registerForActivityResult<String, Uri>(ActivityResultContracts.GetContent()) { uri -> setImage(uri) }
//             launcher.launch("image/*")
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
        chattingService.setChattingUserProfileView(this)
        chattingService.setSendImageChattingView(this)
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
                bundle.putInt("partyId", partyId)
                bundle.putString("roomId", roomId)
                optionDialog.arguments = bundle
                optionDialog.show(supportFragmentManager, "chattingUserOptionDialog")
            }
        }

        binding.chattingRoomBackBtn.setOnClickListener {
            finish()
        }

        binding.chattingRoomTopLayoutOrderCompleteBtn.setOnClickListener { // 주문완료 버튼
            Log.d("orderComplete-request",ChattingOrderCompleteRequest(roomId).toString())
            chattingService.chattingOrderComplete(ChattingOrderCompleteRequest(roomId))
        }

        binding.chattingRoomTopLayoutRemittanceCompleteBtn.setOnClickListener { //송금완료 버튼
            Log.d("remittanceComplete",ChattingRemittanceCompleteRequest(roomId).toString()  )
            chattingService.chattingRemittanceComplete(ChattingRemittanceCompleteRequest(roomId))
        }
    }

    private fun initTextChangedListener() {
        binding.chattingRoomChattingTextEt.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun afterTextChanged(p0: Editable?) {
                if (binding.chattingRoomChattingTextEt.text.isNotEmpty()) {
                    binding.chattingRoomSendTv.setTextColor(ContextCompat.getColor(applicationContext, R.color.main))
                    binding.chattingRoomSendTv.isEnabled = true
                } else {
                    binding.chattingRoomSendTv.setTextColor(ContextCompat.getColor(applicationContext, R.color.gray_2))
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
            CoroutineScope(Dispatchers.Main).launch {
                withContext(Dispatchers.IO) {
                    WebSocketManager.connect()
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
                    WebSocketManager.sendMessage(chatData.toString())

                    binding.chattingRoomChattingTextEt.setText("")

                    if ( WebSocketManager .sendMessage( " Client send " )) {
                        Log.d("CHATTING-SYSTEM-TEST", " Send from the client \n " )
                    }

                    var sendChatData = SendChattingRequest(chatId, chatRoomId, chatType, content, isSystemMessage, jwt, memberId, profileImgUrl)
                    chattingService.sendChatting(sendChatData)

                    Log.d("CHATTING-SYSTEM-TEST", "sendChatData = $sendChatData")

                    // 보낸 채팅을 다시 받아오기 위함
                    Thread {
                        initRabbitMQSetting()
                    }.start()
                }
            }
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
        chattingRoomRVAdapter.notifyDataSetChanged()
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
        if(message=="마감된 채팅방을 찾을 수 없습니다."){
            CustomToastMsg.createToast(this, "아직 매칭이 마감되지 않았습니다", "#8029ABE2", 53)?.show()
        }
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

    override fun sendImageChattingSuccessView(result: SendChattingResponse) {
        Log.d("SEND-IMAGE-CHATTING", "성공\nresult = $result")
    }

    override fun sendImageChattingFailureView() {
        Log.d("SEND-IMAGE-CHATTING", "실패")
    }
}