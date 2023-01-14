package com.example.geeksasaeng.ChatSetting

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "ChatTable")
data class Chat(
    @PrimaryKey val chatId: String,
    val content: String?,
    val chatRoomId: String,
    val isSystemMessage: Boolean,
    val memberId: Int,
    val nickName: String,
    val profileImgUrl: String?,
    val readMembers: ArrayList<String>,
    val createdAt: String,
    val chatType: String,
    val unreadMemberCnt: Int,
    val isImageMessage: Boolean,
    val viewType: Int,
    val isLeader: Boolean
)