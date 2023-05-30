package com.app.thesisappdemo

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.DocumentSnapshot
import com.squareup.picasso.Picasso

class RecyclerAdapter(private val dataset: List<DocumentSnapshot>, private val fragmentManager: FragmentManager): RecyclerView.Adapter<RecyclerAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerAdapter.ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.card_layout, parent, false)
        val edit_item_button = v.findViewById(R.id.edit_item_button) as ImageButton
        val delete_item_button = v.findViewById(R.id.delete_item_button) as ImageButton

        edit_item_button.setOnClickListener{
            val hal_edit_item = admin_edit_item()
            val transaction = fragmentManager.beginTransaction()
            transaction.replace(R.id.frame_layout, hal_edit_item)
            transaction.commit()
        }

        delete_item_button.setOnClickListener{
            Toast.makeText(v.context, "delete item button", Toast.LENGTH_SHORT).show()
        }

        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val documentSnapshot = dataset[position]
        val nama = documentSnapshot.getString("item_name")
        val description = documentSnapshot.getString("item_bio")
        val imageUrl = documentSnapshot.getString("image_url")

        holder.itemName.text = nama
        holder.itemBio.text = description
        if (imageUrl != null) { Picasso.get().load(imageUrl).into(holder.itemImage)
        } else {
            // If imageUrl is null or empty, you can set a placeholder image or handle it differently
            holder.itemImage.setImageResource(R.drawable.baseline_person_24)
        }

    }

    override fun getItemCount(): Int {
        return dataset.size
    }

    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        var itemImage : ImageView
        var itemName : TextView
        var itemBio : TextView

        init {
            itemImage = itemView.findViewById(R.id.item_display_picture)
            itemName = itemView.findViewById(R.id.item_display_name)
            itemBio = itemView.findViewById(R.id.item_display_bio)
        }
    }
}