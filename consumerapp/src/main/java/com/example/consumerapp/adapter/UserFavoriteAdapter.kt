package com.example.consumerapp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.consumerapp.R
import com.example.consumerapp.entity.UserItems
import kotlinx.android.synthetic.main.item_row_favorite_user.view.*

class UserFavoriteAdapter: RecyclerView.Adapter<UserFavoriteAdapter.FavoriteUserViewHolder>() {

    private lateinit var onButtonDeleteClickCallback: OnButtonDeleteClickCallback


    fun setOnButtonDeleteClickCallback(onButtonDeleteClickCallback: OnButtonDeleteClickCallback) {
        this.onButtonDeleteClickCallback = onButtonDeleteClickCallback
    }


    var listFavoriteUsers = ArrayList<UserItems>()
        set(value) {
            if (value.size > 0){
                this.listFavoriteUsers.clear()
            }
            this.listFavoriteUsers.addAll(value)
            notifyDataSetChanged()
        }

    inner class FavoriteUserViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        fun bind(userItems : UserItems){
            with(itemView){
                tv_username.text = userItems.username
                tv_id.text = userItems.id.toString()
                Glide.with(itemView.context)
                    .load(userItems.avatar)
                    .apply(RequestOptions().override(60,60))
                    .into(img_avatar)
                btn_delete.setOnClickListener{
                    userItems.id?.let { it1 -> onButtonDeleteClickCallback.onButtonDeleteClicked(it1, adapterPosition) }
                }

            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteUserViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_row_favorite_user, parent, false)
        return FavoriteUserViewHolder(view)
    }

    override fun getItemCount(): Int {
        return listFavoriteUsers.size
    }

    override fun onBindViewHolder(holder: FavoriteUserViewHolder, position: Int) {
        holder.bind(listFavoriteUsers[position])
    }

    fun removeItem(position: Int) {
        this.listFavoriteUsers.removeAt(position)
        notifyItemRemoved(position)
        notifyItemRangeChanged(position, this.listFavoriteUsers.size)
    }

    interface OnButtonDeleteClickCallback {
        fun onButtonDeleteClicked(id: Int, position: Int)
    }

}