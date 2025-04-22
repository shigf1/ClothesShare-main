package com.example.clothesshare.ui

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.clothesshare.ConversationItem
import com.example.clothesshare.databinding.ActivityConversationBinding
import com.google.firebase.auth.FirebaseAuth
import com.example.clothesshare.ConversationAdapter
import com.example.clothesshare.R
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.text.SimpleDateFormat
import java.util.*

class ConversationActivity : AppCompatActivity() {

    private lateinit var binding: ActivityConversationBinding
    private lateinit var conversationRecyclerView: RecyclerView
    private lateinit var conversationList: ArrayList<ConversationItem>
    private lateinit var adapter: ConversationAdapter
    private lateinit var conversationId: String
    private val currentUser = FirebaseAuth.getInstance().currentUser

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityConversationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val conversationId = intent.getStringExtra("conversation_id") ?: "mock_conversation_123"

        // Initialize UI with sample data
        setupRecyclerView()
        loadSampleData() // Load mock data instead of Firebase data

        setupSendButton()
    }

    private fun setupRecyclerView() {
        conversationRecyclerView = binding.conversationRecyclerView // Make sure this matches your XML

        conversationRecyclerView.layoutManager = LinearLayoutManager(this)
        conversationRecyclerView.setHasFixedSize(true)

        conversationList = arrayListOf()
        adapter = ConversationAdapter(conversationList)
        conversationRecyclerView.adapter = adapter
    }

    private fun loadSampleData() {
        conversationList.add(ConversationItem(
            username= "Test User",
            message = "This is a sample message",
            message_date = SimpleDateFormat("MMM dd, HH:mm", Locale.getDefault()).format(Date()),
            isCurrentUser = false
        )
        )
        conversationList.add(ConversationItem(
            username = "Test User",
            message = "This is a sample message",
            message_date = SimpleDateFormat("MMM dd, HH:mm", Locale.getDefault()).format(Date()),
            isCurrentUser = true
        )
        )
        conversationList.add(ConversationItem(
            username = "Test User",
            message = "This is a sample message",
            message_date = SimpleDateFormat("MMM dd, HH:mm", Locale.getDefault()).format(Date()),
            isCurrentUser = false
        )
        )
        conversationList.add(ConversationItem(
            username = "Test User",
            message = "This is a sample message",
            message_date = SimpleDateFormat("MMM dd, HH:mm", Locale.getDefault()).format(Date()),
            isCurrentUser = false
        )
        )
        conversationList.add(ConversationItem(
            username = "Test User",
            message = "This is a sample message",
            message_date = SimpleDateFormat("MMM dd, HH:mm", Locale.getDefault()).format(Date()),
            isCurrentUser = true
        )
        )
        conversationList.add(ConversationItem(
            username = "Test User",
            message = "This is a sample message",
            message_date = SimpleDateFormat("MMM dd, HH:mm", Locale.getDefault()).format(Date()),
            isCurrentUser = false
        )
        )
        conversationList.add(ConversationItem(
            username = "Test User",
            message = "This is a sample message",
            message_date = SimpleDateFormat("MMM dd, HH:mm", Locale.getDefault()).format(Date()),
            isCurrentUser = true
        )
        )
        conversationList.add(ConversationItem(
            username = "Test User",
            message = "This is a sample message",
            message_date = SimpleDateFormat("MMM dd, HH:mm", Locale.getDefault()).format(Date()),
            isCurrentUser = true
        )
        )
        adapter.notifyDataSetChanged()
    }

    private fun loadConversationItems() {
        FirebaseDatabase.getInstance().getReference("chats/$conversationId")
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val messages = mutableListOf<ConversationItem>()
                    val currentUserId = FirebaseAuth.getInstance().currentUser?.uid ?: ""

                    for (itemSnapshot in snapshot.children) {
                        val message = itemSnapshot.getValue(ConversationItem::class.java)?.copy()
                        message?.let {
                            // Create a new copy with isCurrentUser set
                            messages.add(it.copy(isCurrentUser = it.username == currentUserId))
                        }
                    }

                    (binding.conversationRecyclerView.adapter as? ConversationAdapter)?.let { adapter ->
                        adapter.messages.clear()
                        adapter.messages.addAll(messages)
                        adapter.notifyDataSetChanged()
                        binding.conversationRecyclerView.smoothScrollToPosition(adapter.messages.size - 1)
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    // Handle error
                }
            })
    }

    private fun setupSendButton() {
        binding.sendButton.setOnClickListener {
            val messageText = binding.messageInput.text.toString().trim()
            if (messageText.isNotEmpty()) {
                sendMessage(messageText)
            }
        }
    }

    private fun sendMessage(message: String) {
        val currentUserId = FirebaseAuth.getInstance().currentUser?.uid ?: ""
        val conversationItem = ConversationItem(
            username = currentUserId,
            message = message,
            message_date = SimpleDateFormat("HH:mm", Locale.getDefault()).format(Date()),
            isCurrentUser = true
        )

        // Add to adapter
        (binding.conversationRecyclerView.adapter as? ConversationAdapter)?.addMessage(conversationItem)
        binding.conversationRecyclerView.smoothScrollToPosition(
            (binding.conversationRecyclerView.adapter?.itemCount ?: 1) - 1
        )

        // Send to Firebase
        FirebaseDatabase.getInstance().getReference("chats/$conversationId")
            .push()
            .setValue(conversationItem)
    }
}