package com.example.caloriecounter

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class AdjustAmountActivity : AppCompatActivity() {

    private lateinit var adjustmentTitle: TextView
    private lateinit var amountTextView: TextView
    private var amount: Int = 0
    private var incrementValue: Int = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.adjust_amount)

        adjustmentTitle = findViewById(R.id.adjustmentTitle)
        amountTextView = findViewById(R.id.amountTextView)
        val increaseButton: Button = findViewById(R.id.increaseButton)
        val decreaseButton: Button = findViewById(R.id.decreaseButton)
        val saveButton: Button = findViewById(R.id.saveButton)

        val type = intent.getStringExtra("type")
        amount = intent.getIntExtra("currentAmount", 0)

        // Set to 10 if adjusting calories
        if (type == "meal" || type == "snack") {
            incrementValue = 10
            adjustmentTitle.text = "Adjust Calories"
        } else if (type == "water") {
            incrementValue = 1
            adjustmentTitle.text = "Adjust Water Cups"
        }

        updateAmountDisplay()

        // Listeners for buttons
        increaseButton.setOnClickListener {
            amount += incrementValue
            updateAmountDisplay()
        }

        decreaseButton.setOnClickListener {
            if (amount > 0) {
                amount -= incrementValue
            }
            updateAmountDisplay()
        }

        // Save adjusted amount
        saveButton.setOnClickListener {
            val resultIntent = Intent()
            resultIntent.putExtra("newAmount", amount)
            resultIntent.putExtra("type", type)
            setResult(Activity.RESULT_OK, resultIntent)
            finish()
        }
    }

    private fun updateAmountDisplay() {
        amountTextView.text = amount.toString()
    }
}