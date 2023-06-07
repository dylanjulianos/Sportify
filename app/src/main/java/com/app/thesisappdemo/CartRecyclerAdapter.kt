package com.app.thesisappdemo

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class CartRecyclerAdapter (private val dataset: List<DocumentSnapshot>, private val fragmentManager: FragmentManager) : RecyclerView.Adapter<CartRecyclerAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartRecyclerAdapter.ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.card_layout_cart, parent, false)
        var checkoutButton = v.findViewById(R.id.checkoutButton) as Button

        val firestore = Firebase.firestore
        val itemCollectionRef = Firebase.firestore.collection("Transaction")

        checkoutButton.setOnClickListener {

        }

        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val documentSnapshot = dataset[position]

        holder.customerNameCard.text = documentSnapshot.get("CustomerName").toString()
        holder.itemNameCard.text = documentSnapshot.get("ItemName").toString()
        holder.quantityCard.text = documentSnapshot.get("Quantity").toString() + " pc(s)"
        holder.rentDateCard.text = documentSnapshot.get("RentDate").toString()
        holder.rentDurationCard.text = documentSnapshot.get("RentDuration").toString() + " day(s)"
        holder.paymentCard.text = documentSnapshot.get("Payment").toString()
        holder.pickupPointCard.text = documentSnapshot.get("PickupPoint").toString()
        holder.totalPriceCard.text = "Rp" + documentSnapshot.get("TotalPrice").toString()
    }

    override fun getItemCount(): Int {
        return dataset.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var itemImageCard: ImageView
        var customerNameCard: TextView
        var itemNameCard: TextView
        var quantityCard: TextView
        var rentDateCard: TextView
        var rentDurationCard: TextView
        var totalPriceCard: TextView
        var paymentCard: TextView
        var pickupPointCard: TextView
//        var checkoutButton: Button

        init {
            itemImageCard = itemView.findViewById(R.id.itemImageCard)
            customerNameCard = itemView.findViewById(R.id.customerNameCard)
            itemNameCard = itemView.findViewById(R.id.itemNameCard)
            quantityCard = itemView.findViewById(R.id.quantityCard)
            rentDateCard = itemView.findViewById(R.id.rentDateCard)
            rentDurationCard = itemView.findViewById(R.id.rentDurationCard)
            totalPriceCard = itemView.findViewById(R.id.itemPriceCard)
            paymentCard = itemView.findViewById(R.id.paymentCard)
            pickupPointCard = itemView.findViewById(R.id.pickupPointCard)
//            checkoutButton = itemView.findViewById(R.id.checkoutButton)
        }
    }
}
