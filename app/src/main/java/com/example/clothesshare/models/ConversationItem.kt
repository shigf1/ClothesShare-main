package com.example.clothesshare.models

data class Conversation(
    val conversationId: String = "",
    val lastMessage: String = "",
    val timestamp: Long = 0,
    val withUserId: String = "",
    val participants: List<String> = emptyList() // For tracking who's in the chat
) {
    // Firebase requires empty constructor, which data class provides by default
}