package com.example.geeksasaeng.Utils

import androidx.appcompat.app.AppCompatActivity
import com.example.geeksasaeng.Utils.ApplicationClass.Companion.mSharedPreferences

// 기숙사 정보
fun removeDormitory(){
    val editor = mSharedPreferences.edit()
    editor.remove("dormitory")
    editor.commit()
}

fun saveDormitory(dorm: String){
    val editor = mSharedPreferences.edit()
    editor.putString("dormitory", dorm)
    editor.apply()
}

// uuid
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

// 자동로그인
fun removeAutoLogin() {
    val editor = mSharedPreferences.edit()
    editor.remove("jwt")
    editor.remove("loginId")
    editor.remove("password")
    editor.commit()
}
fun saveJwt(jwt: String){
    val editor = mSharedPreferences.edit()
    editor.putString("jwt", jwt)
    editor.apply()
}

fun saveAutoLogin(jwt: String, loginId: String, password: String) {
    val editor = mSharedPreferences.edit()
    editor.putString("jwt", jwt)
    editor.putString("loginId", loginId)
    editor.putString("password", password)
    editor.apply()
}

fun saveSocialAutoLogin(jwt: String) {
    val editor = mSharedPreferences.edit()
    editor.putString("jwt", jwt)
    editor.apply()
}

fun getDormitory(): String? = mSharedPreferences.getString("dormitory", null)
fun getJwt(): String? = mSharedPreferences.getString("jwt", null)
fun getLoginId(): String? = mSharedPreferences.getString("loginId", null)
fun getPassword(): String? = mSharedPreferences.getString("password", null)