package com.example.clothesshare

import android.app.Activity
import android.app.ComponentCaller
import android.content.Intent
import android.content.pm.PackageManager
import android.content.pm.ResolveInfo
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.clothesshare.databinding.ActivityUploadBinding
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.Nullable
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.core.widget.doOnTextChanged
//import kotlinx.android.synthetic.main.activity_upload.postPhoto
import java.io.File
import java.util.Date

class UploadActivity : AppCompatActivity() {
    private lateinit var binding: ActivityUploadBinding

    private lateinit var post: PostItem

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUploadBinding.inflate(layoutInflater)
        setContentView(binding.root)
        enableEdgeToEdge()

        ViewCompat.setOnApplyWindowInsetsListener(binding.upload) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        post = PostItem(
            username = "",
            description = "",
            date = Date(),
            photoUriString = ""
        )

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
            recreate()
        }

        // setting ImageView postPhoto to have the image the user selected
        val singlePhotoPickerLauncher =
            registerForActivityResult(ActivityResultContracts.PickVisualMedia()){ uri ->
                binding.postPhoto.setImageURI(uri)
                post = post.copy(photoUriString = uri.toString())

            }

        binding.uploadPicture.setOnClickListener() {
            // launch singlePhotoPickerLauncher after user selects image
            singlePhotoPickerLauncher.launch(
                PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
            )
                //photoName = "IMG_${Date()}.JPG"
                //val photoFile = File(applicationContext.filesDir,
                //    photoName)
                //val photoUri = FileProvider.getUriForFile(
                //    applicationContext,
                //    "com.example.clothesshare.fileprovider",
                //    photoFile
                //)

                //takePhoto.launch(photoUri)
        }

        // change post description when the textview is edited by user
        binding.postUploadDescription.doOnTextChanged { text, _, _, _ ->
            post = post.copy(description = text.toString())
        }

        // change the date displayed on the button to the post's date
        binding.postDate.apply {
            text = post.date.toString()
            isEnabled = false
        }
    }

}