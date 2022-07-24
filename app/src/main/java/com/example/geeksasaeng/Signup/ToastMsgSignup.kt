package com.example.geeksasaeng.Signup

import android.content.Context
import android.content.res.Resources
import android.graphics.Color
import android.view.Gravity
import android.view.LayoutInflater
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.example.geeksasaeng.R
import com.example.geeksasaeng.databinding.ToastMsgSignupBinding

object ToastMsgSignup {
    fun createToast(context: Context, message: String, color: String): Toast? {
        val inflater = LayoutInflater.from(context)
        val binding: ToastMsgSignupBinding = DataBindingUtil.inflate(inflater, R.layout.toast_msg_signup, null, false)

        binding.toastMsgSignupTv.text = message
        binding.toastMsgSignupCv.setCardBackgroundColor(Color.parseColor(color))

        return Toast(context).apply {
            setGravity(Gravity.TOP, 0, 16.toPx())
            duration = Toast.LENGTH_LONG
            view = binding.root
        }
    }

    private fun Int.toPx(): Int = (this * Resources.getSystem().displayMetrics.density).toInt()
}