package com.example.geeksasaeng.Chatting.ChattingList

data class ChattingList(
    var isFirst: Boolean,
    val roomName: String,
    val roomImgUrl: String,
    val lastChat: String,
    val lastTime: String,
    val newMsg: String
)