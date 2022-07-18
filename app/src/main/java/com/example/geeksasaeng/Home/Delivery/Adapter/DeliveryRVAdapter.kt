package com.example.geeksasaeng.Home.Delivery.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.geeksasaeng.Home.Delivery.DeliveryResult
import com.example.geeksasaeng.R

class DeliveryRVAdapter(var deliveryList: ArrayList<DeliveryResult?>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val VIEW_TYPE_ITEM = 0
    private val VIEW_TYPE_LOADING = 1

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == VIEW_TYPE_ITEM) {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.item_delivery, parent, false)
            ItemViewHolder(view)
        } else {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.item_loading, parent, false)
            LoadingViewHolder(view)
        }
    }

    override fun onBindViewHolder(viewHolder: RecyclerView.ViewHolder, position: Int) {
        if (viewHolder is ItemViewHolder) {
            populateItemRows(viewHolder, position)
        } else if (viewHolder is LoadingViewHolder) {
            showLoadingView(viewHolder, position)
        }
    }

    override fun getItemCount(): Int {
        return if (deliveryList == null) 0 else deliveryList!!.size
    }

    override fun getItemViewType(position: Int): Int {
        return if (deliveryList!![position] == null) VIEW_TYPE_LOADING else VIEW_TYPE_ITEM
    }

    private inner class ItemViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {
        // 메인 파티 리스트 부분
        // Not Use = chief, content, currentMatching, foodCategory, id, location
        // Use = currentMatching, maxMatching, orderTime, title

        var deliveryItemMemberIc : ImageView
        var deliveryItemMemberNumber :TextView
        var deliveryItemTime : TextView
        var deliveryItemTitle : TextView
        // var deliveryItemOption1 : TextView
        // var deliveryItemOption2 : TextView

        init {
            deliveryItemMemberIc = itemView.findViewById(R.id.delivery_item_member_ic)
            deliveryItemMemberNumber = itemView.findViewById(R.id.delivery_item_member_number)
            deliveryItemTime = itemView.findViewById(R.id.delivery_item_time)
            deliveryItemTitle = itemView.findViewById(R.id.delivery_item_title)
            // deliveryItemOption1 = itemView.findViewById(R.id.delivery_item_option1)
            // deliveryItemOption2 = itemView.findViewById(R.id.delivery_item_option2)
        }
    }

    private inner class LoadingViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {
        var progressBar: ProgressBar = itemView.findViewById(R.id.progressBar)
    }

    private fun showLoadingView(viewHolder: LoadingViewHolder, position: Int) { }

    private fun populateItemRows(viewHolder: ItemViewHolder, position: Int) {
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
        viewHolder.deliveryItemTime.setText(item!!.orderTime)
        viewHolder.deliveryItemTitle.setText(item!!.title)

        // viewHolder.deliveryItemOption1.setText("option1")
        // viewHolder.deliveryItemOption2.setText("option2")
    }
}