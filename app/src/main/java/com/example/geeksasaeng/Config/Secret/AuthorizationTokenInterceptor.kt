package com.example.geeksasaeng.Config.Secret

import android.util.Log
import com.example.geeksasaeng.Utils.ApplicationClass.Companion.Authorization_TOKEN
import com.example.geeksasaeng.Utils.getJwt
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response

class AuthorizationTokenInterceptor: Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val builder: Request.Builder = chain.request().newBuilder()


        val jwtToken: String? = getJwt()

        jwtToken?.let{
            builder.addHeader(Authorization_TOKEN, "Bearer $jwtToken")
            Log.d("interceptor","Bearer $jwtToken")
        }

        return chain.proceed(builder.build())
    }
}