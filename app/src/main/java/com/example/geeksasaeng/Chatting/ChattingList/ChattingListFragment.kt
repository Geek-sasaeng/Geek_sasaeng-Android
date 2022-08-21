package com.example.geeksasaeng.Chatting.ChattingList

import android.animation.Animator
import android.content.Intent
import android.util.Log
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.airbnb.lottie.LottieAnimationView
import com.example.geeksasaeng.Chatting.ChattingRoom.ChattingRoomActivity
import com.example.geeksasaeng.MainActivity
import com.example.geeksasaeng.Utils.BaseFragment
import com.example.geeksasaeng.Utils.getNickname
import com.example.geeksasaeng.databinding.FragmentChattingBinding
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.util.*

class ChattingListFragment: BaseFragment<FragmentChattingBinding>(FragmentChattingBinding::inflate), ChatDataView{

    lateinit var loadingAnimationView: LottieAnimationView
    lateinit var chattingListRVAdapter : ChattingListRVAdapter
    private var chattingList = ArrayList<ChattingData>()
    private val db = Firebase.firestore //파이어스토어

    override fun onResume() {
        super.onResume()
        initChattingList()
        initAdapter()
    }

    override fun onStart() {
        super.onStart()
        loadingStart()
    }

    override fun initAfterBinding() {
        initClickListener()
    }

    private fun initChattingList() {
        chattingList.clear()

        // 채팅 리스트 불러오기
        db.collection("Rooms")
            .whereEqualTo("roomInfo.category", "배달파티")
            .whereEqualTo("roomInfo.isFinish", false)
            .orderBy("roomInfo.updatedAt", Query.Direction.DESCENDING)
            .get()
            .addOnSuccessListener { documents ->
                var position = 0
                for (document in documents) {
                    val roomInfo =
                        document.data.getValue("roomInfo") as HashMap<String, Any>

                    val participants =
                        roomInfo.getValue("participants") as ArrayList<HashMap<String, Any>>

                    for (member in participants) {
                        if (member.getValue("participant") == getNickname()) {
                            val roomUuid = document.id
                            val roomName = roomInfo.getValue("title").toString()
                            val roomImgUrl = "http://geeksasaeng.shop/s3/neo.jpg"

                            val roomData = RoomData(roomName, roomUuid, roomImgUrl)
                            val chattingData = ChattingData(roomData, "", "", "")
                            chattingList.add(chattingData)

                            // 마지막 채팅 받아오기
                            val chatData = FirestoreChatData(roomUuid, position, chattingList)
                            chatData.setChatDataView(this)
                            chatData.getLastChatData()

                            position += 1
                        }
                    }
                }
                initAdapter()
                loadingStop()
            }
            .addOnFailureListener { exception ->
                Log.d("firestore", "Error getting documents: ", exception)
            }
    }

    private fun initAdapter() {
        chattingListRVAdapter = ChattingListRVAdapter(chattingList)
        binding.chattingListRv.adapter = chattingListRVAdapter
        binding.chattingListRv.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)

        chattingListRVAdapter.setOnItemClickListener(object:
            ChattingListRVAdapter.OnItemClickListener {
            override fun onItemClick(chatting: ChattingData, position: Int) { //채팅방 입장할때
                val intent = Intent(activity, ChattingRoomActivity::class.java)
                intent.putExtra("roomName", chatting.roomData.roomName)
                intent.putExtra("roomUuid", chatting.roomData.roomUuid)
                startActivity(intent)
            }
        })
    }

    override fun onSuccessGetChatData(position: Int, chattingData: ChattingData) {
        chattingListRVAdapter.setChattingData(position, chattingData)
    }

    private fun initClickListener() {
        binding.chattingSectionRb1.setOnClickListener {
            binding.chattingListPreparingIv.visibility = View.GONE
            binding.chattingListRv.visibility = View.VISIBLE
            initChattingList()
            loadingStart()
        }
        binding.chattingSectionRb2.setOnClickListener {
            binding.chattingListRv.visibility = View.GONE
            binding.chattingListPreparingIv.visibility = View.VISIBLE
        }
        binding.chattingSectionRb3.setOnClickListener {
            binding.chattingListRv.visibility = View.GONE
            binding.chattingListPreparingIv.visibility = View.VISIBLE
        }
    }

    private fun loadingStart() {
        loadingAnimationView = binding.animationView
        binding.animationViewLayout.visibility = View.VISIBLE
        loadingAnimationView.visibility = View.VISIBLE
        loadingAnimationView.playAnimation()
        loadingAnimationView.addAnimatorListener(object : Animator.AnimatorListener {
            override fun onAnimationStart(p0: Animator?) {
            }
            override fun onAnimationEnd(animation: Animator?) {
                // initAfterBinding()
            }
            override fun onAnimationCancel(p0: Animator?) {
            }
            override fun onAnimationRepeat(p0: Animator?) {
            }
        })
    }

    private fun loadingStop() {
        loadingAnimationView.cancelAnimation()
        binding.animationViewLayout.visibility = View.GONE
        loadingAnimationView.visibility = View.GONE
    }
}
