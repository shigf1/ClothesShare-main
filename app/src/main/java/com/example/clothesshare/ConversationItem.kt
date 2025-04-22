package com.example.clothesshare

import com.google.firebase.database.Exclude

data class ConversationItem(
    val id: String? = null,
    val username: String? = null,
    val message: String? = null,
    val message_date: String = "" ,
    val isCurrentUser: Boolean = false // Add this field
) {
    @Exclude
    fun toMap(): Map<String, Any?> = mapOf(
        "id" to id,
        "username" to username,
        "message" to message,
        "message_date" to message_date,
        "isCurrentUser" to isCurrentUser
    )
}