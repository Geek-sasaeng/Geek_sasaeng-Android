package com.example.geeksasaeng

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ApplicationClass: Application() {
    companion object {
        const val BASE_URL = "https://geeksasaeng.shop"
        const val TAG: String = "geeksasaeng-pref" // Log, SharedPreference

        lateinit var retrofit: Retrofit
        lateinit var mSharedPreferences: SharedPreferences
    }

    override fun onCreate() {
        super.onCreate()

        //레트로핏
        retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            /*.client(client)*/
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        mSharedPreferences = applicationContext.getSharedPreferences(TAG, Context.MODE_PRIVATE)
    }
}