package com.example.geeksasaeng.Login

import DormitoryDataService
import android.util.Log
import android.widget.EditText
import androidx.core.content.ContextCompat
import androidx.core.view.get
import com.example.geeksasaeng.Login.Retrofit.DormitoryRequest
import com.example.geeksasaeng.Login.Retrofit.DormitoryResult
import com.example.geeksasaeng.Login.Retrofit.DormitoryView
import com.example.geeksasaeng.MainActivity
import com.example.geeksasaeng.Profile.Retrofit.ProfileDataService
import com.example.geeksasaeng.Profile.Retrofit.ProfileViewDormitoryResult
import com.example.geeksasaeng.Profile.Retrofit.ProfileViewDormitoryView
import com.example.geeksasaeng.R
import com.example.geeksasaeng.Utils.*
import com.example.geeksasaeng.databinding.ActivityDormitoryBinding

class DormitoryActivity : BaseActivity<ActivityDormitoryBinding>(ActivityDormitoryBinding::inflate), DormitoryView, ProfileViewDormitoryView {

    private lateinit var dormitoryService: DormitoryDataService
    private lateinit var profileDataService : ProfileDataService //기숙사 리스트 불러오기 용

    private var name = getNickname() //사용자 이름
    private var dormitoryId = -1 // 기숙사 정보 (default값은 -1)

    override fun onStart() {
        super.onStart()
        dormitoryService = DormitoryDataService()
        dormitoryService.setDormitoryView(this)
        profileDataService = ProfileDataService()
        profileDataService.setProfileViewDormitoryView(this)
    }

    override fun initAfterBinding() {
        name = intent.getStringExtra("nickName")!! // 닉네임은 LoginActivity로 부터 받아온다.
        profileDataService.profileViewDormiotrySender(1) // TODO: 일단 가천대만 지원하니까 1로 값을 줌

        binding.domitoryHiTv.text = "${name}님,\n환영합니다"
        initDormPicker()
        initClickListener()
    }

    private fun initDormPicker(){
        binding.domitoryPicker.setOnValueChangedListener { picker, oldVal, newVal ->
            Log.d("dormitory", newVal.toString())
            dormitoryId = newVal+1
        }
    }

    private fun initClickListener(){
        // 시작하기 버튼 클릭 시
        binding.dormitoryStartBtn.setOnClickListener {
            Log.d("dormitory", dormitoryId.toString())
            val dormitoryRequest = DormitoryRequest(dormitoryId)
            dormitoryService.dormitorySender(dormitoryRequest) //★ 기숙사 수정 api
            changeActivity(MainActivity::class.java)
        }
    }

    //기숙사 수정 api
    override fun onDormitySuccess(result: DormitoryResult) {
        //TODO: SharedPref에 기숙사값 넣어주기
        Log.d("dormitory",result.dormitoryName)
        saveDormitory("제"+result.dormitoryName)
        saveDormitoryId(result.dormitoryId) //가끔 null뜨고, 목록 안 불러와지는 오류 이거 때문이었다.
        finish()
    }

    override fun onDormityFailure(message: String) {
        showToast("기숙사 수정 실패"+message) //디버깅용
    }

    override fun onProfileViewDormitorySuccess(result: ArrayList<ProfileViewDormitoryResult>) {
        val dormitoryArray = arrayListOf<String>()
        for (i in result){
            Log.d("getDormitoryList", "${i.id}, ${i.name}")
            dormitoryArray.add("제 "+i.name)
        }

        binding.domitoryPicker.minValue = 0
        binding.domitoryPicker.maxValue = dormitoryArray.size-1
        binding.domitoryPicker.displayedValues = dormitoryArray.toArray() as Array<out String>?
        binding.domitoryPicker.value = (dormitoryArray.size-1)/2 //중간값을 초기값으로 세팅
        dormitoryId = ((dormitoryArray.size-1)/2)+1 //(deafult값 갱신)
    }

    override fun onProfileViewDormitoryFailure(message: String) {
        Log.d("getDormitoryList", "기숙사 불러오기 실패")
    }

}