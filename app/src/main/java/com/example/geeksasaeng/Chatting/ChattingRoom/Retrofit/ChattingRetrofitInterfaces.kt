package com.example.geeksasaeng.Chatting.ChattingRoom.Retrofit

import retrofit2.Call
import retrofit2.http.*

interface ChattingRetrofitInterfaces {
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

}