package com.example.newfirstapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val btnMinus = findViewById<Button>(R.id.mybuttonminus)
        val btnPlus = findViewById<Button>(R.id.mybuttonplus)
        val mainText = findViewById<TextView>(R.id.textView)
        var timesClicked = 0

        btnMinus.setOnClickListener{

            if(timesClicked >0){
                timesClicked = timesClicked - 1
                mainText.text= timesClicked.toString() + " personas"
            }

        }

        btnPlus.setOnClickListener{
            timesClicked = timesClicked + 1
            mainText.text= timesClicked.toString() + " personas"
        }
    }
}