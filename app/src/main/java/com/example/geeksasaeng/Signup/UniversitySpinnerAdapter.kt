package com.example.geeksasaeng.Signup

import com.example.geeksasaeng.R
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.Dimension
import androidx.core.content.ContextCompat

class UniversitySpinnerAdapter(context: Context, spinnerList: Array<String>?) : BaseAdapter() {

    private val context: Context = context
    private val spinnerList: Array<String>? = spinnerList

    override fun getCount(): Int {
        return (spinnerList?.size ?: 0) -1
    }
    //마지막 아이템은 힌트용으로 사용할 것이므로 getCount에서 1을 빼준다.

    override fun getItem(i: Int): Any {
        return spinnerList!![i]
    }

    override fun getItemId(i: Int): Long {
        return 0
    }

    override fun getView(i: Int, view: View?, viewGroup: ViewGroup?): View {
        Log.d("cherry", "스피너 뷰 생성됨")
        val rootView: View = LayoutInflater.from(context).inflate(R.layout.item_signup_university_spinner, viewGroup, false)
        val textName: TextView = rootView.findViewById(R.id.spinner_university_text)
        val image: ImageView = rootView.findViewById(R.id.university_arrow_iv)

        if (i == 0) { // 제일 상단 아이템
            textName.setText("자신의 학교를 선택해주세요")
            textName.setTextColor(ContextCompat.getColor(context, R.color.gray_1))
            image.setImageResource(R.drawable.ic_spinner_down)
        } else if (spinnerList!![i].length == 1) { // 자음 아이템 (한 글자인 것을 통해 판단)
            textName.setText(spinnerList!![i])
            image.visibility = View.INVISIBLE
            textName.setTextColor(ContextCompat.getColor(context, R.color.gray_2))
            textName.setTextSize(Dimension.SP, 12F)
            view?.isEnabled = false
            view?.isClickable = false
        } else { // 학교 아이템
            textName.setText(spinnerList!![i])
            image.visibility = View.INVISIBLE
            textName.setTextColor(ContextCompat.getColor(context, R.color.gray_3))
        }

        return rootView
    }
}