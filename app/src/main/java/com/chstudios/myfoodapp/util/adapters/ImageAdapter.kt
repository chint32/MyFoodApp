package com.chstudios.myfoodapp.util.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.chstudios.myfoodapp.R
import javax.inject.Inject

class ImageAdapter @Inject constructor() : RecyclerView.Adapter<ImageAdapter.ImageViewHolder>() {

    var images = listOf<String>()
    var flags = mutableListOf<Boolean>()

    inner class ImageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView: ImageView = itemView.findViewById(R.id.select_food_image)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(
            R.layout.rv_list_image_item,
            parent,
            false
        )
        return ImageViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        val currentImage = images[position]

        val url: String? = currentImage

        Glide.with(holder.itemView.context)
            .load(url)
            .placeholder(R.drawable.ic_launcher_background)
            .into(holder.imageView)


        holder.imageView.setOnClickListener{
            holder.imageView.alpha = .5f
            flags[position] = true
        }
    }

    internal fun setListNames(images: List<String>, flags: MutableList<Boolean>) {
        this.images = images
        this.flags = flags
        notifyDataSetChanged()
    }

    override fun getItemCount() = images.size
}