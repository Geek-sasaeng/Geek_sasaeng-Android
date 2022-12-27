package com.example.geeksasaeng.Chatting.ChattingRoom.Retrofit

import android.util.Log
import com.example.geeksasaeng.Utils.ApplicationClass.Companion.retrofit
import com.example.geeksasaeng.Utils.NetworkModule
import com.example.geeksasaeng.Utils.getJwt
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ChattingService {
    private lateinit var createChattingView: CreateChattingView
    private lateinit var chattingMemberLeaveView: ChattingMemberLeaveView
    private lateinit var chattingLeaderLeaveView: ChattingLeaderLeaveView
    private lateinit var chattingDeliveryComplicatedView: ChattingDeliveryComplicatedView

    private var chattingService = NetworkModule.getInstance()?.create(ChattingRetrofitInterfaces::class.java)

    fun setCreateChattingView(createChattingView: CreateChattingView) {
        this.createChattingView = createChattingView
    }
    fun setChattingMemberLeaveView(chattingMemberLeaveView: ChattingMemberLeaveView){
        this.chattingMemberLeaveView = chattingMemberLeaveView
    }
    fun setChattingLeaderLeaveView(chattingLeaderLeaveView: ChattingLeaderLeaveView){
        this.chattingLeaderLeaveView = chattingLeaderLeaveView
    }
    fun setChattingDeliveryComplicatedView(chattingDeliveryComplicatedView: ChattingDeliveryComplicatedView){
        this.chattingDeliveryComplicatedView = chattingDeliveryComplicatedView
    }

    // 채팅방 생성
    fun createChatting(createChattingRequest: CreateChattingRequest) {
        chattingService?.createChatting("Bearer " + getJwt(), createChattingRequest)?.enqueue(object : Callback<CreateChattingResponse> {
            override fun onResponse(call: Call<CreateChattingResponse>, response: Response<CreateChattingResponse>) {
                if (response.isSuccessful && response.code() == 200) {
                    val createChattingResponse = response.body()!!
                    when (createChattingResponse.code) {
                        1000 -> createChattingView.createChattingSuccess(createChattingResponse.result)
                        else -> createChattingView.createChattingFailure(createChattingResponse.code, createChattingResponse.message)
                    }
                }
            }
            override fun onFailure(call: Call<CreateChattingResponse>, t: Throwable) {
                Log.d("CHATTING-CREATE", "실패")
            }
        })
    }
    
    // 파티 멤버 나가기
    fun getChattingPartyMemberLeave(chattingPartyMemberLeaveRequest: ChattingPartyMemberLeaveRequest){
        chattingService?.partyMemberChattingLeave("Bearer " + getJwt(), chattingPartyMemberLeaveRequest)?.enqueue(object : Callback<ChattingPartyMemberLeaveResponse> {
            override fun onResponse(call: Call<ChattingPartyMemberLeaveResponse>, response: Response<ChattingPartyMemberLeaveResponse>) {
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
        chattingService?.partyLeaderChattingLeave("Bearer " + getJwt(), chattingPartyLeaderLeaveRequest)
            ?.enqueue(object : Callback<ChattingPartyLeaderLeaveResponse> {
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

    //배달완료 알림보내기
    fun sendDeliveryComplicatedAlarm(chattingDeliveryComplicatedRequest: ChattingDeliveryComplicatedRequest){
        Log.d("deliveryComplicated", "Bearer " + getJwt() + "            :           "+chattingDeliveryComplicatedRequest.toString())
        chattingService?.partyDeliveryComplicated("Bearer " + getJwt(),chattingDeliveryComplicatedRequest)
            ?.enqueue(object : Callback<ChattingDeliveryComplicatedResponse?>{
                override fun onResponse(
                    call: Call<ChattingDeliveryComplicatedResponse?>,
                    response: Response<ChattingDeliveryComplicatedResponse?>
                ) {
                    Log.d("deliveryComplicated", response.toString())
                    if (response.isSuccessful && response.code() == 200) {
                        val resp = response.body()!!
                        when(resp.code){
                            1000->chattingDeliveryComplicatedView.chattingDeliveryComplicatedSuccess()
                            else->chattingDeliveryComplicatedView.chattingDeliveryComplicatedFailure(resp.message)
                        }
                    }
                }

                override fun onFailure(call: Call<ChattingDeliveryComplicatedResponse?>, t: Throwable) {
                    Log.d("delivery-배달완료알람", "실패"+ t.toString())
                    //TODO: 계속 타임아웃 오류나...
                }
            })
    }
}
