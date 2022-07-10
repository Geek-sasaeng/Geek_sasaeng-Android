package com.example.geeksasaeng.Data

import android.text.Editable
import com.google.gson.annotations.SerializedName

data class EmailSend(
    @SerializedName("email") var email: String? = "",
    @SerializedName("university") var university: String? = "",
    @SerializedName("uuid") var uuid: String? = ""
)