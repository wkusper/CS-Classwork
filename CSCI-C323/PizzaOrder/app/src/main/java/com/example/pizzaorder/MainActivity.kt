package com.example.pizzaorder

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import android.widget.*
import androidx.appcompat.app.AlertDialog

class MainActivity : AppCompatActivity() {
    private lateinit var sauceSpinner: Spinner
    private lateinit var chkChicken: CheckBox
    private lateinit var chkBacon: CheckBox
    private lateinit var chkPepperoni: CheckBox
    private lateinit var chkOlives: CheckBox
    private lateinit var chkOnions: CheckBox
    private lateinit var totalCostTextView: TextView

    private var sauceCost = 0
    private var toppingCost = 0
    private var toppingMessage = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Initialize Views
        sauceSpinner = findViewById(R.id.sauceSpinner)
        chkChicken = findViewById(R.id.chkChicken)
        chkBacon = findViewById(R.id.chkBacon)
        chkPepperoni = findViewById(R.id.chkPepperoni)
        chkOlives = findViewById(R.id.chkOlives)
        chkOnions = findViewById(R.id.chkOnions)
        totalCostTextView = findViewById(R.id.totalCost)

        // Handle Sauce Selection
        sauceSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View,
                position: Int,
                id: Long
            ) {
                sauceCost = when (position) {
                    0 -> 12  // Tomato Basil
                    1 -> 10  // Marinara
                    2 -> 15  // Barbecue
                    else -> 0
                }
                showToast("Selected: ${parent.getItemAtPosition(position)}")
                updateTotalCost()
            }

            override fun onNothingSelected(parent: AdapterView<*>) {}
        }

        // Handle Topping Selection
        val toppings = listOf(chkChicken, chkBacon, chkPepperoni, chkOlives, chkOnions)
        for (topping in toppings) {
            topping.setOnCheckedChangeListener { _, isChecked ->
                val selectedToppingsCount = toppings.count { it.isChecked }
                if (isChecked) {
                    // Checks if 4 toppings are selected. If so, selecting a 5th doesn't add to price
                    if (selectedToppingsCount <=4) {
                        toppingCost += 1
                    } } else {
                    if (selectedToppingsCount < 4) {
                        toppingCost -= 1
                    }
                }

                // Check if more than 2 toppings are selected and show dialog if thatr is the case

                if (selectedToppingsCount >= 2 && !toppingMessage) {
                    showToppingLimitDialog()
                    toppingMessage = true
                }

                // Update dislpayed cost
                updateTotalCost()
            }
        }
    }

    // Function to show topping selection dialog
    private fun showToppingLimitDialog() {

        val builder = AlertDialog.Builder(this)
        builder.setTitle("Topping Limit")
        // Whatever message you wanna put here
        builder.setMessage("Select 5 toppings for the price of 4.")
        // Confirm button
        builder.setPositiveButton("Confirmed") { dialog, which ->
            dialog.dismiss()
        }
        val dialog = builder.create()
        dialog.show()
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun updateTotalCost() {
        val totalCost = sauceCost + toppingCost
        totalCostTextView.text = "Total Cost: $$totalCost"
    }
}