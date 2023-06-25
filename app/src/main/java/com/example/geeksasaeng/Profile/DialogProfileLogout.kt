package com.example.geeksasaeng.Profile

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.LinearSmoothScroller
import androidx.recyclerview.widget.RecyclerView
import com.example.geeksasaeng.Chatting.ChattingList.Retrofit.ChattingListService
import com.example.geeksasaeng.ChatSetting.ChatRoomDB.ChatDatabase
import com.example.geeksasaeng.Login.LoginActivity
import com.example.geeksasaeng.MainActivity
import com.example.geeksasaeng.Profile.Retrofit.ProfileDataService
import com.example.geeksasaeng.Profile.Retrofit.ProfileLogoutView
import com.example.geeksasaeng.R
import com.example.geeksasaeng.Utils.CustomToastMsg
import com.example.geeksasaeng.Utils.removeAutoLogin
import com.example.geeksasaeng.Utils.removeIsSocial
import com.example.geeksasaeng.databinding.DialogLogoutBinding
import com.navercorp.nid.NaverIdLoginSDK.applicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class DialogProfileLogout: DialogFragment(), ProfileLogoutView {

    lateinit var binding: DialogLogoutBinding
    lateinit var profileDataService: ProfileDataService
    // RoomDB
    private lateinit var chatDB: ChatDatabase

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DialogLogoutBinding.inflate(inflater, container, false)
        chatDB = ChatDatabase.getDBInstance(dialog?.context!!)!!

        initListener()

        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT)) // 배경 투명하게 만들어줘야 둥근 테두리가 보인다.
        return binding.root
    }

    private fun initListener(){
        binding.logoutOkBtn.setOnClickListener {
            this.dismiss()
            (context as MainActivity).finish()
            removeAutoLogin()

            CoroutineScope(Dispatchers.IO).launch {
                chatDB.chatDao().deleteAllChats()
            }

            startActivity(Intent(activity, LoginActivity::class.java))
            profileDataService = ProfileDataService()
            profileDataService.setProfileLogoutView(this)
            profileDataService.profileLogoutSender()
        }

        binding.logoutCancelBtn.setOnClickListener {
            this.dismiss()
        }
    }

    override fun onResume() {
        super.onResume()
        val width = resources.getDimensionPixelSize(R.dimen.party_request_width)
        val height = resources.getDimensionPixelSize(R.dimen.chatting_exit_popup_height)
        dialog?.window?.setLayout(width,height)
    }

    override fun onProfileLogoutSuccess(result: String) {
        this.dismiss()
        (context as MainActivity).finish()
        removeAutoLogin()
        CustomToastMsg.createToast(requireContext(), "로그아웃 되었습니다", "#8029ABE2", 53)?.show()
        startActivity(Intent(activity, LoginActivity::class.java))
    }

    override fun onProfileLogoutFailure(message: String) {
        Log.d("logout 실패", message)
    }
}