package com.example.geeksasaeng.Utils

import com.example.geeksasaeng.Utils.ApplicationClass.Companion.mSharedPreferences

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
