package com.example.geeksasaeng.Chatting.ChattingList

import android.animation.Animator
import android.content.Intent
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.airbnb.lottie.LottieAnimationView
import com.example.geeksasaeng.Chatting.ChattingList.Retrofit.*
import com.example.geeksasaeng.Chatting.ChattingStorage.ChattingStorageFragment
import com.example.geeksasaeng.MainActivity
import com.example.geeksasaeng.R
import com.example.geeksasaeng.Chatting.ChattingRoom.ChattingRoomActivity
import com.example.geeksasaeng.Chatting.ChattingRoom.Retrofit.ChattingDetailResult
import com.example.geeksasaeng.Utils.BaseFragment
import com.example.geeksasaeng.databinding.FragmentChattingBinding
import com.google.gson.annotations.SerializedName

class ChattingListFragment : BaseFragment<FragmentChattingBinding>(FragmentChattingBinding::inflate),
    ChattingListView {
    lateinit var loadingAnimationView: LottieAnimationView
    private lateinit var chattingListRVAdapter: ChattingListRVAdapter
    lateinit var chattingListService: ChattingListService
    var cursor: Int = 0
    private var chattingList = ArrayList<ChattingList?>()
    private var checkBinding: Boolean = false

    // 채팅방 정보를 넘기기 위해 필요한 부분
    private lateinit var roomTitle: String
    private lateinit var roomId: String

    override fun onResume() {
        super.onResume()
        initChattingList()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        checkBinding = false
    }

    override fun initAfterBinding() {
        checkBinding = true
        loadingStart()
        initAdapter()
        initClickListener()
        initScrollListener()
    }

    private fun initChattingList() {
        chattingList.clear()
        chattingListService = ChattingListService()
        chattingListService.setChattingListView(this)
        getChattingList()
    }

    private fun initAdapter() {
        if (checkBinding) {
            chattingListRVAdapter = ChattingListRVAdapter(chattingList)
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

                    //ChattingRoomActivity 실행
                    val intent = Intent(activity, ChattingRoomActivity::class.java)
                    intent.putExtra("roomName", roomTitle)
                    intent.putExtra("roomId", roomId)
                    startActivity(intent)
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

    override fun getChattingListSuccess(result: ChattingListResult) {
        result.parties?.let { chattingList.addAll(it) }
        chattingListRVAdapter.notifyDataSetChanged()
        var finalPage = result.finalPage
        Log.d("GET-CHATTING-LIST", "result = $result")

        // 로딩화면 제거
        loadingStop()
    }

    override fun getChattingListFailure(code: Int, msg: String) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT)
        Log.d("GET-CHATTING-LIST", "code = $code, msg = $msg")
    }

}