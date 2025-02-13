package com.example.grocerylist

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ListAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.FirebaseApp
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class MainActivity : AppCompatActivity() {

    private lateinit var database: DatabaseReference
    private lateinit var ListRecyclerView: RecyclerView
    private lateinit var ListItemAdapter: ListItemAdapter
    private val lists: MutableList<ListItem> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // put this in all of the activites b/c was getting a wierd error and this seemed to fix it
        FirebaseApp.initializeApp(this)

        // database instance to store/retrieve list of grocery lists
        database = FirebaseDatabase.getInstance().reference

        val createListButton: Button = findViewById(R.id.createListButton)
        val joinListButton: Button = findViewById(R.id.joinListButton)

        ListRecyclerView = findViewById(R.id.recycler_view)
        ListRecyclerView.layoutManager = LinearLayoutManager(this)
        ListItemAdapter = ListItemAdapter(lists, this)
        ListRecyclerView.adapter = ListItemAdapter


        createListButton.setOnClickListener {
            startActivity(Intent(this, CreateListActivity::class.java))
        }

        joinListButton.setOnClickListener {
            startActivity(Intent(this, JoinListActivity::class.java))
        }

        fetchGroceryLists()
    }
    private fun fetchGroceryLists() {
        database.child("groceryLists").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                lists.clear()
                for (dataSnapshot in snapshot.children) {
                    val name = dataSnapshot.child("name").getValue(String::class.java)
                    val token = dataSnapshot.key // Firebase key is the token
                    if (name != null && token != null) {
                        lists.add(ListItem(name, token))
                    }
                }
                ListItemAdapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@MainActivity, "Error loading lists", Toast.LENGTH_SHORT).show()
            }
        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == RESULT_OK) {
            // Check if a list was removed
            val listToken = data?.getStringExtra("LIST_REMOVED")
            if (!listToken.isNullOrEmpty()) {
                // Remove the list from RecyclerView by updating the data source
                lists.removeAll { it.token == listToken }  // Assuming 'groceryList' is your data source
                ListItemAdapter.notifyDataSetChanged()
            }
        }
    }
}