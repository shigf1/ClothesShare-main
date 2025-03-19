package com.example.clothesshare

import android.text.Layout
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class PostAdapter(private val posts: List<PostItem>) :
RecyclerView.Adapter<PostAdapter.ViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int):
            ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.cv_post_list_design, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val post = posts[position]

        holder.ivPostImage.setImageResource(post.image)

        holder.tvUsername.text = post.username

        holder.tvDescription.text = post.description
    }

    override fun getItemCount() = posts.size

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val ivPostImage: ImageView = itemView.findViewById(R.id.post_image)
        val tvUsername: TextView = itemView.findViewById(R.id.username)
        val tvDescription: TextView = itemView.findViewById(R.id.post_description)
    }
}