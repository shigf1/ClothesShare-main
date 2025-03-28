package com.example.clothesshare

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.clothesshare.databinding.ActivityMainBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    private lateinit var firebaseRef: DatabaseReference

    private lateinit var postArrayList: ArrayList<PostItem>

    private lateinit var postRecyclerview: RecyclerView

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

        postRecyclerview = findViewById(R.id.recyclerview)

        postRecyclerview.layoutManager = LinearLayoutManager(this)

        postRecyclerview.setHasFixedSize(true)

        postArrayList = arrayListOf<PostItem>()

        getUserdata()

        postRecyclerview.adapter = PostAdapter(postArrayList)

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

    private fun getUserdata() {
        firebaseRef = FirebaseDatabase.getInstance().getReference("Posts")

        firebaseRef.addValueEventListener(object : ValueEventListener{

            override fun onDataChange(snapshot: DataSnapshot) {

                postArrayList.clear()

                if (snapshot.exists()){

                    for (postSnapshot in snapshot.children){

                        val postItem = postSnapshot.getValue(PostItem::class.java)

                        postItem?.let {

                            postArrayList.add(it)

                        }

                    }

                    postRecyclerview.adapter?.notifyDataSetChanged() // Notify adapter

                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("MainActivity", "Database error: ${error.message}")
            }

        })
    }
}