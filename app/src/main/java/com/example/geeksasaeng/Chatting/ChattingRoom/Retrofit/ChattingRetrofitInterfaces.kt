package com.example.geeksasaeng.Chatting.ChattingRoom.Retrofit

import retrofit2.Call
import retrofit2.http.*

interface ChattingRetrofitInterfaces {

    // <<party-chat-room>> //
    // 채팅방 생성
    @POST("/party-chat-room")
    fun createChattingRoom(
        @Body createChattingRequest: CreateChattingRoomRequest
    ): Call<CreateChattingRoomResponse>

    // 채팅방 멤버 추가
    @POST("/party-chat-room/member")
    fun chattingMemberAdd(
        @Body chattingMemberAddRequest: ChattingMemberAddRequest
    ): Call<ChattingMemberAddResponse>

    // 채팅 전송
    @POST("/party-chat-room/chat")
    fun sendChatting(
        @Body sendChattingRequest: SendChattingRequest
    ): Call<SendChattingResponse>

    //방장이 배달 파티 채팅 멤버를 강제퇴장
    @DELETE("/party-chat-room/members")
    fun chattingMemberForcedExit(
        @Body chattingMemberForcedExitRequest: ChattingMemberForcedExitRequest
    ): Call<ChattingMemberForcedExitResponse>

    //주문 완료
    @PATCH("/party-chat-room/order")
    fun chattingOrderComplete(
        @Body chattingOrderCompleteRequest: ChattingOrderCompleteRequest
    ): Call<ChattingOrderCompleteResponse>

    //송금 완료
    @PATCH("/party-chat-room/members/remittance")
    fun chattingRemittanceComplete(
        @Body chattingRemittanceCompleteRequest: ChattingRemittanceCompleteRequest
    ): Call<ChattingRemittanceCompleteResponse>

    // <<delivery-party>>//
    // 파티 멤버 나가기 for 배달파티
    @PATCH("/delivery-party/member")
    fun partyMemberPartyLeave(
        @Body chattingPartyMemberLeavePartyRequest: ChattingPartyMemberLeavePartyRequest
    ): Call<ChattingPartyMemberLeavePartyResponse>

    //배달 파티멤버가 스스로 퇴장 for 채팅방
    @HTTP(method = "DELETE", path = "/party-chat-room/members/self", hasBody = true)
    fun partyMemberChattingLeave(
        @Body chattingPartyMemberLeaveChatRequest: ChattingPartyMemberLeaveChatRequest
    ): Call<ChattingPartyMemberLeaveChatResponse>

    // 배달파티 방장 삭제 및 교체 for 배달파티
    @PATCH("/delivery-party/chief")
    fun partyLeaderPartyLeave(
        @Body chattingPartyLeaderLeavePartyRequest: ChattingPartyLeaderLeavePartyRequest
    ): Call<ChattingPartyLeaderLeavePartyResponse>

    // 배달파티 방장 삭제 및 교체 for 채팅방
    @PATCH("/party-chat-room/chief")
    fun partyLeaderChattingLeave(
        @Body chattingPartyLeaderLeaveChatRequest: ChattingPartyLeaderLeaveChatRequest
    ): Call<ChattingPartyLeaderLeaveChatResponse>

    //배달 완료 알림
    @PATCH("/party-chat-room/delivery-complete")
    fun partyDeliveryComplete(
        @Body chattingDeliveryCompleteRequest: ChattingDeliveryCompleteRequest
    ): Call<ChattingDeliveryCompleteResponse>

    //배달파티 수동 매칭마감
    @PATCH("/delivery-party/{partyId}/matching-status")
    fun matchingEnd(
        @Path("partyId") partyId: Int
    ): Call<MatchingEndResponse>

    //채팅방 상세조회
    @GET("/party-chat-room/{chatRoomId}")
    fun getChattingDetail(
        @Path("chatRoomId") chatRoomId: String
    ): Call<ChattingDetailResponse>

}