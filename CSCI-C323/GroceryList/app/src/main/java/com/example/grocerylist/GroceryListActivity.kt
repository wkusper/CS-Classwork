package com.example.grocerylist

import GroceryItem
import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.*
import org.w3c.dom.Text
import java.util.*

class GroceryListActivity : AppCompatActivity() {

    private lateinit var database: DatabaseReference
    private lateinit var adapter: GroceryListAdapter
    private lateinit var totalCostText: TextView
    private val groceryItems = mutableListOf<GroceryItem>()
    private lateinit var listToken: String
    private var totalCost: Double = 0.0

    private var selectedDate: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_grocery_list)

        val listToken = intent.getStringExtra("TOKEN") ?: ""


        database = FirebaseDatabase.getInstance().getReference("groceryLists").child(listToken)
            .child("items")

        //Textview for total cost
        totalCostText = findViewById(R.id.totalCost)

        // Set up RecyclerView
        val recyclerView: RecyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        adapter = GroceryListAdapter(groceryItems) { selectedItem ->
            // Confirm deletion with the user
            AlertDialog.Builder(this)
                .setTitle("Delete Item")
                .setMessage("Are you sure you want to delete '${selectedItem.name}'?")
                .setPositiveButton("Yes") { _, _ ->
                    // Remove the item from Firebase
                    database.child(selectedItem.name).removeValue()
                        .addOnSuccessListener {
                            groceryItems.remove(selectedItem)
                            adapter.notifyDataSetChanged() // Notify adapter that data has changed

                            recalculateTotalCost() // find new total cost
                            totalCostText.text = "Total Cost: $$totalCost" // set text

                            Toast.makeText(
                                this,
                                "Item deleted: ${selectedItem.name}",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                        .addOnFailureListener { error ->
                            Toast.makeText(
                                this,
                                "Error deleting item: ${error.message}",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                }
                .setNegativeButton("No", null)
                .show()
        }
        recyclerView.adapter = adapter

        // Fetch items from Firebase
        fetchGroceryItems()

        // Initialize buttons
        val addItemButton: Button = findViewById(R.id.addButton)
        val homeButton: Button = findViewById(R.id.homeButton)
        val leaveButton: Button = findViewById(R.id.leaveButton)




        // Add item to the list
        addItemButton.setOnClickListener {
            val intent = Intent(this, AddGroceryItemActivity::class.java)
            intent.putExtra("TOKEN", listToken)
            startActivity(intent)
        }

        //Handle Home Button - navigate to MainActivity
        homeButton.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        //Handle Leave button - navigate to MainActivity and remove the list from MainActivity
        leaveButton.setOnClickListener {
            // Remove the list from Firebase (this will remove all items too)
            val groceryListRef = FirebaseDatabase.getInstance().getReference("groceryLists").child(listToken)
            groceryListRef.removeValue()
                .addOnSuccessListener {
                    // Notify MainActivity that the list was removed by sending a result
                    val intent = Intent()
                    intent.putExtra("LIST_REMOVED", listToken)  // Send back the list token that was removed
                    setResult(RESULT_OK, intent)

                    // Go to MainActivity and close this activity
                    val mainActivityIntent = Intent(this, MainActivity::class.java)
                    startActivity(mainActivityIntent)
                    finish()  // Close this activity
                }
                .addOnFailureListener { error ->
                    Toast.makeText(this, "Error removing list: ${error.message}", Toast.LENGTH_SHORT).show()
                }
        }
    }

    //function to fetch the items from the database.
    private fun fetchGroceryItems() {
        // fetch data in realtime
        database.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                //claer local list to avoid dupes, and reset totalCost
                groceryItems.clear()
                totalCost = 0.0
                //iterate through each item
                for (itemSnapshot in snapshot.children) {
                    //convert to the groceryitem class
                    val item = itemSnapshot.getValue(GroceryItem::class.java)
                    item?.let {
                        groceryItems.add(it)
                        it.price?.let { price ->
                            totalCost += price
                        }
                    }

                }
                adapter.notifyDataSetChanged()
                totalCostText.text = "Total cost: $$totalCost"
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@GroceryListActivity, "Failed to fetch items: ${error.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    //function to add itmes to the list
    private fun addItemToList(name: String, quantity: String, buyBeforeDate: String, price: Double, addedBy: String) {
        //unique ID for the items
        val itemId = database.push().key ?: return
        val newItem = GroceryItem(
            name = name,
            quantity = quantity,
            buyBeforeDate = buyBeforeDate,
            price = price,
            addedBy = addedBy
        )
        //add to database under unique id
        database.child(itemId).setValue(newItem)
            .addOnSuccessListener {
                Toast.makeText(this, "Item added: $name", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener { error ->
                Toast.makeText(this, "Error adding item: ${error.message}", Toast.LENGTH_SHORT).show()
            }
    }

    private fun recalculateTotalCost() {
        totalCost = 0.0
        for (item in groceryItems) {
            item.price?.let {
                totalCost += it
            }
        }
    }
}