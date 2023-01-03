package com.example.geeksasaeng.Chatting.ChattingRoom.Retrofit

import com.example.geeksasaeng.Home.Delivery.DeliveryBannerResult
import com.google.gson.annotations.SerializedName

data class CreateChattingRequest(
    @SerializedName("accountNumber") val accountNumber: String,
    @SerializedName("bank") val bank: String,
    @SerializedName("category") val category: String,
    @SerializedName("deliveryPartyId") val deliveryPartyId: Int,
    @SerializedName("maxMatching") val maxMatching :Int,
    @SerializedName("title") val title: String
)

data class CreateChattingResponse(
    @SerializedName("code") val code: Int,
    @SerializedName("isSuccess") val isSuccess : Boolean,
    @SerializedName("message") val message: String,
    @SerializedName("result") val result: CreateChattingResult
)

data class CreateChattingResult(
    @SerializedName("partyChatRoomId") val partyChatRoomId: String,
    @SerializedName("title") val title: String
)

data class SendChattingRequest(
    @SerializedName("chatId") val chatId: String?,
    @SerializedName("chatRoomId") val chatRoomId: String,
    @SerializedName("chatType") val chatType: String?,
    @SerializedName("content") val content: String,
    @SerializedName("isSystemMessage") val isSystemMessage: Boolean?,
    @SerializedName("jwt") val jwt: String?,
    @SerializedName("memberId") val memberId: String?,
    @SerializedName("profileImgUrl") val profileImgUrl: String?,
)

data class SendChattingResponse(
    @SerializedName("code") val code: Int,
    @SerializedName("isSuccess") val isSuccess: Boolean,
    @SerializedName("message") val message: String,
    @SerializedName("result") val result: String
)

data class ChattingMemberAddRequest(
    @SerializedName("partyChatRoomId") val partyChatRoomId: String
)

data class ChattingMemberAddResponse(
    @SerializedName("code") val code: Int,
    @SerializedName("isSuccess") val isSuccess : Boolean,
    @SerializedName("message") val message: String,
    @SerializedName("result") val result: ChattingMemberAddResult
)

data class ChattingMemberAddResult(
    @SerializedName("enterTime") val enterTime: String,
    @SerializedName("partyChatRoomId") val partyChatRoomId: String,
    @SerializedName("partyChatRoomMemebrId") val partyChatRoomMemberId: String,
    @SerializedName("remittance") val remittance: Boolean
)

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

