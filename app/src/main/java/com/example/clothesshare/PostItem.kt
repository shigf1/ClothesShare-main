package com.example.clothesshare

import android.net.Uri
import java.util.Date

data class PostItem(
    val username: String,
    val description: String,
    val date: Date,
    val photoUriString: String
)
