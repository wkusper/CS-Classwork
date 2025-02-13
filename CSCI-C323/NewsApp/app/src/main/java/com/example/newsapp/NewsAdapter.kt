package com.example.newsapp

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.signature.ObjectKey

class NewsAdapter(private var newsTitles: List<String>, private var imageUrl: String? = null) : RecyclerView.Adapter<NewsAdapter.NewsViewHolder>() {

    fun updateData(newTitles: List<String>, newImageUrl: String?) {
        newsTitles = newTitles
        imageUrl = newImageUrl
        notifyDataSetChanged()
        Log.d("NewsAdapter", "Adapter updated with ${newTitles.size} items.")
    }

    // ViewHolder for news items
    class NewsViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val titleTextView: TextView = view.findViewById(R.id.newsTitle)
        val imageView: ImageView = view.findViewById(R.id.newsImage)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsViewHolder {
        // Inflate the item layout
        val view = LayoutInflater.from(parent.context).inflate(R.layout.news_layout, parent, false)
        return NewsViewHolder(view)
    }

    override fun onBindViewHolder(holder: NewsViewHolder, position: Int) {
        // Bind the news title to TextView
        holder.titleTextView.text = newsTitles[position]

        // Use glide libraries to load the image
        imageUrl?.let{
            Glide.with(holder.imageView.context).load(it).into(holder.imageView)
        }

    }

    override fun getItemCount(): Int = newsTitles.size
}