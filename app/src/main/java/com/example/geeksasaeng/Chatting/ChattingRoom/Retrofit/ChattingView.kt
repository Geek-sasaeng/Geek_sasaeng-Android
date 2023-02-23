package com.example.geeksasaeng.Chatting.ChattingRoom.Retrofit


interface CreateChattingRoomView {
    fun createChattingRoomSuccess(result: CreateChattingResult)
    fun createChattingRoomFailure(code: Int, message: String)
}

interface ChattingMemberAddView {
    fun chattingMemberAddSuccess(result: ChattingMemberAddResult)
    fun chattingMemberAddFailure(code: Int, message: String)
}

interface ChattingMemberForcedExitView {
    fun chattingMemberForcedExitSuccess(result: ChattingMemberForcedExitResult)
    fun chattingMemberForcedExitFailure(code: Int, message: String)
}

interface ChattingOrderCompleteView {
    fun chattingOrderCompleteSuccess(result: String)
    fun chattingOrderCompleteFailure(code: Int, message: String)
}

interface ChattingRemittanceCompleteView {
    fun chattingRemittanceCompleteSuccess(result: String)
    fun chattingRemittanceCompleteFailure(code: Int, message: String)
}

interface SendChattingView {
    fun sendChattingSuccess(result: String)
    fun sendChattingFailure(code: Int, message: String)
}

interface ChattingMemberLeavePartyView {
    fun chattingMemberLeavePartySuccess(result: String)
    fun chattingMemberLeavePartyFailure(code: Int, message: String)
}

interface ChattingMemberLeaveChatView {
    fun chattingMemberLeaveChatSuccess(result: ChattingPartyMemberLeaveChatResult)
    fun chattingMemberLeaveChatFailure(code: Int, message: String)
}

interface ChattingLeaderLeavePartyView {
    fun chattingLeaderLeavePartySuccess(result: ChattingPartyLeaderLeavePartyResult)
    fun chattingLeaderLeavePartyFailure(code: Int, message: String)
}

interface ChattingLeaderLeaveChatView {
    fun chattingLeaderLeaveChatSuccess(result: ChattingPartyLeaderLeaveChatResult)
    fun chattingLeaderLeaveChatFailure(code: Int, message: String)
}

interface ChattingDeliveryCompleteView {
    fun chattingDeliveryCompleteSuccess()
    fun chattingDeliveryCompleteFailure(message: String)
}

interface MatchingEndView {
    fun onMatchingEndSuccess()
    fun onMatchingEndFailure(message: String)
}

interface ChattingDetailView {
    fun getChattingDetailSuccess(result: ChattingDetailResult)
    fun getChattingDetailFailure(code: Int, msg: String)
}

interface ChattingUserProfileView {
    fun getChattingUserProfileSuccess(result: ChattingUserProfileResult)
    fun getChattingUserProfileFailure(code: Int, msg: String)
}