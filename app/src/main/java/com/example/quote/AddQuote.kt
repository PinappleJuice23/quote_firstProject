package com.example.quote

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class AddQuote : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_quote)
        auth = Firebase.auth
        val db = Firebase.firestore
        val user = auth.currentUser

        val textEditText: EditText = findViewById(R.id.AddAuthorEditText)
        val authorEditText: EditText = findViewById(R.id.AddTextEditText)
        val addPostButton: Button = findViewById(R.id.AddQuoteButton)


        val addQuoteButton: Button = findViewById(R.id.HomeAddQuoteButton)
        val profileQuoteButton: Button = findViewById(R.id.HomeProfileButton)
        val viewQuoteButton: Button = findViewById(R.id.HomeViewButton)

        profileQuoteButton.setOnClickListener {
            val intent = Intent(this, Profile::class.java)
            startActivity(intent)
            finish()
        }

        viewQuoteButton.setOnClickListener {
            val intent = Intent(this, Home::class.java)
            startActivity(intent)
            finish()
        }

        addQuoteButton.setOnClickListener {
            val intent = Intent(this, AddQuote::class.java)
            startActivity(intent)
            finish()
        }
        addPostButton.setOnClickListener {
            val post = hashMapOf(
                "text" to textEditText.text.toString(),
                "author" to authorEditText.text.toString(),
                "uid" to user!!.uid
            )

            db.collection("quotes")
                .add(post)
                .addOnSuccessListener {
                    Log.d("AddQuote", "DocumentSnapshot successfully written!")
                    Toast.makeText(
                        baseContext,
                        "Added quote",
                        Toast.LENGTH_SHORT,
                    ).show()
                    val intent = Intent(this, Home::class.java)
                    startActivity(intent)
                    finish()
                }
                .addOnFailureListener { e -> Log.w("AddQuote", "Error writing document", e) }

        }

    }
}