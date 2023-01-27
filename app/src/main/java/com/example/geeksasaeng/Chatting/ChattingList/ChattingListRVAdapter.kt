package com.example.geeksasaeng.Chatting.ChattingList

import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.geeksasaeng.Chatting.ChattingList.Retrofit.ChattingList
import com.example.geeksasaeng.R
import com.example.geeksasaeng.databinding.ItemChattingListBinding
import okhttp3.internal.notify
import java.text.SimpleDateFormat
import java.util.*

class ChattingListRVAdapter(private var chattingList: java.util.ArrayList<ChattingList?>) : RecyclerView.Adapter<ChattingListRVAdapter.ViewHolder>() {

    private lateinit var mItemClickListener : OnItemClickListener

    fun setOnItemClickListener(listener : OnItemClickListener) {
        mItemClickListener = listener
    }

    interface OnItemClickListener {
        fun onItemClick(chatting: ChattingList, position: Int)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding: ItemChattingListBinding = ItemChattingListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.itemView.setOnClickListener {
            mItemClickListener.onItemClick(chattingList[position]!!, position)
        }
        holder.bind(chattingList[position]!!)
    }

//    fun setChattingData(position: Int, chattingList: ChattingList) {
//        chatting.set(position, chattingList)
//        this.notifyItemChanged(position)
//    }

    fun getRoomData(position: Int): ChattingList {
        return chattingList[position]!!
    }

    fun addItem(item: ChattingList) {
        chattingList.add(item)
        this.notifyDataSetChanged()
    }

    fun addAllItems(items: java.util.ArrayList<ChattingList?>) {
        chattingList.addAll(items)
        this.notifyDataSetChanged()
    }

    override fun getItemCount(): Int = chattingList.size

    inner class ViewHolder(val binding: ItemChattingListBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(chatting: ChattingList) {
            binding.itemChattingListTitle.text = chatting.roomTitle
            // 배달파티인지, 거래인지 등을 구분하기 위함
            binding.itemChattingListSectionIv.setImageResource(R.drawable.ic_delivery_party_ic)
            binding.itemChattingListLastChatting.text = chatting.lastChatting
            binding.itemChattingListTimeTv.text = calculateTime(chatting.lastChattingTime)

            if (chatting.lastChattingTime != "")
                Log.d("CHATTING-LIST-TIME-TEST", "current = ${calculateToday()} / chatting = ${chatting.lastChattingTime} / result = ${calculateTime(chatting.lastChattingTime)}")

            binding.itemChattingListChattingNumberTv.text = chatting.newChattingNumber.toString()
            binding.itemChattingListLastChatting.setTextColor(Color.parseColor("#636363"))
        }
    }

    /*
    방금 : 5분 이내의 채팅
    10분 전 - 12시간 전
    오늘 (13시간 이후-그날의 채팅)
    어제
    2일 전
    3일 전
    -----------
    이후로는 날짜 표기
     */

    private var dateFormat: SimpleDateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.KOREAN)
    var nowTime: Long = 0
    var date: Date? = null
    var todaySec: Int = 0

    // 남은 시간 계산
    fun calculateTime(orderTime: String): String {
        if (orderTime != "") {
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

            todaySec = todaySeconds

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

            var remainTime = today - order

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
        } else return ""
    }

    // 오늘 날짜 계산
    fun calculateToday(): String {
        nowTime = System.currentTimeMillis() + 32400000
        date = Date(nowTime)
        return dateFormat.format(date)
    }
}