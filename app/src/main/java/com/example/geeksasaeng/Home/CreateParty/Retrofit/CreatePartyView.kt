package com.example.geeksasaeng.Home.CreateParty.Retrofit

interface CreatePartyDefaultLocView{
    fun onDefaultLocSuccess(result: CreatePartyDefaultLocResult)
    fun onDefaultLocFailure()
}

interface CreatePartyView{
    fun onCreatePartySuccess()
    fun onCreatePartyFailure(message: String)
}