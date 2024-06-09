package com.example.quote

import android.os.Bundle
import android.util.Log
import android.widget.EditText
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class SingleQuoteActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_single_quote)

        auth = Firebase.auth
        val quoteId = intent.getStringExtra("quoteId")

        val postText: TextView = findViewById(R.id.singlePost_text)
        val postAuthor: TextView = findViewById(R.id.singlePost_text)


        val db = Firebase.firestore
        db.collection("quotes").document(quoteId!!)
            .get()
            .addOnSuccessListener { document ->
                if (document != null) {
                    postText.text = document.data!!["text"].toString()
                    postAuthor.text = document.data!!["author"].toString()
                }
            }
            .addOnFailureListener { exception ->
                postText.text = "Error"
                postAuthor.text = "Error"
            }

    }
}