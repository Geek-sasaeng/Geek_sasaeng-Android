package com.example.geeksasaeng.Home.Delivery.Retrofit

import android.util.Log
import com.example.geeksasaeng.Home.Delivery.DeliveryResponse
import com.example.geeksasaeng.Utils.NetworkModule
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DeliveryService {
    private lateinit var deliveryView: DeliveryView

    fun setDeliveryView(deliveryView: DeliveryView){
        this.deliveryView = deliveryView;
    }

    // 배달 리스트 목록들 불러오기
    fun getDeliveryAllList(dormitoryId: Int, cursor: Int){
        val deliveryPartyService = NetworkModule.getInstance()?.create(DeliveryInterface::class.java)
        deliveryPartyService?.getAllDeliveryList(dormitoryId, cursor)?.enqueue(object:
            Callback<DeliveryResponse> {
            override fun onResponse(call: Call<DeliveryResponse>, response: Response<DeliveryResponse>) {
                if (response.isSuccessful && response.code() == 200) {
                    val deliveryResponse: DeliveryResponse = response.body()!!
                    Log.d("DELIVERY-RESPONSE", "DELIVERY-SERVICE-DORMITORY-ID : ${dormitoryId}")
                    Log.d("DELIVERY-RESPONSE", "DELIVERY-SERVICE-CURSOR : $cursor")

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