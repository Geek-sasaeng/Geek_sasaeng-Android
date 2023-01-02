package com.example.geeksasaeng.Chatting.ChattingRoom

import android.os.Bundle
import android.os.Parcelable
import android.util.Log
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.geeksasaeng.Chatting.ChattingRoom.Adapter.ChattingRoomForcedExitRVAdapter
import com.example.geeksasaeng.Chatting.ChattingRoom.Retrofit.MemberData
import com.example.geeksasaeng.R
import com.example.geeksasaeng.Utils.BaseFragment
import com.example.geeksasaeng.Utils.removeForcedExitCheckList
import com.example.geeksasaeng.databinding.FragmentChattingRoomForcedExitBinding


class ChattingRoomForcedExitFragment : BaseFragment<FragmentChattingRoomForcedExitBinding>(FragmentChattingRoomForcedExitBinding::inflate) {

    private lateinit var chattingRoomForcedExitRVAdapter: ChattingRoomForcedExitRVAdapter
    private var mMemberList: MutableList<MemberData> = ArrayList()
    private var mForcedExitMemberList: MutableList<MemberData> = ArrayList()

    override fun onDestroy() {
        Log.d("forcedExit", "fragment-onDestroy")
        super.onDestroy()
        removeForcedExitCheckList() // 뒤로가기해서 fragment가 종료될때만, 체크 표시 기록 삭제해주기
    }

    override fun initAfterBinding() {
        initAdapter()
        initClickListener()
    }

    private fun initAdapter() {
        /*
         더미 데이터
         */

        mMemberList.clear()
        mMemberList.add(MemberData("긱사생1","https://geeksasaeng-s3.s3.ap-northeast-2.amazonaws.com/%ED%94%84%EB%A1%9C%ED%95%84+%EC%9D%B4%EB%AF%B8%EC%A7%80/baseProfileImg.png",false))
        mMemberList.add(MemberData("긱사생2","https://geeksasaeng-s3.s3.ap-northeast-2.amazonaws.com/%ED%94%84%EB%A1%9C%ED%95%84+%EC%9D%B4%EB%AF%B8%EC%A7%80/baseProfileImg.png",false))
        mMemberList.add(MemberData("긱사생3","https://geeksasaeng-s3.s3.ap-northeast-2.amazonaws.com/%ED%94%84%EB%A1%9C%ED%95%84+%EC%9D%B4%EB%AF%B8%EC%A7%80/baseProfileImg.png",false))
        mMemberList.add(MemberData("긱사생4","https://geeksasaeng-s3.s3.ap-northeast-2.amazonaws.com/%ED%94%84%EB%A1%9C%ED%95%84+%EC%9D%B4%EB%AF%B8%EC%A7%80/baseProfileImg.png",false))
        mMemberList.add(MemberData("긱사생5","https://geeksasaeng-s3.s3.ap-northeast-2.amazonaws.com/%ED%94%84%EB%A1%9C%ED%95%84+%EC%9D%B4%EB%AF%B8%EC%A7%80/baseProfileImg.png",false))
        mMemberList.add(MemberData("긱사생6","https://geeksasaeng-s3.s3.ap-northeast-2.amazonaws.com/%ED%94%84%EB%A1%9C%ED%95%84+%EC%9D%B4%EB%AF%B8%EC%A7%80/baseProfileImg.png",false))
        mMemberList.add(MemberData("긱사생7","https://geeksasaeng-s3.s3.ap-northeast-2.amazonaws.com/%ED%94%84%EB%A1%9C%ED%95%84+%EC%9D%B4%EB%AF%B8%EC%A7%80/baseProfileImg.png",false))
        mMemberList.add(MemberData("긱사생8","https://geeksasaeng-s3.s3.ap-northeast-2.amazonaws.com/%ED%94%84%EB%A1%9C%ED%95%84+%EC%9D%B4%EB%AF%B8%EC%A7%80/baseProfileImg.png",false))
        mMemberList.add(MemberData("긱사생9","https://geeksasaeng-s3.s3.ap-northeast-2.amazonaws.com/%ED%94%84%EB%A1%9C%ED%95%84+%EC%9D%B4%EB%AF%B8%EC%A7%80/baseProfileImg.png",false))
        mMemberList.add(MemberData("긱사생10","https://geeksasaeng-s3.s3.ap-northeast-2.amazonaws.com/%ED%94%84%EB%A1%9C%ED%95%84+%EC%9D%B4%EB%AF%B8%EC%A7%80/baseProfileImg.png",false))

        chattingRoomForcedExitRVAdapter = ChattingRoomForcedExitRVAdapter(mMemberList)
        binding.chattingRoomForcedExitRv.adapter = chattingRoomForcedExitRVAdapter
        binding.chattingRoomForcedExitRv.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)

        chattingRoomForcedExitRVAdapter.setOnItemClickListener(object : ChattingRoomForcedExitRVAdapter.OnItemClickListener{
            override fun onItemClick(forcedExitMemberList: MutableList<MemberData>) {
                mForcedExitMemberList = forcedExitMemberList
                Log.d("forcedExit", "forcedExit 멤버 수 : " + mForcedExitMemberList.size)
                if(mForcedExitMemberList.size==0){
                    Log.d("forcedExit", "forcedExit bottombar 비활성화" )
                    binding.chattingRoomForcedExitBottomBar.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.main_unactivated))
                    binding.chattingRoomForcedExitBottomBarTv.text = "퇴장시킬 파티원을 선택해 주세요"
                }else{
                    Log.d("forcedExit", "forcedExit bottombar 활성화" )
                    //하단 바 활성화
                    binding.chattingRoomForcedExitBottomBar.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.main))
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
                val dialog = DialogForcedExitLeader()
                val bundle = Bundle()
                bundle.putParcelableArrayList("list", mForcedExitMemberList as ArrayList<out Parcelable?>?) //강제퇴장리스트 넘겨주기
                dialog.show(parentFragmentManager, "DialogForcedExitLeader")
            }
        }
    }
}