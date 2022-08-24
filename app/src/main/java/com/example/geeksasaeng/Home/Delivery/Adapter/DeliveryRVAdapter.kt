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
import com.example.geeksasaeng.Home.Search.Retrofit.SearchPartiesVoList
import com.example.geeksasaeng.R
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class DeliveryRVAdapter(private var deliveryList: ArrayList<DeliveryPartiesVoList?>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var dateFormat: SimpleDateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
    private var deliveryTimerMap: HashMap<Int, DeliveryTimer> = HashMap<Int, DeliveryTimer>()
    private lateinit var mItemClickListener: OnItemClickListener
    private var mDeliveryList = deliveryList
    var minuteFlag: Boolean = false

    private val VIEW_TYPE_ITEM = 0

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
        Log.d("why-filter", "onBindViewHolder 실행됨")
        itemBind(viewHolder as ItemViewHolder, position)
        timerBind(viewHolder as ItemViewHolder, position)
        viewHolder.itemView.setOnClickListener {
            mItemClickListener.onItemClick(mDeliveryList[position]!!, position)
        }
    }

    override fun getItemCount(): Int {
        return if (mDeliveryList == null) 0 else mDeliveryList!!.size
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
        //Log.d("why-filter", "타이머바인드 함수 실행됨")
        // 실시간 타이머 ON
        if(!deliveryTimerMap.containsKey(position)) {
            //Log.d("why-filter", "타이머바인드 함수 실행됨2")
            val leftTime = dateFormat.parse(deliveryList[position]!!.orderTime).time
            val deliveryTimer = DeliveryTimer(viewHolder.deliveryItemTime, leftTime, 1000)
            deliveryTimer.start()
            deliveryTimerMap.put(position, deliveryTimer)
        }
    }

    fun getDeliveryItemId(position: Int): Int? {
        return mDeliveryList[position]?.id
    }

    fun setArrayList(list: ArrayList<DeliveryPartiesVoList?>){
        Log.d("why-1",mDeliveryList.toString()+list.toString())
        //mDeliveryList.clear()
        //mDeliveryList.addAll(list)
        mDeliveryList = list //TODO: 얕은복사-왜 얘만되는진 모르겠지만..!
        Log.d("why-2",mDeliveryList.toString()+list.toString())
        notifyDataSetChanged()
    }
}