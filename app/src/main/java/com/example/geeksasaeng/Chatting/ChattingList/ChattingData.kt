package com.example.geeksasaeng.Chatting.ChattingList

data class ChattingData(
    val roomData: RoomData,
    var lastChat: String,
    var lastMsgTime: String,
    var newMsg: String
    )


data class ParticipantsInfo(
    val enterTime : String,
    val participant: String,
    val isRemittance: Boolean
)

data class RoomData(
    val roomName: String,
    val roomUuid: String,
    val roomImgUrl: String
)
