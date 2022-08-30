package com.example.geeksasaeng.Home.Party.UpdateParty.Retrofit

import com.example.geeksasaeng.Home.Party.Retrofit.CreatePartyDefaultLocResult
import com.google.gson.annotations.SerializedName

//배달 파티 수정
data class UpdatePartyResponse(
    @SerializedName("code") val code: Int,
    @SerializedName("isSuccess") val isSuccess : Boolean,
    @SerializedName("message") val message: String,
    @SerializedName("result") val result: CreatePartyDefaultLocResult?
)

data class UpdatePartyResult(
    @SerializedName("chief") val chief: String,
    @SerializedName("content") val content: String,
    @SerializedName("currentMatching") val currentMatching: Int,
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
    @SerializedName("title") val title: String,
    @SerializedName("updatedAt") val updatedAt: String
)

data class UpdatePartyRequest(
    @SerializedName("content") val content: String,
    @SerializedName("foodCategory") val foodCategory: Int?,
    @SerializedName("hashTag") val hashTag: Boolean?,
    @SerializedName("latitude") val latitude: Double,
    @SerializedName("longitude") val longitude: Double,
    @SerializedName("maxMatching") val maxMatching: Int,
    @SerializedName("orderTime") val orderTime: String?,
    @SerializedName("storeUrl") val storeUrl: String?,
    @SerializedName("title") val title: String?
)