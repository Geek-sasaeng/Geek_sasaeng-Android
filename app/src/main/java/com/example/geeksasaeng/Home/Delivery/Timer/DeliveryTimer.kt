package com.example.geeksasaeng.Home.Delivery.Timer

import android.os.CountDownTimer
import android.util.Log
import android.widget.TextView
import java.text.SimpleDateFormat
import java.util.*

class DeliveryTimer(val textView: TextView, millisInFuture: Long,
                    countDownInterval: Long): CountDownTimer(millisInFuture, countDownInterval) {
    private var dateFormat: SimpleDateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
    var nowTime: Long = 0
    var date: Date? = null
    var todaySec: Int = 0

    override fun onTick(millsUntilFinished: Long) {
        val date = Date(millsUntilFinished)
        val leftTime = dateFormat.format(date)
        textView.setText(calculateTime(leftTime))
    }

    override fun onFinish() {
        textView.setText("0 초")
    }

    // 남은 시간 계산
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

    // 오늘 날짜 계산
    private fun calculateToday(): String {
        nowTime = System.currentTimeMillis();
        date = Date(nowTime)
        return dateFormat.format(date)
    }
}