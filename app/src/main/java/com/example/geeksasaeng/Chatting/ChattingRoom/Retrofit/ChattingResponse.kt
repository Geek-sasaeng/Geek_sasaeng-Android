package com.example.geeksasaeng.Chatting.ChattingRoom.Retrofit

import com.example.geeksasaeng.Home.Delivery.DeliveryBannerResult
import com.google.gson.annotations.SerializedName

data class ChattingPartyMemberLeaveRequest(
    @SerializedName("uuid") val uuid: String,
)

data class ChattingPartyMemberLeaveResponse(
    @SerializedName("code") val code: Int,
    @SerializedName("isSuccess") val isSuccess : Boolean,
    @SerializedName("message") val message: String,
    @SerializedName("result") val result: String
)

data class ChattingPartyLeaderLeaveRequest(
    @SerializedName("nickName") val nickName: String?,
    @SerializedName("uuid") val uuid: String
)

data class ChattingPartyLeaderLeaveResult(
    @SerializedName("result") val result: String
)

data class ChattingPartyLeaderLeaveResponse(
    @SerializedName("code") val code: Int,
    @SerializedName("isSuccess") val isSuccess : Boolean,
    @SerializedName("message") val message: String,
    @SerializedName("result") val result: ChattingPartyLeaderLeaveResult
)

//배달완료 알림보내기
data class ChattingDeliveryComplicatedRequest(
    @SerializedName("uuid") val uuid: String
)

data class ChattingDeliveryComplicatedResponse(
    @SerializedName("code") val code: Int,
    @SerializedName("isSuccess") val isSuccess : Boolean,
    @SerializedName("message") val message: String,
    @SerializedName("result") val result: ChattingDeliveryComplicatedResult
)

data class ChattingDeliveryComplicatedResult(
    @SerializedName("body") val body: ArrayList<Any>,
    @SerializedName("statusCode") val statusCode: String,
    @SerializedName("statusCodeValue") val statusCodeValue: Int

)

