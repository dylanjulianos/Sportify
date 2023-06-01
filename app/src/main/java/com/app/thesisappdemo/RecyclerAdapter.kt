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
import com.google.firebase.firestore.FirebaseFirestore
import com.squareup.picasso.Picasso
import kotlinx.coroutines.currentCoroutineContext
import kotlin.coroutines.coroutineContext

class RecyclerAdapter(private val dataset: List<DocumentSnapshot>, private val fragmentManager: FragmentManager): RecyclerView.Adapter<RecyclerAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerAdapter.ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.card_layout, parent, false)

        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val documentSnapshot = dataset[position]
        val nama = documentSnapshot.getString("item_name")
        val description = documentSnapshot.getString("item_bio")
        val imageUrl = documentSnapshot.getString("image_url")
        val kode_barang = documentSnapshot.getString("item_code")
        val kategori = documentSnapshot.getString("sport_category")
        val harga = documentSnapshot.getLong("item_price")
        val db = FirebaseFirestore.getInstance()

        holder.itemName.text = nama
        holder.itemBio.text = description
        if (imageUrl != null) { Picasso.get().load(imageUrl).into(holder.itemImage)
        } else {
            // If imageUrl is null or empty, you can set a placeholder image or handle it differently
            holder.itemImage.setImageResource(R.drawable.baseline_camera_alt_24)
        }

        holder.edit_item_button.setOnClickListener{
            val hal_edit_item = admin_edit_item()
            val transaction = fragmentManager.beginTransaction()
            transaction.replace(R.id.frame_layout, hal_edit_item)
            transaction.commit()
        }

        holder.delete_item_button.setOnClickListener{
            val collectionName = "Items"
            val documentId: String = kode_barang.toString()

            val documentRef = db.collection(collectionName).document(documentId)
            documentRef.delete().addOnSuccessListener {
                // Document deleted successfully
                Toast.makeText(holder.itemView.context, "Item has been deleted", Toast.LENGTH_SHORT).show()
                val hal_awal = admin_home()
                val transaction = fragmentManager.beginTransaction()
                transaction.replace(R.id.frame_layout, hal_awal)
                transaction.commit()
            }
                .addOnFailureListener { e ->
                    // An error occurred while deleting the document
                    Toast.makeText(holder.itemView.context, e.message, Toast.LENGTH_LONG).show()
                }
        }

    }

    override fun getItemCount(): Int {
        return dataset.size
    }

    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        var itemImage : ImageView
        var itemName : TextView
        var itemBio : TextView
        lateinit var edit_item_button : ImageButton
        lateinit var delete_item_button : ImageButton

        init {
            itemImage = itemView.findViewById(R.id.item_display_picture)
            itemName = itemView.findViewById(R.id.item_display_name)
            itemBio = itemView.findViewById(R.id.item_display_bio)
            edit_item_button = itemView.findViewById(R.id.edit_item_button)
            delete_item_button = itemView.findViewById(R.id.delete_item_button)
        }
    }
}