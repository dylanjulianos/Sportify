package com.app.thesisappdemo

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat.startActivity
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.squareup.picasso.Picasso
import org.w3c.dom.Text

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class CartRecyclerAdapter (private val bundle: Bundle, private val dataset: List<DocumentSnapshot>, private val fragmentManager: FragmentManager) : RecyclerView.Adapter<CartRecyclerAdapter.ViewHolder>() {

    private val firestore = Firebase.firestore
    private val firestoreRef = firestore.collection("Transaction")
    private val firestoreSource = firestore.collection("Cart").document()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartRecyclerAdapter.ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.card_layout_cart, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val documentSnapshot = dataset[position]
        val uid = bundle?.getString("userid")
        val uid1 = documentSnapshot?.getString("UserId")

        val namaCustomer = documentSnapshot?.getString("CustomerName")
        val jemput = documentSnapshot?.getString("PickupPoint")
        val namaitem = documentSnapshot?.getString("ItemName")
        val tanggalsewa = documentSnapshot?.getString("RentDate")
        val durasisewa = documentSnapshot?.getLong("RentDuration")
        val totalharga = documentSnapshot?.getString("TotalPrice")
        val pembayaran = documentSnapshot?.getString("Payment")
        val nomor = documentSnapshot?.getString("PhoneNumber")
        val imageurl = documentSnapshot?.getString("ImageUrl")
        val cartcode = documentSnapshot?.getString("CartId")

        if (imageurl != null) { Picasso.get().load(imageurl).into(holder.gambaritem)
        } else {
            // If imageUrl is null or empty, you can set a placeholder image or handle it differently
            holder.gambaritem.setImageResource(R.drawable.item)
        }
        holder.uid.text = uid //dari bundle
        holder.uid1.text = uid1 //dari firestore

        holder.customerNameCard.text = namaCustomer
        holder.itemNameCard.text = namaitem
        holder.pickupCard.text = jemput
        holder.rentDateCard.text = tanggalsewa
        holder.rentDurationCard.text = durasisewa.toString() + " day(s)"
        holder.totalPriceCard.text = totalharga
        holder.paymentCard.text = pembayaran
        holder.phoneNumberCard.text = nomor

        holder.checkoutButton.setOnClickListener { view ->
            val tambahTransaction = HashMap<String, Any>()
            tambahTransaction["UserId"] = uid1.toString()
            tambahTransaction["ImageUrl"] = imageurl.toString()
            tambahTransaction["CustomerName"] = namaCustomer.toString()
            tambahTransaction["ItemName"] = namaitem.toString()
            tambahTransaction["PickupPoint"] = jemput.toString()
            tambahTransaction["RentDate"] = tanggalsewa.toString()
            tambahTransaction["RentDuration"] = durasisewa.toString().toFloat()
            tambahTransaction["TotalPrice"] = totalharga.toString()
            tambahTransaction["Payment"] = pembayaran.toString()
            tambahTransaction["PhoneNumber"] = nomor.toString()
            tambahTransaction["TransactionID"] = "a"

            firestoreRef.add(tambahTransaction).addOnSuccessListener { documentReference ->
                val itemId = documentReference.id

                val updatedDocument = hashMapOf(
                    "UserId" to uid1.toString(),
                    "ImageUrl" to imageurl.toString(),
                    "CustomerName" to namaCustomer.toString(),
                    "ItemName" to namaitem.toString(),
                    "PickupPoint" to jemput.toString(),
                    "RentDate" to tanggalsewa.toString(),
                    "RentDuration" to durasisewa.toString().toFloat(),
                    "TotalPrice" to totalharga.toString(),
                    "payment" to pembayaran.toString(),
                    "PhoneNumber" to nomor.toString(),
                    "TransactionID" to itemId
                )

                documentReference.set(updatedDocument).addOnSuccessListener {
                    println("Succesfully Checked Out Item")
                }.addOnFailureListener { e ->
                    // Handle any errors that occurred during the update
                    println("Error Checking Out Item: ${e.message}")
                }
            }

            val bundle = Bundle()
            bundle.putString("userid", uid.toString())

            val docRef = firestore.collection("Cart").document(cartcode.toString())

            docRef.delete()
                .addOnSuccessListener {
                    // Document successfully deleted
                    // You can perform any additional operations here
                    val bundle = Bundle()
                    bundle.putString("userid", uid.toString())
                    val intent = Intent(view.context, TransactionSucceedActivity::class.java)
                    intent.putExtras(bundle)
                    view.context.startActivity(intent)

//                    val hal_layout_detail = TransactionFragment()
//                    hal_layout_detail.arguments = bundle
//                    val transaction = fragmentManager.beginTransaction()
//                    transaction.replace(R.id.frame_layout, hal_layout_detail)
//                    transaction.commit()
                }
                .addOnFailureListener { e ->
                    // An error occurred while deleting the document
                    // Handle the error appropriately
                }
//            firestore.collection("Cart").document(cartcode.toString()).delete()
//
//            val intent = Intent(view.context, TransactionSucceedActivity::class.java)
//            intent.putExtras(bundle)
//            view.context.startActivity(intent)

        }
    }


    override fun getItemCount(): Int {
        return dataset.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var uid : TextView
        var uid1 : TextView
        var gambaritem: ImageView
        var customerNameCard: TextView
        var itemNameCard: TextView
        var pickupCard: TextView
        var rentDateCard: TextView
        var rentDurationCard: TextView
        var totalPriceCard: TextView
        var paymentCard: TextView
        var phoneNumberCard: TextView
        var checkoutButton: Button

        init {
            uid = itemView.findViewById(R.id.uid)
            uid1 = itemView.findViewById(R.id.uid1)
            gambaritem = itemView.findViewById(R.id.itemImageCard)
            customerNameCard = itemView.findViewById(R.id.customerNameCard)
            itemNameCard = itemView.findViewById(R.id.itemNameCard)
            pickupCard = itemView.findViewById(R.id.pickupCard)
            rentDateCard = itemView.findViewById(R.id.rentDateCard)
            rentDurationCard = itemView.findViewById(R.id.rentDurationCard)
            totalPriceCard = itemView.findViewById(R.id.itemPriceCard)
            paymentCard = itemView.findViewById(R.id.paymentCard)
            phoneNumberCard = itemView.findViewById(R.id.phoneNumberCard)
            checkoutButton = itemView.findViewById(R.id.checkoutButtonCard)
        }
    }
}