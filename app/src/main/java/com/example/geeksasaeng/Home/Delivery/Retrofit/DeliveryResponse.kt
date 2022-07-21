package com.example.geeksasaeng.Home.Delivery

import com.google.gson.annotations.SerializedName
import retrofit2.http.Body

//광고 commercials

data class DeliveryBannerResponse(
    @SerializedName("code") val code: Int,
    @SerializedName("isSuccess") val isSuccess : Boolean,
    @SerializedName("message") val message: String,
    @SerializedName("result") val result: Array<DeliveryBannerResult>?
)

data class DeliveryBannerResult(
    @SerializedName("id") var id :Int,
    @SerializedName("imgUrl") var imgUrl :String,
)

//
data class DeliveryResponse(
    @SerializedName("code") val code: Int,
    @SerializedName("isSuccess") val isSuccess : Boolean,
    @SerializedName("message") val message: String,
    @SerializedName("result") val result: Array<DeliveryResult>?
)

data class DeliveryResult (
    @SerializedName("chief") var chief: String? = "",
    @SerializedName("content") var content: String? = "",
    @SerializedName("currentMatching") var currentMatching: Int? = 0,
    @SerializedName("foodCategory") var foodCategory: String? = "",
    @SerializedName("id") var id: String? = "",
    @SerializedName("location") var location: String? = "",
    @SerializedName("matchingStatus") var matchingStatus: String? = "",
    @SerializedName("maxMatching") var maxMatching: Int? = 0,
    @SerializedName("orderTime") var orderTime: String? = "",
    @SerializedName("title") var title: String? = "",
    @SerializedName("hashTags") var hashTags: Array<String?> = arrayOf()
//    var option1: Boolean? = false,
//    var option2: Boolean? = false,
)