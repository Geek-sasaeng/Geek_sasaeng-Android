package com.example.geeksasaeng.Home.Delivery.Retrofit

data class DeliveryPartyListResponse(
    val isSuccess: Boolean,
    val code: Int,
    val message: String,
    val result: List<DeliveryPartyResult>
)

data class DeliveryPartyResult(
    val partyIdx: Int,
    val hashTag: List<String>?,
    val title: String,
    val content: String,
    val orderTime: String?,
    val currentMatching: Int,
    val maxMatching: Int,
    val location: String?,
    val matchingStatus: String?,
    val createdAt: String,
    val updatedAt: String,
    val status: String
)