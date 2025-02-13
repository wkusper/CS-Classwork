package com.example.to_do_list

import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class AboutActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_about)

        val backButton: Button = findViewById(R.id.back_button)
        backButton.setOnClickListener {
            finish() // This will close the AboutActivity and return to the previous one
        }
    }
}