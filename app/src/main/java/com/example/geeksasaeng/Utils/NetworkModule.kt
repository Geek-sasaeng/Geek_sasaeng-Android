package com.example.geeksasaeng.Utils

import android.util.Log
import com.example.geeksasaeng.Config.Secret.AuthorizationTokenInterceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class NetworkModule {
    companion object {
        // retrofit 변수를 외부에서 업데이트할 수 없도록 방지
        private var retrofit: Retrofit? = null

        // retrofit 객체를 가져오는 함수
        // 새로 만들지 않아도 된다!!
        fun getInstance(): Retrofit? {

            if (retrofit == null) {
                synchronized(this) {
                    val client: OkHttpClient = OkHttpClient.Builder()
/*                        .readTimeout(30000, TimeUnit.MILLISECONDS)
                        .connectTimeout(30000, TimeUnit.MILLISECONDS)*/
                        .addNetworkInterceptor(AuthorizationTokenInterceptor()) // JWT 자동 헤더 전송
                        .build()

                    retrofit = Retrofit.Builder()
                        .baseUrl(ApplicationClass.BASE_URL)
                        .addConverterFactory(GsonConverterFactory.create())
                        .client(client)
                        .build()
                }
            }

            return retrofit
        }
    }
}
