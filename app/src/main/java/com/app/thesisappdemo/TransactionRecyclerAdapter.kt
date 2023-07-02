package com.app.thesisappdemo

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Transaction
import com.google.firebase.firestore.auth.User
import com.squareup.picasso.Picasso
import org.w3c.dom.Text
import java.util.Date

class TransactionRecyclerAdapter(private val dataset: List<DocumentSnapshot>, private val fragmentManager: FragmentManager) : RecyclerView.Adapter<TransactionRecyclerAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TransactionRecyclerAdapter.ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.card_layout_transaction, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val documentSnapshot = dataset[position]
        val db = FirebaseFirestore.getInstance()
//        val collectionRef = db.collection("Transaction")
        val imageurl = documentSnapshot.getString("ImageUrl")

        if (imageurl != null) { Picasso.get().load(imageurl).into(holder.gambaritem)
        } else {
            // If imageUrl is null or empty, you can set a placeholder image or handle it differently
            holder.gambaritem.setImageResource(R.drawable.item)
        }
        holder.customerNameCard.text = documentSnapshot.get("CustomerName").toString()
        holder.itemNameCard.text = documentSnapshot.get("ItemName").toString()
        holder.pickupCard.text = documentSnapshot.get("PickupPoint").toString()
        holder.rentDateCard.text = documentSnapshot.get("RentDate").toString()
        holder.rentDurationCard.text = documentSnapshot.get("RentDuration").toString() + " day(s)"
        holder.totalPriceCard.text = documentSnapshot.get("TotalPrice").toString()

//        collectionRef.get()
//            .addOnSuccessListener { querySnapshot ->
//
//                for (document in querySnapshot.documents) {
//
//                    val data = document.data
//                    val namaItem = data!!["itemName"] as String
////                    val nomorTelepon = data["phoneNumber"] as Number
////                    val kuantitas = data["quantity"] as String
////                    val tanggalSewa = data["rentalDate"] as String
//                    val durasiSewa = data["rentalDuration"] as String
//                    val hargaTotal = data["totalPrice"] as Number
//
//                    holder.itemNameCard.text = namaItem
////                    holder.phoneNumberCard.text = nomorTelepon.toString()
////                    holder.quantityCard.text = kuantitas
////                    holder.rentDateCard.text = tanggalSewa
//                    holder.rentDurationCard.text = durasiSewa
//                    holder.totalPriceCard.text = hargaTotal.toString()
//                }
//            }
    }

    override fun getItemCount(): Int {
        return dataset.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var customerNameCard: TextView
        var itemNameCard: TextView
        var pickupCard: TextView
        var rentDateCard: TextView
        var rentDurationCard: TextView
        var totalPriceCard: TextView
        var gambaritem : ImageView

        init {
            gambaritem = itemView.findViewById(R.id.itemImageCard)
            customerNameCard = itemView.findViewById(R.id.customerNameCard)
            itemNameCard = itemView.findViewById(R.id.itemNameCard)
            pickupCard = itemView.findViewById(R.id.pickupCard)
            rentDateCard = itemView.findViewById(R.id.rentDateCard)
            rentDurationCard = itemView.findViewById(R.id.rentDurationCard)
            totalPriceCard = itemView.findViewById(R.id.itemPriceCard)
        }
    }
}