package com.example.geeksasaeng.ChatSetting

data class ChatRequest (
    var content: String,
    var chatRoomId: String,
    var isSystemMessage: Boolean?,
    var memberId: Int?,
    var profileImgUrl: String?,
    var chatType: String?,
    var chatId: String?,
    var jwt: String?
)

data class ChatResponse (
    var chatId: String,
    var content: String?,
    var chatRoomId: String,
    var isSystemMessage: Boolean,
    var memberId: Int,
    var nickName: String,
    var profileImgUrl: String?,
    var readMembers: ArrayList<Int>,
    var createdAt: String,
    var chatType: String,
    var unreadMemberCnt: Int,
    var isImageMessage: Boolean
)
