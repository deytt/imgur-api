package com.example.imgur_api.utils

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.drawable.GlideDrawable
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.example.imgur_api.R
import com.example.imgur_api.model.Images
import com.example.imgur_api.model.Imgur
import com.example.imgur_api.utils.LoadingViewHolder.Companion.BASE_URL_IMG
import com.example.imgur_api.utils.LoadingViewHolder.Companion.ITEM
import com.example.imgur_api.utils.LoadingViewHolder.Companion.LOADING


class PaginationAdapter(private val context: Context) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var imgurResults = mutableListOf<Imgur>()
    var isLoadingAdded = false

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return when (viewType) {
            LOADING -> {
                val v2: View = inflater.inflate(R.layout.item_progress, parent, false)
                LoadingViewHolder(v2)
            }
            //ITEM
            else -> getViewHolder(parent, inflater)
        }
    }

    private fun getViewHolder(parent: ViewGroup, inflater: LayoutInflater): RecyclerView.ViewHolder {
        val viewHolder: RecyclerView.ViewHolder
        val v1: View = inflater.inflate(R.layout.item_list, parent, false)
        viewHolder = ImgurViewHolder(v1)
        return viewHolder
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val result: Imgur = imgurResults[position]
        when (getItemViewType(position)) {
            ITEM -> {
                val imgurVH = holder as ImgurViewHolder
                imgurVH.tvImgurTitle.text = result.title
                imgurVH.tvImgurViews.text = "Views: ${result.views}"
                imgurVH.tvImgurDesc.text = "Ups: ${result.ups} | Downs: ${result.downs}\nComments: ${result.comment_count}"

                val image: Images? = result.images?.firstOrNull()
                val imageId = image?.id ?: "nothing"

                Glide
                    .with(context)
                    .load("$BASE_URL_IMG/$imageId.jpg")
                    .listener(object : RequestListener<String, GlideDrawable> {
                        override fun onException(
                            e: Exception,
                            model: String,
                            target: Target<GlideDrawable>,
                            isFirstResource: Boolean
                        ): Boolean {
                            imgurVH.tvImgurProgress.visibility = View.GONE
                            return false
                        }

                        override fun onResourceReady(
                            resource: GlideDrawable,
                            model: String,
                            target: Target<GlideDrawable>,
                            isFromMemoryCache: Boolean,
                            isFirstResource: Boolean
                        ): Boolean {
                            // image ready, hide progress now
                            imgurVH.tvImgurProgress.visibility = View.GONE
                            return false // return false if you want Glide to handle everything else.
                        }
                    })
                    .diskCacheStrategy(DiskCacheStrategy.ALL) // cache both original & resized image
                    .centerCrop()
                    .crossFade()
                    .into(imgurVH.tvImgurPosterImg)
            }
            LOADING -> {}
        }
    }

    override fun getItemCount(): Int = imgurResults.size

    override fun getItemViewType(position: Int): Int {
        return if ((position == imgurResults.size - 1 && isLoadingAdded)) LOADING else ITEM
    }

    /**
     * Helpers
    */
    private fun add(imgur: Imgur) {
        imgurResults.add(imgur)
        notifyItemInserted(imgurResults.size - 1)
    }

    fun addAll(imgurResult: List<Imgur>) {
        for (imgur: Imgur in imgurResult) {
            add(imgur)
        }
    }

    fun removeLoadingFooter() {
        isLoadingAdded = false
        val position = imgurResults.size - 1
        val result: Imgur = getItem(position)
        if (result != null) {
            imgurResults.removeAt(position)
            notifyItemRemoved(position)
        }
    }

    private fun getItem(position: Int): Imgur {
        return imgurResults[position]
    }

}