package com.example.geeksasaeng.Chatting.Retrofit

import com.example.geeksasaeng.Signup.Retrofit.SignUpResult
import com.google.gson.annotations.SerializedName

//배달파티 수동 매칭마감
data class MatchingEndResponse(
    @SerializedName("code") val code: Int,
    @SerializedName("isSuccess") val isSuccess : Boolean,
    @SerializedName("message") val message: String,
    @SerializedName("result") val result: MatchingEndResult?
)

data class MatchingEndResult(
    @SerializedName("deliveryPartyId") val deliveryPartyId: Int,
    @SerializedName("matchingStatus") val matchingStatus: String
)
