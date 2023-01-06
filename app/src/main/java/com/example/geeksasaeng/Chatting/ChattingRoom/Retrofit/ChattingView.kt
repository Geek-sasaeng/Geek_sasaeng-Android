package com.example.geeksasaeng.Chatting.ChattingRoom.Retrofit

interface CreateChattingView {
    fun createChattingSuccess(result: CreateChattingResult)
    fun createChattingFailure(code: Int, message: String)
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

interface SendChattingView {
    fun sendChattingSuccess(result: String)
    fun sendChattingFailure(code: Int, message: String)
}

interface ChattingMemberLeaveView {
    fun chattingMemberLeaveSuccess(result: String)
    fun chattingMemberLeaveFailure(code: Int, message: String)
}

interface ChattingLeaderLeaveView {
    fun chattingLeaderLeaveSuccess(result: String, leaderMap: HashMap<String, String>)
    fun chattingLeaderLeaveFailure(code: Int, message: String)
}

interface ChattingDeliveryComplicatedView {
    fun chattingDeliveryComplicatedSuccess()
    fun chattingDeliveryComplicatedFailure(message: String)
}

interface MatchingEndView {
    fun onMatchingEndSuccess()
    fun onMatchingEndFailure(message: String)
}