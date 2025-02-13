package com.example.to_do_list

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.firestore

class MainActivity : AppCompatActivity() {
    private lateinit var taskRecyclerView: RecyclerView
    private lateinit var addTaskButton: Button
    private lateinit var statusTextView: TextView
    private lateinit var sharedPreferences: SharedPreferences
    private var taskList = mutableListOf<String>() // List to hold tasks
    private val db = Firebase.firestore
    private val userId = FirebaseAuth.getInstance().currentUser?.uid ?: "defaultUser"


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        taskRecyclerView = findViewById(R.id.recycler_view)
        taskRecyclerView.layoutManager = LinearLayoutManager(this)

        addTaskButton = findViewById(R.id.add_button)
        statusTextView = findViewById(R.id.status_text)

        sharedPreferences = this?.getPreferences(Context.MODE_PRIVATE) ?: return

        // Load tasks
        loadTasks()

        val taskAdapter = TaskAdapter(taskList) { task ->
            openTaskDetail(task)
        }
        taskRecyclerView.adapter = taskAdapter

        // Add task button logic
        addTaskButton.setOnClickListener {
                val intent = Intent(this, AddTaskActivity::class.java)
                startActivityForResult(intent, 1)
        }
    }

    private fun openTaskDetail(taskIndex: String) {
        val intent = Intent(this, TaskDetailActivity::class.java)
        intent.putStringArrayListExtra("taskList", ArrayList(taskList))  // Pass the full task list
        intent.putExtra("taskIndex", taskIndex)
        startActivityForResult(intent, 2) // Use request code 2 for TaskDetailActivity

    }

    // Handle result from AddTaskActivity
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1 && resultCode == RESULT_OK) {
            val newTask = data?.getStringExtra("task")
            if (newTask != null && newTask.length <= 25) {

                addTaskToFirestore(newTask)

                taskList.add(newTask)
                taskRecyclerView.adapter?.notifyDataSetChanged()
            }
        } else if (requestCode == 2 && resultCode == RESULT_OK) {
            val updatedTaskList = data?.getStringArrayListExtra("taskList")
            if (updatedTaskList != null) {
                taskList.clear()
                taskList.addAll(updatedTaskList)
                taskRecyclerView.adapter?.notifyDataSetChanged()
            }
        }
    }

    private fun addTaskToFirestore(task: String) {
    // Add task to Firestore
        db.collection("users").document(userId).collection("tasks")
            .add(mapOf("task" to task))
            .addOnSuccessListener {
                taskRecyclerView.adapter?.notifyDataSetChanged()
            }  .addOnFailureListener { e -> // Handle the error
             statusTextView.text = "Error adding task: ${e.message}" }
    }


    // Load tasks from SharedPreferences
    private fun loadTasks() {
        db.collection("users").document(userId).collection("tasks").get()
            .addOnSuccessListener { result ->
                taskList.clear()
                for (document in result) {
                    document.getString("task")?.let {
                        taskList.add(it)
                    }
                }
                taskRecyclerView.adapter?.notifyDataSetChanged()
            }
            .addOnFailureListener { e ->
                statusTextView.text = "Error loading task : ${e.message}"
            }
    }
}