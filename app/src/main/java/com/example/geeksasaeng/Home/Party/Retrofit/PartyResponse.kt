package com.example.geeksasaeng.Home.Party.Retrofit

import com.example.geeksasaeng.Signup.Retrofit.SignUpResult
import com.google.gson.annotations.SerializedName

// 파티보기 상세조회
data class PartyDetailResponse(
    @SerializedName("code") val code: Int,
    @SerializedName("isSuccess") val isSuccess : Boolean,
    @SerializedName("message") val message: String,
    @SerializedName("result") val result: PartyDetailResult?
)

data class PartyDetailResult(
    @SerializedName("authorStatus") val authorStatus: Boolean,
    @SerializedName("belongStatus") val belongStatus: String,
    @SerializedName("chief") val chief: String,
    @SerializedName("chiefId") val chiefId: Int,
    @SerializedName("chiefProfileImgUrl") val chiefProfileImgUrl: String?,
    @SerializedName("content") val content: String,
    @SerializedName("currentMatching") val currentMatching: Int,
    @SerializedName("dormitory") val dormitory: Int,
    @SerializedName("foodCategory") val foodCategory: String,
    @SerializedName("hashTag") val hashTag: Boolean,
    @SerializedName("id") val id: Int,
    @SerializedName("latitude") val latitude: Double,
    @SerializedName("longitude") val longitude: Double,
    @SerializedName("matchingStatus") val matchingStatus: String,
    @SerializedName("maxMatching") val maxMatching: Int,
    @SerializedName("orderTime") val orderTime: String,
    @SerializedName("partyChatRoomId") val partyChatRoomId: String,
    @SerializedName("partyChatRoomTitle") val partyChatRoomTitle: String,
    @SerializedName("storeUrl") val storeUrl: String?,
    @SerializedName("title") val title: String,
    @SerializedName("updatedAt") val updatedAt: String,
    @SerializedName("uuid") val uuid: String
)

data class PartyDeleteResponse(
    @SerializedName("code") val code: Int,
    @SerializedName("isSuccess") val isSuccess : Boolean,
    @SerializedName("message") val message: String,
    @SerializedName("result") val result: PartyDeleteResult?
)

data class PartyDeleteResult(
    @SerializedName("deliveryPartyId") val deliveryPartyId: Int,
    @SerializedName("status") val status: String
)

data class PartyReportResponse(
    @SerializedName("code") val code: Int,
    @SerializedName("isSuccess") val isSuccess : Boolean,
    @SerializedName("message") val message: String,
    @SerializedName("result") val result: String
)

data class PartyReportRequest(
    @SerializedName("block") val block: Boolean,
    @SerializedName("reportCategoryId") val reportCategoryId: Int,
    @SerializedName("reportContent") val reportContent: String?,
    @SerializedName("reportedDeliveryPartyId") val reportedDeliveryPartyId: Int,
    @SerializedName("reportedMemberId") val reportedMemberId: Int
)

data class UserReportResponse(
    @SerializedName("code") val code: Int,
    @SerializedName("isSuccess") val isSuccess : Boolean,
    @SerializedName("message") val message: String,
    @SerializedName("result") val result: String
)

data class UserReportRequest(
    @SerializedName("block") val block: Boolean,
    @SerializedName("reportCategoryId") val reportCategoryId: Int,
    @SerializedName("reportContent") val reportContent: String?,
    @SerializedName("reportedMemberId") val reportedMemberId: Int
)

data class JoinPartyResponse(
    @SerializedName("code") val code: Int,
    @SerializedName("isSuccess") val isSuccess: Boolean,
    @SerializedName("message") val message: String,
    @SerializedName("result") val result: JoinPartyResult
)

data class JoinPartyResult(
    @SerializedName("deliveryPartyId") val deliveryPartyId: Int,
    @SerializedName("deliveryPartyMemberId") val deliveryPartyMemberId: Int
)

data class JoinPartyRequest(
    @SerializedName("partyId") val partyId: Int
)