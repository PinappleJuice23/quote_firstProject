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
import com.google.firebase.ktx.Firebase

class Login : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        auth = Firebase.auth

        val currentUser = auth.currentUser
        val loginEmailEdit: EditText = findViewById(R.id.LoginEmailEditText)
        val loginPasswordEdit: EditText = findViewById(R.id.LoginPasswordEditText)
        val loginButton: Button = findViewById(R.id.LoginLoginButton)
        val loginRegisterButton: Button = findViewById(R.id.LoginRegisterButton)

        loginRegisterButton.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }

        loginButton.setOnClickListener {
            auth.signInWithEmailAndPassword(loginEmailEdit.text.toString(), loginPasswordEdit.text.toString())
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.d("TAG", "signInWithEmail:success")
                        Toast.makeText(
                            baseContext,
                            "Hi! Welcome!",
                            Toast.LENGTH_SHORT,
                        ).show()
                        val intent = Intent(this, Home::class.java)
                        startActivity(intent)
                        finish()
                    } else {
                        // If sign in fails, display a message to the user.
                        Log.w("TAG", "signInWithEmail:failure", task.exception)
                        Toast.makeText(
                            baseContext,
                            "Authentication failed.",
                            Toast.LENGTH_SHORT,
                        ).show()
                    }
                }
        }
    }
}