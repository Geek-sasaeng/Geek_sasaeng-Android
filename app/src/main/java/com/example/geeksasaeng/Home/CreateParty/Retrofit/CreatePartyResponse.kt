package com.example.geeksasaeng.Home.CreateParty.Retrofit

import com.google.gson.annotations.SerializedName

//기숙사 default-location
data class CreatePartyDefaultLocResponse(
    @SerializedName("code") val code: Int,
    @SerializedName("isSuccess") val isSuccess : Boolean,
    @SerializedName("message") val message: String,
    @SerializedName("result") val result: CreatePartyDefaultLocResult?
)

data class CreatePartyDefaultLocResult(
    @SerializedName("latitude") var latitude :Int,
    @SerializedName("longitude") var longitude :Int
)

data class CreatePartyDefaultLocRequest(
    @SerializedName("dormitoryId") val dormitoryId : Int
)