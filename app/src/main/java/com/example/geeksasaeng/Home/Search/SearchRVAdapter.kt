package com.example.geeksasaeng.Home.Search

import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.geeksasaeng.Home.Delivery.DeliveryPartiesVoList
import com.example.geeksasaeng.Home.Delivery.DeliveryResult
import com.example.geeksasaeng.Home.Search.Retrofit.SearchPartiesVoList
import com.example.geeksasaeng.R

class SearchRVAdapter(private var searchList: ArrayList<SearchPartiesVoList?>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private lateinit var mItemClickListener : OnItemClickListener

    // 클릭 리스너 구현 위한 인터페이스
    interface OnItemClickListener{
        fun onItemClick(data: SearchPartiesVoList, pos : Int)
    }

    fun setOnItemClickListener(listener : OnItemClickListener) {
        mItemClickListener = listener
    }

    private val VIEW_TYPE_ITEM = 0

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_delivery, parent, false)
        return ItemViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: RecyclerView.ViewHolder, position: Int) {
        populateItemRows(viewHolder as ItemViewHolder, position)

        viewHolder.itemView.setOnClickListener {
            // val intent = Intent(holder.itemView?.context, PostSelectImgActivity::class.java)
            // intent.putExtra("position", position)
            mItemClickListener.onItemClick(searchList[position]!!, position)
            Log.d("ItemClickCheck", "position = $position")

            // imgList[position].img!!
            // ContextCompat.startActivity(holder.itemView.context, intent, null)
        }
    }

    override fun getItemCount(): Int {
        return if (searchList == null) 0 else searchList!!.size
    }

    override fun getItemViewType(position: Int): Int {
        return VIEW_TYPE_ITEM
    }

    private inner class ItemViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView)
    {
        // 메인 파티 리스트 부분
        // Not Use = chief, content, currentMatching, foodCategory, id, location
        // Use = currentMatching, maxMatching, orderTime, title

        var deliveryItemMemberIc : ImageView
        var deliveryItemMemberNumber :TextView
        var deliveryItemTime : TextView
        var deliveryItemTitle : TextView
        var deliveryItemCategory : TextView
        var deliveryItemHashTag : TextView

        init {
            deliveryItemMemberIc = itemView.findViewById(R.id.delivery_item_member_ic)
            deliveryItemMemberNumber = itemView.findViewById(R.id.delivery_item_member_number)
            deliveryItemTime = itemView.findViewById(R.id.delivery_item_time)
            deliveryItemTitle = itemView.findViewById(R.id.delivery_item_title)
            deliveryItemCategory = itemView.findViewById(R.id.delivery_item_category)
            deliveryItemHashTag = itemView.findViewById(R.id.delivery_item_hashTag)
        }
    }

    private fun populateItemRows(viewHolder: ItemViewHolder, position: Int) {
        // 메인 파티 리스트 부분
        // Use = currentMatching, maxMatching, orderTime, title

        val item = searchList!![position]

        // (최대 멤버 - 현재 매칭 멤버 = 1)인 상황에는 파란색 아이콘, 아닐 경우 회색 아이콘을 구분하기 위한 부분
        if (item!!.maxMatching!! - item!!.currentMatching!! == 1) {
            viewHolder.deliveryItemMemberIc.setImageResource(R.drawable.ic_member_blue)
        } else {
            viewHolder.deliveryItemMemberIc.setImageResource(R.drawable.ic_member_gray)
        }

        viewHolder.deliveryItemMemberNumber.setText(item!!.currentMatching.toString() + "/" + item!!.maxMatching)
        viewHolder.deliveryItemTime.setText(item!!.orderTime)
        viewHolder.deliveryItemTitle.setText(item!!.title)
        viewHolder.deliveryItemCategory.setText(item!!.foodCategory)

        if (item!!.hasHashTag!!) {
            viewHolder.deliveryItemHashTag.setTextColor(Color.parseColor("#636363"))
        } else if (!item!!.hasHashTag!!) {
            viewHolder.deliveryItemHashTag.setTextColor(Color.parseColor("#EFEFEF"))
        }
    }

    fun getSearchItemId(position: Int): Int? {
        return searchList[position]?.id
    }

}