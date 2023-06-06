package com.app.thesisappdemo

import android.app.DatePickerDialog
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.FragmentTransaction
import com.app.thesisappdemo.databinding.FragmentCustomerOrderBinding
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_customer_order.customername
import kotlinx.android.synthetic.main.fragment_customer_order.customerpickup
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [CustomerOrder.newInstance] factory method to
 * create an instance of this fragment.
 */
class CustomerOrder : Fragment(){
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

//    private var _binding : FragmentCustomerOrderBinding? = null
//    private val binding get() = _binding!!

    private lateinit var binding : FragmentCustomerOrderBinding
    private lateinit var tvDatePicker : TextInputEditText
    private lateinit var buttonDatePicker : TextInputLayout

    private val firestore = Firebase.firestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    private val itemCollectionRef = Firebase.firestore.collection("Transaction Hanzel")
    var transaction_id = 1

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val v = inflater.inflate(R.layout.fragment_customer_order, container, false)
        val bundle = arguments

        val kode = bundle?.getString("kode")
        val collection = "Items"
        val firestoreref = firestore.collection(collection).document(kode.toString())

        val picture = v.findViewById(R.id.imagevieworder) as ImageView
        val item_name = v.findViewById(R.id.productnameorder) as TextView
        val price = v.findViewById(R.id.totalprice) as TextInputEditText

        val button = v.findViewById(R.id.rentaldate) as TextInputEditText
        val save_button = v.findViewById(R.id.buttonorder) as Button

//        val pickup_point = v.findViewById(R.id.customerpickup) as TextInputEditText
        val pickup_point = customerpickup.setText("Binus @Malang, Jl. Araya Mansion No.8 - 22")

        val payment = v.findViewById(R.id.payment) as TextInputEditText
        val phone_number = v.findViewById(R.id.customerphone) as TextInputEditText
        val customer_name = v.findViewById(R.id.customername) as TextInputEditText

        firestoreref.get().addOnSuccessListener { document ->
            Picasso.get().load(document.data?.get("image_url") as? String).into(picture)
            item_name.setText(document.data?.get("item_name") as? String)
            price.setText("Rp" + document.data?.get("item_price").toString().toFloat() as? Float + "-/day")

        }.addOnFailureListener {
                e-> println("Error showing document: ${e.message}")
        }

        binding = FragmentCustomerOrderBinding.inflate(layoutInflater)

        tvDatePicker = v.findViewById(R.id.rentaldate)

        val myCalendar = Calendar.getInstance()
        val datePicker = DatePickerDialog.OnDateSetListener{
                view,year,month,dayOfMonth ->
            myCalendar.set(Calendar.YEAR, year)
            myCalendar.set(Calendar.MONTH, month)
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
            updateLable(myCalendar)
        }

        button.setOnClickListener{
            DatePickerDialog(requireActivity(), datePicker, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                myCalendar.get(Calendar.DAY_OF_MONTH)).show()
        }

        save_button.setOnClickListener{
            val kode_transaksi = "TC" + transaction_id
            val item_name_input = item_name.text.toString()
            val customer_name_input = customer_name.text.toString()
//            val item_price_input = price.text.toString().toFloat()
            val pickup_point_input = pickup_point.toString()

            val item = TransactionItems(kode_transaksi, item_name_input, customer_name_input, pickup_point_input)

            saveItem(item)
//            item_name.setText("")
//            price.setText("")
        }

        return v
    }

    private fun updateLable(myCalendar: Calendar) {
        val myFormat = "dd/MM/yyyy"
        val sdf = SimpleDateFormat(myFormat, Locale.UK)
        tvDatePicker.setText(sdf.format(myCalendar.time))
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    private fun saveItem(items: TransactionItems) = CoroutineScope(Dispatchers.IO).launch {
        try{
            itemCollectionRef.add(items).addOnSuccessListener { documentReference ->
                val documentId = documentReference.id

                val updatedDocument = hashMapOf(
                    "item_name" to items.item_name,
//                    "item_price" to items.price,
                    "transaction_id" to documentId,
                    "customer_name" to items.customer_name,
                    "pickup_point" to items.pickup_point
                )
                documentReference.set(updatedDocument).addOnSuccessListener {
                    println("Succesfully Added Item")
                }.addOnFailureListener { e ->
                    // Handle any errors that occurred during the update
                    println("Error updating document: ${e.message}")
                }
            }.addOnFailureListener { e ->
                // Handle any errors that occurred during the addition of the document
                println("Error adding document: ${e.message}")
            }
        } catch (e: Exception){
            withContext(Dispatchers.Main){
                Toast.makeText(activity, e.message, Toast.LENGTH_LONG).show()
            }

            }
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment CustomerOrder.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            CustomerOrder().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}