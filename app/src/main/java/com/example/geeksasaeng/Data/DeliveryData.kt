package com.example.geeksasaeng.Data

data class DeliveryData(
    var time: String? = "",
    var title: String? = "",
    var option1: Boolean = false,
    var option2: Boolean = false,
    var currentMember: Int = 1,
    var totalMember: Int = 0,
    var hostProfile: Int = 0,
    var hostName: String? = ""
)
