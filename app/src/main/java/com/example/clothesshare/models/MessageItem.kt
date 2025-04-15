package com.example.clothesshare.models

data class Message(
    val messageId: String = "",
    val senderId: String = "",
    val text: String = "",
    val timestamp: Long = System.currentTimeMillis(),
    val read: Boolean = false,
    val type: String = "text", // Can be "text", "image", "video", etc.
    val mediaUrl: String? = null // For multimedia messages
) {
    // Helper function to convert to map for Firebase
    fun toMap(): Map<String, Any?> {
        return mapOf(
            "messageId" to messageId,
            "senderId" to senderId,
            "text" to text,
            "timestamp" to timestamp,
            "read" to read,
            "type" to type,
            "mediaUrl" to mediaUrl
        )
    }
}