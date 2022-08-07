package com.example.geeksasaeng.Login.Retrofit

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.PATCH

interface DormitoryRetrofitInterfaces {
    @Headers("Authorization:Bearer eyJ0eXBlIjoiand0IiwiYWxnIjoiSFMyNTYifQ.eyJqd3RJbmZvIjp7InVuaXZlcnNpdHlJZCI6MSwidXNlcklkIjozNH0sImlhdCI6MTY1OTU5OTI1MywiZXhwIjoxNjYwNDg4Mjg2fQ.zX-GEg_E4SkWAiKmtHxkhoNraJJk6yXO-I_tCrOi47M")
    @PATCH("/members/dormitory")
    fun dormitoryUpdate(@Body dormitoryId: DormitoryRequest): Call<DormitoryResponse>
}
