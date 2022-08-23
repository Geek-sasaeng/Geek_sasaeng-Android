package com.example.geeksasaeng.Chatting.ChattingRoom.Retrofit

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