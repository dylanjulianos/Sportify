package com.app.thesisappdemo

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Transaction
import com.google.firebase.firestore.auth.User
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

        holder.customerNameCard.text = documentSnapshot.get("CustomerName").toString()
        holder.itemNameCard.text = documentSnapshot.get("ItemName").toString()
        holder.quantityCard.text = documentSnapshot.get("Quantity").toString() + " pc(s)"
        holder.rentDateCard.text = documentSnapshot.get("RentDate").toString()
        holder.rentDurationCard.text = documentSnapshot.get("RentDuration").toString() + " day(s)"
        holder.totalPriceCard.text = "Rp" + documentSnapshot.get("TotalPrice").toString()

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
        var quantityCard: TextView
        var rentDateCard: TextView
        var rentDurationCard: TextView
        var totalPriceCard: TextView

        init {
            customerNameCard = itemView.findViewById(R.id.customerNameCard)
            itemNameCard = itemView.findViewById(R.id.itemNameCard)
            quantityCard = itemView.findViewById(R.id.quantityCard)
            rentDateCard = itemView.findViewById(R.id.rentDateCard)
            rentDurationCard = itemView.findViewById(R.id.rentDurationCard)
            totalPriceCard = itemView.findViewById(R.id.itemPriceCard)
        }
    }
}