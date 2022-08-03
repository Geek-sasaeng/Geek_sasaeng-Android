package com.example.geeksasaeng.Login.Retrofit

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.PATCH

interface DormitoryRetrofitInterfaces {
    @PATCH("/members/dormitory/{id}")
    fun dormitoryUpdate(@Body dormitoryId: DormitoryRequest): Call<DormitoryResponse>
}
