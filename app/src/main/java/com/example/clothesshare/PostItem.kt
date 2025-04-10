package com.example.clothesshare

/* Data Class for post information */
data class PostItem(
    val image: String ?= null,
    val username: String ?= null,
    val description: String ?= null,
    val date: String ?= null,
    val postId: String = "",
    //val story: String ?= null
) {
    fun withId(id: String): PostItem = this.copy(postId = id)
}
