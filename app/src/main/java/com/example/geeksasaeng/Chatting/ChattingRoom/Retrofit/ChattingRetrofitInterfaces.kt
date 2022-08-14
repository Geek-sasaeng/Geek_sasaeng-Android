package com.example.geeksasaeng.Chatting.ChattingRoom.Retrofit

import retrofit2.Call
import retrofit2.http.*

interface ChattingRetrofitInterfaces {
    // 파티 멤버 나가기
    @PATCH("/delivery-party/leave")
    fun partyMemberChattingLeave(
        @Header("Authorization") jwt: String?,
        @Body chattingPartyLeaveRequest: ChattingPartyLeaveRequest
    ): Call<ChattingPartyLeaveResponse>

    // 파티장 나가기
    @PATCH("/delivery-party/cheif")
    fun partyLeaderChattingLeave(
        @Header("Authorization") jwt: String?,
        @Body chattingPartyLeaveRequest: ChattingPartyLeaveRequest
    ): Call<ChattingPartyLeaveResponse>
}