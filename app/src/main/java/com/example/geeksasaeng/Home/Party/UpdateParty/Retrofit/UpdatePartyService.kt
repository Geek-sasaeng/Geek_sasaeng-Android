package com.example.geeksasaeng.Home.Party.UpdateParty.Retrofit

import android.util.Log
import com.example.geeksasaeng.Utils.ApplicationClass.Companion.retrofit
import com.example.geeksasaeng.Utils.NetworkModule
import com.example.geeksasaeng.Utils.getJwt
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class UpdatePartyService {
    private lateinit var updatePartyView: UpdatePartyView

    private val updatePartyService = NetworkModule.getInstance()?.create(UpdatePartyRetrofitInterfaces::class.java)

    fun setUpdatePartyView(updatePartyView: UpdatePartyView){
        this.updatePartyView = updatePartyView
    }

    fun updatePartySender(dormitoryId: Int, partyId:Int, updatePartyRequest: UpdatePartyRequest){
        updatePartyService?.updateParty("Bearer " + getJwt(), dormitoryId, updatePartyRequest, partyId)?.enqueue(object:Callback<UpdatePartyResponse> {
            override fun onResponse(call: Call<UpdatePartyResponse>, response: Response<UpdatePartyResponse>) {
                if (response.isSuccessful && response.code() == 200) {
                    val resp: UpdatePartyResponse = response.body()!!
                    when (resp.code) {
                        1000 -> updatePartyView.onUpdatePartySuccess()
                        else -> updatePartyView.onUpdatePartyFailure(resp.message)
                    }
                }
            }
            override fun onFailure(call: Call<UpdatePartyResponse>, t: Throwable) {
                Log.d("cherry","실패패")
            }
        })
    }
}