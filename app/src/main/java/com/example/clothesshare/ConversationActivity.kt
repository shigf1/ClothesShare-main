package com.example.clothesshare

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class ConversationActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // SET TO MESSAGING CONTENT VIEW WHEN CREATED
        //setContentView(R.layout)

        val message = intent.getParcelableExtra<MessageItem>("message")
        if (message != null) {
            // SET DATA IN CONTENT VIEW
        }

    }
}