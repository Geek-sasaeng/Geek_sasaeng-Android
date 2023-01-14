package com.example.geeksasaeng.ChatSetting.ChatRoomDB

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.geeksasaeng.ChatSetting.Chat

@Database(entities = [Chat::class], version = 1)
@TypeConverters(DataTypeConverters::class)
abstract class ChatDatabase: RoomDatabase() {
    abstract fun chatDao(): ChatDao

    companion object {
        private var instance : ChatDatabase? = null

        @Synchronized
        fun getDBInstance(context: Context) : ChatDatabase? {
            if (instance == null) {
                synchronized(ChatDatabase::class) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        ChatDatabase::class.java,
                        "user-database"
                    ).fallbackToDestructiveMigration().build()
                }
            }
            return instance
        }
    }
}