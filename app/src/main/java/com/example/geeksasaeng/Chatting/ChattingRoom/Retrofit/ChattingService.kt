package com.example.geeksasaeng.Chatting.ChattingRoom.Retrofit

import android.util.Log
import com.example.geeksasaeng.Utils.ApplicationClass.Companion.retrofit
import com.example.geeksasaeng.Utils.getJwt
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ChattingService {
    private lateinit var chattingMemberLeaveView: ChattingMemberLeaveView
    private lateinit var chattingLeaderLeaveView: ChattingLeaderLeaveView

    private var chattingService = retrofit.create(ChattingRetrofitInterfaces::class.java)

    fun setChattingMemberLeaveView(chattingMemberLeaveView: ChattingMemberLeaveView){
        this.chattingMemberLeaveView = chattingMemberLeaveView
    }
    fun setChattingLeaderLeaveView(chattingLeaderLeaveView: ChattingLeaderLeaveView){
        this.chattingLeaderLeaveView = chattingLeaderLeaveView
    }

    // 파티 멤버 나가기
    fun getChattingPartyMemberLeave(chattingPartyMemberLeaveRequest: ChattingPartyMemberLeaveRequest){
        chattingService.partyMemberChattingLeave("Bearer " + getJwt(), chattingPartyMemberLeaveRequest).enqueue(object : Callback<ChattingPartyMemberLeaveResponse> {
            override fun onResponse(
                call: Call<ChattingPartyMemberLeaveResponse>,
                response: Response<ChattingPartyMemberLeaveResponse>
            ) {
                if (response.isSuccessful && response.code() == 200) {
                    val chattingPartyMemberLeaveResponse = response.body()!!

                    when (chattingPartyMemberLeaveResponse.code) {
                        1000 -> chattingMemberLeaveView.chattingMemberLeaveSuccess(chattingPartyMemberLeaveResponse.result!!)
                        else -> chattingMemberLeaveView.chattingMemberLeaveFailure(chattingPartyMemberLeaveResponse.code, chattingPartyMemberLeaveResponse.message)
                    }
                }
            }

            override fun onFailure(call: Call<ChattingPartyMemberLeaveResponse>, t: Throwable) {
                Log.d("CHATTING-MEMBER-LEAVE", "실패")
            }

        })
    }

    // 파티장 나가기
    fun getChattingPartyLeaderLeave(chattingPartyLeaderLeaveRequest: ChattingPartyLeaderLeaveRequest, leaderMap: HashMap<String, String>){
        chattingService.partyLeaderChattingLeave("Bearer " + getJwt(), chattingPartyLeaderLeaveRequest).enqueue(object : Callback<ChattingPartyLeaderLeaveResponse> {
            override fun onResponse(
                call: Call<ChattingPartyLeaderLeaveResponse>,
                response: Response<ChattingPartyLeaderLeaveResponse>
            ) {
                if (response.isSuccessful && response.code() == 200) {
                    val ChattingPartyLeaderLeaveResponse = response.body()!!

                    when (ChattingPartyLeaderLeaveResponse.code) {
                        1000 -> chattingLeaderLeaveView.chattingLeaderLeaveSuccess(ChattingPartyLeaderLeaveResponse.result!!.result, leaderMap)
                        else -> chattingLeaderLeaveView.chattingLeaderLeaveFailure(ChattingPartyLeaderLeaveResponse.code, ChattingPartyLeaderLeaveResponse.message)
                    }
                }
            }

            override fun onFailure(call: Call<ChattingPartyLeaderLeaveResponse>, t: Throwable) {
                Log.d("CHATTING-MEMBER-LEAVE", "실패")
            }
        })
    }
}
