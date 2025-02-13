package com.example.to_do_list

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity

class AddTaskActivity : AppCompatActivity() {

    private lateinit var taskEditText: EditText
    private lateinit var saveButton: Button
    private lateinit var cancelButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_task)

        taskEditText = findViewById(R.id.task_edit_text)
        saveButton = findViewById(R.id.save_button)
        cancelButton = findViewById(R.id.cancel_button)

        // Save task
        saveButton.setOnClickListener {
            val task = taskEditText.text.toString().trim()
            if (task.length in 1..25) {
                val resultIntent = Intent()
                resultIntent.putExtra("task", task)
                setResult(Activity.RESULT_OK, resultIntent)
                finish() // Close activity
            } else {
                print( "Task must be between 1 and 25 characters")
            }
        }

        // Cancel task addition
        cancelButton.setOnClickListener {
            setResult(Activity.RESULT_CANCELED)
            finish()
        }
    }
}