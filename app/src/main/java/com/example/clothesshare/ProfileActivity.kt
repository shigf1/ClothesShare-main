package com.example.clothesshare

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.clothesshare.databinding.ActivityProfileBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage

class ProfileActivity : AppCompatActivity() {
    private lateinit var binding: ActivityProfileBinding

    private lateinit var postArrayList: ArrayList<PostItem>

    private lateinit var recyclerview: RecyclerView

    private val pickImageLauncher = registerForActivityResult(ActivityResultContracts.GetContent()) {
            uri: Uri? -> uri?.let { uploadProfilePhoto(it) }
    }

    private lateinit var currentUser: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)
        enableEdgeToEdge()

        currentUser = intent.getStringExtra("USERNAME") ?: ""
        binding.profileName.text = currentUser

        ViewCompat.setOnApplyWindowInsetsListener(binding.profile) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, 0)
            insets
        }
        currentUser.let { uid ->
            FirebaseDatabase.getInstance()
                .getReference("users/$uid/profilePic")
                .addListenerForSingleValueEvent(object: ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        snapshot.getValue(String::class.java)
                            ?.takeIf { it.isNotEmpty() }
                            ?.let { url ->
                                Glide.with(this@ProfileActivity)
                                    .load(url)
                                    .into(binding.profileImage)
                            }
                    }

                    override fun onCancelled(error: DatabaseError) { /* no-op */ }
                })
        }

        binding.profileImage.setOnClickListener() {
            pickImageLauncher.launch("image/*")
        }

        recyclerview = findViewById(R.id.recyclerview)

        recyclerview.layoutManager = LinearLayoutManager(this)

        recyclerview.setHasFixedSize(true)

        postArrayList = arrayListOf<PostItem>()

        fetchUserPosts(currentUser)

        recyclerview.adapter = PostAdapter(postArrayList) { clickedPost ->
            // Launch ExperienceActivity with the clicked post's ID
            val intent = Intent(this, ExperienceActivity::class.java).apply {
                putExtra("POST_ID", clickedPost.postId)
                putExtra("USERNAME", currentUser)
            }
            startActivity(intent)
            @Suppress("DEPRECATION")
            overridePendingTransition(0, 0)
        }

        // Set click listeners for navigation buttons
        binding.homebutton.setOnClickListener() {
            val intent = Intent(this,MainActivity::class.java).apply {
                putExtra("USERNAME", currentUser)
            }
            startActivity(intent)
            @Suppress("DEPRECATION")
            overridePendingTransition(0, 0)
        }

        binding.profilebutton.setOnClickListener() {

        }

        binding.messagesbutton.setOnClickListener() {
            val intent = Intent(this,MessagesActivity::class.java).apply {
                putExtra("USERNAME", currentUser)
            }
            startActivity(intent)
            @Suppress("DEPRECATION")
            overridePendingTransition(0, 0)
        }

        binding.uploadbutton.setOnClickListener() {
            val intent = Intent(this,UploadActivity::class.java).apply {
                putExtra("USERNAME", currentUser)
            }
            startActivity(intent)
            @Suppress("DEPRECATION")
            overridePendingTransition(0, 0)
        }
    }

    private fun fetchUserPosts(username: String) {
        val userRef = FirebaseDatabase.getInstance().getReference("users/$username/posts")

        userRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val postIds = snapshot.children.map { it.key }

                postIds.forEach { postId ->
                    FirebaseDatabase.getInstance().getReference("Posts/$postId")
                        .addListenerForSingleValueEvent(object : ValueEventListener {
                            override fun onDataChange(postSnapshot: DataSnapshot) {
                                val post = postSnapshot.getValue(PostItem::class.java)
                                if (post != null) {
                                    postArrayList.add(post)
                                    recyclerview.adapter?.notifyDataSetChanged() // Notify adapter
                                } else {
                                    Log.e("ProfileActivity", "Failed to parse post: $postId")
                                }

                            }
                            override fun onCancelled(error: DatabaseError) {
                                Log.e("ProfileActivity", "Database error: ${error.message}")
                            }
                        })
                }
            }
            override fun onCancelled(error: DatabaseError) {
                // Handle error
            }
        })
    }

    private fun uploadProfilePhoto(uri: Uri) {
        val username = currentUser ?: return
        val storageRef = FirebaseStorage
            .getInstance()
            .getReference("profilePics/$username.jpg")

        // 1) upload to Storage, 2) get download URL
        storageRef.putFile(uri)
            .continueWithTask { it.result.storage.downloadUrl }
            .addOnSuccessListener { downloadUri ->
                // 3) save URL to RTDB
                FirebaseDatabase.getInstance()
                    .getReference("users/$username/profilePic")
                    .setValue(downloadUri.toString())

                // 4) update UI immediately
                Glide.with(this)
                    .load(downloadUri).circleCrop()
                    .into(binding.profileImage)
            }


            .addOnFailureListener { e ->
                Toast.makeText(this,
                    "Failed to upload profile photo: ${e.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }
    }
}