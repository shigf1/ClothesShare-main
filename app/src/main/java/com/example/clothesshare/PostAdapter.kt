package com.example.clothesshare

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import android.widget.Toast
import android.util.Log



class PostAdapter(private val posts: List<PostItem>, private val onItemClick: (PostItem) -> Unit) :
    RecyclerView.Adapter<PostAdapter.ViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int):
            ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.cv_post_design, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        // set current post information
        val post = posts[position]

        Glide.with(holder.itemView.context)
            .load(post.image) // Use the URL from Firebase
            .into(holder.ivPostImage)

        // sets username to tvUsername TextView
        holder.tvUsername.text = post.username

        // sets description to tvDescription TextView
        holder.tvDescription.text = post.description

        // In PostAdapter.onBindViewHolder(...)
        holder.nextExpoButton.setOnClickListener {
               // now actually delegate back to MainActivity
            onItemClick(post)
        }


    }

    // return size/amount of posts
    override fun getItemCount() = posts.size


    // holds views for post details
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val ivPostImage: ImageView = itemView.findViewById(R.id.ivPost)
        val tvUsername: TextView = itemView.findViewById(R.id.cvUsername)
        val tvDescription: TextView = itemView.findViewById(R.id.tvPost)
        val nextExpoButton: Button = itemView.findViewById(R.id.next_expo)



    }
}