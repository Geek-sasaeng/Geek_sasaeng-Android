package com.example.geeksasaeng.ChatSetting.ChatRoomDB

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.geeksasaeng.ChatSetting.Chat
import com.example.geeksasaeng.ChatSetting.ChatResponse

@Dao
interface ChatDao {
    @Insert
    fun insert(chat: Chat)

    @Query("Select * from ChatTable where chatRoomId = :chatRoomId")
    fun getRoomChats(chatRoomId: String): List<Chat>

    @Query("Select * from ChatTable")
    fun getAllChats(): List<Chat>

    @Query("Delete from ChatTable")
    fun deleteAllChats()
}