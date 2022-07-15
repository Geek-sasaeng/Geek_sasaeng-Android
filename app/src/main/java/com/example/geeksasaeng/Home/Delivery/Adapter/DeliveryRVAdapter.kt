package com.example.geeksasaeng.Home.Delivery.Adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.geeksasaeng.Data.Delivery
import com.example.geeksasaeng.R

class DeliveryRVAdapter(var deliveryList: ArrayList<Delivery?>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val VIEW_TYPE_ITEM = 0
    private val VIEW_TYPE_LOADING = 1

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == VIEW_TYPE_ITEM) {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.item_delivery, parent, false)
            ItemViewHolder(view)
        } else {
            val view =
                LayoutInflater.from(parent.context).inflate(R.layout.item_loading, parent, false)
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
        var deliveryItemMemberIc : ImageView
        var deliveryItemMemberNumber :TextView
        var deliveryItemTime : TextView
        var deliveryItemTitle : TextView
        var deliveryItemOption1 : TextView
        var deliveryItemOption2 : TextView

        init {
            deliveryItemMemberIc = itemView.findViewById(R.id.delivery_item_member_ic)
            deliveryItemMemberNumber = itemView.findViewById(R.id.delivery_item_member_number)
            deliveryItemTime = itemView.findViewById(R.id.delivery_item_time)
            deliveryItemTitle = itemView.findViewById(R.id.delivery_item_title)
            deliveryItemOption1 = itemView.findViewById(R.id.delivery_item_option1)
            deliveryItemOption2 = itemView.findViewById(R.id.delivery_item_option2)
        }
    }

    private inner class LoadingViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {
        var progressBar: ProgressBar = itemView.findViewById(R.id.progressBar)
    }

    private fun showLoadingView(viewHolder: LoadingViewHolder, position: Int) { }

    private fun populateItemRows(viewHolder: ItemViewHolder, position: Int) {
        val item = deliveryList!![position]
        viewHolder.deliveryItemMemberIc.setImageResource(R.drawable.ic_member_blue)
        viewHolder.deliveryItemMemberNumber.setText(item?.currentMember.toString() + "/" + item?.totalMember.toString())
        viewHolder.deliveryItemTime.setText(item?.time)
        viewHolder.deliveryItemTitle.setText(item?.title)
        viewHolder.deliveryItemOption1.setText("option1")
        viewHolder.deliveryItemOption2.setText("option2")
    }
}