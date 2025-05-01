package com.example.clothesshare

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class MessageAdapter(private val messages: List<MessageItem>, private val onItemClick: (MessageItem) -> Unit) :
    RecyclerView.Adapter<MessageAdapter.ViewHolder>(){

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int):
                ViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.active_conversation_design, parent, false)

            return ViewHolder(view)
        }

    override fun onBindViewHolder(holder: ViewHolder, position: Int){

        // set most recent message information
        val message = messages[position]

        Glide.with(holder.itemView.context)
            .load(message.profile_pic).circleCrop()
            .into(holder.profilePicture)

        // set username to the username tied to the message
        holder.username.text = message.username

        // set the most_recent_message to the most_recent_message tied to the message
        holder.recentMessage.text = message.most_recent_message

        holder.recentMessageDate.text = message.most_recent_message_date


        holder.itemView.setOnClickListener {
            onItemClick(message)
        }
    }

    // return the item count of the recycler view
    override fun getItemCount() = messages.size

    // create a view holder for all of the elements in a message
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view){
        val profilePicture: ImageView = itemView.findViewById(R.id.profile_picture)
        val username: TextView = itemView.findViewById(R.id.username)
        val recentMessage: TextView = itemView.findViewById(R.id.recent_message)
        val recentMessageDate: TextView = itemView.findViewById(R.id.recent_message_date)
    }

}