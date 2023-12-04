package com.example.avianenthusiasts

import android.app.AlertDialog
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.avianenthusiasts.databinding.ItemBirdBinding

class BirdAdapter(var birds: MutableList<Bird>, val app: MyApplication) :
    RecyclerView.Adapter<BirdAdapter.ViewHolder>() {
    lateinit var binding: ItemBirdBinding

    class ViewHolder(val binding: ItemBirdBinding) : RecyclerView.ViewHolder(binding.root) {
        val speciesTextView: TextView = binding.speciesTextView
        val commentTextView: TextView = binding.commentTextView
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        binding = ItemBirdBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_bird, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val bird = birds[position]
        holder.speciesTextView.text = bird.species
        holder.commentTextView.text = bird.comment

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
                    app.deleteSubject(birdToRemove.uuid)
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