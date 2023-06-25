package com.example.geeksasaeng.Chatting.ChattingRoom.Retrofit

import android.util.Log
import com.example.geeksasaeng.Utils.NetworkModule
import com.example.geeksasaeng.Utils.getJwt
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ChattingService {
    private lateinit var createChattingRoomView: CreateChattingRoomView
    private lateinit var sendImageChattingView: SendImageChattingView
    private lateinit var chattingMemberAddView: ChattingMemberAddView
    private lateinit var preChattingMemberForcedExitView: PreChattingMemberForcedExitView
    private lateinit var chattingMemberForcedExitView: ChattingMemberForcedExitView
    private lateinit var deliveryPartyMemberForcedExitView: DeliveryPartyMemberForcedExitView
    private lateinit var chattingOrderCompleteView: ChattingOrderCompleteView
    private lateinit var chattingRemittanceCompleteView: ChattingRemittanceCompleteView
    private lateinit var sendChattingView: SendChattingView
    private lateinit var chattingMemberLeavePartyView: ChattingMemberLeavePartyView
    private lateinit var chattingMemberLeaveChatView: ChattingMemberLeaveChatView
    private lateinit var chattingLeaderLeavePartyView: ChattingLeaderLeavePartyView
    private lateinit var chattingLeaderLeaveChatView: ChattingLeaderLeaveChatView
    private lateinit var chattingDeliveryCompleteView: ChattingDeliveryCompleteView
    private lateinit var matchingEndView: MatchingEndView
    private lateinit var getChattingDetailView: ChattingDetailView
    private lateinit var chattingUserProfileView: ChattingUserProfileView

    private var chattingService = NetworkModule.getInstance()?.create(ChattingRetrofitInterfaces::class.java)

    fun setCreateChattingView(createChattingRoomView: CreateChattingRoomView) {
        this.createChattingRoomView = createChattingRoomView
    }
    fun setSendImageChattingView(sendImageChattingView: SendImageChattingView) {
        this.sendImageChattingView = sendImageChattingView
    }
    fun setChattingMemberAddView(chattingMemberAddView: ChattingMemberAddView) {
        this.chattingMemberAddView = chattingMemberAddView
    }
    fun setSendChattingView(sendChattingView: SendChattingView) {
        this.sendChattingView = sendChattingView
    }
    fun setPreChattingMemberForcedExitView(preChattingMemberForcedExitView: PreChattingMemberForcedExitView) {
        this.preChattingMemberForcedExitView = preChattingMemberForcedExitView
    }
    fun setChattingMemberForcedExitView(chattingMemberForcedExitView: ChattingMemberForcedExitView) {
        this.chattingMemberForcedExitView = chattingMemberForcedExitView
    }
    fun setDeliveryPartyMemberForcedExitView(deliveryPartyMemberForcedExitView: DeliveryPartyMemberForcedExitView) {
        this.deliveryPartyMemberForcedExitView = deliveryPartyMemberForcedExitView
    }
    fun setChattingOrderCompleteView(chattingOrderCompleteView: ChattingOrderCompleteView) {
        this.chattingOrderCompleteView = chattingOrderCompleteView
    }
    fun setChattingRemittanceCompleteView(chattingRemittanceCompleteView: ChattingRemittanceCompleteView) {
        this.chattingRemittanceCompleteView = chattingRemittanceCompleteView
    }
    fun setChattingMemberLeavePartyView(chattingMemberLeavePartyView: ChattingMemberLeavePartyView){
        this.chattingMemberLeavePartyView = chattingMemberLeavePartyView
    }
    fun setChattingMemberLeaveChatView(chattingMemberLeaveChatView: ChattingMemberLeaveChatView){
        this.chattingMemberLeaveChatView = chattingMemberLeaveChatView
    }
    fun setChattingLeaderLeavePartyView(chattingLeaderLeavePartyView: ChattingLeaderLeavePartyView){
        this.chattingLeaderLeavePartyView = chattingLeaderLeavePartyView
    }
    fun setChattingLeaderLeaveChatView(chattingLeaderLeaveChatView: ChattingLeaderLeaveChatView){
        this.chattingLeaderLeaveChatView = chattingLeaderLeaveChatView
    }
    fun setChattingDeliveryCompleteView(chattingDeliveryCompleteView: ChattingDeliveryCompleteView){
        this.chattingDeliveryCompleteView = chattingDeliveryCompleteView
    }
    fun setMatchingEndView(matchingEndView: MatchingEndView){
        this.matchingEndView = matchingEndView
    }
    fun setChattingDetailView(getChattingDetailView: ChattingDetailView) {
        this.getChattingDetailView = getChattingDetailView
    }
    fun setChattingUserProfileView(chattingUserProfileView: ChattingUserProfileView)  {
        this.chattingUserProfileView = chattingUserProfileView
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

    // 이미지 채팅 전송
    fun sendImgChatting(images: MutableList<MultipartBody.Part?>, data: HashMap<String, RequestBody>) {
        chattingService?.sendImgChatting(images, data)?.enqueue(object: Callback<SendChattingResponse> {
            override fun onResponse(
                call: Call<SendChattingResponse>,
                response: Response<SendChattingResponse>
            ) {
                Log.d("API-TEST", "response = $response")
                Log.d("API-TEST", "sendImageChatting = ${response.body()}")
                if (response.isSuccessful && response.code() == 200) {
                    val resp = response.body()!!
                    when (resp.code) {
                        1000 -> sendImageChattingView.sendImageChattingSuccessView(resp)
                        else -> sendImageChattingView.sendImageChattingFailureView()
                    }
                }
            }
            override fun onFailure(call: Call<SendChattingResponse>, t: Throwable) {
                Log.d("CHATTING-IMG-CHATTING", "실패")
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

    //강제퇴장 조회 api
    fun prechattingMemberForcedExit(partyId: Int, roomId: String) {
        chattingService?.preChattingMemberForcedExit(partyId,roomId)?.enqueue(object: Callback<PreChattingMemberForcedExitResponse> {
            override fun onResponse(call: Call<PreChattingMemberForcedExitResponse>, response: Response<PreChattingMemberForcedExitResponse>) {
                Log.d("preForcedExit", "$partyId/$roomId")
                Log.d("preForcedExit", response.toString())
                if (response.isSuccessful && response.code() == 200) {
                    val resp = response.body()!!
                    Log.d("preForcedExit-resp", resp.toString())
                    when (resp.code) {
                        1000 -> preChattingMemberForcedExitView.preChattingMemberForcedExitSuccess(resp.result)
                        else -> preChattingMemberForcedExitView.preChattingMemberForcedExitFailure(resp.code, resp.message)
                    }
                }
            }

            override fun onFailure(call: Call<PreChattingMemberForcedExitResponse>, t: Throwable) {
                Log.d("preForcedExit", "실패 : $t")
            }
        })
    }

    // 방장이 배달 파티 채팅 멤버를 강제퇴장 for 채팅방
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
                Log.d("MEMBER-FORCED-EXIT", "실패")
            }
        })
    }

    // 방장이 배달 파티 채팅 멤버를 강제퇴장 for 배달파티
    fun deliveryPartyMemberForcedExit(deliveryPartyMemberForcedExitRequest: DeliveryPartyMemberForcedExitRequest) {
        chattingService?.deliveryPartyMemberForcedExit(deliveryPartyMemberForcedExitRequest)?.enqueue(object: Callback<DeliveryPartyMemberForcedExitResponse> {

            override fun onResponse(call: Call<DeliveryPartyMemberForcedExitResponse>, response: Response<DeliveryPartyMemberForcedExitResponse>) {
                if (response.isSuccessful && response.code() == 200) {
                    val resp = response.body()!!
                    when (resp.code) {
                        1000 -> deliveryPartyMemberForcedExitView.deliveryPartyMemberForcedExitSuccess(resp.result)
                        else -> deliveryPartyMemberForcedExitView.deliveryPartyMemberForcedExitFailure(resp.code, resp.message)
                    }
                }
            }

            override fun onFailure(call: Call<DeliveryPartyMemberForcedExitResponse>, t: Throwable) {
                Log.d("MEMBER-FORCED-EXIT", "실패")
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

    // 파티 멤버 나가기 for 배달 파티
    fun getChattingPartyMemberPartyLeave(chattingPartyMemberLeavePartyRequest: ChattingPartyMemberLeavePartyRequest){
        chattingService?.partyMemberPartyLeave(chattingPartyMemberLeavePartyRequest)?.enqueue(object : Callback<ChattingPartyMemberLeavePartyResponse> {
            override fun onResponse(call: Call<ChattingPartyMemberLeavePartyResponse>, response: Response<ChattingPartyMemberLeavePartyResponse>) {
                Log.d("exit", "배달파티 나가기 response : ${chattingPartyMemberLeavePartyRequest.toString()}")
                Log.d("exit", "배달파티 나가기 response : ${response.toString()}")
                if (response.isSuccessful && response.code() == 200) {
                    val chattingPartyMemberLeavePartyResponse = response.body()!!
                    when (chattingPartyMemberLeavePartyResponse.code) {
                        1000 -> chattingMemberLeavePartyView.chattingMemberLeavePartySuccess(chattingPartyMemberLeavePartyResponse.result!!)
                        else -> chattingMemberLeavePartyView.chattingMemberLeavePartyFailure(chattingPartyMemberLeavePartyResponse.code, chattingPartyMemberLeavePartyResponse.message)
                    }
                }
            }
            override fun onFailure(call: Call<ChattingPartyMemberLeavePartyResponse>, t: Throwable) {
                Log.d("exit", "멤버 배달파티 나가기 실패 => onFailure"+t.toString())
            }
        })
    }

    // 파티 멤버 나가기 for 채팅방
    fun getChattingPartyMemberChatLeave(chattingPartyMemberLeaveChatRequest: ChattingPartyMemberLeaveChatRequest){
        chattingService?.partyMemberChattingLeave(chattingPartyMemberLeaveChatRequest)?.enqueue(object : Callback<ChattingPartyMemberLeaveChatResponse> {

            override fun onResponse(call: Call<ChattingPartyMemberLeaveChatResponse>, response: Response<ChattingPartyMemberLeaveChatResponse>) {
                if (response.isSuccessful && response.code() == 200) {
                    val chattingPartyMemberLeaveChatResponse = response.body()!!
                    when (chattingPartyMemberLeaveChatResponse.code) {
                        1000 -> chattingMemberLeaveChatView.chattingMemberLeaveChatSuccess(chattingPartyMemberLeaveChatResponse.result!!)
                        else -> chattingMemberLeaveChatView.chattingMemberLeaveChatFailure(chattingPartyMemberLeaveChatResponse.code, chattingPartyMemberLeaveChatResponse.message)
                    }
                }
            }

            override fun onFailure(call: Call<ChattingPartyMemberLeaveChatResponse>, t: Throwable) {
                Log.d("exit", "멤버 채팅방 나가기 실패 => onFailure"+t.toString()+t.toString())
            }
        })
    }

    // 파티장 나가기 for 배달 파티
    fun getChattingPartyLeaderPartyLeave(chattingPartyLeaderLeavePartyRequest: ChattingPartyLeaderLeavePartyRequest){
        chattingService?.partyLeaderPartyLeave(chattingPartyLeaderLeavePartyRequest)?.enqueue(object : Callback<ChattingPartyLeaderLeavePartyResponse> {
                override fun onResponse(
                    call: Call<ChattingPartyLeaderLeavePartyResponse>,
                    response: Response<ChattingPartyLeaderLeavePartyResponse>
                ) {
                    if (response.isSuccessful && response.code() == 200) {
                        val chattingPartyLeaderLeavePartyResponse = response.body()!!

                        when (chattingPartyLeaderLeavePartyResponse.code) {
                            1000 -> chattingLeaderLeavePartyView.chattingLeaderLeavePartySuccess(chattingPartyLeaderLeavePartyResponse.result)
                            else -> chattingLeaderLeavePartyView.chattingLeaderLeavePartyFailure(chattingPartyLeaderLeavePartyResponse.code, chattingPartyLeaderLeavePartyResponse.message)
                        }
                    }
                }

                override fun onFailure(call: Call<ChattingPartyLeaderLeavePartyResponse>, t: Throwable) {
                    Log.d("exit", "방장 배달파티 나가기 실패 => onFailure"+t.toString())
                }
            })
    }

    // 파티장 나가기 for 채팅방
    fun getChattingPartyLeaderChatLeave(chattingPartyLeaderLeaveChatRequest: ChattingPartyLeaderLeaveChatRequest){
        chattingService?.partyLeaderChattingLeave(chattingPartyLeaderLeaveChatRequest)?.enqueue(object : Callback<ChattingPartyLeaderLeaveChatResponse> {

            override fun onResponse(call: Call<ChattingPartyLeaderLeaveChatResponse>, response: Response<ChattingPartyLeaderLeaveChatResponse>) {
                Log.d("exit", "방장 채팅방 나가기 : "+chattingPartyLeaderLeaveChatRequest.toString())
                Log.d("exit", "방장 채팅방 나가기 : "+response.toString())
                if (response.isSuccessful && response.code() == 200) {
                    val chattingPartyLeaderLeaveChatResponse = response.body()!!

                    when (chattingPartyLeaderLeaveChatResponse.code) {
                        1000 -> chattingLeaderLeaveChatView.chattingLeaderLeaveChatSuccess(chattingPartyLeaderLeaveChatResponse.result!!)
                        else -> chattingLeaderLeaveChatView.chattingLeaderLeaveChatFailure(chattingPartyLeaderLeaveChatResponse.code, chattingPartyLeaderLeaveChatResponse.message)
                    }
                }
            }

            override fun onFailure(call: Call<ChattingPartyLeaderLeaveChatResponse>, t: Throwable) {
                Log.d("exit", "방장 채팅방 나가기 실패 => onFailure"+ t.toString())
            }
        })
    }

    //배달완료 알림보내기
    fun sendDeliveryCompleteAlarm(chattingDeliveryCompleteRequest: ChattingDeliveryCompleteRequest){
        Log.d("deliveryAlarm", "Bearer " + getJwt() + "            :           "+chattingDeliveryCompleteRequest.toString())
        chattingService?.partyDeliveryComplete(chattingDeliveryCompleteRequest)?.enqueue(object : Callback<ChattingDeliveryCompleteResponse?>{
                override fun onResponse(call: Call<ChattingDeliveryCompleteResponse?>,response: Response<ChattingDeliveryCompleteResponse?>) {
                    Log.d("deliveryAlarm", response.toString())
                    Log.d("deliveryAlarm", response.body().toString())
                    if (response.isSuccessful && response.code() == 200) {
                        val resp = response.body()!!
                        when(resp.code){
                            1000->chattingDeliveryCompleteView.chattingDeliveryCompleteSuccess()
                            else->chattingDeliveryCompleteView.chattingDeliveryCompleteFailure(resp.message)
                        }
                    }
                }

                override fun onFailure(call: Call<ChattingDeliveryCompleteResponse?>, t: Throwable) {
                    Log.d("deliveryAlarm", "실패"+ t.toString())
                    //TODO: 계속 타임아웃 오류나...
                }
            })
    }

    //배달파티 수동 매칭마감
    fun matchingEndSender(partyId: Int){
        Log.d("matchingEND","Bearer " + getJwt()+"/"+partyId.toString())
        chattingService?.matchingEnd(partyId)?.enqueue(object : Callback<MatchingEndResponse>{
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

    //채팅방 상세조회
    fun getChattingDetail(chatRoomId: String) {
        chattingService?.getChattingDetail(chatRoomId)?.enqueue(object: Callback<ChattingDetailResponse> {
            override fun onResponse(call: Call<ChattingDetailResponse>, response: Response<ChattingDetailResponse>) {
                Log.d("chatDetail", "채팅방 디테일 chatRoomId :${chatRoomId.toString()} / jwt : ${getJwt()}")
                Log.d("chatDetail", "채팅방 디테일 response :${response.toString()}")
                if (response.isSuccessful && response.code() == 200) {
                    val resp = response.body()!!
                    when (resp.code) {
                        1000 -> getChattingDetailView.getChattingDetailSuccess(resp.result)
                        else -> getChattingDetailView.getChattingDetailFailure(resp.code, resp.message)
                    }
                }
            }
            override fun onFailure(call: Call<ChattingDetailResponse>, t: Throwable) {
                Log.d("chatDetail", "채팅방 디테일 onFailure :${t.toString()}")
                Log.d("RETROFIT-SERVICE", "ChattingListService-getChattingDetail-Failure")
            }
        })
    }

    // 채팅방 유저 프로필 조회
    fun getChattingUserProfile(chatRoomId: String, memberId: Int) {
        chattingService?.getChattingUserProfile(chatRoomId, memberId)?.enqueue(object: Callback<ChattingUserProfileResponse> {
            override fun onResponse(
                call: Call<ChattingUserProfileResponse>,
                response: Response<ChattingUserProfileResponse>
            ) {
                if (response.isSuccessful && response.code() == 200) {
                    val resp = response.body()!!
                    when (resp.code) {
                        1000 -> chattingUserProfileView.getChattingUserProfileSuccess(resp.result)
                        else -> chattingUserProfileView.getChattingUserProfileFailure(resp.code, resp.message)
                    }
                }
            }
            override fun onFailure(call: Call<ChattingUserProfileResponse>, t: Throwable) {
                Log.d("RETROFIT-SERVICE", "ChattingRoomService-getChattingUserProfile-Failure")
            }
        })
    }
}
