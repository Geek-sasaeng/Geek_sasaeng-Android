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
    fun chattingMemberLeaveChatSuccess(result: String)
    fun chattingMemberLeaveChatFailure(code: Int, message: String)
}

interface ChattingLeaderLeavePartyView {
    fun chattingLeaderLeavePartySuccess(result: String, leaderMap: HashMap<String, String>)
    fun chattingLeaderLeavePartyFailure(code: Int, message: String)
}

interface ChattingLeaderLeaveChatView {
    fun chattingLeaderLeaveChatSuccess(result: String, leaderMap: HashMap<String, String>)
    fun chattingLeaderLeaveChatFailure(code: Int, message: String)
}

interface ChattingDeliveryComplicatedView {
    fun chattingDeliveryComplicatedSuccess()
    fun chattingDeliveryComplicatedFailure(message: String)
}

interface MatchingEndView {
    fun onMatchingEndSuccess()
    fun onMatchingEndFailure(message: String)
}