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
    @SerializedName("result") val result: DeliveryResult
)

data class DeliveryResult (
    @SerializedName("deliveryPartiesVoList") var deliveryPartiesVoList: Array<DeliveryPartiesVoList>,
    @SerializedName("finalPage") var finalPage: Boolean
)

data class DeliveryPartiesVoList (
    @SerializedName("currentMatching") var currentMatching: Int? = 0,
    @SerializedName("foodCategory") var foodCategory: String? = "",
    @SerializedName("id") var id: Int? = 0,
    @SerializedName("maxMatching") var maxMatching: Int? = 0,
    @SerializedName("orderTime") var orderTime: String? = "",
    @SerializedName("title") var title: String? = "",
    @SerializedName("hasHashTag") var hasHashTag: Boolean? = null
)