package com.example.geeksasaeng.Chatting.ChattingList.Retrofit

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path
import retrofit2.http.Query

interface ChattingListRetrofitInterfaces {
    @GET("/party-chat-room")
    fun getChattingList(
        @Query("cursor") cursor: Int
    ): Call<ChattingListResponse>

    @GET("/party-chat-room/{chatRoomId}")
    fun getChattingDetail(
        @Path("chatRoomId") chatRoomId: String
    ): Call<ChattingDetailResponse>
}