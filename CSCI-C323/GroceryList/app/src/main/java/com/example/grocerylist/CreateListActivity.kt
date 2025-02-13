package com.example.grocerylist

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.FirebaseApp

class CreateListActivity : AppCompatActivity() {
    private lateinit var database: DatabaseReference


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_list)

        FirebaseApp.initializeApp(this)
        database = FirebaseDatabase.getInstance().reference

        val listNameEditText: EditText = findViewById(R.id.listNameEditText)
        val createButton: Button = findViewById(R.id.createButton)

        createButton.setOnClickListener {
            val listName = listNameEditText.text.toString().trim()
            if (listName.isNotEmpty()) {
                createNewList(listName)
            } else {
                Toast.makeText(this, "List name cannot be empty", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun createNewList(listName: String) {
        // Generate a unique token for the list
        val token = database.push().key ?: return

        // Create a map of data to save
        val listData = mapOf(
            "name" to listName,
            "createdAt" to System.currentTimeMillis()
        )

        // Save to Firebase
        database.child("groceryLists").child(token).setValue(listData)
            .addOnSuccessListener {
                // Show success message and close the activity
                Toast.makeText(this, "List created successfully. Token: $token", Toast.LENGTH_LONG)
                    .show()
                //copy token to clipboard so its easy to send to someone.
                copyToClipboard(token)
                finish()
            }
            .addOnFailureListener { error ->
                // Show error message
                Toast.makeText(this, "Error: ${error.message}", Toast.LENGTH_LONG).show()
            }
    }

    // Function to copy the insane token that gets generated.
    private fun copyToClipboard(token: String) {
        val clipboard = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clip = ClipData.newPlainText("Grocery List Token", token)
        clipboard.setPrimaryClip(clip)
        Toast.makeText(this, "Token copied to clipboard", Toast.LENGTH_SHORT).show()
    }
}
