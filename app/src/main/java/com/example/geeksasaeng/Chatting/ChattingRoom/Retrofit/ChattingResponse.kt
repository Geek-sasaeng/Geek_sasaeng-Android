package com.example.geeksasaeng.Chatting.ChattingRoom.Retrofit

import com.example.geeksasaeng.Home.Delivery.DeliveryBannerResult
import com.google.gson.annotations.SerializedName

data class ChattingPartyLeaveRequest(
    @SerializedName("uuid") val uuid: String,
)

data class ChattingPartyLeaveResponse(
    @SerializedName("code") val code: Int,
    @SerializedName("isSuccess") val isSuccess : Boolean,
    @SerializedName("message") val message: String,
    @SerializedName("result") val result: String
)

