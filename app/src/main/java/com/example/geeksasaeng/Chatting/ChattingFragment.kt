package com.example.geeksasaeng.Chatting

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.geeksasaeng.Home.Delivery.Adapter.DeliveryRVAdapter
import com.example.geeksasaeng.Utils.BaseFragment
import com.example.geeksasaeng.databinding.FragmentChattingBinding

class ChattingFragment: BaseFragment<FragmentChattingBinding>(FragmentChattingBinding::inflate) {

    lateinit var chattingListRVAdapter : ChattingListRVAdapter
    private var chattingList = ArrayList<ChattingList>()

    override fun initAfterBinding() {
        initChattingList()
        initAdapter()
    }

    private fun initChattingList() {
        // 채팅방 DummyData
        chattingList.apply {
            // add(ChattingList("roomName", "roomImgUrl", "lastChat", "lastTime", "newMsg"))
            add(ChattingList("채팅방0", "http://geeksasaeng.shop/s3/neo.jpg", "마지막 채팅입니다ㅏㅏㅏㅏㅏㅏㅏ", "방금", "+10"))
            add(ChattingList("채팅방1", "http://geeksasaeng.shop/s3/neo.jpg", "가나다라마바사아자차카타파하", "방금", "+10"))
            add(ChattingList("채팅방2", "http://geeksasaeng.shop/s3/neo.jpg", "가 나 다 라 마 바 사 아 자 차 카 타 파 하", "방금", "+10"))
            add(ChattingList("채팅방3", "http://geeksasaeng.shop/s3/neo.jpg", "마지막채팅마지막채팅", "방금", "+10"))
            add(ChattingList("채팅방4", "http://geeksasaeng.shop/s3/neo.jpg", "룰루랄라마지막채팅", "어제", "+10"))
            add(ChattingList("채팅방5", "http://geeksasaeng.shop/s3/neo.jpg", "테스트 채팅 테스트", "어제", "+10"))
            add(ChattingList("채팅방6", "http://geeksasaeng.shop/s3/neo.jpg", "마 지 막 채 팅 테 스 트", "1일 전", "+10"))
            add(ChattingList("채팅방7", "http://geeksasaeng.shop/s3/neo.jpg", "L A S T C H A T T I N G", "3일 전", "+10"))
            add(ChattingList("채팅방8", "http://geeksasaeng.shop/s3/neo.jpg", "채팅리스트", "4일 전", "+10"))
            add(ChattingList("채팅방9", "http://geeksasaeng.shop/s3/neo.jpg", "채팅채팅채팅채팅", "4일 전", "+10"))
            add(ChattingList("채팅방10", "http://geeksasaeng.shop/s3/neo.jpg", "마지막채팅마지막채팅마지막채팅", "5일 전", "+10"))
        }
    }

    private fun initAdapter() {
        chattingListRVAdapter = ChattingListRVAdapter(chattingList)
        binding.chattingListRv.adapter = chattingListRVAdapter
        binding.chattingListRv.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
    }
}