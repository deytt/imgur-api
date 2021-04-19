package com.example.imgur_api.utils

import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.imgur_api.R

class ImgurViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val tvImgurTitle: TextView = itemView.findViewById(R.id.imgur_title)
    val tvImgurDesc: TextView = itemView.findViewById(R.id.imgur_desc)
    val tvImgurViews: TextView = itemView.findViewById(R.id.imgur_views)
    val tvImgurPosterImg: ImageView = itemView.findViewById(R.id.imgur_poster)
    val tvImgurProgress: ProgressBar = itemView.findViewById(R.id.imgur_progress)
}