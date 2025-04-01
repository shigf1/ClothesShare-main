package com.example.clothesshare

data class PostItem(
    val username: String,
    val image: Int,
    val description: String,
    val photoFileName: String? = null
)
