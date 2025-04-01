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
//import kotlinx.android.synthetic.main.activity_upload.postPhoto
import java.io.File
import java.util.Date

class UploadActivity : AppCompatActivity() {
    private lateinit var binding: ActivityUploadBinding

    private val takePhoto = registerForActivityResult(
        ActivityResultContracts.TakePicture()
    ) { didTakePhoto: Boolean ->
        // handle the result
        if (didTakePhoto && photoName != null) {

        }
    }

    var pickedPhoto : Uri? = null
    var pickedBitMap : Bitmap? = null

    private var photoName: String? = null

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

        val singlePhotoPickerLauncher =
            registerForActivityResult(ActivityResultContracts.PickVisualMedia()){ uri ->
                binding.postPhoto.setImageURI(uri)
            }

        binding.uploadPicture.setOnClickListener() {
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

        //val captureImageIntent = takePhoto.contract.createIntent(
        //    applicationContext,
        //    Uri.parse("")
        //)
        //binding.uploadPicture.isEnabled = canResolveIntent(captureImageIntent)
    }

    //fun pickedPhoto (view: View) {
    //    if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_EXTERNAL_STORAGE)
    //        != PackageManager.PERMISSION_GRANTED) {
    //        ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE),
    //            1)
    //    } else {
    //        val galleryIntext = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
    //        startActivityForResult(galleryIntext, 2)
    //    }
   //}

    //override fun onRequestPermissionsResult(
    //    requestCode: Int,
    //    permissions: Array<out String>,
    //    grantResults: IntArray
    //) {
    //    if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
    //        val galleryIntext = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
    //        startActivityForResult(galleryIntext, 2)
    //    }
    //    super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    //}

    //override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
    //    if (requestCode == 2 && resultCode == Activity.RESULT_OK && data != null){
    //        pickedPhoto = data.data
    //        if (Build.VERSION.SDK_INT >= 28) {
    //            val source = ImageDecoder.createSource(this.contentResolver, pickedPhoto!!)
    //            pickedBitMap = ImageDecoder.decodeBitmap(source)
                //postPhoto.setImageBitmap(pickedBitMap)
    //            binding.postPhoto.setImageBitmap(pickedBitMap)
    //        } else {
    //            pickedBitMap = MediaStore.Images.Media.getBitmap(this.contentResolver, pickedPhoto!!)
    //            //postPhoto.setImageBitmap(pickedBitMap)
    //            binding.postPhoto.setImageBitmap(pickedBitMap)
    //        }
    //    }
    //   super.onActivityResult(requestCode, resultCode, data)
    //}
    
    //private fun canResolveIntent(intent: Intent): Boolean {
    //    val packageManager: PackageManager = application.packageManager
    //    val resolvedActivity: ResolveInfo? =
    //        packageManager.resolveActivity(
    //            intent,
    //            PackageManager.MATCH_DEFAULT_ONLY
    //        )
    //    return resolvedActivity != null
    //}

}