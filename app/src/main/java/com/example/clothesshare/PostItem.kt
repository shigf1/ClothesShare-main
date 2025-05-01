package com.example.clothesshare

data class PostItem(
    val image: String ?= null,
    val username: String ?= null,
    val description: String ?= null,
    val date: String ?= null,
    val postId: String ?= null,
    val story: String ?= null,
    val experience_photo_1: String ?= null,
    val experience_photo_2: String ?= null,
    val experience_photo_3: String ?= null,
    val experience_photo_4: String ?= null,
    val brand: String ?= "",
    val experienceNum: String ?= null,
    val parentPostId: String? = null,
    val isLatest: Boolean = true,
    val location: String ?= ""
) {
    fun withId(id: String): PostItem = this.copy(postId = id)
}