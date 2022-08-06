package com.example.geeksasaeng.Chatting.ChattingRoom

import android.text.Editable
import android.text.TextWatcher
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.geeksasaeng.Chatting.ChattingList.ChattingList
import com.example.geeksasaeng.Chatting.ChattingList.ChattingListRVAdapter
import com.example.geeksasaeng.Chatting.ChattingList.ChattingRoomRVAdapter
import com.example.geeksasaeng.R
import com.example.geeksasaeng.Utils.BaseActivity
import com.example.geeksasaeng.databinding.ActivityChattingRoomBinding

class ChattingRoomActivity: BaseActivity<ActivityChattingRoomBinding>(ActivityChattingRoomBinding::inflate) {

    private var roomName = String()
    private var chattingArray = ArrayList<Chatting>()
    lateinit var chattingRoomRVAdapter: ChattingRoomRVAdapter

    override fun initAfterBinding() {
        roomName = intent.getStringExtra("roomName").toString()

        binding.chattingRoomTitleTv.text = roomName

        initChatArray()
        initAdapter()
        initClickListener()
        initTextChangedListener()
    }

    private fun initChatArray() {
        chattingArray.apply {
            add(Chatting(3, null, null, "네오님이 입장하셨습니다", null))
            add(Chatting(3, null, null, "루나님이 입장하셨습니다", null))
            add(Chatting(1, true, R.drawable.ic_default_profile, "안녕하세요안녕하세요안녕하세요안녕하세요안녕하세요안녕하세요안녕하세요안녕하세요안녕하세요", null))
            add(Chatting(2, false, R.drawable.ic_default_profile2, "안녕하세요!", null))
            add(Chatting(3, null, null, "제로님이 입장하셨습니다", null))
            add(Chatting(3, null, null, "모든 파티원이 입장을 마쳤습니다!\n안내에 따라 메뉴를 입력해주세요", null))
            add(Chatting(1, true, R.drawable.ic_default_profile, "안녕하세요~", 1))
            add(Chatting(2, false, R.drawable.ic_default_profile2, "안녕안녕안녕안녕안녕안녕안녕안녕안녕안녕안녕안녕안녕안녕안녕안녕안녕안녕안녕안녕안녕안녕안녕안녕", 1))
            add(Chatting(1, true, R.drawable.ic_default_profile, "뭐 먹을까요", 1))
            add(Chatting(2, false, R.drawable.ic_default_profile2, "저는 짜장면이요!", 1))
            add(Chatting(1, true, R.drawable.ic_default_profile, "좋아요", 2))
        }
    }

    private fun initAdapter() {
        chattingRoomRVAdapter = ChattingRoomRVAdapter(chattingArray)
        binding.chattingRoomChattingRv.adapter = chattingRoomRVAdapter
        binding.chattingRoomChattingRv.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
    }

    private fun initClickListener() {
        binding.chattingRoomBackBtn.setOnClickListener {
            finish()
        }
    }

    private fun initTextChangedListener() {
        binding.chattingRoomChattingTextEt.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun afterTextChanged(p0: Editable?) {
                if (binding.chattingRoomChattingTextEt.text.isNotEmpty())
                    binding.chattingRoomSendTv.setTextColor(ContextCompat.getColor(applicationContext, R.color.main))
                else {
                    binding.chattingRoomSendTv.setTextColor(ContextCompat.getColor(applicationContext, R.color.gray_2))
                }
            }
        })
    }
}