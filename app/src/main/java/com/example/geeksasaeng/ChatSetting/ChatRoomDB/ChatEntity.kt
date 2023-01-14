package com.example.geeksasaeng.ChatSetting

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "ChatTable")
data class Chat(
    @PrimaryKey val chatId: String,
    @ColumnInfo(name = "content") val content: String?,
    @ColumnInfo(name = "chatRoomId") val chatRoomId: String,
    @ColumnInfo(name = "isSystemMessage") val isSystemMessage: Boolean,
    @ColumnInfo(name = "memberId") val memberId: Int,
    @ColumnInfo(name = "nickName") val nickName: String,
    @ColumnInfo(name = "profileImgUrl") val profileImgUrl: String?,
    @ColumnInfo(name = "readMembers") val readMembers: ArrayList<String>,
    @ColumnInfo(name = "createdAt") val createdAt: String,
    @ColumnInfo(name = "chatType") val chatType: String,
    @ColumnInfo(name = "unreadMemberCnt") val unreadMemberCnt: Int,
    @ColumnInfo(name = "isImageMessage") val isImageMessage: Boolean,
    @ColumnInfo(name = "viewType") val viewType: Int,
    @ColumnInfo(name = "isLeader") val isLeader: Boolean
)