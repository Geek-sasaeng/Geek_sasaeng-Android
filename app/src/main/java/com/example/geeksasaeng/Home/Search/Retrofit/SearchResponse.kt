package com.example.geeksasaeng.Home.Search.Retrofit

import com.example.geeksasaeng.Home.Delivery.DeliveryPartiesVoList
import com.google.gson.annotations.SerializedName

data class SearchResponse(
    @SerializedName("code") val code: Int,
    @SerializedName("isSuccess") val isSuccess : Boolean,
    @SerializedName("message") val message: String,
    @SerializedName("result") val result: SearchResult
)

data class SearchResult (
    @SerializedName("deliveryPartiesVoList") var searchPartiesVoList: Array<SearchPartiesVoList>,
    @SerializedName("finalPage") var finalPage: Boolean
)

data class SearchPartiesVoList (
    @SerializedName("currentMatching") var currentMatching: Int? = 0,
    @SerializedName("foodCategory") var foodCategory: String? = "",
    @SerializedName("id") var id: Int? = 0,
    @SerializedName("maxMatching") var maxMatching: Int? = 0,
    @SerializedName("orderTime") var orderTime: String? = "",
    @SerializedName("title") var title: String? = "",
    @SerializedName("hasHashTag") var hasHashTag: Boolean? = null
)