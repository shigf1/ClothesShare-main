package com.example.clothesshare

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.clothesshare.databinding.ActivityProfileBinding
import de.hdodenhof.circleimageview.CircleImageView;

class ProfileActivity : AppCompatActivity() {
    private lateinit var binding: ActivityProfileBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)
        enableEdgeToEdge()

        ViewCompat.setOnApplyWindowInsetsListener(binding.profile) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Set click listeners for navigation buttons
        binding.homebutton.setOnClickListener() {
            startActivity(Intent(this, MainActivity::class.java))
        }

        binding.profilebutton.setOnClickListener() {
            recreate()
        }

        binding.messagesbutton.setOnClickListener(){
            startActivity(Intent(this, MessagesActivity::class.java))
        }

        binding.uploadbutton.setOnClickListener() {
            startActivity(Intent(this, UploadActivity::class.java))
        }
    }
}