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

        val kode = bundle?.getString("kode")
        val collection = "Items"
        val firestoreref = firestore.collection(collection).document(kode.toString())

        val picture = v.findViewById(R.id.imagevieworder) as ImageView
        val item_name = v.findViewById(R.id.productnameorder) as TextView
        val price = v.findViewById(R.id.totalprice) as TextInputEditText
        val save_button = v.findViewById(R.id.buttonorder) as Button
        val pickup_point = "Binus @Malang, Jl. Araya Mansion No.8 - 22"
        val payment = "Cash"

        checkoutButton.setOnClickListener {
            val kode_transaksi = "TC" + transaction_id
            val item_name_input = item_name.text.toString()
            val customer_name_input = customer_name.text.toString()

            val item_price_input = price.text.toString()
            val pickup_point_input = pickup_point
            val payment_input = payment

            val phone_number_input = phone_number_new.text.toString()
            val rental_duration_input = rental_duration_new.text.toString().toFloat()
            val rental_date_input = rental_date.text.toString()
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
