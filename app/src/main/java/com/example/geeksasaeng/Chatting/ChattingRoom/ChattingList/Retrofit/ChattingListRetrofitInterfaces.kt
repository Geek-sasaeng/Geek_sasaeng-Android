package com.example.geeksasaeng.Chatting.ChattingRoom.ChattingList.Retrofit

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface ChattingListRetrofitInterfaces {
    @GET("/party-chat-room")
    fun getChattingList(
        @Header("Authorization") jwt: String,
        @Query("cursor") cursor: Int
    ): Call<ChattingListResponse>
}