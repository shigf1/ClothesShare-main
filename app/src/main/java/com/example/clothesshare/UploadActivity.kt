package com.example.clothesshare

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.clothesshare.databinding.ActivityUploadBinding
import android.net.Uri
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.widget.doOnTextChanged
//import kotlinx.android.synthetic.main.activity_upload.postPhoto
import java.util.Date
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import java.text.SimpleDateFormat
import java.util.Locale

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
            date = Date().toString(),
            image = ""
            //story = ""
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
                post = post.copy(image = uri.toString())

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

       // binding.postUploadStory.doOnTextChanged { text, _, _, _ ->
           // post = post.copy(story = text.toString())
        //}

        // change the date displayed on the button to the post's date
        binding.postDate.apply {
            text = post.date.toString()
            isEnabled = false
        }


        binding.uploadPost.setOnClickListener(){
            pushData(post)
        }
    }

    private fun pushData(postItem: PostItem) {
        // 1. Get references to Firebase services
        val storageRef = FirebaseStorage.getInstance().reference
        val databaseRef = FirebaseDatabase.getInstance().getReference("Posts")
        val currentUser = "Test_user"

        // 2. Validate inputs
        if (postItem.image?.isEmpty() == true) {
            Toast.makeText(this, "Please select an image", Toast.LENGTH_SHORT).show()
            return
        }

        if (postItem.description?.isEmpty() == true) {
            Toast.makeText(this, "Please add a description", Toast.LENGTH_SHORT).show()
            return
        }

        //if (postItem.story?.isEmpty() == true) {
           // Toast.makeText(this, "Please add a story", Toast.LENGTH_SHORT).show()
            //return
        //}

        // 3. Show loading indicator
        val progressDialog = ProgressDialog(this).apply {
            setMessage("Uploading post...")
            setCancelable(false)
            show()
        }

        try {
            // 4. Convert URI to file and upload to Firebase Storage
            val imageUri = Uri.parse(postItem.image)
            val imageRef = storageRef.child("posts/${currentUser}/${System.currentTimeMillis()}.jpg")

            imageRef.putFile(imageUri)
                .addOnSuccessListener { taskSnapshot ->
                    // 5. Get download URL after successful upload
                    imageRef.downloadUrl.addOnSuccessListener { uri ->
                        // 6. Create post object with all data
                        val post = PostItem(
                            username = currentUser,
                            description = postItem.description,
                            date = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
                                .format(Date()),
                            image = uri.toString(),
                            //story = postItem.story
                        )

                        // 7. Save to Realtime Database
                        val postId = databaseRef.push().key
                        if (postId != null) {
                            databaseRef.child(postId).setValue(post)
                                .addOnSuccessListener {
                                    progressDialog.dismiss()
                                    Toast.makeText(this, "Post uploaded successfully!", Toast.LENGTH_SHORT).show()
                                    finish() // Close the upload activity
                                }
                                .addOnFailureListener { e ->
                                    progressDialog.dismiss()
                                    Toast.makeText(this, "Failed to save post: ${e.message}", Toast.LENGTH_SHORT).show()
                                }
                        }
                    }
                }
                .addOnFailureListener { e ->
                    progressDialog.dismiss()
                    Toast.makeText(this, "Image upload failed: ${e.message}", Toast.LENGTH_SHORT).show()
                }
        } catch (e: Exception) {
            progressDialog.dismiss()
            Toast.makeText(this, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
        }
    }

}