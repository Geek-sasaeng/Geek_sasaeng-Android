package com.example.geeksasaeng.Home.Delivery.Adapter

import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.geeksasaeng.Home.Delivery.DeliveryPartiesVoList
import com.example.geeksasaeng.R
import java.text.SimpleDateFormat
import java.util.*

class DeliveryRVAdapter(private var deliveryList: ArrayList<DeliveryPartiesVoList?>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private lateinit var mItemClickListener : OnItemClickListener

    var nowTime: Long = 0
    var date: Date? = null
    private var dateFormat: SimpleDateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
    var remainSecond: Int = 0

    private val VIEW_TYPE_ITEM = 0

    // 클릭 리스너 구현 위한 인터페이스
    interface OnItemClickListener{
        fun onItemClick(data: DeliveryPartiesVoList, pos : Int)
    }

    // 타이머 구현을 위한 인터페이스
    interface SetTimerListener {
        fun setTimer()
    }

    fun checkTimerRemainSecond(): Int {
        return remainSecond
    }

    fun setOnItemClickListener(listener : OnItemClickListener) {
        mItemClickListener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_delivery, parent, false)
        return ItemViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: RecyclerView.ViewHolder, position: Int) {
        itemBind(viewHolder as ItemViewHolder, position)
        timerBind(viewHolder as ItemViewHolder, position)

        checkTimerRemainSecond()

        Log.d("DELIVERY-ADAPTER", "onBindViewHolder")

        viewHolder.itemView.setOnClickListener {
            mItemClickListener.onItemClick(deliveryList[position]!!, position)
        }
    }

    override fun getItemCount(): Int {
        return if (deliveryList == null) 0 else deliveryList!!.size
    }

    override fun getItemViewType(position: Int): Int {
        return VIEW_TYPE_ITEM
    }

    private inner class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var deliveryItemMemberIc : ImageView = itemView.findViewById(R.id.delivery_item_member_ic)
        var deliveryItemMemberNumber :TextView = itemView.findViewById(R.id.delivery_item_member_number)
        var deliveryItemTime : TextView = itemView.findViewById(R.id.delivery_item_time)
        var deliveryItemTitle : TextView = itemView.findViewById(R.id.delivery_item_title)
        var deliveryItemCategory : TextView = itemView.findViewById(R.id.delivery_item_category)
        var deliveryItemHashTag : TextView = itemView.findViewById(R.id.delivery_item_hashTag)
    }

    // 타이머를 제외한 나머지 부분 Binding
    private fun itemBind(viewHolder: ItemViewHolder, position: Int) {
        // 메인 파티 리스트 부분
        // Use = currentMatching, maxMatching, orderTime, title

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
        viewHolder.deliveryItemTime.setText(calculateTime(deliveryList[position]!!.orderTime.toString()))
    }

    fun getDeliveryItemId(position: Int): Int? {
        return deliveryList[position]?.id
    }

    // 오늘 날짜 계산
    private fun calculateToday(): String {
        nowTime = System.currentTimeMillis();
        date = Date(nowTime)
        return dateFormat.format(date)
    }

    // 남은 시간 계산
    // TODO: 흠... 실시간으로 해야하는데 흠...
    private fun calculateTime(orderTime: String): String {
        var orderYear = Integer.parseInt(orderTime.substring(0, 4))
        var orderMonth = Integer.parseInt(orderTime.substring(5, 7))
        var orderDay = Integer.parseInt(orderTime.substring(8, 10))
        var orderHours = Integer.parseInt(orderTime.substring(11, 13))
        var orderMinutes = Integer.parseInt(orderTime.substring(14, 16))

        var currentTime = calculateToday()
        var todayYear = Integer.parseInt(currentTime.substring(0, 4))
        var todayMonth = Integer.parseInt(currentTime.substring(5, 7))
        var todayDay = Integer.parseInt(currentTime.substring(8, 10))
        var todayHours = Integer.parseInt(currentTime.substring(11, 13))
        var todayMinutes = Integer.parseInt(currentTime.substring(14, 16))
        var todaySeconds = Integer.parseInt(currentTime.substring(17, 19))

        remainSecond = todaySeconds

        var today = Calendar.getInstance().apply {
            set(Calendar.YEAR, todayYear)
            set(Calendar.MONTH, todayMonth)
            set(Calendar.DAY_OF_MONTH, todayDay)
        }.timeInMillis + (60000 * 60 * todayHours) + (60000 * todayMinutes)

        var order = Calendar.getInstance().apply {
            set(Calendar.YEAR, orderYear)
            set(Calendar.MONTH, orderMonth)
            set(Calendar.DAY_OF_MONTH, orderDay)
        }.timeInMillis + (60000 * 60 * orderHours) + (60000 * orderMinutes)

        var remainTime = order - today

        var day = remainTime / (24*60*60*1000)
        var sec = (remainTime % (24*60*60*1000)) / 1000
        var hour = sec / 3600
        var minute = (sec % 3600) / 60

        return if (day > 0)
            "${day}일 ${hour}시간 ${minute}분 남았어요"
        else if (hour > 0)
            "${hour}시간 ${minute}분 남았어요"
        else
            "${minute}분 남았어요"
    }

    /*
    internal class TimerThread : Thread() {
        var sec: Int = 0

        override fun run() {
            while (true) {
                sec++
                try {
                    sleep(1000)
                } catch (e: InterruptedException) {
                    e.printStackTrace()
                }

                if (sec % 60 == 0) {

                }

                Log.d("TIMER-TEST", "sec = $sec")
            }
        }
    }
    */
}