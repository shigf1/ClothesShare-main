package com.example.clothesshare

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.clothesshare.databinding.ActivityLoginBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseAuth = FirebaseAuth.getInstance()

        binding.loginButtonPage.setOnClickListener {
            val intent = Intent(this, SignupActivity::class.java)
            startActivity(intent)
        }

        binding.loginButton.setOnClickListener {
            val progressDialog = ProgressDialog(this).apply {
                setMessage("Logging in...")
                setCancelable(true)
                show()
            }

            val email = binding.ETemail.text.toString()
            val pass = binding.ETpass.text.toString()

            if (email.isNotEmpty() && pass.isNotEmpty()) {
                firebaseAuth.signInWithEmailAndPassword(email, pass).addOnCompleteListener {
                    if (it.isSuccessful) {
                        getUsernameByEmail(email) { username ->
                            if (username != null) {
                                val intent = Intent(this, MainActivity::class.java).apply {
                                    putExtra("USERNAME", username)
                                }
                                startActivity(intent)
                            } else {
                                Toast.makeText(this, "Username not found", Toast.LENGTH_SHORT).show()
                                progressDialog.dismiss()
                            }
                        }
                    } else {
                        Toast.makeText(this, it.exception.toString(), Toast.LENGTH_SHORT).show()
                        progressDialog.dismiss()
                    }
                }

            } else {
                Toast.makeText(this, "Enter all fields", Toast.LENGTH_SHORT).show()
                progressDialog.dismiss()
            }
        }
    }
    private fun getUsernameByEmail(email: String, callback: (String?) -> Unit) {
        val sanitizedEmail = email.replace(".", ",")
        FirebaseDatabase.getInstance().getReference("usernames/$sanitizedEmail")
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    callback(snapshot.getValue(String::class.java))
                }
                override fun onCancelled(error: DatabaseError) {
                    callback(null)
                }
            })
    }
}