package com.example.clothesshare

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.clothesshare.databinding.ActivityMessagesBinding
import java.util.Date

class MessagesActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMessagesBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMessagesBinding.inflate(layoutInflater)
        setContentView(binding.root)
        enableEdgeToEdge()

        ViewCompat.setOnApplyWindowInsetsListener(binding.messages) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val recyclerview: RecyclerView = findViewById(R.id.messages_recyclerview)

        recyclerview.layoutManager = LinearLayoutManager(this)

        val data = ArrayList<MessageItem>()

        val image = "Placeholder"

        for (i in 1..20) {
            data.add(MessageItem(image, "Username $i", "Most recent message sent $i"))
        }

        val adapter = MessageAdapter(data)

        recyclerview.adapter = adapter

        adapter.onItemClick = {
            val intent = Intent(this, ConversationActivity::class.java).apply {
                putExtra("username", it)
            }
            startActivity(intent)
        }

        // Set click listeners for navigation buttons
        binding.homebutton.setOnClickListener() {
            startActivity(Intent(this, MainActivity::class.java))
        }

        binding.profilebutton.setOnClickListener() {
            startActivity(Intent(this, ProfileActivity::class.java))
        }

        binding.messagesbutton.setOnClickListener(  ) {
            recreate()
        }

        binding.uploadbutton.setOnClickListener() {
            startActivity(Intent(this, UploadActivity::class.java))
        }
    }
}