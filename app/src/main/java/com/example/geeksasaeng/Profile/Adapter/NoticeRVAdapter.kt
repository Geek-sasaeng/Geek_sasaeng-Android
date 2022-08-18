package com.example.geeksasaeng.Profile.Adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.geeksasaeng.Profile.NoticeFragment
import com.example.geeksasaeng.Profile.Retrofit.ProfileAnnouncementResult
import com.example.geeksasaeng.R
import com.example.geeksasaeng.databinding.ItemNoticeBinding

class NoticeRVAdapter(private var noticeList: ArrayList<ProfileAnnouncementResult>) : RecyclerView.Adapter<NoticeRVAdapter.ViewHolder>(){

    // 클릭 인터페이스 정의
    interface OnItemClickListener{
        fun onItemClick(announcement: ProfileAnnouncementResult, pos: Int)
    }

    // 리스너 객체를 전달받는 함수랑 리스너 객체를 저장할 변수
    private lateinit var mItemClickListener: OnItemClickListener

    fun setOnItemClickListener(itemClickListener: OnItemClickListener){
        mItemClickListener = itemClickListener
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): NoticeRVAdapter.ViewHolder {
        val binding: ItemNoticeBinding = ItemNoticeBinding.inflate(LayoutInflater.from(viewGroup.context), viewGroup, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: NoticeRVAdapter.ViewHolder, position: Int) {
        holder.bind(noticeList[position])
        holder.binding.noticeItemBtn.setOnClickListener { mItemClickListener.onItemClick(noticeList[position], position) } //화살표에 클릭리스너 달아줌
        // holder.itemView.setOnClickListener { mItemClickListener.onItemClick(noticeList[position], position) }
    }

    override fun getItemCount(): Int = noticeList.size

    fun getNoticeItemId(position: Int): Int? {
        return noticeList[position]?.id
    }

    // 뷰홀더
    inner class ViewHolder(val binding: ItemNoticeBinding): RecyclerView.ViewHolder(binding.root){

        fun bind(announcement: ProfileAnnouncementResult){
            binding.noticeItemTitleTv.text = announcement.title
            /*if(announcement.status == "ACTIVE"){
                binding.noticeItemIconIv.setImageResource(R.drawable.ic_notice)
                binding.noticeItemTitleTv.setTextColor(Color.parseColor("#000000"))
            }else{
                binding.noticeItemIconIv.setImageResource(R.drawable.ic_notice_inactive)
                binding.noticeItemTitleTv.setTextColor(Color.parseColor("#636363")) //gray_3
            }*/
        }
    }

}

/*
data class Announcement( //얘랑 같아... ProfileAnnouncementResult
    var announcementId : Int,
    var title : String,
    var content : String,
    var imgUrl: String,
    var createdAt: String,
    var updatedAt: String,
    var status: String
)*/
