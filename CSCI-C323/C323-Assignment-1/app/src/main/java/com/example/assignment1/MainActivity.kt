package com.example.assignment1

import android.os.Bundle
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val primeTextView: TextView = findViewById(R.id.primeTextView)


        val primes = GeneratePrime()
        primeTextView.text = primes
    }

    fun GeneratePrime(): StringBuilder {
        var n=1;
        val list = StringBuilder();
        while (n < 20){
            if (n < 2){
                n=n+1;
                continue;
            }
            if(n%2 == 0){
                n=n+1;
                continue;
            }
            if(n%3 ==0 && n != 3){
                n=n+1;
                continue;
            }
            else{
                list.append(n, ", ");
                n++;
            }
        }
       return list

    }
}