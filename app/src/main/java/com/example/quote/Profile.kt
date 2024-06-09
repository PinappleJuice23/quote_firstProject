package com.example.quote

import android.content.ContentValues
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class Profile : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private val quotes = mutableListOf<Quote>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_profile)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val db = Firebase.firestore

        auth = Firebase.auth

        val currentUser = auth.currentUser

        val ProfileImageView: ImageView = findViewById(R.id.imageView5)
        val ProfileNameTextView: TextView = findViewById(R.id.ProfileNameTextView)
        val addQuoteButton: Button = findViewById(R.id.HomeAddQuoteButton)
        val myQuotesAdapter = QuoteAdapter(quotes)
        val signOutButton: Button = findViewById(R.id.HomeSignOutButton)
        val profileQuoteButton: Button = findViewById(R.id.HomeProfileButton)
        val viewQuoteButton: Button = findViewById(R.id.HomeViewButton)

        val myQuotesRecyclerView : RecyclerView = findViewById(R.id.ProfileRecyclerView)
        myQuotesRecyclerView.adapter = myQuotesAdapter

        val docRef = db.collection("users").document(currentUser?.uid!!)
        docRef.get()
            .addOnSuccessListener { document ->
                if (document != null) {
                    Log.d("TAG", "DocumentSnapshot data: ${document.data}")
                    ProfileNameTextView.text = document.getString("name")
                } else {
                    Log.d("TAG", "No such document")
                }
            }
            .addOnFailureListener { exception ->
                Log.d("TAG", "get failed with ", exception)
            }

        ProfileImageView.setOnClickListener {
            val intent = Intent(this, Profile::class.java)
            startActivity(intent)
            finish()
        }

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

        signOutButton.setOnClickListener {
            Firebase.auth.signOut()
            val intent = Intent(this, Login::class.java)
            Toast.makeText(
                baseContext,
                "Loged Out!",
                Toast.LENGTH_SHORT,
            ).show()
            startActivity(intent)
            finish()
        }

        db.collection("quotes")
            .get()
            .addOnSuccessListener { result ->

                for (document in result) {
                    if (currentUser!!.uid == document.data["uid"].toString()){
                        Log.d(ContentValues.TAG, "${document.id} => ${document.data}")
                        quotes.add(
                            Quote(document.data["quoteId"].toString(), document.data["uid"].toString(), document.data["text"].toString(), document.data["author"].toString())
                        )
                    }
                }
                myQuotesAdapter.notifyDataSetChanged()
            }
            .addOnFailureListener { exception ->
                Log.d(ContentValues.TAG, "Error getting documents: ", exception)
            }
        myQuotesRecyclerView.layoutManager = LinearLayoutManager(this)

    }
}