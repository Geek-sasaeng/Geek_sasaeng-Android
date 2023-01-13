package com.example.geeksasaeng.ChatSetting.ChatRoomDB

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.geeksasaeng.ChatSetting.Chat

@Database(entities = [Chat::class], version = 1)
abstract class ChatDB: RoomDatabase() {
    abstract fun ChatDao(): ChatDao

    companion object {
        private var instance : ChatDB? = null

        @Synchronized
        fun getDBInstance(context: Context) : ChatDB? {
            if (instance == null) {
                synchronized(ChatDB::class) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        ChatDB::class.java,
                        "user-database"
                    ).fallbackToDestructiveMigration().build()
                }
            }
            return instance
        }
    }
}