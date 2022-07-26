package com.example.geeksasaeng.Home.Delivery.Retrofit

import android.util.Log
import com.example.geeksasaeng.Home.Delivery.DeliveryBannerResponse
import com.example.geeksasaeng.Home.Delivery.DeliveryResponse
import com.example.geeksasaeng.Signup.Retrofit.SignUpResponse
import com.example.geeksasaeng.Utils.ApplicationClass.Companion.retrofit
import com.example.geeksasaeng.Utils.NetworkModule
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DeliveryService {
    //뷰 객체 생성
    private lateinit var deliveryBannerView: DeliveryBannerView
    private lateinit var deliveryView: DeliveryView

    private val deliveryDataService = retrofit.create(DeliveryRetrofitInterfaces::class.java)

    //setView
    fun setDeliveryBannerView(deliveryBannerView: DeliveryBannerView){
        this.deliveryBannerView = deliveryBannerView
    }

    fun setDeliveryView(deliveryView: DeliveryView){
        this.deliveryView = deliveryView
    }

    fun getDeliveryBanner(){
        deliveryDataService.getCommercials().enqueue(object : Callback<DeliveryBannerResponse> {
            override fun onResponse(
                call: Call<DeliveryBannerResponse>,
                response: Response<DeliveryBannerResponse>
            ) {
                if (response.isSuccessful && response.code() == 200) {
                    val deliveryBannerResponse = response.body()!!

                    when (deliveryBannerResponse.code) {
                        1000 -> deliveryBannerView.ondeliveryBannerSuccess(deliveryBannerResponse.result!!) //광고불러오기
                        else -> deliveryBannerView.ondeliveryBannerFailure(deliveryBannerResponse.message)
                    }
                }
            }

            override fun onFailure(call: Call<DeliveryBannerResponse>, t: Throwable) {
                Log.d("commerical", "실패")
            }

        })
    }

    // 배달 리스트 목록들 불러오기
    fun getDeliveryAllList(dormitoryId: Int, cursor: Int){
        val deliveryPartyService = NetworkModule.getInstance()?.create(DeliveryRetrofitInterfaces::class.java)
        deliveryPartyService?.getAllDeliveryList(dormitoryId, cursor)?.enqueue(object:
            Callback<DeliveryResponse> {
            override fun onResponse(call: Call<DeliveryResponse>, response: Response<DeliveryResponse>) {
                if (response.isSuccessful && response.code() == 200) {
                    val deliveryResponse: DeliveryResponse = response.body()!!
                    when (deliveryResponse.code) {
                        1000 -> deliveryView.deliverySuccess(response.body()!!)
                        else -> deliveryView.deliveryFailure(deliveryResponse.code, deliveryResponse.message)
                    }
                }
            }
            override fun onFailure(call: Call<DeliveryResponse>, t: Throwable) {
                Log.d("DELIVERY-RESPONSE", "DeliveryService-onFailure : DeliveryFailed", t)
            }
        })
    }
}