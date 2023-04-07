package com.example.geeksasaeng.Profile

import android.database.Cursor
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.core.net.toUri
import androidx.fragment.app.DialogFragment
import com.example.geeksasaeng.Profile.Retrofit.ProfileDataService
import com.example.geeksasaeng.Profile.Retrofit.ProfileMemberInfoModifyResult
import com.example.geeksasaeng.Profile.Retrofit.ProfileMemberInfoModifyView
import com.example.geeksasaeng.Utils.*
import com.example.geeksasaeng.databinding.DialogProfileUpdateBinding
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File

class DialogProfileUpdate(override val contentResolver: Any) : DialogFragment(), ProfileMemberInfoModifyView {
    lateinit var binding: DialogProfileUpdateBinding
    lateinit var profileDataService: ProfileDataService //내 정보 수정용

    private var dormitoryId :Int = 0
    private lateinit var loginId : String
    private lateinit var nickname : String
    private lateinit var currentImageURI : Uri

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DialogProfileUpdateBinding.inflate(inflater, container, false)
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT)) // 배경 투명하게 만들어줘야 둥근 테두리가 보인다.
        // Dialog 사이즈 조절 하기
        val params = dialog!!.window!!.attributes
        params.width = WindowManager.LayoutParams.WRAP_CONTENT
        params.height = WindowManager.LayoutParams.WRAP_CONTENT
        dialog!!.window!!.attributes = params as WindowManager.LayoutParams

        initData()
        initView()
        initLClickListener()
        return binding.root
    }

    private fun initData() {
        dormitoryId = arguments!!.getInt("dormitoryId")
        loginId = arguments!!.getString("loginId").toString()
        nickname  = arguments!!.getString("nickname").toString()
        currentImageURI = arguments!!.getString("currentImageURI")!!.toUri()
    }

    private fun initView() {
        profileDataService = ProfileDataService() // 서비스 객체 생성
        profileDataService.setProfileMemberInfoModifyView(this)
    }

    private fun initLClickListener() {
        binding.dialogProfileUpdateCancelBtn.setOnClickListener {
            this.dismiss()
        }

        binding.dialogProfileUpdateOkBtn.setOnClickListener {
            Log.d("dialogProfileUpdate",currentImageURI.toString())
            editProfile()
        }
    }


    fun editProfile(){
        var profileImg: MultipartBody.Part? = null

        Log.d("sendImg- editProfile",currentImageURI.toString())
        if(currentImageURI.toString() != getProfileImgUrl()){ // 프로필 이미지가 변경할게 있다면,
            Log.d("sendImg- editProfile?",currentImageURI.path.toString())
            val file = File(absolutelyPath(currentImageURI!!))
            val requestFile = RequestBody.create("image/*".toMediaTypeOrNull(), file)
            profileImg = MultipartBody.Part.createFormData("profileImg", file.name, requestFile) //폼데이터
        }else{
            val file = File(currentImageURI.toString())
            val requestFile = RequestBody.create("image/*".toMediaTypeOrNull(), file)
            profileImg = MultipartBody.Part.createFormData("profileImg", file.name, requestFile) //폼데이터
        }

        Log.d("sendImg-정보", "$dormitoryId/$nickname/$profileImg")

        //나머지 dormitoryId랑 nickname
        val mapData = HashMap<String, RequestBody>()
        val dormitoryIdRequest: RequestBody = RequestBody.create("text/plain".toMediaTypeOrNull(), dormitoryId.toString())
        val nicknameRequest: RequestBody = RequestBody.create("text/plain".toMediaTypeOrNull(), nickname)
        mapData.put("dormitoryId", dormitoryIdRequest)
        mapData.put("nickname", nicknameRequest)

        profileDataService.profileMemberInfoModifySender(profileImg,mapData) // 프로필 수정 api 호출 ★
    }

    override fun onProfileMemberInfoModifySuccess(result: ProfileMemberInfoModifyResult) {
        Log.d("sendImg", "성공:"+result.toString())
        saveDormitoryId(result.dormitoryId)
        saveDormitory("제 "+result.dormitoryName)
        saveNickname(result.nickname)
        saveProfileImgUrl(result.profileImgUrl)
    }

    override fun onProfileMemberInfoModifyFailure(message: String) {
        Log.d("sendImg", "실패:"+ message)
    }

    // 절대경로 변환
    fun absolutelyPath(path: Uri): String {
        var proj: Array<String> = arrayOf(MediaStore.Images.Media.DATA)
        var c: Cursor = requireActivity().contentResolver.query(path, proj, null, null, null)!!
        var index = c.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
        c.moveToFirst()
        var result = c.getString(index)
        return result
    }
}