package com.example.avianenthusiasts

import android.app.AlertDialog
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.avianenthusiasts.databinding.ItemBirdBinding
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso

class BirdAdapter(var birds: MutableList<Bird>, val app: MyApplication) :
    RecyclerView.Adapter<BirdAdapter.ViewHolder>() {
    lateinit var binding: ItemBirdBinding

    class ViewHolder(val binding: ItemBirdBinding) : RecyclerView.ViewHolder(binding.root) {
        val speciesTextView: TextView = binding.speciesTextView
        val commentTextView: TextView = binding.commentTextView
        val latitudeTextView: TextView = binding.latitudeTextView
        val longitudeTextView: TextView = binding.longitudeTextView
        val birdImageView: ImageView = binding.birdImageView
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        binding = ItemBirdBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        // !!!!!!!!!!!!!!!!! removed something
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val bird = birds[position]
        holder.speciesTextView.text = bird.species
        holder.commentTextView.text = bird.comment
        holder.latitudeTextView.text = bird.latitude.toString()
        holder.longitudeTextView.text = bird.longitude.toString()

        if (bird.imageUri != "") {
            println("uriOOOOO!!!!!!!!!!: ${bird.imageUri}")

            Picasso.get()
                .load(Uri.parse(bird.imageUri))
                .error(R.drawable.crow)
                .into(holder.birdImageView, object : Callback {
                    override fun onSuccess() {
                        // Image successfully loaded
                    }

                    override fun onError(e: Exception) {
                        // Error loading the image
                        println("Picasso error: $e")
                    }
                })

/*
            Glide.with(holder.itemView.getContext())
                .load(Uri.parse(bird.imageUri))
                .error(R.drawable.crow)
                .into(holder.birdImageView);

 */
        } else {
            holder.birdImageView.setImageResource(R.drawable.crow)
            //holder.birdImageView.visibility = View.GONE
        }

        holder.itemView.setOnClickListener {
            val context = holder.itemView.context
            if (context is ListActivity) {
                context.editSubject(position, bird)
            }
        }

        holder.itemView.setOnLongClickListener { view ->
            val context = view.context
            AlertDialog.Builder(context)
                .setTitle("Delete bird entry")
                .setMessage("Are you sure you want to delete this bird entry?")
                .setPositiveButton("Yes") { dialog, which ->
                    val birdToRemove = birds[position]
                    birds.removeAt(position)
                    notifyItemRemoved(position)
                    notifyItemRangeChanged(position, birds.size - 1)
                    app.deleteItem(birdToRemove.uuid)
                }
                .setNegativeButton("No", null)
                .show()
            true
        }
    }

    override fun getItemCount() = birds.size

    fun addSubject(subject: Bird) {
        birds.add(subject)
        notifyItemInserted(birds.size - 1)
    }
}