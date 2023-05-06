package com.example.geeksasaeng.Login

import DormitoryDataService
import android.os.Bundle
import android.util.Log
import com.example.geeksasaeng.Login.Retrofit.DormitoryRequest
import com.example.geeksasaeng.Login.Retrofit.DormitoryResult
import com.example.geeksasaeng.Login.Retrofit.DormitoryView
import com.example.geeksasaeng.MainActivity
import com.example.geeksasaeng.Profile.Retrofit.ProfileDataService
import com.example.geeksasaeng.Profile.Retrofit.ProfileViewDormitoryResult
import com.example.geeksasaeng.Profile.Retrofit.ProfileViewDormitoryView
import com.example.geeksasaeng.Utils.*
import com.example.geeksasaeng.databinding.ActivityDormitoryBinding

class DormitoryActivity : BaseActivity<ActivityDormitoryBinding>(ActivityDormitoryBinding::inflate), DormitoryView, ProfileViewDormitoryView {

    private lateinit var dormitoryService: DormitoryDataService
    private lateinit var profileDataService : ProfileDataService //기숙사 리스트 불러오기 용

    private var name = getNickname() //사용자 이름
    private var dormitoryId = -1 // 기숙사 정보 (default값은 -1)

    override fun onStart() {
        super.onStart()
        Log.d("API-TEST", "onStart")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("API-TEST", "onCreate")
    }

    override fun initAfterBinding() {
        name = intent.getStringExtra("nickName")!! // 닉네임은 LoginActivity로 부터 받아온다.
        profileDataService = ProfileDataService()
        profileDataService.setProfileViewDormitoryView(this)
        profileDataService.profileViewDormiotrySender(1) // TODO: 일단 가천대만 지원하니까 1로 값을 줌

        binding.domitoryHiTv.text = "${name}님,\n환영합니다"
        initDormPicker()
        initClickListener()
    }

    private fun initDormPicker(){
        binding.dormitoryPicker.setOnValueChangedListener { picker, oldVal, newVal ->
            Log.d("dormitory", newVal.toString())
            dormitoryId = newVal+1
        }
    }

    private fun initClickListener(){
        // 시작하기 버튼 클릭 시
        binding.dormitoryStartBtn.setOnClickListener {
            Log.d("dormitory", dormitoryId.toString())
            val dormitoryRequest = DormitoryRequest(dormitoryId)
            dormitoryService = DormitoryDataService()
            dormitoryService.setDormitoryView(this)
            dormitoryService.dormitorySender(dormitoryRequest) //★ 기숙사 수정 api
            changeActivity(MainActivity::class.java)
        }
    }

    //기숙사 수정 api
    override fun onDormitySuccess(result: DormitoryResult) {
        //TODO: SharedPref에 기숙사값 넣어주기
        saveDormitory("제 " + result.dormitoryName)
        saveDormitoryId(result.dormitoryId) //가끔 null뜨고, 목록 안 불러와지는 오류 이거 때문이었다.
        finish()
    }

    override fun onDormityFailure(message: String) {
        showToast("기숙사 수정 실패"+message) //디버깅용
    }

    override fun onProfileViewDormitorySuccess(result: ArrayList<ProfileViewDormitoryResult>) {
        val dormitoryArrayList = arrayListOf<String>()
        var dormitoryArray = arrayOf<String>()
        for (i in result){
            dormitoryArrayList.add("제 ${i.name}")
        }
        dormitoryArray = dormitoryArrayList.toArray(arrayOfNulls<String>(dormitoryArrayList.size))

        binding.dormitoryPicker.minValue = 0
        binding.dormitoryPicker.maxValue = dormitoryArrayList.size-1
        binding.dormitoryPicker.displayedValues = dormitoryArray
        binding.dormitoryPicker.value = (dormitoryArrayList.size-1)/2 //중간값을 초기값으로 세팅
        // binding.dormitoryPicker.setPadding(10, 0, 10, 0)
        dormitoryId = ((dormitoryArrayList.size-1)/2)+1 //(deafult값 갱신)
    }

    override fun onProfileViewDormitoryFailure(message: String) {
        Log.d("getDormitoryList", "기숙사 불러오기 실패")
    }
}