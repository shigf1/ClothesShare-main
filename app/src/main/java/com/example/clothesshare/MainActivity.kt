package com.example.clothesshare

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.clothesshare.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        enableEdgeToEdge()

        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val recyclerview: RecyclerView = findViewById(R.id.recyclerview)

        recyclerview.layoutManager = LinearLayoutManager(this)

        val data = ArrayList<PostItem>()

        for (i in 1..20) {
            data.add(PostItem(R.drawable.post_mosaic, "Username $i", "Description $i", i))
        }

        val adapter = PostAdapter(data)

        recyclerview.adapter = adapter

        // Set click listeners for navigation buttons
        binding.homebutton.setOnClickListener() {
            recreate()
        }

        binding.profilebutton.setOnClickListener() {
            startActivity(Intent(this, ProfileActivity::class.java))
        }

        binding.messagesbutton.setOnClickListener() {
            startActivity(Intent(this, MessagesActivity::class.java))
        }

        binding.uploadbutton.setOnClickListener() {
            startActivity(Intent(this, UploadActivity::class.java))
        }
    }
}