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

class MainActivity : AppCompatActivity() {
    private lateinit var taskRecyclerView: RecyclerView
    private lateinit var addTaskButton: Button
    private lateinit var statusTextView: TextView
    private lateinit var sharedPreferences: SharedPreferences
    private var taskList = mutableListOf<String>() // List to hold tasks

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
            if (taskList.size < 7) {
                val intent = Intent(this, AddTaskActivity::class.java)
                startActivityForResult(intent, 1)
            }
        }

        updateAddButton()

        // About Button
        findViewById<Button>(R.id.about_button).setOnClickListener {
            startActivity(Intent(this, AboutActivity::class.java)) // Launch About activity
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
                taskList.add(newTask)
                taskRecyclerView.adapter?.notifyDataSetChanged()
                updateAddButton()
            }
        } else if (requestCode == 2 && resultCode == RESULT_OK) {
            val updatedTaskList = data?.getStringArrayListExtra("taskList")
            if (updatedTaskList != null) {
                taskList.clear()
                taskList.addAll(updatedTaskList)
                taskRecyclerView.adapter?.notifyDataSetChanged()
                updateAddButton()
            }
        }
    }


    // Update Add Button based on number of tasks
    private fun updateAddButton() {
        if (taskList.size >= 7) {
            addTaskButton.visibility = Button.GONE
            statusTextView.text = "Max tasks reached."
        } else {
            addTaskButton.visibility = Button.VISIBLE
            statusTextView.text = ""
        }
    }

    // Load tasks from SharedPreferences
    private fun loadTasks() {
        val tasks = sharedPreferences.getStringSet("taskList", emptySet())?.toMutableList()
        if (tasks != null) {
            taskList.addAll(tasks)
        }
    }

    // Save tasks to SharedPreferences
    override fun onStop() {
        super.onStop()
        val sharedPref = this.getPreferences(Context.MODE_PRIVATE) ?: return
        with(sharedPref.edit()) {
            putStringSet("taskList", taskList.toSet())
            apply()
        }
    }
}