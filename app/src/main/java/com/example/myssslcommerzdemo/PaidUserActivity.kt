package com.example.myssslcommerzdemo

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import org.w3c.dom.Text

class PaidUserActivity : AppCompatActivity() {

    private lateinit var tv1 : TextView
    private lateinit var tv2 : TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_paid_user)

        tv1 = findViewById(R.id.tv1_a2)
        tv2 = findViewById(R.id.tv2_a2)

        val sharedPref = this@PaidUserActivity.getPreferences(Context.MODE_PRIVATE)
        val durationType = sharedPref.getString(Constants.DURATION_TYPE,"gg")
        val amount = sharedPref.getString(Constants.AMOUNT,"gg")

        tv2.text = "You have Subscribed in $durationType package by $amount BDT "

    }
}