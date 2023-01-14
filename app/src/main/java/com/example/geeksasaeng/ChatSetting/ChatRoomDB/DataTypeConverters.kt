package com.example.geeksasaeng.ChatSetting.ChatRoomDB

import androidx.room.TypeConverter
import com.example.geeksasaeng.ChatSetting.ChatResponse
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class DataTypeConverters {
    /*
    @TypeConverter
    fun listToJson(value: List<ChatResponse>?): String? {
        return Gson().toJson(value)
    }

    @TypeConverter
    fun jsonToList(value: String): List<ChatResponse>? {
        return Gson().fromJson(value,Array<ChatResponse>::class.java)?.toList()
    }
     */

    @TypeConverter
    fun fromString(value: String?): ArrayList<String?>? {
        val listType = object : TypeToken<ArrayList<String?>?>() {}.type
        return Gson().fromJson(value, listType)
    }

    @TypeConverter
    fun fromArrayList(list: ArrayList<String?>?): String? {
        val gson = Gson()
        return gson.toJson(list)
    }
}