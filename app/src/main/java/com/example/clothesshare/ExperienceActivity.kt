package com.example.clothesshare

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.clothesshare.databinding.ActivityExperienceBinding
import com.google.android.material.snackbar.Snackbar

class ExperienceActivity : AppCompatActivity() {
    private lateinit var binding: ActivityExperienceBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityExperienceBinding.inflate(layoutInflater)
        setContentView(binding.root)
        enableEdgeToEdge()


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


        // Set click listeners for navigation buttons
        binding.homebutton.setOnClickListener() {
            startActivity(Intent(this, MainActivity::class.java))
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