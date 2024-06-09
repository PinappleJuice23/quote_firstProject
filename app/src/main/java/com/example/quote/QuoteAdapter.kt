package com.example.quote

import android.app.AlertDialog
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.view.menu.MenuView.ItemView
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class QuoteAdapter(var quotes:MutableList<Quote>) : RecyclerView.Adapter<QuoteAdapter.MyViewHolder>() {
    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    val db = Firebase.firestore
    var auth: FirebaseAuth = Firebase.auth
    val user = auth.currentUser

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_item,parent,false)
        return MyViewHolder(view)
    }


    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val quoteText : TextView = holder.itemView.findViewById(R.id.HomeQuoteAuthor)
        val quoteAuthor : TextView = holder.itemView.findViewById(R.id.HomeQuoteText)
        val deleteImageView: ImageView = holder.itemView.findViewById(R.id.delete_imageView)
        val followImage: ImageView = holder.itemView.findViewById(R.id.follow_imageView)


        quoteText.text = quotes[position].text
        quoteAuthor.text = quotes[position].author


        holder.itemView.setOnClickListener {
            val intent = Intent(holder.itemView.context, SingleQuoteActivity::class.java)
            intent.putExtra("quoteId", quotes[position].quoteId)
            holder.itemView.context.startActivity(intent)
        }

        val docRef = db.collection("follows")
            .document(user!!.uid + ":" + quotes[position].quoteId)
        docRef.get()
            .addOnSuccessListener { document ->
                if (document.exists()) {
                    followImage.setImageResource(R.drawable.baseline_favorite_24)
                    Log.d("TAG", "DocumentSnapshot data: ${document.data}")
                } else {
                    Log.d("TAG", "No such document")
                }
            }
            .addOnFailureListener { exception ->
                Log.d("TAG", "get failed with ", exception)
            }

        //following posts
        followImage.setOnClickListener {
            db.collection("follows").document(user!!.uid + ":" + quotes[position].quoteId)
            docRef.get()
                .addOnSuccessListener { document ->
                    if (document.exists()) {
                        db.collection("follows").document(user!!.uid + ":" + quotes[position].quoteId)
                            .delete()
                            .addOnSuccessListener {
                                followImage.setImageResource(R.drawable.baseline_favorite_border_24)
                            }
                            .addOnFailureListener { e -> Log.w("TAG", "Error deleting document", e) }
                    } else {
                        val newFollow = hashMapOf(
                            "uid" to user!!.uid,
                            "quoteId" to quotes[position].quoteId
                        )
                        db.collection("follows").document(user!!.uid + ":" + quotes[position].quoteId)
                            .set(newFollow)
                            .addOnSuccessListener {
                                followImage.setImageResource(R.drawable.baseline_favorite_24)
                            }
                            .addOnFailureListener { e -> Log.w("Follow", "Error writing document", e) }
                    }
                }
                .addOnFailureListener { exception ->
                    Log.d("TAG", "get failed with ", exception)
                }


        }


        //deleting posts
        if(user!!.uid != quotes[position].uid)
        {
            deleteImageView.visibility = View.INVISIBLE
        }
        deleteImageView.setOnClickListener {
            if(user!!.uid == quotes[position].uid)
            {
                AlertDialog.Builder(holder.itemView.context)
                    .setTitle("Delete")
                    .setMessage("Are you shure you want to delete this quote?")
                    .setPositiveButton("Cancel") { dialog, which ->
                        dialog.dismiss()
                    }.setNegativeButton("Delete") { dialog, which ->
                        db.collection("quotes").document(quotes[position].quoteId)
                            .delete()
                            .addOnSuccessListener {
                                Log.d("TAG", "DocumentSnapshot successfully deleted!")
                                quotes.removeAt(position)
                                notifyItemRemoved(position)
                                notifyItemRangeChanged(position, quotes.size)
                            }
                            .addOnFailureListener { e -> Log.w("TAG", "Error deleting document", e) }
                    }.show()


            }

        }
    }

    override fun getItemCount(): Int {
        return quotes.size
    }
}