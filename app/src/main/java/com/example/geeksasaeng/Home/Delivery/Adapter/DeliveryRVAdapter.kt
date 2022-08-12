package com.example.geeksasaeng.Home.Delivery.Adapter

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.os.CountDownTimer
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.geeksasaeng.Home.Delivery.DeliveryPartiesVoList
import com.example.geeksasaeng.Home.Delivery.Timer.DeliveryTimer
import com.example.geeksasaeng.R
import okhttp3.internal.notify
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class DeliveryRVAdapter(private var deliveryList: ArrayList<DeliveryPartiesVoList?>,
                        private var deliveryTimer: DeliveryTimer) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var dateFormat: SimpleDateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
    private lateinit var mItemClickListener: OnItemClickListener
    var minuteFlag: Boolean = false

    // 클릭 리스너 구현 위한 인터페이스
    interface OnItemClickListener {
        fun onItemClick(data: DeliveryPartiesVoList, pos: Int)
    }

    fun setOnItemClickListener(listener: OnItemClickListener) {
        mItemClickListener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_delivery, parent, false)
        return ItemViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: RecyclerView.ViewHolder, position: Int) {
        itemBind(viewHolder as ItemViewHolder, position)
        timerBind(viewHolder, position)
        viewHolder.itemView.setOnClickListener {
            mItemClickListener.onItemClick(deliveryList[position]!!, position)
        }
    }

    override fun getItemCount(): Int {
        return if (deliveryList == null) 0 else deliveryList!!.size
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    private inner class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var deliveryItemMemberIc: ImageView = itemView.findViewById(R.id.delivery_item_member_ic)
        var deliveryItemMemberNumber: TextView =
            itemView.findViewById(R.id.delivery_item_member_number)
        var deliveryItemTime: TextView = itemView.findViewById(R.id.delivery_item_time)
        var deliveryItemTitle: TextView = itemView.findViewById(R.id.delivery_item_title)
        var deliveryItemCategory: TextView = itemView.findViewById(R.id.delivery_item_category)
        var deliveryItemHashTag: TextView = itemView.findViewById(R.id.delivery_item_hashTag)
//        var timer: TimerTask? = null
    }

    // 타이머를 제외한 나머지 부분 Binding
    private fun itemBind(viewHolder: ItemViewHolder, position: Int) {
        val item = deliveryList!![position]

        // (최대 멤버 - 현재 매칭 멤버 = 1)인 상황에는 파란색 아이콘, 아닐 경우 회색 아이콘을 구분하기 위한 부분
        if (item!!.maxMatching!! - item!!.currentMatching!! == 1) {
            viewHolder.deliveryItemMemberIc.setImageResource(R.drawable.ic_member_blue)
        } else {
            viewHolder.deliveryItemMemberIc.setImageResource(R.drawable.ic_member_gray)
        }

        viewHolder.deliveryItemMemberNumber.setText(item!!.currentMatching.toString() + "/" + item!!.maxMatching)
        viewHolder.deliveryItemTitle.setText(item!!.title)
        viewHolder.deliveryItemCategory.setText(item!!.foodCategory)

        if (item!!.hasHashTag!!) {
            viewHolder.deliveryItemHashTag.setTextColor(Color.parseColor("#636363"))
        } else if (!item!!.hasHashTag!!) {
            viewHolder.deliveryItemHashTag.setTextColor(Color.parseColor("#EFEFEF"))
        }
    }
    // 타이머 부분 Binding
    private fun timerBind(viewHolder: ItemViewHolder, position: Int) {
        // 타이머 세팅
        val textView = viewHolder.deliveryItemTime
        deliveryTimer.removeExistTextView(textView)
        val leftTime = dateFormat.parse(deliveryList[position]!!.orderTime).time
        deliveryTimer.addTimerData(textView, leftTime)
    }

    fun getDeliveryItemId(position: Int): Int? {
        return deliveryList[position]?.id
    }
}