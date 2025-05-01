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
import android.util.Log
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.widget.doOnTextChanged
import com.google.android.gms.tasks.Task
import com.google.android.gms.tasks.Tasks
//import com.google.firebase.auth.FirebaseAuth
//import kotlinx.android.synthetic.main.activity_upload.postPhoto
import java.util.Date
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import java.text.SimpleDateFormat
import java.util.Locale

class UploadActivity : AppCompatActivity() {
    private lateinit var binding: ActivityUploadBinding

    private lateinit var post: PostItem
    private lateinit var currentUser: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUploadBinding.inflate(layoutInflater)
        setContentView(binding.root)
        enableEdgeToEdge()

        ViewCompat.setOnApplyWindowInsetsListener(binding.upload) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, 0)
            insets
        }

        currentUser = intent.getStringExtra("USERNAME").toString()

        post = PostItem(
            username = "",
            description = "",
            date = Date().toString(),
            image = "",
            story = "",
            experience_photo_1 = "",
            experience_photo_2 = "",
            experience_photo_3 = "",
            experience_photo_4 = "",
            brand = "",
            location = ""
        )

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

        }

        // setting ImageView postPhoto to have the image the user selected
        val singlePhotoPickerLauncher =
            registerForActivityResult(ActivityResultContracts.PickVisualMedia()){ uri ->
                binding.postPhoto.setImageURI(uri)
                post = post.copy(image = uri.toString())

            }

        val experiencePhoto1PickerLauncher =
            registerForActivityResult(ActivityResultContracts.PickVisualMedia()){ uri ->
                binding.experiencePhoto1.setImageURI(uri)
                post = post.copy(experience_photo_1 = uri.toString())

            }

        val experiencePhoto2PickerLauncher =
            registerForActivityResult(ActivityResultContracts.PickVisualMedia()){ uri ->
                binding.experiencePhoto2.setImageURI(uri)
                post = post.copy(experience_photo_2 = uri.toString())

            }

        val experiencePhoto3PickerLauncher =
            registerForActivityResult(ActivityResultContracts.PickVisualMedia()){ uri ->
                binding.experiencePhoto3.setImageURI(uri)
                post = post.copy(experience_photo_3 = uri.toString())

            }

        val experiencePhoto4PickerLauncher =
            registerForActivityResult(ActivityResultContracts.PickVisualMedia()){ uri ->
                binding.experiencePhoto4.setImageURI(uri)
                post = post.copy(experience_photo_4 = uri.toString())

            }


        binding.postPhoto.setOnClickListener{
            singlePhotoPickerLauncher.launch(
                PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
            )
        }

        binding.experiencePhoto1.setOnClickListener{
            experiencePhoto1PickerLauncher.launch(
                PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
            )
        }

        binding.experiencePhoto2.setOnClickListener{
            experiencePhoto2PickerLauncher.launch(
                PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
            )
        }

        binding.experiencePhoto3.setOnClickListener{
            experiencePhoto3PickerLauncher.launch(
                PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
            )
        }

        binding.experiencePhoto4.setOnClickListener{
            experiencePhoto4PickerLauncher.launch(
                PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
            )
        }


        // change post description when the textview is edited by user
        binding.postUploadDescription.doOnTextChanged { text, _, _, _ ->
            post = post.copy(description = text.toString())
        }

        binding.postUploadStory.doOnTextChanged { text, _, _, _ ->
            post = post.copy(story = text.toString())
        }

        binding.postUploadBrand.doOnTextChanged { text, _, _, _ ->
            post = post.copy(brand = text.toString())
        }

        binding.postUploadLocation.doOnTextChanged { text, _, _, _ ->
            post = post.copy(location = text.toString())
        }

        binding.uploadPost.setOnClickListener{
            pushData(post, currentUser)
        }

    }

    private fun pushData(postItem: PostItem, currentUser: String?) {
        // Validate inputs
        if (postItem.image?.isEmpty() == true) {
            Toast.makeText(this, "Please select a main image", Toast.LENGTH_SHORT).show()
            return
        }

        if (postItem.brand?.isEmpty() == true) {
            Toast.makeText(this, "Please enter a brand", Toast.LENGTH_SHORT).show()
            return
        }

        if (postItem.location?.isEmpty() == true) {
            Toast.makeText(this, "Please enter a location", Toast.LENGTH_SHORT).show()
            return
        }

        val progressDialog = ProgressDialog(this).apply {
            setMessage("Uploading post...")
            setCancelable(false)
            show()
        }

        try {
            val storageRef = FirebaseStorage.getInstance().reference
            val databaseRef = FirebaseDatabase.getInstance().getReference("Posts")
            val userId = postItem.username
            val postId = databaseRef.push().key!!

            // Upload main image
            val mainImageUri = Uri.parse(postItem.image)
            val mainImageRef = storageRef.child("posts/$userId/$postId/main.jpg")

            // List of all upload tasks (main + 4 experiences)
            val uploadTasks = mutableListOf<Task<Uri>>()

            // Upload main image
            val mainUploadTask = mainImageRef.putFile(mainImageUri)
                .continueWithTask { task ->
                    if (!task.isSuccessful) throw task.exception!!
                    mainImageRef.downloadUrl
                }
            uploadTasks.add(mainUploadTask)

            // Upload experience photos
            val experienceUris = listOf(
                postItem.experience_photo_1,
                postItem.experience_photo_2,
                postItem.experience_photo_3,
                postItem.experience_photo_4
            )

            experienceUris.forEachIndexed { index, uri ->
                if (!uri.isNullOrEmpty()) {
                    val expRef = storageRef.child("posts/$userId/$postId/experience_${index+1}.jpg")
                    val uploadTask = expRef.putFile(Uri.parse(uri))
                        .continueWithTask { task ->
                            if (!task.isSuccessful) throw task.exception!!
                            expRef.downloadUrl
                        }
                    uploadTasks.add(uploadTask)
                }
            }

            // Wait for all uploads to complete
            Tasks.whenAllSuccess<Uri>(uploadTasks).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val urls = task.result
                    val mainImageUrl = urls[0].toString()

                    // Create post object with experience URLs
                    val post = PostItem(
                        brand = postItem.brand,
                        location = postItem.location,
                        image = mainImageUrl,
                        experience_photo_1 = urls.getOrNull(1)?.toString() ?: "",
                        experience_photo_2 = urls.getOrNull(2)?.toString() ?: "",
                        experience_photo_3 = urls.getOrNull(3)?.toString() ?: "",
                        experience_photo_4 = urls.getOrNull(4)?.toString() ?: "",
                        description = postItem.description,
                        date = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(Date()),
                        username = currentUser,
                        postId = postId,
                        story = postItem.story
                    )

                    // Save to database
                    databaseRef.child(postId).setValue(post)
                        .addOnSuccessListener {
                            savePostToUser(postId, currentUser)
                            progressDialog.dismiss()
                            Toast.makeText(this, "Post uploaded successfully!", Toast.LENGTH_SHORT).show()
                            finish()
                        }
                        .addOnFailureListener { e ->
                            progressDialog.dismiss()
                            Toast.makeText(this, "Failed to save post: ${e.message}", Toast.LENGTH_SHORT).show()
                        }
                } else {
                    progressDialog.dismiss()
                    Toast.makeText(this, "Image upload failed: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                }
            }
        } catch (e: Exception) {
            progressDialog.dismiss()
            Toast.makeText(this, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
        }
    }

    private fun savePostToUser(postId: String, currentUser: String?) {
        val userRef = FirebaseDatabase.getInstance().getReference("users/$currentUser/posts")

        // Add the post ID to the user's posts list
        userRef.child(postId).setValue(true)
            .addOnSuccessListener {
                Log.d("Upload", "Post added to user's list")
            }
            .addOnFailureListener { e ->
                Log.e("Upload", "Failed to update user posts", e)
            }
    }

}