package com.app.thesisappdemo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.squareup.picasso.Picasso
import java.text.NumberFormat

class MyAdapter(private val bundle: Bundle, private val dataset: List<DocumentSnapshot>, private val fragmentManager: FragmentManager): RecyclerView.Adapter<MyAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyAdapter.ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.recycleritem, parent, false)

        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val documentSnapshot = dataset[position]
        val numberFormat = NumberFormat.getInstance()
        numberFormat.minimumFractionDigits = 2
        numberFormat.maximumFractionDigits = 4
        numberFormat.minimumIntegerDigits = 1

        val db = FirebaseFirestore.getInstance()

        val nama = documentSnapshot.getString("item_name")
        val description = documentSnapshot.getString("item_bio")
        val kode_barang = documentSnapshot.getString("item_code")
        val kategori = documentSnapshot.getString("sport_category")
        val price = documentSnapshot.getDouble("item_price")
        val imageUrl = documentSnapshot.getString("image_url")

        val uid = bundle?.getString("userid")

        val formattedNumber = numberFormat.format(price)

        holder.itemName.text = nama
        holder.itemPrice.text = "Rp" + formattedNumber + "-/day"
        if (imageUrl != null) { Picasso.get().load(imageUrl).into(holder.itemImage)
        } else {
            // If imageUrl is null or empty, you can set a placeholder image or handle it differently
            holder.itemImage.setImageResource(R.drawable.item)
        }

        holder.card_view_item.setOnClickListener{
            val bundle = Bundle()
            bundle.putString("kode", kode_barang.toString())
            bundle.putString("userid", uid.toString())

            val hal_layout_detail = DetailProduct()
            hal_layout_detail.arguments = bundle
            val transaction = fragmentManager.beginTransaction()
            transaction.replace(R.id.frame_layout, hal_layout_detail)
            transaction.commit()
        }

    }

    override fun getItemCount(): Int {
        return dataset.size
    }

    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        var itemImage : ImageView
        var itemName : TextView
        var itemPrice : TextView
        lateinit var card_view_item : CardView

        init {
            itemImage = itemView.findViewById(R.id.item_display_picture)
            itemName = itemView.findViewById(R.id.item_display_name)
            itemPrice= itemView.findViewById(R.id.item_price)
            card_view_item = itemView.findViewById(R.id.card_view_item)
        }
    }
}