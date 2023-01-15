package com.example.geeksasaeng.ChatSetting

import androidx.room.Entity
import androidx.room.PrimaryKey

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
    var readMembers: ArrayList<String>,
    var createdAt: String,
    var chatType: String,
    var unreadMemberCnt: Int,
    var isImageMessage: Boolean,
    var viewType: Int = 0,
    var isLeader: Boolean
)

const val myChatting = 1
const val yourChatting = 2
const val systemChatting = 3
const val imageChatting = 4