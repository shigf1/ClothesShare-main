package com.example.clothesshare

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import com.example.clothesshare.databinding.ActivityExperienceBinding
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.example.clothesshare.ui.ConversationActivity

class ExperienceActivity : AppCompatActivity() {
    private lateinit var binding: ActivityExperienceBinding

    private lateinit var postItem: PostItem

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityExperienceBinding.inflate(layoutInflater)
        setContentView(binding.root)
        enableEdgeToEdge()
        val postId = intent.getStringExtra("POST_ID")
        val currentUser = intent.getStringExtra("USERNAME")
        if (postId != null) {
            getPostById(postId)
        }
        else{
            Toast.makeText(this, "PostItem is null", Toast.LENGTH_SHORT).show()
        }

        ViewCompat.setOnApplyWindowInsetsListener(binding.experience) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.nextButton.setOnClickListener {
            // go to next experience
        }
        binding.previousButton.setOnClickListener {
            // go to next experience
        }

        binding.requestButton.setOnClickListener {
            val intent = Intent(this, ConversationActivity::class.java).apply {
                putExtra("USERNAME", currentUser)
                putExtra("POST USERNAME", postItem?.username)
                putExtra("IS_PROMPT", true)
            }
            startActivity(intent)
        }


        // Set click listeners for navigation buttons
        binding.homebutton.setOnClickListener() {
            val intent = Intent(this,MainActivity::class.java).apply {
                putExtra("USERNAME", currentUser)
            }
            startActivity(intent)
        }

        binding.profilebutton.setOnClickListener() {
            val intent = Intent(this,ProfileActivity::class.java).apply {
                putExtra("USERNAME", currentUser)
            }
            startActivity(intent)
        }

        binding.messagesbutton.setOnClickListener() {
            val intent = Intent(this,MessagesActivity::class.java).apply {
                putExtra("USERNAME", currentUser)
            }
            startActivity(intent)
        }

        binding.uploadbutton.setOnClickListener() {
            val intent = Intent(this,UploadActivity::class.java).apply {
                putExtra("USERNAME", currentUser)
            }
            startActivity(intent)
        }
    }

    private fun getPostById(postId: String) {
        val databaseRef = FirebaseDatabase.getInstance().getReference("Posts/$postId")

        databaseRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val post = snapshot.getValue(PostItem::class.java)
                if (post != null) {
                    postItem = post
                    displayPost(postItem)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("Firebase", "Error retrieving post: ${error.message}")
            }
        })
    }

    private fun displayPost(postItem: PostItem) {
        // Implement your UI updates here
        // Example:
        findViewById<TextView>(R.id.itemName).text = postItem.description
        findViewById<TextView>(R.id.username).text = postItem.username
        findViewById<TextView>(R.id.experienceDescriptionTextView).text = postItem.story
        findViewById<TextView>(R.id.itemDetails).text = postItem.brand
        findViewById<TextView>(R.id.locationText).text = postItem.location
        findViewById<TextView>(R.id.experienceDate).text = postItem.date
        Glide.with(this)
            .load(postItem.experience_photo_1)
            .into(findViewById(R.id.photo1))
        Glide.with(this)
            .load(postItem.experience_photo_2)
            .into(findViewById(R.id.photo2))
        Glide.with(this)
            .load(postItem.experience_photo_3)
            .into(findViewById(R.id.photo3))
        Glide.with(this)
            .load(postItem.experience_photo_4)
            .into(findViewById(R.id.photo4))
        Glide.with(this)
            .load(postItem.image)
            .into(findViewById(R.id.featuredPhoto))
    }




    /*  private fun checkExperience(userAnswer:Boolean){

      IF AT LAST EXPERIENCE, NEXT BUTTON DISSAPEARS AND A PROMPT TO ADD EXPERIENCE TO ITEM APPEARS
      if (experiences.all(it.isAnswered) {
             have images fill with placeholders
             add button and text to add experience becomes visible
             binding.nextButton.visibility.gone
         }
         PREVIOUS BUTTON INVISIBLE AT START BY DEFUALT, ONCE YOU PASS BECOMES VISIBLE
         if experiences.currentindex == 1 {
             binding.previousButton.visibility.visible
         }
         PREVIOUS BUTTON BECOMES INVISIBLE IF YOU GO BACK TO THE FIRST EXPERIENCE
         if experiences.currentindex == 0 {
             binding.previousButton.visibility.invisible
         }

         }
         */


}