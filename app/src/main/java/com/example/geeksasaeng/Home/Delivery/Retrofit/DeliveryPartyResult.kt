package com.example.geeksasaeng.Home.Delivery.Retrofit

import java.time.LocalDateTime

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