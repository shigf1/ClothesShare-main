package com.example.clothesshare.ui.chat

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.clothesshare.databinding.ActivityChatBinding
import com.example.clothesshare.models.Message
import com.example.clothesshare.repository.ChatRepository
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class ChatActivity : AppCompatActivity() {
    private lateinit var binding: ActivityChatBinding
    private lateinit var adapter: MessagesAdapter
    private val chatRepository = ChatRepository()
    private lateinit var conversationId: String
    private lateinit var otherUserId: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChatBinding.inflate(layoutInflater)
        setContentView(binding.root)

        conversationId = intent.getStringExtra("conversation_id") ?: return
        otherUserId = intent.getStringExtra("other_user_id") ?: return

        setupRecyclerView()
        loadMessages()
        setupSendButton()
    }

    private fun setupRecyclerView() {
        adapter = MessagesAdapter(Firebase.auth.currentUser?.uid ?: "")
        binding.messagesRecyclerView.apply {
            layoutManager = LinearLayoutManager(this@ChatActivity)
            adapter = this@ChatActivity.adapter
        }
    }

    private fun loadMessages() {
        chatRepository.getMessages(conversationId) { messages ->
            adapter.submitList(messages)
            binding.messagesRecyclerView.scrollToPosition(adapter.itemCount - 1)
        }
    }

    private fun setupSendButton() {
        binding.sendButton.setOnClickListener {
            val messageText = binding.messageInput.text.toString()
            if (messageText.isNotBlank()) {
                sendMessage(messageText)
            }
        }
    }

    private fun sendMessage(text: String) {
        val currentUserId = Firebase.auth.currentUser?.uid ?: return
        val message = Message(
            senderId = currentUserId,
            text = text,
            timestamp = System.currentTimeMillis()
        )

        chatRepository.sendMessage(conversationId, message) {
            binding.messageInput.text.clear()
        }
    }
}