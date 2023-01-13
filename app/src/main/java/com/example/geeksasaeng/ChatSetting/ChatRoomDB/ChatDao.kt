package com.example.geeksasaeng.ChatSetting.ChatRoomDB

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.geeksasaeng.ChatSetting.Chat

@Dao
interface ChatDao {
    @Insert
    fun insertChat(chat: Chat)

    @Query("Select * from ChatTable where chatRoomId = :chatRoomId")
    fun getAllChats(chatRoomId: String): ArrayList<Chat>
}