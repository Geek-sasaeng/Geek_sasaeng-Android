package com.example.geeksasaeng.Chatting.ChattingRoom.Retrofit

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class CreateChattingRoomRequest(
    @SerializedName("accountNumber") val accountNumber: String,
    @SerializedName("bank") val bank: String,
    @SerializedName("category") val category: String,
    @SerializedName("deliveryPartyId") val deliveryPartyId: Int,
    @SerializedName("maxMatching") val maxMatching :Int,
    @SerializedName("title") val title: String
)

data class CreateChattingRoomResponse(
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
    @SerializedName("memberId") val memberId: Int?,
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

data class ChattingMemberForcedExitRequest(
    @SerializedName("removedMemberId") val removedMemberId: String,
    @SerializedName("roomId") val roomId: String
)

// 강제퇴장
data class ChattingMemberForcedExitResponse(
    @SerializedName("code") val code: Int,
    @SerializedName("isSuccess") val isSuccess : Boolean,
    @SerializedName("message") val message: String,
    @SerializedName("result") val result: ChattingMemberForcedExitResult
)

data class ChattingMemberForcedExitResult(
    @SerializedName("message") val message: String
)

// 주문 완료
data class ChattingOrderCompleteResponse(
    @SerializedName("code") val code: Int,
    @SerializedName("isSuccess") val isSuccess : Boolean,
    @SerializedName("message") val message: String,
    @SerializedName("result") val result: String
)

data class ChattingOrderCompleteRequest(
    @SerializedName("roomId") val roomId: String
)

// 송금 완료
data class ChattingRemittanceCompleteResponse(
    @SerializedName("code") val code: Int,
    @SerializedName("isSuccess") val isSuccess : Boolean,
    @SerializedName("message") val message: String,
    @SerializedName("result") val result: String
)

data class ChattingRemittanceCompleteRequest(
    @SerializedName("roomId") val roomId: String
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

//배달파티 수동 매칭마감
data class MatchingEndResponse(
    @SerializedName("code") val code: Int,
    @SerializedName("isSuccess") val isSuccess : Boolean,
    @SerializedName("message") val message: String,
    @SerializedName("result") val result: MatchingEndResult?
)

data class MatchingEndResult(
    @SerializedName("deliveryPartyId") val deliveryPartyId: Int,
    @SerializedName("matchingStatus") val matchingStatus: String
)

data class MemberData(
    @SerializedName("nickName") val nickName: String,
    @SerializedName("profileImgUrl") val profileImgUrl: String,
    @SerializedName("accountTransferStatus") val accountTransferStatus: Boolean
): Serializable
