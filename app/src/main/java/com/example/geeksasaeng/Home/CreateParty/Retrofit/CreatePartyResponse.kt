package com.example.geeksasaeng.Home.CreateParty.Retrofit

import com.example.geeksasaeng.Signup.Retrofit.SignUpResult
import com.google.gson.annotations.SerializedName

//기숙사 default-location
data class CreatePartyDefaultLocResponse(
    @SerializedName("code") val code: Int,
    @SerializedName("isSuccess") val isSuccess : Boolean,
    @SerializedName("message") val message: String,
    @SerializedName("result") val result: CreatePartyDefaultLocResult?
)

data class CreatePartyDefaultLocResult(
    @SerializedName("latitude") var latitude :Double,
    @SerializedName("longitude") var longitude :Double
)

data class CreatePartyDefaultLocRequest(
    @SerializedName("dormitoryId") val dormitoryId : Int
)

//파티 생성하기
data class CreatePartyResponse(
    @SerializedName("code") val code: Int,
    @SerializedName("isSuccess") val isSuccess : Boolean,
    @SerializedName("message") val message: String,
    @SerializedName("result") val result: CreatePartyResult?
)

data class CreatePartyResult(
    @SerializedName("chief") val chief: String,
    @SerializedName("content") val content: String,
    @SerializedName("createdAt") val createdAt: String,
    @SerializedName("dormitory") val dormitory: String,
    @SerializedName("foodCategory") val foodCategory: String,
    @SerializedName("hashTag") val hashTag: Boolean,
    @SerializedName("latitude") val latitude: Double,
    @SerializedName("longitude") val longitude: Double,
    @SerializedName("matchingStatus") val matchingStatus: String,
    @SerializedName("maxMatching") val maxMatching: Int,
    @SerializedName("orderTime") val orderTime: String,
    @SerializedName("orderTimeCategoryType") val orderTimeCategoryType: String,
    @SerializedName("storeUrl") val storeUrl: String,
    @SerializedName("title") val title: String
)

data class CreatePartyRequest (
    @SerializedName("content") val content: String,
    @SerializedName("dormitory") val dormitory: String,
    @SerializedName("foodCategory") val foodCategory: String,
    @SerializedName("hashTag") val hashTag: Boolean,
    @SerializedName("latitude") val latitude: Double,
    @SerializedName("longitude") val longitude: Double,
    @SerializedName("maxMatching") val maxMatching: Int,
    @SerializedName("orderTime") val orderTime: String,
    @SerializedName("storeUrl") val storeUrl: String,
    @SerializedName("title") val title: String
)