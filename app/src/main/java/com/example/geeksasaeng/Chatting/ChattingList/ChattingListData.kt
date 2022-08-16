package com.example.geeksasaeng.Chatting.ChattingList

data class ChattingListData(
    val roomName: String,
    val roomUuid: String,
    val roomImgUrl: String,
    val lastChat: String,
    val lastTime: String,
    val newMsg: String,
    val updatedAt : String
)

data class ParticipantsInfo(
    val enterTime : String,
    val participant: String,
    /*val isRemittance: Boolean*/
)
