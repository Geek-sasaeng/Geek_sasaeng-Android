package com.example.geeksasaeng.Chatting.ChattingList

import android.content.Intent
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.geeksasaeng.Chatting.ChattingRoom.ChattingRoomActivity
import com.example.geeksasaeng.Utils.BaseFragment
import com.example.geeksasaeng.Utils.getNickname
import com.example.geeksasaeng.databinding.FragmentChattingBinding
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class ChattingFragment: BaseFragment<FragmentChattingBinding>(FragmentChattingBinding::inflate) {

    lateinit var chattingListRVAdapter : ChattingListRVAdapter
    private var chattingList = ArrayList<ChattingListData>()
    private val db = Firebase.firestore //파이어스토어

    override fun initAfterBinding() {
        initChattingList()
    }

    private fun initChattingList() {
        // 채팅방 DummyData


        chattingList.apply {

            // add(ChattingList("roomName", "roomImgUrl", "lastChat", "lastTime", "newMsg"))
            db.collection("Rooms")
                .whereArrayContains("roomInfo.participants", getNickname().toString()) //사용자 닉네임을 이용해서 사용자가 참여중인 채팅방 찾아올 수 있다.
                .whereEqualTo("roomInfo.isFinish", false) //유저가 참여하고 있고, 종료되지 않은 채팅방 데이터만 가져올 쿼리 생성
                .get()
                .addOnSuccessListener { documents ->
                    for (document in documents) {
                        val roomInfo = document.data.getValue("roomInfo") as HashMap<String, Any>
                        Log.d("firestore", "성공함 + ${document.id} => ${document.data}"+roomInfo.getValue("title").toString() )
                        val roomName = roomInfo.getValue("title").toString()
                        add(ChattingListData(roomName, "http://geeksasaeng.shop/s3/neo.jpg", "firebase채팅입니다.", "방금", "+10"))
                    }
                    initAdapter()
                }
                .addOnFailureListener { exception ->
                    Log.w("firestore", "Error getting documents: ", exception)
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
            override fun onItemClick(chattingList: ChattingListData, position: Int) {
                val intent = Intent(activity, ChattingRoomActivity::class.java)
                intent.putExtra("roomName", chattingList.roomName)
                startActivity(intent)
            }
        })
    }
}