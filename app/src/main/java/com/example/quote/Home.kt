package com.example.quote

import android.content.ContentValues.TAG
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class Home : AppCompatActivity() {
    private val quotes = mutableListOf<Quote>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        val db = Firebase.firestore
        val addQuoteButton: Button = findViewById(R.id.HomeAddQuoteButton)
        val profileQuoteButton: Button = findViewById(R.id.HomeProfileButton)
        val viewQuoteButton: Button = findViewById(R.id.HomeViewButton)
        val searchButton: Button = findViewById(R.id.search_button)
        val searchEditText: TextView = findViewById(R.id.search_editText)

        val myQuotesAdapter = QuoteAdapter(quotes)
        val myQuotesRecyclerView : RecyclerView = findViewById(R.id.recyclerView)
        myQuotesRecyclerView.adapter = myQuotesAdapter

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


        db.collection("quotes")
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    Log.d(TAG, "${document.id} => ${document.data}")
                    quotes.add(
                        Quote(document.data["quoteId"].toString(), document.data["uid"].toString(), document.data["text"].toString(), document.data["author"].toString())
                    )
                }
                myQuotesAdapter.notifyDataSetChanged()
            }
            .addOnFailureListener { exception ->
                Log.d(TAG, "Error getting documents: ", exception)
            }
        myQuotesRecyclerView.layoutManager = LinearLayoutManager(this)

        searchButton.setOnClickListener {
            db.collection("quotes")
                .whereGreaterThanOrEqualTo("text", searchEditText.text.toString())
                .get()
                .addOnSuccessListener { result ->
                    quotes.clear()
                    for (document in result) {
                        Log.d("Home", "${document.id} => ${document.data}")
                        quotes.add(
                            Quote(document.data["quoteId"].toString(), document.data["uid"].toString(), document.data["text"].toString(), document.data["author"].toString())
                        )

                    }
                    myQuotesAdapter.notifyDataSetChanged()
                }
                .addOnFailureListener { exception ->
                    Log.d("Home", "Error getting documents: ", exception)
                }
        }

    }
}