package com.example.geeksasaeng.Chatting.ChattingRoom

import android.os.Bundle
import android.os.Parcelable
import android.util.Log
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.geeksasaeng.Chatting.ChattingRoom.Adapter.ChattingRoomForcedExitRVAdapter
import com.example.geeksasaeng.Chatting.ChattingRoom.Retrofit.*
import com.example.geeksasaeng.Home.Delivery.DeliveryBannerResult
import com.example.geeksasaeng.Home.Delivery.Retrofit.DeliveryService
import com.example.geeksasaeng.R
import com.example.geeksasaeng.Utils.BaseActivity
import com.example.geeksasaeng.Utils.BaseFragment
import com.example.geeksasaeng.Utils.getJwt
import com.example.geeksasaeng.Utils.removeForcedExitCheckList
import com.example.geeksasaeng.databinding.ActivityChattingRoomForcedExitBinding
import kotlin.properties.Delegates

class ChattingRoomForcedExitActivity : BaseActivity<ActivityChattingRoomForcedExitBinding>(ActivityChattingRoomForcedExitBinding::inflate), PreChattingMemberForcedExitView {

    private lateinit var chattingRoomForcedExitRVAdapter: ChattingRoomForcedExitRVAdapter
    private var partyId by Delegates.notNull<Int>()
    private lateinit var roomId: String
    private var mMemberList: MutableList<MemberData> = ArrayList()
    private var mForcedExitMemberList: MutableList<MemberData> = ArrayList()
    private lateinit var chattingDataService: ChattingService

    override fun onDestroy() {
        Log.d("forcedExit", "fragment-onDestroy")
        super.onDestroy()
        removeForcedExitCheckList() // 뒤로가기해서 fragment가 종료될때만, 체크 표시 기록 삭제해주기
    }

    override fun initAfterBinding() {
        initChattingService()
        initClickListener()
    }

    private fun initChattingService() {
        chattingDataService = ChattingService()
        chattingDataService.setPreChattingMemberForcedExitView(this)
        //partyId, roomId 받아오기
        partyId = intent.getIntExtra("partyId",-1)
        roomId = intent.getStringExtra("roomId").toString()
        Log.d("preForcedExit", getJwt().toString())
        chattingDataService.prechattingMemberForcedExit(partyId, roomId) //★배달 파티 채팅방 멤버 정보를 조회 API 호출!
    }

    private fun initAdapter() {
        chattingRoomForcedExitRVAdapter = ChattingRoomForcedExitRVAdapter(mMemberList)
        binding.chattingRoomForcedExitRv.adapter = chattingRoomForcedExitRVAdapter
        binding.chattingRoomForcedExitRv.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

        chattingRoomForcedExitRVAdapter.setOnItemClickListener(object : ChattingRoomForcedExitRVAdapter.OnItemClickListener{
            override fun onItemClick(forcedExitMemberList: MutableList<MemberData>) {
                mForcedExitMemberList = forcedExitMemberList
                Log.d("forcedExit", "forcedExit 멤버 수 : " + mForcedExitMemberList.size)
                if(mForcedExitMemberList.size==0){
                    binding.chattingRoomForcedExitBottomBar.setBackgroundColor(ContextCompat.getColor(applicationContext, R.color.main_unactivated))
                    binding.chattingRoomForcedExitBottomBarTv.text = "퇴장시킬 파티원을 선택해 주세요"
                }else{
                    //하단 바 활성화
                    binding.chattingRoomForcedExitBottomBar.setBackgroundColor(ContextCompat.getColor(applicationContext, R.color.main))
                    //안내문구 변경 (퇴장시킬 파티원을 선택해 주세요->다음)
                    binding.chattingRoomForcedExitBottomBarTv.text = "다음"
                }
                //몇명 선택했는지 표시
                binding.chattingRoomForcedExitMemberNumber.text = "${mForcedExitMemberList.size}/${mMemberList.size}명"
            }
        })
    }

    private fun initClickListener() {
        binding.chattingRoomForcedExitNext.setOnClickListener {
            if(mForcedExitMemberList.size>0){
                //다음버튼 누를 시, 다이얼로그 띄우기
                val dialog = DialogForcedExit()
                val bundle = Bundle()
                // TODO: 나중에 넘길때는 mForcedExitMemberList를 체크한 순서가 아니라, 채팅방 멤버 정렬 순으로 넣어주는게 더 좋아보여!
                // 나중에 실제 데이터 들어오면 그거 속성값 중에 하나 정렬순서로 삼아서 하면 될 것 같기두?
                bundle.putString("roomId", roomId)
                bundle.putParcelableArrayList("forcedExitList", mForcedExitMemberList as ArrayList<out Parcelable?>?) //강제퇴장리스트 넘겨주기
                dialog.arguments = bundle
                dialog.show(supportFragmentManager, "DialogForcedExitLeader")
            }
        }
    }

    override fun preChattingMemberForcedExitSuccess(results: Array<PreChattingMemberForcedExitResult>) {
        mMemberList.clear()
        for (i in results) {
            Log.d("preForcedExit-success", i.toString())
            mMemberList.add(MemberData(i.chatMemberId, i.memberId, i.userName,i.userProfileImgUrl,false))
        }
        initAdapter()
    }

    override fun preChattingMemberForcedExitFailure(code: Int, message: String) {
        Log.d("preForcedExit-failure", message)
    }
}