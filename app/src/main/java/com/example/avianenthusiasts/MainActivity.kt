package com.example.avianenthusiasts

import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.example.avianenthusiasts.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    lateinit var app: MyApplication
    private lateinit var binding: ActivityMainBinding
    var getBird =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val data: Intent? = result.data
                val newBird = Bird(
                    species = data?.getStringExtra("species")!!,
                    comment = data.getStringExtra("comment")!!,
                    imageUri = data.getStringExtra("imageUri") ?: ""
                )

                app.loadFromFile()
                app.addItem(newBird)
            }
        }
    override fun onCreate(savedInstanceState: Bundle?) {
        app = application as MyApplication

        app.generateData()

        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        binding.buttonExit.setOnClickListener {
            finish()
        }
        binding.buttonInput.setOnClickListener {
            val intent = Intent(this, InputActivity::class.java)
            getBird.launch(intent)
        }
        binding.buttonList.setOnClickListener {
            val intent = Intent(this, ListActivity::class.java)
            startActivity(intent)
        }
    }
}