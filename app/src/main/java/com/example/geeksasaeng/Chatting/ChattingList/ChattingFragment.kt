package com.example.geeksasaeng.Chatting.ChattingList

import android.content.Intent
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.geeksasaeng.Chatting.ChattingRoom.ChattingRoomActivity
import com.example.geeksasaeng.Utils.BaseFragment
import com.example.geeksasaeng.Utils.getNickname
import com.example.geeksasaeng.databinding.FragmentChattingBinding
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

class ChattingFragment: BaseFragment<FragmentChattingBinding>(FragmentChattingBinding::inflate) {

    lateinit var chattingListRVAdapter : ChattingListRVAdapter
    private var chattingList = ArrayList<ChattingListData>()
    private val db = Firebase.firestore //파이어스토어

    override fun initAfterBinding() {
        initChattingList()
    }

    private fun initChattingList() {

        chattingList.clear() // ChattingRoomActivity 들어갔다가 나오면 방 하나더 추가되는 문제 해결 위해 clear한 후 추가해주는 방식으로 바꿈
        chattingList.apply {

            // add(ChattingList("roomName", "roomImgUrl", "lastChat", "lastTime", "newMsg"))
            db.collection("Rooms")
                .whereEqualTo("roomInfo.category", "배달파티")
                .whereEqualTo("roomInfo.isFinish", false) //배달파티 카테고리고, 종료되지 않은 채팅방 데이터만 가져올 쿼리 생성
                .get()
                .addOnSuccessListener { documents ->
                    for (document in documents) {
                        val roomInfo = document.data.getValue("roomInfo") as HashMap<String, Any> //roomInfo 필드 값 정보들을 해시맵 형태로 얻어온다.
                        Log.d("firestore", "성공함 + ${document.id} => ${document.data}"+roomInfo.getValue("title").toString() )

                        val roomName = roomInfo.getValue("title").toString()
                        val roomUuid = document.id
                        val participantsArray = roomInfo.getValue("participants") as ArrayList<HashMap<String,Any>>
                        for (member in participantsArray){ //ParticipantInfo로 받고 싶었는데 HashMap을 ParticipantInfo로 cast를 못한뎅,,
                            if (member.getValue("participant") == getNickname()){ // 그 중에 '이 유저가 속하는' 채팅방 정보만 가져온다!
                                add(ChattingListData(roomName, roomUuid,"http://geeksasaeng.shop/s3/neo.jpg", "firebase채팅입니다.", "방금", "+10"))
                            }
                        }
                    }
                    initAdapter()
                }
                .addOnFailureListener { exception ->
                    Log.d("firestore", "Error getting documents: ", exception)
                }

            /*add(ChattingListData("채팅방0", "http://geeksasaeng.shop/s3/neo.jpg", "마지막 채팅입니다ㅏㅏㅏㅏㅏㅏㅏ", "방금", "+10"))
            add(ChattingListData("채팅방1", "http://geeksasaeng.shop/s3/neo.jpg", "가나다라마바사아자차카타파하", "방금", "+10"))
            add(ChattingListData("채팅방2", "http://geeksasaeng.shop/s3/neo.jpg", "가 나 다 라 마 바 사 아 자 차 카 타 파 하", "방금", "+10"))
            add(ChattingListData("채팅방3", "http://geeksasaeng.shop/s3/neo.jpg", "마지막채팅마지막채팅", "방금", "+10"))
            add(ChattingListData("채팅방4", "http://geeksasaeng.shop/s3/neo.jpg", "룰루랄라마지막채팅", "어제", "+10"))
            add(ChattingListData("채팅방5", "http://geeksasaeng.shop/s3/neo.jpg", "테스트 채팅 테스트", "어제", "+10"))
            add(ChattingListData("채팅방6", "http://geeksasaeng.shop/s3/neo.jpg", "마 지 막 채 팅 테 스 트", "1일 전", "+10"))
            add(ChattingListData("채팅방7", "http://geeksasaeng.shop/s3/neo.jpg", "L A S T C H A T T I N G", "3일 전", "+10"))
            add(ChattingListData("채팅방8", "http://geeksasaeng.shop/s3/neo.jpg", "채팅리스트", "4일 전", "+10"))
            add(ChattingListData("채팅방9", "http://geeksasaeng.shop/s3/neo.jpg", "채팅채팅채팅채팅", "4일 전", "+10"))
            add(ChattingListData("채팅방10", "http://geeksasaeng.shop/s3/neo.jpg", "마지막채팅마지막채팅마지막채팅", "5일 전", "+10"))*/
        }
    }

    private fun initAdapter() {
        chattingListRVAdapter = ChattingListRVAdapter(chattingList)
        Log.d("firestore", chattingList.toString())
        binding.chattingListRv.adapter = chattingListRVAdapter
        binding.chattingListRv.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)

        chattingListRVAdapter.setOnItemClickListener(object:
            ChattingListRVAdapter.OnItemClickListener {
            override fun onItemClick(chattingList: ChattingListData, position: Int) { //채팅방 입장할때
                val intent = Intent(activity, ChattingRoomActivity::class.java)
                updateEnterTime(chattingList.roomUuid)
                intent.putExtra("roomName", chattingList.roomName)
                intent.putExtra("roomUuid", chattingList.roomUuid)
                startActivity(intent)
            }
        })
    }

    fun updateEnterTime(roomUuid: String){ //TODO: 여기서 닉네임이 있으면 걔를 업데이트 해줘야하는데,,, 그 새로 만드는게 아니라 그게 안되고 있습니당..
        db.collection("Rooms")
            .document(roomUuid)
            .update("roomInfo.participants", FieldValue.arrayUnion(ParticipantsInfo(getCurrentDateTime(), getNickname().toString()))) //현재시간, 닉네임
            .addOnSuccessListener { Log.d("firebase", "DocumentSnapshot successfully written!") }
            .addOnFailureListener { e -> Log.w("firebase", "Error update document", e) }
    }

    fun getCurrentDateTime(): String{
        val formatter = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        return formatter.format(Calendar.getInstance().time)
    }
}