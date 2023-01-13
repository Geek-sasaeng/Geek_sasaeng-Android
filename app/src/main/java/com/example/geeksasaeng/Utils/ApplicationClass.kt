package com.example.geeksasaeng.Utils

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.*
import java.util.concurrent.TimeUnit

class ApplicationClass: Application() {
    companion object {
        const val Authorization_TOKEN: String = "Authorization"         // JWT Token Key
        const val BASE_URL = "https://geeksasaeng.shop"
        //const val BASE_URL = "http://192.168.1.2:8080"
        const val TAG: String = "geeksasaeng-pref" // Log, SharedPreference

        lateinit var retrofit: Retrofit
        lateinit var mSharedPreferences: SharedPreferences
    }

    override fun onCreate() {
        super.onCreate()

        val client: OkHttpClient = OkHttpClient.Builder()
            .readTimeout(30000, TimeUnit.MILLISECONDS)
            .connectTimeout(30000, TimeUnit.MILLISECONDS)
            .addNetworkInterceptor(AuthorizationTokenInterceptor()) // JWT 자동 헤더 전송
            .build()

        //레트로핏
        retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        //SharedPreference
        mSharedPreferences = applicationContext.getSharedPreferences(TAG, Context.MODE_PRIVATE)
        if(getUuid()==null){ //uuid가 존재하지 않으면, (uuid는 최초1회만 생성하면 되기 때문에)
            val uuid = UUID.randomUUID().toString() //uuid 생성
            saveUuid(uuid) // sharedpref에 저장
        }
    }
}