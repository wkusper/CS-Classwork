package com.example.c323_assignment_2

import android.content.Context
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {

    private lateinit var displayTextView: TextView
    private var currentInput: String = ""
    private var prevInput: String = ""
    private var operator: String? = null
    private var lastNum: Boolean = false
    private var stateError: Boolean = false
    private var lastDot: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        displayTextView = findViewById(R.id.calculatorText)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val sharedPref = this?.getPreferences(Context.MODE_PRIVATE)?: return
        displayTextView.setText(sharedPref.getString("lastText", "").toString())
        operator = (sharedPref.getString("lastOperatorUsed", null))
        prevInput = (sharedPref.getString("lastInput", "").toString())






        val b0: Button = findViewById(R.id.zeroButton)
        val b1: Button = findViewById(R.id.oneButton)
        val b2: Button = findViewById(R.id.twoButton)
        val b3: Button = findViewById(R.id.threeButton)
        val b4: Button = findViewById(R.id.fourButton)
        val b5: Button = findViewById(R.id.fiveButton)
        val b6: Button = findViewById(R.id.sixButton)
        val b7: Button = findViewById(R.id.sevenButton)
        val b8: Button = findViewById(R.id.eightButton)
        val b9: Button = findViewById(R.id.nineButton)
        val bAdd: Button = findViewById(R.id.addButton)
        val bSub: Button = findViewById(R.id.subtractButton)
        val bMult: Button = findViewById(R.id.multiplyButton)
        val bDiv: Button = findViewById(R.id.divideButton)
        val bClear: Button = findViewById(R.id.clearButton)
        val bEquals: Button = findViewById(R.id.equalsButton)
        val bDecimal: Button = findViewById(R.id.decimalButton)
        val bNegPos: Button = findViewById(R.id.negPosButton)

        b0.setOnClickListener{ onDigitPressed("0")}
        b1.setOnClickListener{ onDigitPressed("1")}
        b2.setOnClickListener{ onDigitPressed("2")}
        b3.setOnClickListener{ onDigitPressed("3")}
        b4.setOnClickListener{ onDigitPressed("4")}
        b5.setOnClickListener{ onDigitPressed("5")}
        b6.setOnClickListener{ onDigitPressed("6")}
        b7.setOnClickListener{ onDigitPressed("7")}
        b8.setOnClickListener{ onDigitPressed("8")}
        b9.setOnClickListener{ onDigitPressed("9")}
        bAdd.setOnClickListener{ onOpPressed("+")}
        bSub.setOnClickListener{ onOpPressed("-")}
        bMult.setOnClickListener{ onOpPressed("*")}
        bDiv.setOnClickListener{ onOpPressed("/")}
        bClear.setOnClickListener{ onClear()}
        bEquals.setOnClickListener{ onEqual()}
        bDecimal.setOnClickListener{ onDecimal()}
        bNegPos.setOnClickListener{ onNegPos()}
    }

    private fun onDigitPressed( digit: String){
        if (stateError){
            displayTextView.text = digit
            stateError = false
        } else{
            displayTextView.append(digit)
        }
        currentInput += digit
        lastNum = true
    }

    private fun onOpPressed( op: String){
        if (lastNum && !stateError){
            prevInput = currentInput
            operator = op
            displayTextView.append(" $op ")
            currentInput = ""
            lastNum = false
            lastDot = false
        }
    }

    private fun Math(): Double {
        val val1 = prevInput.toDouble()
        val val2 = currentInput.toDouble()

        return when (operator){
            "+" -> val1 + val2
            "-" -> val1 - val2
            "*" -> val1 * val2
            "/" -> val1 / val2
            else -> 0.0
        }
    }

    private fun onEqual(){
        if (lastNum && operator !=null && prevInput.isNotEmpty()){
            val result = Math()
            displayTextView.text = result.toString()
            currentInput = result.toString()
            operator = null
            prevInput = ""
        }
    }

    private fun onClear(){
        displayTextView.text = ""
        currentInput = ""
        prevInput = ""
        operator = null
        stateError = false
        lastNum = false
        lastDot = false
    }

    private fun onDecimal(){
        if(lastNum && !stateError && !lastDot){
            displayTextView.append(".")
            currentInput += "."
            lastNum = false
            lastDot = true
        }
    }

    private fun onNegPos(){
        if (currentInput.isNotEmpty() && currentInput != "0"){
            if (currentInput.startsWith("-")){
                currentInput = currentInput.substring(1)
            } else {
                currentInput = "-$currentInput"
            }
            displayTextView.text = currentInput
        }
    }


    override fun onStop() {
        super.onStop()
        val sharedPref = this?.getPreferences(Context.MODE_PRIVATE) ?: return

        with (sharedPref.edit()) {
            val calcText : TextView = displayTextView
            val lastOperator : String? = operator
            val lastInput : String = prevInput

            putString("lastText", calcText.text.toString() )
            putString("lastOperatorUsed", lastOperator)
            putString("lastInput", lastInput)

            apply()

        }
    }
}