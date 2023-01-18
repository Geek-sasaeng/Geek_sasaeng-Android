package com.example.geeksasaeng.Chatting.ChattingList.Retrofit

import com.google.gson.annotations.SerializedName

data class ChattingListResponse(
    @SerializedName("code") val code: Int,
    @SerializedName("isSuccess") val isSuccess : Boolean,
    @SerializedName("message") val message: String,
    @SerializedName("result") val result: ChattingListResult
)

data class ChattingListResult(
    @SerializedName("parties") val parties: ArrayList<ChattingList?>,
    @SerializedName("finalPage") val finalPage: Boolean
)

data class ChattingList(
    @SerializedName("roomId") val roomId: String,
    @SerializedName("roomTitle") val roomTitle: String,
    var lastChatting: String,
    var lastChattingTime: String,
    var newChattingNumber: Int
)

data class ChattingDetailResponse(
    @SerializedName("code") val code: Int,
    @SerializedName("isSuccess") val isSuccess : Boolean,
    @SerializedName("message") val message: String,
    @SerializedName("result") val result: ChattingDetailResult
)

data class ChattingDetailResult(
    @SerializedName("accountNumber") val accountNumber: String,
    @SerializedName("bank") val bank: String,
    @SerializedName("chiefId") val chiefId: Int,
    @SerializedName("enterTime") val enterTime: String,
    @SerializedName("isChief") val isChief: Boolean,
    @SerializedName("isOrderFinish") val isOrderFinish: Boolean,
    @SerializedName("isRemittanceFinish") val isRemittanceFinish: Boolean
)