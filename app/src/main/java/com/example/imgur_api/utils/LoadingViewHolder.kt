package com.example.imgur_api.utils

import android.view.View
import androidx.recyclerview.widget.RecyclerView

class LoadingViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    companion object {
        val ITEM = 0
        val LOADING = 1
        val BASE_URL_IMG = "https://i.imgur.com"
    }
}