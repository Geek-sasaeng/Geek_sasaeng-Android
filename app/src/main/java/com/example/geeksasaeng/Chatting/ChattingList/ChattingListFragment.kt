package com.example.geeksasaeng.Chatting.ChattingList

import android.content.Intent
import android.os.Message
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.airbnb.lottie.LottieAnimationView
import com.example.geeksasaeng.BuildConfig
import com.example.geeksasaeng.ChatSetting.*
import com.example.geeksasaeng.ChatSetting.ChatRoomDB.ChatDatabase
import com.example.geeksasaeng.Chatting.ChattingList.Retrofit.*
import com.example.geeksasaeng.Chatting.ChattingRoom.ChattingRoomActivity
import com.example.geeksasaeng.Chatting.ChattingStorage.ChattingStorageFragment
import com.example.geeksasaeng.MainActivity
import com.example.geeksasaeng.R
import com.example.geeksasaeng.Utils.BaseFragment
import com.example.geeksasaeng.Utils.getMemberId
import com.example.geeksasaeng.databinding.FragmentChattingBinding
import com.rabbitmq.client.Connection
import com.rabbitmq.client.ConnectionFactory
import com.rabbitmq.client.DeliverCallback
import com.rabbitmq.client.Delivery
import kotlinx.coroutines.*
import org.json.JSONObject


class ChattingListFragment : BaseFragment<FragmentChattingBinding>(FragmentChattingBinding::inflate),
    ChattingListView, ChattingDetailView {
    lateinit var loadingAnimationView: LottieAnimationView
    private lateinit var chattingListRVAdapter: ChattingListRVAdapter
    lateinit var chattingListService: ChattingListService
    var cursor: Int = 0
    private var chattingRoomList = ArrayList<ChattingList?>()
    private var checkBinding: Boolean = false

    // 채팅방 정보를 넘기기 위해 필요한 부분
    private lateinit var roomTitle: String
    private lateinit var roomId: String

    // RabbitMQ
    private val rabbitMQUri = "amqp://" + BuildConfig.RABBITMQ_ID + ":" + BuildConfig.RABBITMQ_PWD + "@" + BuildConfig.RABBITMQ_ADDRESS
    private val factory = ConnectionFactory()
    // QUEUE_NAME = MemberID!
    var QUEUE_NAME = getMemberId().toString()
    private val chattingList = arrayListOf<Chat>()

    // RoomDB
    private lateinit var chatDB: ChatDatabase

    override fun onResume() {
        super.onResume()
        initChattingList()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        checkBinding = false
    }

    override fun initAfterBinding() {
        chatDB = ChatDatabase.getDBInstance(context as MainActivity)!!

        checkBinding = true
        loadingStart()
        initAdapter()
        initClickListener()
        initScrollListener()

        // Main Thread에서 Network 관련 작업을 하려고 하면 NetworkOnMainThreadException 발생!!
        // So, 새로운 Thread를 만들어 그 안에서 작동되도록!!!!
        Thread {
            initRabbitMQSetting()
        }.start()
    }

    private fun initChattingList() {
        chattingRoomList.clear()
        chattingListService = ChattingListService()
        chattingListService.setChattingListView(this)
        getChattingList()
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
        var readMembers = java.util.ArrayList<String>()
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

        // var isLeader: Boolean = chiefId == memberId
        // TODO: 리더인지 아닌지 데이터 넘기기
        var isLeader = true

        return Chat(chatId, content, chatRoomId, isSystemMessage, memberId, nickName, profileImgUrl, readMembers, createdAt, chatType, unreadMemberCnt, isImageMessage, viewType, isLeader)
    }

    private fun getChattingDetail(roomId: String) {
        chattingListService.setChattingDetailView(this)
        chattingListService.getChattingDetail(roomId)
    }

    private fun initAdapter() {
        if (checkBinding) {
            chattingListRVAdapter = ChattingListRVAdapter(chattingRoomList)
            binding.chattingListRv.adapter = chattingListRVAdapter
            binding.chattingListRv.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)

//        chattingListRVAdapter.setChattingData(position, chattingList)

            chattingListRVAdapter.setOnItemClickListener(object :
                ChattingListRVAdapter.OnItemClickListener {
                override fun onItemClick(chatting: ChattingList, position: Int) {
                    // 채팅방 입장할때
                    var chattingRoomData = chattingListRVAdapter.getRoomData(position)
                    roomTitle = chattingRoomData.roomTitle
                    roomId = chattingRoomData.roomId

                    getChattingDetail(roomId)
                }
            })
        }
    }

    private fun getChattingList() {
        chattingListService.getChattingList(cursor)
    }

//    override fun onSuccessGetChatData(position: Int, chattingList: ChattingList) {
//        chattingListRVAdapter.setChattingData(position, chattingList)
//    }

    private fun initClickListener() {
        //필터버튼
        binding.chattingSectionRb1.setOnClickListener {
            binding.chattingListPreparingIv.visibility = View.GONE
            binding.chattingListRv.visibility = View.VISIBLE
            loadingStart()
            initChattingList()
        }
        binding.chattingSectionRb2.setOnClickListener {
            binding.chattingListRv.visibility = View.GONE
            binding.chattingListPreparingIv.visibility = View.VISIBLE
        }
        binding.chattingSectionRb3.setOnClickListener {
            binding.chattingListRv.visibility = View.GONE
            binding.chattingListPreparingIv.visibility = View.VISIBLE
        }

        //보관함 버튼
       binding.chattingStorageIv.setOnClickListener {
           (context as MainActivity).supportFragmentManager.beginTransaction().replace(R.id.main_frm, ChattingStorageFragment()).addToBackStack("chattingStorage").commit()
        }
    }

    private fun initScrollListener() {
        binding.chattingListRv.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val lastVisibleItemPosition = (recyclerView.layoutManager as LinearLayoutManager?)!!.findLastCompletelyVisibleItemPosition() // 마지막 스크롤된 항목 위치
                val itemTotalCount = recyclerView.adapter!!.itemCount - 1 // 항목 전체 개수
                if (lastVisibleItemPosition >= itemTotalCount - 1) { //마지막 아이템 전 아이템이 되면
                    binding.chattingListBottomView.visibility = View.INVISIBLE
                } else binding.chattingListBottomView.visibility = View.VISIBLE
            }
        })
    }

    private fun loadingStart() {
        loadingAnimationView = binding.animationView
        binding.animationViewLayout.visibility = View.VISIBLE
        loadingAnimationView.visibility = View.VISIBLE
        loadingAnimationView.playAnimation()
    }

    private fun loadingStop() {
        if (checkBinding) {
            loadingAnimationView.cancelAnimation()
            binding.animationViewLayout.visibility = View.GONE
            loadingAnimationView.visibility = View.GONE
        }
    }

    private fun getChattingRoomInfo(roomId: String) {
        // roomId를 이용해 마지막 채팅 연결

    }

    override fun getChattingListSuccess(result: ChattingListResult) {
        CoroutineScope(Dispatchers.Main).launch {
            // 여러 개의 작업 (data access 작업 및 ui 작업)을 하나의 coroutine 안에서 작업하기 위해 withContext 사용!
            // http://www.gisdeveloper.co.kr/?p=10279#:~:text=withContext%EB%A5%BC%20%EC%8D%A8%EC%84%9C%20%EC%83%88%EB%A1%9C%EC%9A%B4%20%EC%BD%94%EB%A3%A8%ED%8B%B4%EC%9D%84%20%EB%8B%A4%EB%A5%B8%20%EC%8A%A4%EB%A0%88%EB%93%9C%EC%97%90%EC%84%9C%20%EB%8F%99%EA%B8%B0%EC%A0%81%EC%9C%BC%EB%A1%9C%20%EC%8B%A4%ED%96%89%ED%95%98%EB%8F%84%EB%A1%9D%20%ED%95%98%EB%8A%94%20%EC%BD%94%EB%93%9C%EC%9E%85%EB%8B%88%EB%8B%A4.%20%EA%B2%B0%EA%B3%BC%EB%8A%94%20%EB%8B%A4%EC%9D%8C%EA%B3%BC%20%EA%B0%99%EC%8A%B5%EB%8B%88%EB%8B%A4.
            withContext(Dispatchers.IO) {
                for (i in 0 until result.parties.size) {
                    var lastChatting = ""
                    var lastChattingTime = ""
                    var newChattingNumber = 0

                    var chat = result.parties[i]?.let { chatDB.chatDao().getRoomChats(it.roomId) }
                    Log.d("CHATTING-SYSTEM-TEST", "chat = $chat")

                    if (chat != null) {
                        if (chat.size > 1) {
                            lastChatting = chat[chat.size - 1].content.toString()
                            lastChattingTime = chat[chat.size - 1].createdAt

                            // TODO: 임시로 넣은 부분
                            newChattingNumber = 0
                        }
                    }

                    var partyData = result.parties[i]?.let { ChattingList(it.roomId, it.roomTitle, lastChatting, lastChattingTime, newChattingNumber) }
                    chattingRoomList.add(partyData)
                }
            }

            Log.d("CHATTING-SYSTEM-TEST", "out chattingRoomList = $chattingRoomList")
            chattingListRVAdapter.notifyDataSetChanged()
        }

        var finalPage = result.finalPage

        // 로딩화면 제거
        loadingStop()
    }

    override fun getChattingListFailure(code: Int, msg: String) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT)
        Log.d("GET-CHATTING-LIST", "code = $code, msg = $msg")
    }

    override fun getChattingDetailSuccess(result: ChattingDetailResult) {
        val intent = Intent(activity, ChattingRoomActivity::class.java)

        intent.putExtra("accountNumber", result.accountNumber)
        intent.putExtra("bank", result.bank)
        intent.putExtra("chiefId", result.chiefId)
        intent.putExtra("enterTime", result.enterTime)
        intent.putExtra("isChief", result.isChief)
        intent.putExtra("isOrderFinish", result.isOrderFinish)
        intent.putExtra("isRemittanceFinish", result.isRemittanceFinish)

        intent.putExtra("roomName", roomTitle)
        intent.putExtra("roomId", roomId)

        startActivity(intent)
    }

    override fun getChattingDetailFailure(code: Int, msg: String) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT)
        Log.d("GET-CHATTING-DETAIL-LIST", "code = $code, msg = $msg")
    }
}