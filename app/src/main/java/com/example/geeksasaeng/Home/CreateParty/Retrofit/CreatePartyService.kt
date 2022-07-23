package com.example.geeksasaeng.Home.CreateParty.Retrofit

import com.example.geeksasaeng.Signup.Retrofit.SignUpResponse
import com.example.geeksasaeng.Utils.ApplicationClass.Companion.retrofit
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CreatePartyService {
    //뷰 객체 생성
    private lateinit var createPartyDefaultLocView: CreatePartyDefaultLocView

    private val createPartyDataService = retrofit.create(CreatePartyRetrofitInterfaces::class.java)

    //setView
    fun setCreatePartyDefaultLocView(createPartyDefaultLocView: CreatePartyDefaultLocView){
        this.createPartyDefaultLocView = createPartyDefaultLocView
    }

    fun getDefaultLoc( defaultLocRequest :CreatePartyDefaultLocRequest) {
        createPartyDataService.getDeliveryPartyDefaultLocation(defaultLocRequest).enqueue(object :
            Callback<CreatePartyDefaultLocResponse> {
            override fun onResponse(
                call: Call<CreatePartyDefaultLocResponse>,
                response: Response<CreatePartyDefaultLocResponse>
            ) {
                if (response.isSuccessful && response.code() == 200) {
                    val defaultLocResponse: CreatePartyDefaultLocResponse = response.body()!!
                    //성공실패 나눠주기
                }
            }

            override fun onFailure(call: Call<CreatePartyDefaultLocResponse>, t: Throwable) {

            }
        })
    }

}

