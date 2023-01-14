package com.example.geeksasaeng.ChatSetting.ChatRoomDB

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.geeksasaeng.ChatSetting.ChatResponse

@Dao
interface ChatDao {
    @Insert
    fun insert(chat: ChatResponse)

    @Query("Select * from ChatTable where chatRoomId = :chatRoomId")
    fun getRoomChats(chatRoomId: String): List<ChatResponse>

    @Query("Select * from ChatTable")
    fun getAllChats(): List<ChatResponse>
}