package com.example.geeksasaeng.Chatting.ChattingRoom

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.example.geeksasaeng.Chatting.ChattingList.ParticipantsInfo
import com.example.geeksasaeng.Chatting.Retrofit.ChattingDataService
import com.example.geeksasaeng.Chatting.Retrofit.MatchingEndView
import com.example.geeksasaeng.R
import com.example.geeksasaeng.Signup.Naver.SignUpNaverActivity
import com.example.geeksasaeng.Utils.CustomToastMsg
import com.example.geeksasaeng.Utils.getNickname
import com.example.geeksasaeng.databinding.DialogMatchingEndLayoutBinding
import java.text.SimpleDateFormat
import java.util.*

class DialogMatchingEnd: DialogFragment(), MatchingEndView {

    lateinit var binding: DialogMatchingEndLayoutBinding
    private lateinit var chattingService: ChattingDataService
    lateinit var uuid: String

    override fun onStart() {
        super.onStart()
        chattingService = ChattingDataService() //서비스 객체생성
        chattingService.setMatchingEndView(this)
    }

    override fun onResume() {
        super.onResume()
        // TODO: 테스트해보고 수정하기 (폰에 따라 크기 다르게 지정?!)
        val width = resources.getDimensionPixelSize(R.dimen.matching_end_popup_width) //256dp
        val height = resources.getDimensionPixelSize(R.dimen.matching_end_popup_height) //250dp
        dialog?.window?.setLayout(width,height)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DialogMatchingEndLayoutBinding.inflate(inflater, container, false)
        initListener()
        uuid = requireArguments().getString("roomUuid")!!
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT)) // 배경 투명하게 만들어줘야 둥근 테두리가 보인다.
        return binding.root
    }

    private fun initListener(){
        binding.matchingEndOkayBtn.setOnClickListener { //완료버튼
            chattingService.matchingEndSender(uuid) //★매칭마감 api호출
        }

        binding.matchingEndCancelBtn.setOnClickListener { //X버튼
           this.dismiss()
        }
    }

    //매칭마감 성공
    override fun onMatchingEndSuccess() {
        Log.d("matchingEnd", "매칭 마감 실패")
        this.dismiss()
        Toast.makeText(requireContext(), "매칭이 마감되었습니다", Toast.LENGTH_SHORT).show() //TODO: 일단은 시스템 메세지로 해뒀는데 이거 FIGMA에서 커스텀 되어있다..
        // TODO: 매칭 마감 성공
    }

    //매칭마감 실패
    override fun onMatchingEndFailure(message: String) {
        CustomToastMsg.createToast((activity as SignUpNaverActivity), message, "#80A8A8A8", 53)?.show()
        Log.d("matchingEnd", "매칭 마감 실패")

    }

    private fun getCurrentDateTime(): String {
        val formatter = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        return formatter.format(Calendar.getInstance().time)
    }
}