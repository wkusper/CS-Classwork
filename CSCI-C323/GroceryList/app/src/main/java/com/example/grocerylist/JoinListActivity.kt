package com.example.grocerylist

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.FirebaseApp
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class JoinListActivity : AppCompatActivity() {
    private lateinit var database: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_join_list)

        FirebaseApp.initializeApp(this)

        val tokenEditText: EditText = findViewById(R.id.tokenEditText)
        val nicknameEditText: EditText = findViewById(R.id.nicknameEditText)
        val joinButton: Button = findViewById(R.id.joinButton)


        database = FirebaseDatabase.getInstance().reference

        // Retrieve token if using RecyclerView
        val token = intent.getStringExtra("token")  // Token passed from MainActivity via Intent
        if (token != null) {
            // Token exists, populate the EditText with the passed token
            tokenEditText.setText(token)
            Log.d("JoinListActivity", "Joining list with token: $token")
        }

        // Handle joining a list
        joinButton.setOnClickListener {
            val token = tokenEditText.text.toString()
            val nickname = nicknameEditText.text.toString()

            if (token.isNotEmpty() && nickname.isNotEmpty()) {
                database.child("groceryLists").child(token.trim()).get()
                    .addOnSuccessListener { snapshot ->
                        if (snapshot.exists()) {
                            val listName = snapshot.child("name").value.toString()
                            Log.d("JoinListActivity", "List exists: $listName")

                            // go to the list
                            val intent = Intent(this, GroceryListActivity::class.java)
                            intent.putExtra("TOKEN", token.trim())
                            intent.putExtra("NICKNAME", nickname)
                            startActivity(intent)
                        } else {
                            Log.d("JoinListActivity", "Snapshot does not exist for token: $token")
                            Toast.makeText(this, "Invalid token", Toast.LENGTH_SHORT).show()
                        }
                    }
                    .addOnFailureListener { error ->
                        Log.e("JoinListActivity", "Error: ${error.message}")
                        Toast.makeText(this, "Error connecting to database", Toast.LENGTH_LONG)
                            .show()
                    }
            }
        }
    }
}