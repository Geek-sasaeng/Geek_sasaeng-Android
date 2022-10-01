package com.example.geeksasaeng.Chatting.ChattingList

import android.animation.Animator
import android.content.Intent
import android.util.Log
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.airbnb.lottie.LottieAnimationView
import com.example.geeksasaeng.Chatting.ChattingRoom.ChattingRoomActivity
import com.example.geeksasaeng.Chatting.ChattingStorage.ChattingStorageFragment
import com.example.geeksasaeng.Home.Delivery.DeliveryFragment
import com.example.geeksasaeng.MainActivity
import com.example.geeksasaeng.R
import com.example.geeksasaeng.Signup.Basic.SignUpActivity
import com.example.geeksasaeng.Signup.Basic.StepTwoFragment
import com.example.geeksasaeng.Utils.BaseFragment
import com.example.geeksasaeng.Utils.getNickname
import com.example.geeksasaeng.databinding.FragmentChattingBinding
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.util.*

class ChattingListFragment :
    BaseFragment<FragmentChattingBinding>(FragmentChattingBinding::inflate), ChatDataView {

    lateinit var loadingAnimationView: LottieAnimationView
    lateinit var chattingListRVAdapter: ChattingListRVAdapter
    private var chattingList = ArrayList<ChattingData>()
    private val db = Firebase.firestore //파이어스토어
    private var checkBinding: Boolean = false

    override fun onResume() {
        super.onResume()
        initChattingList()
        initAdapter()
    }

    override fun onStart() {
        super.onStart()
        loadingStart()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        checkBinding = false
    }

    override fun initAfterBinding() {
        checkBinding = true
        initClickListener()
        initScrollListener()
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
        if (checkBinding) {
            chattingListRVAdapter = ChattingListRVAdapter(chattingList)
            binding.chattingListRv.adapter = chattingListRVAdapter
            binding.chattingListRv.layoutManager =
                LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)

            chattingListRVAdapter.setOnItemClickListener(object :
                ChattingListRVAdapter.OnItemClickListener {
                override fun onItemClick(chatting: ChattingData, position: Int) { //채팅방 입장할때
                    val intent = Intent(activity, ChattingRoomActivity::class.java)
                    intent.putExtra("roomName", chatting.roomData.roomName)
                    intent.putExtra("roomUuid", chatting.roomData.roomUuid)
                    startActivity(intent)
                }
            })
        }
    }

    override fun onSuccessGetChatData(position: Int, chattingData: ChattingData) {
        chattingListRVAdapter.setChattingData(position, chattingData)
    }

    private fun initClickListener() {
        //필터버튼
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

        //보관함 버튼
       binding.chattingStorageIv.setOnClickListener {
           (context as MainActivity).supportFragmentManager.beginTransaction().replace(R.id.main_frm, ChattingStorageFragment()).addToBackStack("chattingStorage").commit()
        }
    }

    private fun initScrollListener() {
        binding.chattingListRv.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val lastVisibleItemPosition =
                    (recyclerView.layoutManager as LinearLayoutManager?)!!.findLastCompletelyVisibleItemPosition() // 마지막 스크롤된 항목 위치
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
        if (checkBinding) {
            loadingAnimationView.cancelAnimation()
            binding.animationViewLayout.visibility = View.GONE
            loadingAnimationView.visibility = View.GONE
        }
    }
}
