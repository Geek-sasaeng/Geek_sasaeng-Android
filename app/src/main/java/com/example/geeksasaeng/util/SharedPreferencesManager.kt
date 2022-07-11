package com.example.geeksasaeng.util

import com.example.geeksasaeng.ApplicationClass.Companion.mSharedPreferences
import com.google.gson.Gson

fun removeUuid(){
    val editor = mSharedPreferences.edit()
    editor.remove("uuid")
    editor.commit()
}
fun saveUuid(uuid: String) {
    val editor = mSharedPreferences.edit()
    editor.putString("uuid", uuid)
    editor.apply()
}

fun getUuid(): String? = mSharedPreferences.getString("uuid", null)
