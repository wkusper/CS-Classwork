package com.example.grocerylist

import GroceryItem
import android.app.DatePickerDialog
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import java.util.*

class AddGroceryItemActivity : AppCompatActivity() {

    private lateinit var database: DatabaseReference
    private var selectedDate: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_grocery_item)

        val listToken = intent.getStringExtra("TOKEN") ?: ""
        database = FirebaseDatabase.getInstance().getReference("groceryLists").child(listToken).child("items")

        val itemNameEditText: EditText = findViewById(R.id.itemNameEditText)
        val itemQuantityEditText: EditText = findViewById(R.id.itemQuantityEditText)
        val itemPriceEditText: EditText = findViewById(R.id.itemPriceEditText)
        val itemOwnerEditText: EditText = findViewById(R.id.itemOwnerEditText)
        val datePickerButton: Button = findViewById(R.id.datePickerButton)
        val dateTextView: TextView = findViewById(R.id.dateTextView)
        val addItemButton: Button = findViewById(R.id.addButton)

        datePickerButton.setOnClickListener {
            val calendar = Calendar.getInstance()
            val datePicker = DatePickerDialog(
                this,
                { _, year, month, dayOfMonth ->
                    selectedDate = "$year-${month + 1}-$dayOfMonth"
                    dateTextView.text = "Buy Before Date: $selectedDate"
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
            )
            datePicker.show()
        }

        addItemButton.setOnClickListener {
            val itemName = itemNameEditText.text.toString().trim()
            val itemQuantity = itemQuantityEditText.text.toString().trim()
            val itemPrice = itemPriceEditText.text.toString().toDoubleOrNull()
            val itemOwner = itemOwnerEditText.text.toString().trim()

            if (itemName.isEmpty() || itemQuantity.isEmpty() || itemPrice == null || itemOwner.isEmpty() || selectedDate.isEmpty()) {
                Toast.makeText(this, "Please fill in all fields.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            addItemToList(itemName, itemQuantity, selectedDate, itemPrice, itemOwner)
        }
    }

    private fun addItemToList(name: String, quantity: String, buyBeforeDate: String, price: Double, addedBy: String) {
        val itemId = database.push().key ?: return
        val newItem = GroceryItem(
            name = name,
            quantity = quantity,
            buyBeforeDate = buyBeforeDate,
            price = price,
            addedBy = addedBy
        )
        database.child(itemId).setValue(newItem)
            .addOnSuccessListener {
                Toast.makeText(this, "Item added: $name", Toast.LENGTH_SHORT).show()
                finish() // Close activity after item is added
            }
            .addOnFailureListener { error ->
                Toast.makeText(this, "Error adding item: ${error.message}", Toast.LENGTH_SHORT).show()
            }
    }
}
