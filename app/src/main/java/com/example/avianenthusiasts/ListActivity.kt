package com.example.avianenthusiasts

import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.avianenthusiasts.databinding.ActivityListBinding
import com.facebook.FacebookSdk
import com.facebook.share.model.ShareLinkContent
import com.facebook.share.widget.ShareButton

class ListActivity : AppCompatActivity() {
    private lateinit var binding: ActivityListBinding
    private lateinit var birdAdapter: BirdAdapter
    lateinit var app: MyApplication

    var getBird =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val data: Intent? = result.data

                val species = data?.getStringExtra("species")!!
                val comment = data?.getStringExtra("comment")!!
                val position = data?.getStringExtra("position")!!.toInt()

                val bird = app.birdList[position]
                app.updateItem(bird.uuid, species, comment)
                app.loadFromFile()

                //birdAdapter.birds = app.birdList
                birdAdapter.notifyItemChanged(position)
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupRecyclerView()
    }
    private fun setupRecyclerView() {
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        app = application as MyApplication
        app.loadFromFile()
        birdAdapter = BirdAdapter(app.birdList, app)
        binding.recyclerView.adapter = birdAdapter
    }

    fun editSubject(position: Int, bird: Bird) {
        val editIntent = Intent(this, InputActivity::class.java)
        editIntent.putExtra("position", position.toString())
        editIntent.putExtra("species", bird.species)
        editIntent.putExtra("comment", bird.comment)
        getBird.launch(editIntent)
    }
}