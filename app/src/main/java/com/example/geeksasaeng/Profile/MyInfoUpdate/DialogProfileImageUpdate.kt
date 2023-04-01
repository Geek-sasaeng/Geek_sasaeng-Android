package com.example.geeksasaeng.Profile.MyInfoUpdate

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.fragment.app.DialogFragment
import com.example.geeksasaeng.databinding.DialogProfileUpdateBinding
import com.example.geeksasaeng.databinding.DialogProfileImageUpdateBinding

class DialogProfileImageUpdate : DialogFragment() {
    lateinit var binding: DialogProfileImageUpdateBinding
    private lateinit var listener: ProfileImageAlbumUpdateListener

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DialogProfileImageUpdateBinding.inflate(inflater, container, false)
        listener = context as ProfileImageAlbumUpdateListener
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT)) // 배경 투명하게 만들어줘야 둥근 테두리가 보인다.
        // Dialog 사이즈 조절 하기
        val params = dialog!!.window!!.attributes
        params.width = WindowManager.LayoutParams.WRAP_CONTENT
        params.height = WindowManager.LayoutParams.WRAP_CONTENT
        dialog!!.window!!.attributes = params as WindowManager.LayoutParams
        initClickListener()
        return binding.root
    }

    private fun initClickListener() {
        binding.dialogProfileImageUpdateAlbumTv.setOnClickListener {
            listener.onAlbumClicked()
            dismiss()
        }
        binding.dialogProfileImageUpdateDefaultTv.setOnClickListener {
            listener.onDefaultImageClicked()
            dismiss()
        }
    }

    interface ProfileImageAlbumUpdateListener {
        fun onAlbumClicked()
        fun onDefaultImageClicked()
    }
}