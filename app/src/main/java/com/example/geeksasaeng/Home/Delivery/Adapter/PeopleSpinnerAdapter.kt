package com.example.geeksasaeng.Home.Delivery.Adapter

import com.example.geeksasaeng.R
import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat


class PeopleSpinnerAdapter(context: Context, spinnerList: Array<String>?) :
        BaseAdapter() {
        private val context: Context
        private val spinnerList: Array<String>?
        override fun getCount(): Int {
            return spinnerList?.size ?: 0
        }

        override fun getItem(i: Int): Any {
            return spinnerList!![i]
        }

        override fun getItemId(i: Int): Long {
            return 0
        }

        override fun getView(i: Int, view: View?, viewGroup: ViewGroup?): View {
            val rootView: View = LayoutInflater.from(context)
                .inflate(R.layout.item_spinner, viewGroup, false)
            val textName: TextView = rootView.findViewById(R.id.spinner_text)
            val image: ImageView = rootView.findViewById(R.id.arrow_iv)
            //TODO:일단 이게 맞나 싶긴 한데,,
            if (i == 0) { //젤 상단 아이템
                textName.setText(spinnerList!![i])
                image.setImageResource(R.drawable.ic_spinner_down)
                /*textName.setTextColor(R.color.main)*/
                textName.setTextColor(ContextCompat.getColor(context, R.color.main)) //젤 상단 main색으로 바꾸기
            } else { //나머지 아이템들
                textName.setText(spinnerList!![i])
                image.visibility = View.INVISIBLE

                //색상설정
                if (textName.text==spinnerList!![0]){
                    textName.setTextColor(ContextCompat.getColor(context, R.color.gray_0))
                } else {
                    textName.setTextColor(ContextCompat.getColor(context, R.color.gray_2))
                }
            }

            return rootView
        }

        init {
            this.context = context
            this.spinnerList = spinnerList
        }
}