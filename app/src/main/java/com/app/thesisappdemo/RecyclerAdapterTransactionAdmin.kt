package com.app.thesisappdemo

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.DocumentSnapshot

class RecyclerAdapterTransactionAdmin(private val dataset: List<DocumentSnapshot>): RecyclerView.Adapter<RecyclerAdapterTransactionAdmin.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.table_layout_transaction_admin, parent, false)

        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val documentSnapshot = dataset[position]
        val namacustomer = documentSnapshot.getString("CustomerName")
        val namabarang = documentSnapshot.getString("ItemName")
        val jumlahbarang = documentSnapshot.getLong("Quantity")
        val durasisewa = documentSnapshot.getLong("RentDuration")
        val tanggalsewa = documentSnapshot.getString("RentDate")
        val totalharga = documentSnapshot.getLong("TotalPrice")

        holder.CustomerName.text = namacustomer
        holder.ItemName.text = namabarang
        holder.Quantity.text = jumlahbarang.toString()
        holder.RentDuration.text = durasisewa.toString() + "Day(s)"
        holder.RentDate.text = tanggalsewa
        holder.TotalPrice.text = "Rp. " + totalharga + "-"
    }

    override fun getItemCount(): Int {
        return dataset.size
    }

    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        var CustomerName: TextView
        var ItemName: TextView
        var Quantity: TextView
        var RentDuration: TextView
        var RentDate: TextView
        var TotalPrice: TextView

        init {
            CustomerName = itemView.findViewById(R.id.transaction_cust_name)
            ItemName = itemView.findViewById(R.id.transaction_item_name)
            Quantity = itemView.findViewById(R.id.transaction_quantity)
            RentDuration = itemView.findViewById(R.id.transaction_rent_duration)
            RentDate = itemView.findViewById(R.id.transaction_rent_date)
            TotalPrice = itemView.findViewById(R.id.transaction_total_price)
        }
    }

}