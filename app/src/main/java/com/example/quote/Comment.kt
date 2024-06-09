package com.example.quote

data class Comment(val commentId: String, val uid: String, val quoteId: String, val text: String, val timestamp: com.google.firebase.Timestamp)
