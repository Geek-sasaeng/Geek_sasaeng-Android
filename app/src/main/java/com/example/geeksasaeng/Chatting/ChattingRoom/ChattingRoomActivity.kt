package com.example.geeksasaeng.Chatting.ChattingRoom

import android.content.Context
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
    ChattingMemberLeaveView, MemberOptionView, LeaderOptionView, ChattingLeaderLeaveView,
    WebSocketListenerInterface, SendChattingView, ChattingOrderCompleteView, ChattingRemittanceCompleteView {

    private val TAG = "CHATTING-ROOM-ACTIVITY"

    // 채팅 리스트에서 받아오는 값들
    private lateinit var roomName: String
    private lateinit var roomId: String
    private lateinit var accountNumber: String
    private lateinit var bank: String
    private var chiefId by Delegates.notNull<Int>()
    private lateinit var enterTime: String
    private var isChief: Boolean = false
    private var isOrderFinish: Boolean = false
    private var isRemittanceFinish: Boolean = false

    private var checkRemittance: Boolean = false
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
//    private val rabbitMQUri = "amqp://" + BuildConfig.RABBITMQ_ID + ":" + BuildConfig.RABBITMQ_PWD + "@" + BuildConfig.RABBITMQ_ADDRESS
//    private val factory = ConnectionFactory()
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

        accountNumber = intent.getStringExtra("accountNumber").toString()
        bank = intent.getStringExtra("bank").toString()
        chiefId = intent.getIntExtra("chiefId", 0)
        enterTime = intent.getStringExtra("enterTime").toString()
        isChief = intent.getBooleanExtra("isChief", false)
        isOrderFinish = intent.getBooleanExtra("isOrderFinish", false)
        isRemittanceFinish = intent.getBooleanExtra("isRemittanceFinish", false)

        Log.d("CHATTING-ROOM-TEST", "accountNumber = $accountNumber / bank = $bank / isChief = $isChief")

        roomName = intent.getStringExtra("roomName").toString()
        roomId = intent.getStringExtra("roomId").toString()

        binding.chattingRoomTitleTv.text = roomName

        initTopLayout()
        initClickListener()
        initTextChangedListener()
        initSendChatListener()
        initAdapter()
        initChattingService()
//        optionClickListener()

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

    private fun initTopLayout() {
        //TODO: 주문 또는 송금 완료했으면 안보이게 해주기
        //TODO: 그렇지 않았다면~~
        if (isChief) { //리더면 상단 바 구성을 다르게 해줘야하므로
            binding.chattingRoomTopLayoutOrderCompleteBtn.visibility = View.VISIBLE
            binding.chattingRoomSectionIv.visibility = View.GONE
            binding.chattingRoomTopLayoutStatusTv.visibility = View.GONE
            binding.chattingRoomTopLayoutRemittanceCompleteBtn.visibility = View.GONE
        } else {
            binding.chattingRoomTopLayoutOrderCompleteBtn.visibility = View.GONE
            binding.chattingRoomSectionIv.visibility = View.VISIBLE
            binding.chattingRoomTopLayoutStatusTv.visibility = View.VISIBLE
            binding.chattingRoomTopLayoutRemittanceCompleteBtn.visibility = View.VISIBLE
            binding.chattingRoomTopLayoutStatusTv.text = "$bank  $accountNumber"
        }
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
        chattingService.setChattingMemberLeaveView(this)
        chattingService.setSendChattingView(this)
        chattingService.setChattingOrderCompleteView(this) //방장용 주문완료 setview
        chattingService.setChattingRemittanceCompleteView(this) //멤버용 송금완료 setView
    }

    private fun optionClickListener() {
        binding.chattingRoomOptionBtn.setOnClickListener {
            if (isChief) {
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
            Log.d("CHATTING-SYSTEM-TEST", "1")
            thread {
                kotlin.run {
                    WebSocketManager.connect()
                    Log.d("CHATTING-SYSTEM-TEST", "2")

                    Log.d("CHATTING-SYSTEM-TEST", "3")
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
                    Log.d("CHATTING-SYSTEM-TEST", "4")

                    val chatData = JsonParser.parseString(jsonObject.toString()) as JsonObject
                    Log.d("CHATTING-SYSTEM-TEST", "5")

                    Log.d("CHATTING-SYSTEM-TEST", chatData.toString())
                    WebSocketManager.sendMessage(chatData.toString())
                    Log.d("CHATTING-SYSTEM-TEST", "6")

                    binding.chattingRoomChattingTextEt.setText("")

                    if ( WebSocketManager .sendMessage( " Client send " )) {
                        Log.d("CHATTING-SYSTEM-TEST", " Send from the client \n " )
                        Log.d("CHATTING-SYSTEM-TEST", "7")
                    }

                    var sendChatData = SendChattingRequest(chatId, chatRoomId, chatType, content, isSystemMessage, jwt, memberId, profileImgUrl)

                    chattingService.sendChatting(sendChatData)
                    Log.d("CHATTING-SYSTEM-TEST", "8")
                }
            }
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
}