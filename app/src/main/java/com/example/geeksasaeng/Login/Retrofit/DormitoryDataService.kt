import android.util.Log
import com.example.geeksasaeng.Login.Retrofit.DormitoryRequest
import com.example.geeksasaeng.Login.Retrofit.DormitoryResponse
import com.example.geeksasaeng.Login.Retrofit.DormitoryRetrofitInterfaces
import com.example.geeksasaeng.Login.Retrofit.DormitoryView
import com.example.geeksasaeng.Utils.ApplicationClass
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DormitoryDataService {

    //뷰 객체 생성
    private lateinit var dormitoryView: DormitoryView

    private val DormitoryDataService = ApplicationClass.retrofit.create(DormitoryRetrofitInterfaces::class.java)

    //setView
    fun setDormitoryView(dormitoryView: DormitoryView) {
        this.dormitoryView = dormitoryView
    }

    //기숙사수정하기
    fun dormitorySender(dormitoryId: DormitoryRequest){
        DormitoryDataService.dormitoryUpdate(dormitoryId).enqueue(object : Callback<DormitoryResponse> {
            override fun onResponse(
                call: Call<DormitoryResponse>,
                response: Response<DormitoryResponse>
            ) {
                Log.d("dormitoryy", response.toString())
                if (response.isSuccessful && response.code() == 200) {
                    val resp: DormitoryResponse = response.body()!!
                    when(resp.code){
                        200-> dormitoryView.onDormitySuccess(resp.result)
                        else-> dormitoryView.onDormityFailure()
                    }
                }
            }

            override fun onFailure(call: Call<DormitoryResponse>, t: Throwable) {
                //
            }

        })
    }
}