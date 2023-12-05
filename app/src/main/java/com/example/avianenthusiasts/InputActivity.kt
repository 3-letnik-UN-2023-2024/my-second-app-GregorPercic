package com.example.avianenthusiasts

import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.example.avianenthusiasts.databinding.ActivityInputBinding

class InputActivity : AppCompatActivity() {
    private lateinit var binding: ActivityInputBinding
    private var position: String? = null
    private var selectedImageUri: Uri? = null

    private val pickImageLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            selectedImageUri = result.data?.data
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityInputBinding.inflate(layoutInflater)
        setContentView(binding.root)

        position = intent.getStringExtra("position").toString()

        if (!intent.hasExtra("position")) {
            binding.buttonAddImage.visibility = View.VISIBLE
        } else {
            binding.buttonAddImage.visibility = View.GONE
            binding.buttonAdd.text = getString(R.string.edit)
        }

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

                val data = Intent().apply {
                    putExtra("species", speciesString)
                    putExtra("comment", commentString)
                    putExtra("position", position)
                    selectedImageUri?.let {
                        putExtra("imageUri", it.toString())
                    }
                }

                setResult(RESULT_OK, data)
                finish()
            } else {
                Toast.makeText(this, "Species cannot be blank!", Toast.LENGTH_SHORT).show()
            }
        }

        binding.buttonAddImage.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            pickImageLauncher.launch(intent)
        }
    }
}