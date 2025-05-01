package com.example.clothesshare

import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.clothesshare.databinding.ActivityMessagesBinding
import com.example.clothesshare.ui.ConversationActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class MessagesActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMessagesBinding
    private val messageThreads = mutableListOf<MessageItem>()
    private lateinit var adapter: MessageAdapter
    private lateinit var currentUser: String


    @RequiresApi(Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMessagesBinding.inflate(layoutInflater)
        currentUser = intent.getStringExtra("USERNAME") ?: ""
        setContentView(binding.root)

        adapter = MessageAdapter(messageThreads) { msg ->
            startActivity(Intent(this, ConversationActivity::class.java)
                .putExtra("conversation_id", msg.conversationId))
        }
        binding.messagesRecyclerview.apply {
            layoutManager = LinearLayoutManager(this@MessagesActivity)
            adapter = this@MessagesActivity.adapter
        }
        binding.homebutton.setOnClickListener() {
            val intent = Intent(this,MainActivity::class.java).apply {
                putExtra("USERNAME", currentUser)
            }
            startActivity(intent)
            @Suppress("DEPRECATION")
            overridePendingTransition(0, 0)
        }

        binding.profilebutton.setOnClickListener() {
            val intent = Intent(this,ProfileActivity::class.java).apply {
                putExtra("USERNAME", currentUser)
            }
            startActivity(intent)
            @Suppress("DEPRECATION")
            overridePendingTransition(0, 0)
        }

        binding.messagesbutton.setOnClickListener() {

        }

        binding.uploadbutton.setOnClickListener() {
            val intent = Intent(this,UploadActivity::class.java).apply {
                putExtra("USERNAME", currentUser)
            }
            startActivity(intent)
            @Suppress("DEPRECATION")
            overridePendingTransition(0, 0)
        }


        val samplePhotoUrl = "https://firebasestorage.googleapis.com/v0/b/clothes-share-1ff7b.firebasestorage.app/o/profilePics%2FLouis.jpg?alt=media&token=69e34bc3-d2da-43c1-af95-475c5d8dc9a2\"\n"
        val sampleUser    = "Louis"
        val sampleConvId  = "mock_conversation_123"
        val sampleDate    = SimpleDateFormat("MMM dd, HH:mm", Locale.getDefault()).format(Date())

        messageThreads.add(
            MessageItem(
                profile_pic              = samplePhotoUrl,
                username                 = sampleUser,
                most_recent_message      = "Hey there! This is Louis here",
                most_recent_message_date = sampleDate,
                conversationId           = sampleConvId
            )
        )
        adapter.notifyDataSetChanged()

        loadUserConversations()
    }

    private fun loadUserConversations() {
        val uid = FirebaseAuth.getInstance().currentUser?.uid ?: return
        FirebaseDatabase.getInstance()
            .getReference("user_conversations/$uid/with")
            .addListenerForSingleValueEvent(object: ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    for (child in snapshot.children) {
                        val convId = child.getValue(String::class.java)
                        if (!convId.isNullOrEmpty()) {
                            loadConversationPreview(convId)
                        }
                    }
                }
                override fun onCancelled(e: DatabaseError) {}
            })
    }

    private fun loadConversationPreview(convId: String) {
        // First load conversation metadata:
        FirebaseDatabase.getInstance()
            .getReference("conversations/$convId")
            .addListenerForSingleValueEvent(object: ValueEventListener {
                override fun onDataChange(snap: DataSnapshot) {
                    val lastMsg  = snap.child("lastMessage").getValue(String::class.java) ?: ""
                    val lastDate = snap.child("lastMessageDate").getValue(String::class.java) ?: ""

                    // Determine the other user’s UID:
                    val participants = snap.child("participants").value as? Map<*,*>
                    val otherUid = participants
                        ?.keys
                        ?.map { it as String }
                        ?.first { it != FirebaseAuth.getInstance().currentUser?.uid }
                        ?: return

                    // **Now fetch that user’s profilePic and username:**
                    FirebaseDatabase.getInstance()
                        .getReference("users/$otherUid")
                        .addListenerForSingleValueEvent(object: ValueEventListener {
                            override fun onDataChange(userSnap: DataSnapshot) {
                                val profileUrl = userSnap.child("profilePic")
                                    .getValue(String::class.java)
                                    .orEmpty()
                                val username = userSnap.child("username")
                                    .getValue(String::class.java)
                                    ?.takeIf { it.isNotEmpty() }
                                    ?: otherUid

                                // Build your MessageItem with the fetched URL
                                messageThreads.add(
                                    MessageItem(
                                        profile_pic              = profileUrl,
                                        username                 = username,
                                        most_recent_message      = lastMsg,
                                        most_recent_message_date = lastDate,
                                        conversationId           = convId
                                    )
                                )
                                adapter.notifyDataSetChanged()
                            }
                            override fun onCancelled(e: DatabaseError) { /* … */ }
                        })
                }
                override fun onCancelled(e: DatabaseError) { /* … */ }
            })
    }
}


