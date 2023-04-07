package com.example.geeksasaeng.Chatting.ChattingRoom.Retrofit

import com.google.gson.annotations.SerializedName
import java.io.Serializable
import java.util.ArrayList

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

// 강제퇴장을 위한 조회 api
// 강제퇴장

data class PreChattingMemberForcedExitResponse(
    @SerializedName("code") val code: Int,
    @SerializedName("isSuccess") val isSuccess : Boolean,
    @SerializedName("message") val message: String,
    @SerializedName("result") val result: Array<PreChattingMemberForcedExitResult>
)

data class PreChattingMemberForcedExitResult(
    @SerializedName("chatMemberId") val chatMemberId: String,
    @SerializedName("memberId") val memberId: Int,
    @SerializedName("userName") val userName: String,
    @SerializedName("userProfileImgUrl") val userProfileImgUrl: String
)

// 강제퇴장 for 채팅방
data class ChattingMemberForcedExitRequest(
    @SerializedName("removedChatMemberIdList") val removedChatMemberIdList: ArrayList<String>,
    @SerializedName("roomId") val roomId: String
)

data class ChattingMemberForcedExitResponse(
    @SerializedName("code") val code: Int,
    @SerializedName("isSuccess") val isSuccess : Boolean,
    @SerializedName("message") val message: String,
    @SerializedName("result") val result: ChattingMemberForcedExitResult
)

data class ChattingMemberForcedExitResult(
    @SerializedName("message") val message: String
)

// 강제퇴장 for 배달파티
data class DeliveryPartyMemberForcedExitRequest(
    @SerializedName("membersId") val membersId: ArrayList<Int>,
    @SerializedName("partyId") val partyId: Int
)

data class DeliveryPartyMemberForcedExitResponse(
    @SerializedName("code") val code: Int,
    @SerializedName("isSuccess") val isSuccess : Boolean,
    @SerializedName("message") val message: String,
    @SerializedName("result") val result: DeliveryPartyMemberForcedExitResult
)

data class DeliveryPartyMemberForcedExitResult(
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

//파티 멤버 나가기 for 배달파티
data class ChattingPartyMemberLeavePartyRequest(
    @SerializedName("partyId") val partyId: Int
)

data class ChattingPartyMemberLeavePartyResponse(
    @SerializedName("code") val code: Int,
    @SerializedName("isSuccess") val isSuccess : Boolean,
    @SerializedName("message") val message: String,
    @SerializedName("result") val result: String
)

//파티 멤버 나가기 for 채팅방
data class ChattingPartyMemberLeaveChatRequest(
    @SerializedName("roomId") val roomId: String
)

data class ChattingPartyMemberLeaveChatResponse(
    @SerializedName("code") val code: Int,
    @SerializedName("isSuccess") val isSuccess : Boolean,
    @SerializedName("message") val message: String,
    @SerializedName("result") val result: ChattingPartyMemberLeaveChatResult
)


data class ChattingPartyMemberLeaveChatResult(
    @SerializedName("message") val message: String
)

//방장 나가기 for 배달파티
data class ChattingPartyLeaderLeavePartyRequest(
    @SerializedName("nickName") val nickName: String,
    @SerializedName("partyId") val partyId: Int
)

data class ChattingPartyLeaderLeavePartyResponse(
    @SerializedName("code") val code: Int,
    @SerializedName("isSuccess") val isSuccess : Boolean,
    @SerializedName("message") val message: String,
    @SerializedName("result") val result: ChattingPartyLeaderLeavePartyResult
)

data class ChattingPartyLeaderLeavePartyResult(
    @SerializedName("result") val result: String
)

//방장 나가기 for 채팅방
data class ChattingPartyLeaderLeaveChatRequest(
    @SerializedName("roomId") val roomId: String,
)

data class ChattingPartyLeaderLeaveChatResponse(
    @SerializedName("code") val code: Int,
    @SerializedName("isSuccess") val isSuccess : Boolean,
    @SerializedName("message") val message: String,
    @SerializedName("result") val result: ChattingPartyLeaderLeaveChatResult
)

data class ChattingPartyLeaderLeaveChatResult(
    @SerializedName("message") val message: String
)

//배달완료 알림보내기
data class ChattingDeliveryCompleteRequest(
    @SerializedName("roomId") val roomId: String
)

data class ChattingDeliveryCompleteResponse(
    @SerializedName("code") val code: Int,
    @SerializedName("isSuccess") val isSuccess : Boolean,
    @SerializedName("message") val message: String,
    @SerializedName("result") val result: String
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
    @SerializedName("chatMemberId") val chatMemberId: String,
    @SerializedName("memberId") val memberId: Int,
    @SerializedName("userName") val userName: String,
    @SerializedName("userProfileImgUrl") val userProfileImgUrl: String,
    @SerializedName("accountTransferStatus") val accountTransferStatus: Boolean
): Serializable

//채팅방 상세조회
data class ChattingDetailResponse(
    @SerializedName("code") val code: Int,
    @SerializedName("isSuccess") val isSuccess : Boolean,
    @SerializedName("message") val message: String,
    @SerializedName("result") val result: ChattingDetailResult
)

data class ChattingDetailResult(
    @SerializedName("accountNumber") val accountNumber: String,
    @SerializedName("bank") val bank: String,
    @SerializedName("chiefId") val chiefId: Int,
    @SerializedName("enterTime") val enterTime: String,
    @SerializedName("isChief") val isChief: Boolean,
    @SerializedName("isMatchingFinish") val isMatchingFinish: Boolean,
    @SerializedName("isOrderFinish") val isOrderFinish: Boolean,
    @SerializedName("isRemittanceFinish") val isRemittanceFinish: Boolean,
    @SerializedName("partyId") val partyId: Int
)

data class ChattingUserProfileResponse(
    @SerializedName("code") val code: Int,
    @SerializedName("isSuccess") val isSuccess : Boolean,
    @SerializedName("message") val message: String,
    @SerializedName("result") val result: ChattingUserProfileResult
)

data class ChattingUserProfileResult(
    @SerializedName("grade") val grade: String,
    @SerializedName("isChief") val isChief: Boolean,
    @SerializedName("userName") val userName: String
)