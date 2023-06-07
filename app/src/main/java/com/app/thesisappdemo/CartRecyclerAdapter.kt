package com.app.thesisappdemo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import org.w3c.dom.Text

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class CartRecyclerAdapter (private val dataset: List<DocumentSnapshot>, private val fragmentManager: FragmentManager) : RecyclerView.Adapter<CartRecyclerAdapter.ViewHolder>() {

    private val firestore = Firebase.firestore
    private val firestoreRef = firestore.collection("Transaction")

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartRecyclerAdapter.ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.card_layout_cart, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.checkoutButton.setOnClickListener {
            val documentSnapshot = dataset[position]
//            holder.customerNameCard.text = documentSnapshot.get("CustomerName").toString()
//            holder.itemNameCard.text = documentSnapshot.get("ItemName").toString()
//            holder.quantityCard.text = documentSnapshot.get("Quantity").toString() + " pc(s)"
//            holder.rentDateCard.text = documentSnapshot.get("RentDate").toString()
//            holder.rentDurationCard.text = documentSnapshot.get("RentDuration").toString() + " day(s)"
//            holder.totalPriceCard.text = "Rp" + documentSnapshot.get("TotalPrice").toString()
//            holder.paymentCard.text = documentSnapshot.get("Payment").toString()
//            holder.phoneNumberCard.text = documentSnapshot.get("PhoneNumber").toString()
            val namaCustomer = documentSnapshot.getString("CustomerName")
        }
    }

    override fun getItemCount(): Int {
        return dataset.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var customerNameCard: TextView
        var itemNameCard: TextView
        var quantityCard: TextView
        var rentDateCard: TextView
        var rentDurationCard: TextView
        var totalPriceCard: TextView
        var paymentCard: TextView
        var phoneNumberCard: TextView
        var checkoutButton: Button

        init {
            customerNameCard = itemView.findViewById(R.id.customerNameCard)
            itemNameCard = itemView.findViewById(R.id.itemNameCard)
            quantityCard = itemView.findViewById(R.id.quantityCard)
            rentDateCard = itemView.findViewById(R.id.rentDateCard)
            rentDurationCard = itemView.findViewById(R.id.rentDurationCard)
            totalPriceCard = itemView.findViewById(R.id.itemPriceCard)
            paymentCard = itemView.findViewById(R.id.paymentCard)
            phoneNumberCard = itemView.findViewById(R.id.phoneNumberCard)
            checkoutButton = itemView.findViewById(R.id.checkoutButtonCard)
        }
    }
}