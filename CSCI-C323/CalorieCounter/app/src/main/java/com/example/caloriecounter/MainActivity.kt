package com.example.caloriecounter

import android.app.Activity
import android.app.DatePickerDialog
import android.content.Intent
import android.content.SharedPreferences
import android.os.Build
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Calendar

class MainActivity : AppCompatActivity() {

    // Variables to track the counters
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var totalCaloriesTextView: TextView
    private lateinit var totalWaterTextView: TextView
    private lateinit var dateTextView: TextView

    private var totalMealCalories: Int = 0
    private var totalSnackCalories: Int = 0
    private var totalWaterCups: Int = 0

    companion object {
        const val REQUEST_CODE_ADJUST = 1
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        sharedPreferences = getSharedPreferences("CalorieCounterPrefs", MODE_PRIVATE)

        totalCaloriesTextView = findViewById(R.id.totalCaloriesTextView)
        totalWaterTextView = findViewById(R.id.waterCupsTextView)
        dateTextView = findViewById(R.id.dateTextView)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        showCurrentDate()

        loadSavedData()

        // Button listeners
        findViewById<Button>(R.id.addMealButton).setOnClickListener {
            adjustAmount("meal", 10)
        }

        findViewById<Button>(R.id.addSnackButton).setOnClickListener {
            adjustAmount("snack", 10)
        }

        findViewById<Button>(R.id.waterButton).setOnClickListener {
            adjustAmount("water", 1)
        }

        findViewById<Button>(R.id.setDate).setOnClickListener {
            showDatePicker()
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun showCurrentDate() {
        // Use LocalDateTime
        val current = LocalDateTime.now()
        val formatter = DateTimeFormatter.ofPattern("MM-dd-yyyy")
        val formatted = current.format(formatter)
        dateTextView.text = "Date: $formatted"
    }

    private fun showDatePicker() {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(this, { _, selectedYear, selectedMonth, selectedDay ->
            val formattedDate = String.format("%02d-%02d-%d", selectedMonth + 1, selectedDay, selectedYear)
            dateTextView.text = "Date: $formattedDate"
        }, year, month, day)

        datePickerDialog.show()
    }

    private fun loadSavedData() {
        // Load saved values
        totalMealCalories = sharedPreferences.getInt("totalMealCalories", 0)
        totalSnackCalories = sharedPreferences.getInt("totalSnackCalories", 0)
        totalWaterCups = sharedPreferences.getInt("totalWaterCups", 0)

        // Update UI
        totalCaloriesTextView.text = "Total Calories: ${totalMealCalories + totalSnackCalories}"
        findViewById<TextView>(R.id.mealCaloriesTextView).text = "Meal Calories: $totalMealCalories"
        findViewById<TextView>(R.id.snackCaloriesTextView).text = "Snack Calories: $totalSnackCalories"
        findViewById<TextView>(R.id.waterCupsTextView).text = "Water Cups: $totalWaterCups"
    }

    private fun adjustAmount(type: String, increment: Int) {
        val intent = Intent(this, AdjustAmountActivity::class.java)
        intent.putExtra("type", type)
        intent.putExtra("increment", increment)
        startActivityForResult(intent, REQUEST_CODE_ADJUST)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == REQUEST_CODE_ADJUST && resultCode == Activity.RESULT_OK) {
            val newAmount = data?.getIntExtra("newAmount", 0) ?: 0

            when (data?.getStringExtra("type")) {
                "meal" -> {
                    totalMealCalories += newAmount
                    findViewById<TextView>(R.id.mealCaloriesTextView).text = "Meal Calories: $totalMealCalories"
                    saveMealCalories(totalMealCalories)
                }
                "snack" -> {
                    totalSnackCalories += newAmount
                    findViewById<TextView>(R.id.snackCaloriesTextView).text = "Snack Calories: $totalSnackCalories"
                    saveSnackCalories(totalSnackCalories)
                }
                "water" -> {
                    totalWaterCups += newAmount
                    findViewById<TextView>(R.id.waterCupsTextView).text = "Water Cups: $totalWaterCups"
                    saveWaterCups(totalWaterCups)
                }
            }

            // Update the total calories text (meals + snacks)
            totalCaloriesTextView.text = "Total Calories: ${totalMealCalories + totalSnackCalories}"
        }
    }

    // Counters (functions that count different totals)
    private fun saveMealCalories(calories: Int) {
        val editor = sharedPreferences.edit()
        editor.putInt("totalMealCalories", calories)
        editor.apply()
    }

    private fun saveSnackCalories(calories: Int) {
        val editor = sharedPreferences.edit()
        editor.putInt("totalSnackCalories", calories)
        editor.apply()
    }

    private fun saveWaterCups(cups: Int) {
        val editor = sharedPreferences.edit()
        editor.putInt("totalWaterCups", cups)
        editor.apply()
    }
}