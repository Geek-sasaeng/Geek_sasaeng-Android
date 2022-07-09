package com.example.geeksasaeng

import android.app.Application
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ApplicationClass: Application() {
    companion object {
        const val BASE_URL = "https://geeksasaeng.shop"
        lateinit var retrofit: Retrofit
    }

    override fun onCreate() {
        super.onCreate()

        //레트로핏
        retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            /*.client(client)*/
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    }
}