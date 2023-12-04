package com.example.avianenthusiasts

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.avianenthusiasts.databinding.ActivityInputBinding

class InputActivity : AppCompatActivity() {
    private lateinit var binding: ActivityInputBinding
    private var position: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityInputBinding.inflate(layoutInflater)
        setContentView(binding.root)

        position = intent.getStringExtra("position").toString()

        if (intent.hasExtra("species")) {
            binding.editTextSpecies.setText(intent.getStringExtra("species"))
        }
        if (intent.hasExtra("comment")) {
            binding.editTextComment.setText(intent.getStringExtra("comment"))
        }

        binding.buttonAdd.setOnClickListener {
            val speciesString = binding.editTextSpecies.text.toString()
            val commentString = binding.editTextComment.text.toString()

            if (speciesString.isNotBlank()) {
                binding.editTextSpecies.text?.clear()
                binding.editTextComment.text?.clear()

                val data = Intent()
                data.putExtra("species", speciesString)
                data.putExtra("comment", commentString)
                data.putExtra("position", position)
                setResult(RESULT_OK, data)
                finish()
            } else {
                Toast.makeText(this, "Species cannot be blank!", Toast.LENGTH_SHORT).show()
            }
        }
    }
}