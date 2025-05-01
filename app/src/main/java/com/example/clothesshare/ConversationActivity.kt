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
import com.example.clothesshare.MessagesActivity
import com.example.clothesshare.R
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.text.SimpleDateFormat
import java.util.*
import android.widget.ImageButton
import android.widget.Toast
import androidx.cardview.widget.CardView
import android.view.View
import com.example.clothesshare.MainActivity

class ConversationActivity : AppCompatActivity() {



    private lateinit var binding: ActivityConversationBinding
    private lateinit var conversationRecyclerView: RecyclerView
    private lateinit var conversationList: ArrayList<ConversationItem>
    private lateinit var adapter: ConversationAdapter
    private lateinit var conversationId: String
    private lateinit var currentUser: String
    private lateinit var postOwnerId: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityConversationBinding.inflate(layoutInflater)
        setContentView(binding.root)
        currentUser = intent.getStringExtra("USERNAME") ?: ""
        postOwnerId  = intent.getStringExtra("POST USERNAME") ?: ""


        conversationId = intent.getStringExtra("conversation_id") ?: "mock_conversation_123"
        // Initialize UI with sample data
        setupRecyclerView()
        loadConversationItems()// Load mock data instead of Firebase data

        setupSendButton()
        val requestCard = findViewById<CardView>(R.id.request_card)


        val approveBtn = findViewById<ImageButton>(R.id.btn_approve)
        val denyBtn    = findViewById<ImageButton>(R.id.btn_deny)



        approveBtn.setOnClickListener {
            Toast.makeText(
                this,
                "You have approved Louis!",
                Toast.LENGTH_SHORT
            ).show()
            requestCard.visibility = View.GONE
            // (optionally also push an “approved” message to Firebase here)
        }

        denyBtn.setOnClickListener {
            Toast.makeText(
                this,
                "You have denied Louis!",
                Toast.LENGTH_SHORT
            ).show()
            requestCard.visibility = View.GONE
            // (and/or send a “denied” message)
        }

        binding.requestCard.requestText.text = "Louis has requested to take the next turn!"

        binding.conversationUsername.text = postOwnerId

        binding.backButton.setOnClickListener{
            val intent = Intent(this, MainActivity::class.java).apply {
                putExtra("USERNAME", currentUser)
            }
            startActivity(intent)
            @Suppress("DEPRECATION")
            overridePendingTransition(0, 0)
        }
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
        conversationList.add(ConversationItem(
            username= currentUser,
            message = "Hi! I'm interested in this item!",
            message_date = SimpleDateFormat("MMM dd, HH:mm", Locale.getDefault()).format(Date()),
            isCurrentUser = true))
        FirebaseDatabase.getInstance()
            .getReference("chats/$conversationId")
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val msgs = snapshot.children.mapNotNull { it.getValue(ConversationItem::class.java) }
                        .map { it.copy(isCurrentUser = true) }
                    adapter.messages.apply {
                        clear(); addAll(msgs)
                    }
                    adapter.notifyDataSetChanged()
                    binding.conversationRecyclerView.smoothScrollToPosition(adapter.itemCount)

                }
                override fun onCancelled(error: DatabaseError) { /* … */ }
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
        val conversationItem = ConversationItem(
            username = currentUser.toString(),
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
