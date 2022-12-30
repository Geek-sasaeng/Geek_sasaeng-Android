package com.example.geeksasaeng.Chatting.ChattingRoom.Retrofit

import retrofit2.Call
import retrofit2.http.*

interface ChattingRetrofitInterfaces {
    // 채팅방 생성
    @POST("/party-chat-room")
    fun createChatting(
        @Header("Authorization") jwt: String?,
        @Body createChattingRequest: CreateChattingRequest
    ): Call<CreateChattingResponse>

    // 채팅방 멤버 추가
    @POST("/party-chat-room/member")
    fun chattingMemberAdd(
        @Header("Authorization") jwt: String,
        @Body chattingMemberAddRequest: ChattingMemberAddRequest
    ): Call<ChattingMemberAddResponse>

    // 파티 멤버 나가기
    @PATCH("/delivery-party/member")
    fun partyMemberChattingLeave(
        @Body chattingPartyMemeberLeaveRequest: ChattingPartyMemberLeaveRequest
    ): Call<ChattingPartyMemberLeaveResponse>

    // 파티장 나가기
    @PATCH("/delivery-party/chief")
    fun partyLeaderChattingLeave(
        @Body chattingPartyMemberLeaveRequest: ChattingPartyLeaderLeaveRequest
    ): Call<ChattingPartyLeaderLeaveResponse>

    //배달 완료 알림 보내기
    @POST("/delivery-party/complicated")
    fun partyDeliveryComplicated(
        @Body chattingDeliveryComplicatedRequest: ChattingDeliveryComplicatedRequest
    ): Call<ChattingDeliveryComplicatedResponse>

    //배달파티 수동 매칭마감
    @PATCH("/delivery-party/{roomUuid}/matching-status")
    fun matchingEnd(
        @Path("roomUuid") roomUuid: String
    ): Call<MatchingEndResponse>

}