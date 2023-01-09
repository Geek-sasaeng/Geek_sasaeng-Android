package com.example.geeksasaeng.Chatting.ChattingRoom.Retrofit

import android.util.Log
import com.example.geeksasaeng.Chatting.ChattingRoom.DialogForcedExit
import com.example.geeksasaeng.Utils.NetworkModule
import com.example.geeksasaeng.Utils.getJwt
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ChattingService {
    private lateinit var createChattingRoomView: CreateChattingRoomView
    private lateinit var chattingMemberAddView: ChattingMemberAddView
    private lateinit var chattingMemberForcedExitView: ChattingMemberForcedExitView
    private lateinit var chattingOrderCompleteView: ChattingOrderCompleteView
    private lateinit var chattingRemittanceCompleteView: ChattingRemittanceCompleteView
    private lateinit var sendChattingView: SendChattingView
    private lateinit var chattingMemberLeaveView: ChattingMemberLeaveView
    private lateinit var chattingLeaderLeaveView: ChattingLeaderLeaveView
    private lateinit var chattingDeliveryComplicatedView: ChattingDeliveryComplicatedView
    private lateinit var matchingEndView: MatchingEndView

    private var chattingService = NetworkModule.getInstance()?.create(ChattingRetrofitInterfaces::class.java)

    fun setCreateChattingView(createChattingRoomView: CreateChattingRoomView) {
        this.createChattingRoomView = createChattingRoomView
    }
    fun setChattingMemberAddView(chattingMemberAddView: ChattingMemberAddView) {
        this.chattingMemberAddView = chattingMemberAddView
    }
    fun setSendChattingView(sendChattingView: SendChattingView) {
        this.sendChattingView = sendChattingView
    }
    fun setChattingMemberForcedExitView(chattingMemberForcedExitView: ChattingMemberForcedExitView) {
        this.chattingMemberForcedExitView = chattingMemberForcedExitView
    }
    fun setChattingOrderCompleteView(chattingOrderCompleteView: ChattingOrderCompleteView) {
        this.chattingOrderCompleteView = chattingOrderCompleteView
    }
    fun setChattingRemittanceCompleteView(chattingRemittanceCompleteView: ChattingRemittanceCompleteView) {
        this.chattingRemittanceCompleteView = chattingRemittanceCompleteView
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
    fun setMatchingEndView(matchingEndView: MatchingEndView){
        this.matchingEndView = matchingEndView
    }

    // 채팅방 생성
    fun createChattingRoom(createChattingRequest: CreateChattingRoomRequest) {
        chattingService?.createChattingRoom(createChattingRequest)?.enqueue(object : Callback<CreateChattingRoomResponse> {
            override fun onResponse(call: Call<CreateChattingRoomResponse>, response: Response<CreateChattingRoomResponse>) {
                Log.d("CREATE-CHATTING", "response = $response")
                if (response.isSuccessful && response.code() == 200) {
                    val createChattingRoomResponse = response.body()!!
                    Log.d("CREATE-CHATTING", "response body = $createChattingRoomResponse")
                    when (createChattingRoomResponse.code) {
                        1000 -> createChattingRoomView.createChattingRoomSuccess(createChattingRoomResponse.result)
                        else -> createChattingRoomView.createChattingRoomFailure(createChattingRoomResponse.code, createChattingRoomResponse.message)
                    }
                }
            }
            override fun onFailure(call: Call<CreateChattingRoomResponse>, t: Throwable) {
                Log.d("CHATTING-CREATE", "실패")
            }
        })
    }

    // 채팅방 멤버 추가
    fun addChattingMember(chattingMemberAddRequest: ChattingMemberAddRequest) {
        chattingService?.chattingMemberAdd(chattingMemberAddRequest)?.enqueue(object: Callback<ChattingMemberAddResponse> {
            override fun onResponse(call: Call<ChattingMemberAddResponse>, response: Response<ChattingMemberAddResponse>) {
                if (response.isSuccessful && response.code() == 200) {
                    val resp = response.body()!!
                    when (resp.code) {
                        1000 -> chattingMemberAddView.chattingMemberAddSuccess(resp.result)
                        else -> chattingMemberAddView.chattingMemberAddFailure(resp.code, resp.message)
                    }
                }
            }
            override fun onFailure(call: Call<ChattingMemberAddResponse>, t: Throwable) {
                Log.d("CHATTING-ADD-MEMBER", "실패")
            }
        })
    }

    // 채팅 전송
    fun sendChatting(sendChattingRequest: SendChattingRequest) {
        chattingService?.sendChatting(sendChattingRequest)?.enqueue(object: Callback<SendChattingResponse> {
            override fun onResponse(call: Call<SendChattingResponse>, response: Response<SendChattingResponse>) {
                if (response.isSuccessful && response.code() == 200) {
                    val sendChattingResponse = response.body()!!
                    when (sendChattingResponse.code) {
                        1000 -> sendChattingView.sendChattingSuccess(sendChattingResponse.result!!)
                        else -> sendChattingView.sendChattingFailure(sendChattingResponse.code, sendChattingResponse.message)
                    }
                }
            }
            override fun onFailure(call: Call<SendChattingResponse>, t: Throwable) {
                Log.d("CHATTING-SEND", "실패")
            }
        })
    }

    // 방장이 배달 파티 채팅 멤버를 강제퇴장
    fun chattingMemberForcedExit(chattingMemberForcedExitRequest: ChattingMemberForcedExitRequest) {
        chattingService?.chattingMemberForcedExit(chattingMemberForcedExitRequest)?.enqueue(object: Callback<ChattingMemberForcedExitResponse> {

            override fun onResponse(call: Call<ChattingMemberForcedExitResponse>, response: Response<ChattingMemberForcedExitResponse>) {
                if (response.isSuccessful && response.code() == 200) {
                    val resp = response.body()!!
                    when (resp.code) {
                        1000 -> chattingMemberForcedExitView.chattingMemberForcedExitSuccess(resp.result)
                        else -> chattingMemberForcedExitView.chattingMemberForcedExitFailure(resp.code, resp.message)
                    }
                }
            }

            override fun onFailure(call: Call<ChattingMemberForcedExitResponse>, t: Throwable) {
                Log.d("CHATTING-MEMBER-FORCED-EXIT", "실패")
            }
        })
    }

    // 방장 - 주문완료
    fun chattingOrderComplete(chattingOrderCompleteRequest: ChattingOrderCompleteRequest) {
        chattingService?.chattingOrderComplete(chattingOrderCompleteRequest)?.enqueue(object: Callback<ChattingOrderCompleteResponse> {

            override fun onResponse(
                call: Call<ChattingOrderCompleteResponse>,
                response: Response<ChattingOrderCompleteResponse>
            ) {
                Log.d("orderComplete", "response:"+response)
                if (response.isSuccessful && response.code() == 200) {
                    val resp = response.body()!!
                    when (resp.code) {
                        1000 -> chattingOrderCompleteView.chattingOrderCompleteSuccess(resp.result)
                        else -> chattingOrderCompleteView.chattingOrderCompleteFailure(resp.code, resp.message)
                    }
                }
            }

            override fun onFailure(call: Call<ChattingOrderCompleteResponse>, t: Throwable) {
                Log.d("ORDER-COMPLETE", "실패")
            }
        })
    }

    // 멤버 - 송금완료
    fun chattingRemittanceComplete(chattingRemittanceCompleteRequest: ChattingRemittanceCompleteRequest) {
        chattingService?.chattingRemittanceComplete(chattingRemittanceCompleteRequest)?.enqueue(object: Callback<ChattingRemittanceCompleteResponse> {

            override fun onResponse(
                call: Call<ChattingRemittanceCompleteResponse>,
                response: Response<ChattingRemittanceCompleteResponse>
            ) {
                Log.d("orderComplete", "response:"+response)
                if (response.isSuccessful && response.code() == 200) {
                    val resp = response.body()!!
                    when (resp.code) {
                        1000 -> chattingRemittanceCompleteView.chattingRemittanceCompleteSuccess(resp.result)
                        else -> chattingRemittanceCompleteView.chattingRemittanceCompleteFailure(resp.code, resp.message)
                    }
                }
            }

            override fun onFailure(call: Call<ChattingRemittanceCompleteResponse>, t: Throwable) {
                Log.d("REMITTANCE-COMPLETE", "실패")
            }
        })
    }

    // 파티 멤버 나가기
    fun getChattingPartyMemberLeave(chattingPartyMemberLeaveRequest: ChattingPartyMemberLeaveRequest){
        chattingService?.partyMemberChattingLeave(chattingPartyMemberLeaveRequest)?.enqueue(object : Callback<ChattingPartyMemberLeaveResponse> {
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
        chattingService?.partyLeaderChattingLeave(chattingPartyLeaderLeaveRequest)?.enqueue(object : Callback<ChattingPartyLeaderLeaveResponse> {
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
        chattingService?.partyDeliveryComplicated(chattingDeliveryComplicatedRequest)?.enqueue(object : Callback<ChattingDeliveryComplicatedResponse?>{
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

    //배달파티 수동 매칭마감
    fun matchingEndSender(roomUuid: String){
        Log.d("matchingEND","Bearer " + getJwt()+"/"+roomUuid.toString())
        chattingService?.matchingEnd(roomUuid)?.enqueue(object : Callback<MatchingEndResponse>{
            override fun onResponse(call: Call<MatchingEndResponse>, response: Response<MatchingEndResponse>) {
                Log.d("matchingEND", response.toString())
                if (response.isSuccessful && response.code() == 200) {
                    val resp = response.body()!!
                    Log.d("matchingEND", resp.toString())
                    when(resp.code){
                        1000->matchingEndView.onMatchingEndSuccess()
                        else->matchingEndView.onMatchingEndFailure(resp.message)
                    }
                }
            }
            override fun onFailure(call: Call<MatchingEndResponse>, t: Throwable) {
                Log.d("MATCHINGEND-RESPONSE", "ChattingDataService-onFailure : MatchingEndFailed", t)
            }
        })
    }
}
