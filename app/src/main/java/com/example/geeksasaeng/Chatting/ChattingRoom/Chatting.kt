package com.example.geeksasaeng.Chatting.ChattingRoom

data class Chatting(
    val viewType: Int = 0,
    val sender: Boolean?,
    // TODO: senderImgUrl String으로 변경해주기!
    // val senderImgUrl: String?,
    val senderImgUrl: Int?,
    val message: String,
    val notRead: Int?
)

const val myChatting = 1
const val yourChatting = 2
const val systemChatting = 3