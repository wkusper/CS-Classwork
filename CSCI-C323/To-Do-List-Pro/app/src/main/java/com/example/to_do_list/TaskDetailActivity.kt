package com.example.to_do_list

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class TaskDetailActivity : AppCompatActivity() {

    private lateinit var taskTextView: TextView
    private lateinit var markCompleteButton: Button
    private lateinit var cancelButton: Button
    private lateinit var previousButton: Button
    private lateinit var nextButton: Button

    private var taskList = mutableListOf<String>()
    private var currentTaskIndex = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_task_detail)

        taskTextView = findViewById(R.id.task_text_view)
        markCompleteButton = findViewById(R.id.mark_complete_button)
        cancelButton = findViewById(R.id.cancel_button)

        // Get the list of tasks
        taskList = intent.getStringArrayListExtra("taskList")?.toMutableList() ?: mutableListOf()
        currentTaskIndex = intent.getIntExtra("taskIndex", 0)

        updateTaskDisplay()

        // Remove task
        markCompleteButton.setOnClickListener {
            taskList.removeAt(currentTaskIndex)
            saveTasksToSharedPreferences()
            setResult(RESULT_OK, Intent().putStringArrayListExtra("taskList", ArrayList(taskList)))
            finish()
        }

        cancelButton.setOnClickListener {
            finish()
        }
    }

    private fun updateTaskDisplay() {
        taskTextView.text = taskList[currentTaskIndex]
    }

    private fun saveTasksToSharedPreferences() {
        val sharedPreferences = getSharedPreferences("TaskPrefs", MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putStringSet("tasks", taskList.toSet())
        editor.apply()
    }
}