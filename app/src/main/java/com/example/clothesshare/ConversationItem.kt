package com.example.clothesshare

import com.google.firebase.database.Exclude

data class ConversationItem(
    val id: String? = null,
    val username: String? = null,
    val message: String? = null,
    val message_date: String = "" ,
    val isCurrentUser: Boolean = false,
    val isRequest: Boolean = false,
    val requestType: String? = null
) {
    @Exclude
    fun toMap(): Map<String, Any?> = mapOf(
        "id" to id,
        "username" to username,
        "message" to message,
        "message_date" to message_date,
        "isCurrentUser" to isCurrentUser,
        "isRequest"   to isRequest,
        "requestType" to requestType
    )
}