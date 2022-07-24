package com.example.geeksasaeng.Home.CreateParty.Retrofit

import android.location.Address
import android.location.Geocoder
import android.util.Log
import com.example.geeksasaeng.Signup.Retrofit.SignUpResponse
import com.example.geeksasaeng.Utils.ApplicationClass.Companion.retrofit
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

class CreatePartyService {
    //뷰 객체 생성
    private lateinit var createPartyDefaultLocView: CreatePartyDefaultLocView
    private lateinit var createPartyView: CreatePartyView

    private val createPartyDataService = retrofit.create(CreatePartyRetrofitInterfaces::class.java)

    //setView
    fun setCreatePartyDefaultLocView(createPartyDefaultLocView: CreatePartyDefaultLocView){
        this.createPartyDefaultLocView = createPartyDefaultLocView
    }

    fun setCreatePartyView(createPartyView: CreatePartyView){
        this.createPartyView = createPartyView
    }

    fun getDefaultLoc( dormitoryId : Int) {
        createPartyDataService.getDeliveryPartyDefaultLocation(dormitoryId).enqueue(object :
            Callback<CreatePartyDefaultLocResponse> {
            override fun onResponse(
                call: Call<CreatePartyDefaultLocResponse>,
                response: Response<CreatePartyDefaultLocResponse>
            ) {
                Log.d("kakaodefault", response.toString())
                Log.d("kakaodefault", response.body()?.message.toString())
                if (response.isSuccessful && response.code() == 200) {
                    val resp: CreatePartyDefaultLocResponse = response.body()!!
                    Log.d("kakaodefault", resp.toString())
                    createPartyDefaultLocView.onDefaultLocSuccess(resp.result!!)
                }
            }

            override fun onFailure(call: Call<CreatePartyDefaultLocResponse>, t: Throwable) {
                Log.d("kakaodefault", "onFailure 호출됨")
            }
        })
    }

    fun createPartySender(createPartyRequest: CreatePartyRequest){
        createPartyDataService.createParty(createPartyRequest).enqueue(object:
        Callback<CreatePartyResponse>{
            override fun onResponse(
                call: Call<CreatePartyResponse>,
                response: Response<CreatePartyResponse>
            ) {
                if (response.isSuccessful && response.code() == 200) {
                    val resp: CreatePartyResponse = response.body()!!
                    when (resp.code) {
                        1000 -> createPartyView.onCreatePartySuccess()
                        else -> createPartyView.onCreatePartyFailure(resp.message)
                    }
                }
            }

            override fun onFailure(call: Call<CreatePartyResponse>, t: Throwable) {
                TODO("Not yet implemented")
            }

        })
    }

}

