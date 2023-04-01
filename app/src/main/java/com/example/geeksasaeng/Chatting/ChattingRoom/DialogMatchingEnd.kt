package com.example.geeksasaeng.Chatting.ChattingRoom

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.setFragmentResult
import com.example.geeksasaeng.Chatting.ChattingRoom.Retrofit.ChattingService
import com.example.geeksasaeng.Chatting.ChattingRoom.Retrofit.MatchingEndView
import com.example.geeksasaeng.Home.Party.CreateParty.DialogDt
import com.example.geeksasaeng.R
import com.example.geeksasaeng.Signup.Naver.SignUpNaverActivity
import com.example.geeksasaeng.Utils.CustomToastMsg
import com.example.geeksasaeng.databinding.DialogMatchingEndLayoutBinding
import java.text.SimpleDateFormat
import java.util.*
import kotlin.properties.Delegates

class DialogMatchingEnd: DialogFragment(), MatchingEndView {

    lateinit var binding: DialogMatchingEndLayoutBinding
    private lateinit var chattingService: ChattingService
    private var partyId by Delegates.notNull<Int>()

    private lateinit var dialogMatchingEndNextClickListener: MatchingEndClickListener

    interface MatchingEndClickListener{
        fun onMatchingEndClicked()
    }

    override fun onStart() {
        super.onStart()
        chattingService = ChattingService() //서비스 객체생성
        chattingService.setMatchingEndView(this)
        dialogMatchingEndNextClickListener =  activity as MatchingEndClickListener
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
        partyId = requireArguments().getInt("partyId")!!
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT)) // 배경 투명하게 만들어줘야 둥근 테두리가 보인다.
        return binding.root
    }

    private fun initListener(){
        binding.matchingEndOkayBtn.setOnClickListener { //완료버튼
            chattingService.matchingEndSender(partyId) //★매칭마감 api호출
        }

        binding.matchingEndCancelBtn.setOnClickListener { //X버튼
           this.dismiss()
        }
    }

    //매칭마감 성공
    override fun onMatchingEndSuccess() {
        this.dismiss()
        CustomToastMsg.createToast(requireContext(), "매칭이 마감되었습니다", "#8029ABE2", 53)?.show()

        //1) ChattingRoomActivity에 매칭마감 정보전달
        dialogMatchingEndNextClickListener.onMatchingEndClicked()
        //2) LeaderOptionDialog에 매칭마감 정보전달
        val result = "true"
        setFragmentResult("isMatchFinish", bundleOf("bundleKey" to result))
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