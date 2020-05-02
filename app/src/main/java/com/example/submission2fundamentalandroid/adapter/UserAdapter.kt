package com.example.submission2fundamentalandroid.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.submission2fundamentalandroid.R
import com.example.submission2fundamentalandroid.entity.UserItems
import kotlinx.android.synthetic.main.item_row_user.view.*

class UserAdapter: RecyclerView.Adapter<UserAdapter.UserViewAdapter>() {

    private val mData = ArrayList<UserItems>()

    private lateinit var onItemClickCallback: OnItemClickCallback

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback){
        this.onItemClickCallback = onItemClickCallback
    }

    fun setData(items: ArrayList<UserItems>){
        mData.clear()
        mData.addAll(items)
        notifyDataSetChanged()
    }

    inner class UserViewAdapter(itemView: View): RecyclerView.ViewHolder(itemView)  {
        fun bind(userItems: UserItems){
            with(itemView){
                tv_username.text = userItems.username
                tv_id.text = userItems.id.toString()
                Glide.with(itemView.context)
                    .load(userItems.avatar)
                    .apply(RequestOptions().override(60,60))
                    .into(img_avatar)
            }
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewAdapter {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_row_user, parent, false)
        return UserViewAdapter(view)
    }

    override fun getItemCount(): Int {
        return mData.size
    }

    override fun onBindViewHolder(holder: UserViewAdapter, position: Int) {
        holder.bind(mData[position])
        holder.itemView.setOnClickListener{ onItemClickCallback.onItemClicked(mData[holder.adapterPosition]) }
    }

    interface OnItemClickCallback{
        fun onItemClicked(data: UserItems)
    }

}