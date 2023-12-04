package com.example.avianenthusiasts

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.avianenthusiasts.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        binding.buttonExit.setOnClickListener {
            finish()
        }
        binding.buttonInput.setOnClickListener {
            val intent = Intent(this, InputActivity::class.java)
            startActivity(intent)
        }
    }
}