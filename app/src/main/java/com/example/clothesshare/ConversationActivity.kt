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

        conversationId = intent.getStringExtra("conversation_id") ?: return

        setupRecyclerView()
        loadConversationItems()
        setupSendButton()
    }

    private fun setupRecyclerView() {
        conversationRecyclerView = findViewById(R.id.material_card_view)
        conversationRecyclerView.layoutManager = LinearLayoutManager(this)
        conversationRecyclerView.setHasFixedSize(true)

        conversationList = arrayListOf()
        adapter = ConversationAdapter(conversationList)
        conversationRecyclerView.adapter = adapter
    }

    private fun loadConversationItems() {
        FirebaseDatabase.getInstance().getReference("chats/$conversationId")
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    conversationList.clear()

                    if (snapshot.exists()) {
                        for (itemSnapshot in snapshot.children) {
                            val conversationItem = itemSnapshot.getValue(ConversationItem::class.java)
                            conversationItem?.let {
                                conversationList.add(it)
                            }
                        }
                        adapter.notifyDataSetChanged()
                        binding.conversationRecyclerView.smoothScrollToPosition(conversationList.size - 1)
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    // Handle error
                }
            })
    }

    private fun setupSendButton() {
        binding.sendbutton.setOnClickListener {
            val messageText = binding.messageinput.text.toString().trim()
            if (messageText.isNotEmpty()) {
                sendMessage(messageText)
            }
        }
    }

    private fun sendMessage(message: String) {
        // Create properly formatted date string
        val dateFormat = SimpleDateFormat("EEE MMM d HH:mm zzz yyyy", Locale.getDefault())
        val formattedDate = dateFormat.format(Date(System.currentTimeMillis()))

        val conversationItem = ConversationItem(
            username = FirebaseAuth.getInstance().currentUser?.displayName ?: "You",
            message = message,
            message_date = formattedDate  // Now matches XML format
        )

        FirebaseDatabase.getInstance().getReference("chats/$conversationId")
            .push()
            .setValue(conversationItem)
            .addOnSuccessListener {
                binding.messageinput.text.clear()
            }
            .addOnFailureListener {
                // Handle error
            }
    }
}