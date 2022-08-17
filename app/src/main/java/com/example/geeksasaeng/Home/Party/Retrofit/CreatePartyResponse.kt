package com.example.geeksasaeng.Home.Party.Retrofit

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

//파티 생성하기
data class CreatePartyResponse(
    @SerializedName("code") val code: Int,
    @SerializedName("isSuccess") val isSuccess : Boolean,
    @SerializedName("message") val message: String,
    @SerializedName("result") val result: CreatePartyResult?
)

data class CreatePartyResult(
    @SerializedName("accountNumber") val accountNumber: String,
    @SerializedName("authorStatus") val authorStatus: String, //이번에 추가됨(ios 요청- 파티생성후 파티 상세보기로 이동하기 위해서..?)
    @SerializedName("bank") val bank: String,
    @SerializedName("belongStatus") val belongStatus: String, //이번에 추가됨(ios 요청)
    @SerializedName("chatRoomName") val chatRoomName: String,
    @SerializedName("chief") val chief: String,
    @SerializedName("chiefId") val chiefId: Int, //이번에 추가됨(ios 요청)
    @SerializedName("chiefProfileImgUrl") val chiefProfileImgUrl: String,//이번에 추가됨(ios 요청)
    @SerializedName("content") val content: String,
    @SerializedName("createdAt") val createdAt: String,
    @SerializedName("currentMatching") val currentMatching: Int,
    @SerializedName("dormitoryId") val dormitoryId: Int,//이번에 추가됨(ios 요청)
    @SerializedName("dormitoryName") val dormitoryName: String, //이번에 추가됨(ios 요청)
    @SerializedName("foodCategory") val foodCategory: String,
    @SerializedName("hashTag") val hashTag: Boolean,
    @SerializedName("id") val id: Int, //이게 파티 아이디인듯
    @SerializedName("latitude") val latitude: Double,
    @SerializedName("longitude") val longitude: Double,
    @SerializedName("matchingStatus") val matchingStatus: String,
    @SerializedName("maxMatching") val maxMatching: Int,
    @SerializedName("orderTime") val orderTime: String,
    @SerializedName("orderTimeCategoryType") val orderTimeCategoryType: String,
    @SerializedName("storeUrl") val storeUrl: String,
    @SerializedName("title") val title: String,
    @SerializedName("updatedAt") val updatedAt: String, //이번에 추가됨(ios 요청)
    @SerializedName("uuid") val uuid: String
)

data class CreatePartyRequest (
    @SerializedName("accountNumber") val accountNumber: String,
    @SerializedName("bank") val bank: String,
    @SerializedName("chatRoomName") val chatRoomName: String,
    @SerializedName("content") val content: String,
    @SerializedName("foodCategory") val foodCategory: Int,
    @SerializedName("hashTag") val hashTag: Boolean,
    @SerializedName("latitude") val latitude: Double,
    @SerializedName("longitude") val longitude: Double,
    @SerializedName("maxMatching") val maxMatching: Int,
    @SerializedName("orderTime") val orderTime: String,
    @SerializedName("storeUrl") val storeUrl: String,
    @SerializedName("title") val title: String
)