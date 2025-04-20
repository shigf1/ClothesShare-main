package com.example.clothesshare

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.card.MaterialCardView

class ConversationAdapter(private val messages: List<ConversationItem>) :
    RecyclerView.Adapter<ConversationAdapter.ViewHolder>(){

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.conversations_design, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val message = messages[position]

        holder.messagesUsername.text = message.username

        holder.message.text = message.message

        holder.messageDate.text = message.message_date.toString()

        val layoutParams = holder.cardView.layoutParams as ViewGroup.MarginLayoutParams

        // if the username of post is other than the user of the phone, align it to the left of the screen
        // and give it a grey background color
        // CHANGE THIS IF STATEMENT WHEN USERNAME CHECK IS FLESHED OUT
        if (position % 2 == 0) {

            // align to the left
            layoutParams.marginStart = 0
            layoutParams.marginEnd = ViewGroup.LayoutParams.WRAP_CONTENT
            holder.cardView.setBackgroundColor(Color.LTGRAY)
        }
        // if the username is the same as the user of the phone, align it to the right of the phone
        else {

            // align to the right
            layoutParams.marginStart = ViewGroup.LayoutParams.WRAP_CONTENT
            layoutParams.marginEnd = 0
        }
        holder.cardView.layoutParams = layoutParams
    }

    override fun getItemCount() = messages.size

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view){
        val cardView: MaterialCardView = itemView.findViewById(R.id.material_card_view)
        val messagesUsername: TextView = itemView.findViewById(R.id.messages_username)
        val message: TextView = itemView.findViewById(R.id.message)
        val messageDate: TextView = itemView.findViewById(R.id.message_date)
    }
}