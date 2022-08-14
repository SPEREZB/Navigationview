package com.example.navigationview

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    fun click(v:View){
        val intent = Intent(this, MainActivity2::class.java);
        val  us = findViewById<TextView>(R.id.us);
        val  ps = findViewById<TextView>(R.id.ps);
        val b = Bundle();
        b.putString("user", us.text.toString());
        b.putString("password", ps.text.toString());
        intent.putExtras(b);
        startActivity(intent);
    }
}