package com.example.geeksasaeng.ChatSetting

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "ChatTable")
data class Chat(
    @PrimaryKey val chatId: String,
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
    var isImageMessage: Boolean,
    var viewType: Int = 0,
    var isLeader: Boolean
)
