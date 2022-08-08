package com.example.geeksasaeng.Login.Retrofit

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.PATCH

interface DormitoryRetrofitInterfaces {
    @PATCH("/members/dormitory")
    fun dormitoryUpdate(
        @Header("Authorization") jwt: String?,
        @Body dormitoryId: DormitoryRequest
    ): Call<DormitoryResponse>
}
