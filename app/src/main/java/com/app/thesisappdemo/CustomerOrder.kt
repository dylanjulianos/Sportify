package com.app.thesisappdemo

import android.app.DatePickerDialog
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.text.method.DigitsKeyListener
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
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
    private lateinit var phone_number : TextInputEditText
    private lateinit var rental_duration : TextInputEditText


    private val firestore = Firebase.firestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    private val itemCollectionRef = Firebase.firestore.collection("Cart")
    var cart_id = 1

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val v = inflater.inflate(R.layout.fragment_customer_order, container, false)
        val bundle = arguments

        val kode = bundle?.getString("kode")
        val uid = bundle?.getString("userid")
        val useridview = v.findViewById<TextView>(R.id.userid)
        useridview.text = uid

        val collection = "Items"
        val firestoreref = firestore.collection(collection).document(kode.toString())

        val imageurl = v.findViewById<TextView>(R.id.urlimages)
        val picture = v.findViewById(R.id.imagevieworder) as ImageView
        val item_name = v.findViewById(R.id.productnameorder) as TextView
        val price = v.findViewById(R.id.totalprice) as TextInputEditText
        val save_button = v.findViewById(R.id.buttonorder) as Button
        val pickup_point = "Binus @Malang, Jl. Araya Mansion No.8 - 22"
        val payment = "Cash"

        val button = v.findViewById(R.id.rentaldate) as TextInputEditText

        phone_number = v.findViewById(R.id.customerphone)
        val digits = "0123456789"
        val keyListener = DigitsKeyListener.getInstance(digits)
        phone_number.keyListener = keyListener

        rental_duration = v.findViewById(R.id.rentalduration)
        rental_duration.keyListener = keyListener

        val phone_number_new = v.findViewById(R.id.customerphone) as TextInputEditText
        val customer_name = v.findViewById(R.id.customername) as TextInputEditText
        val rental_duration_new = v.findViewById(R.id.rentalduration) as TextInputEditText
//        val rental_date = v.findViewById(R.id.rentaldate) as TextInputEditText

        firestoreref.get().addOnSuccessListener { document ->
            Picasso.get().load(document.data?.get("image_url") as? String).into(picture)
            item_name.setText(document.data?.get("item_name") as? String)
            price.setText("Rp" + document.data?.get("item_price").toString() as? String+ "-/day")
            imageurl.setText(document.data?.get("image_url") as? String)

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
            val userid = useridview.text.toString()
            val cartid = "CI" + cart_id
            val image_url = imageurl.text.toString()
            val item_name_input = item_name.text.toString()
            val customer_name_input = customer_name.text.toString()

            val item_price_input = price.text.toString()
            val pickup_point_input = pickup_point
            val payment_input = payment

            val phone_number_input = phone_number_new.text.toString()
            val rental_duration_input = rental_duration_new.text.toString().toFloat()
            val rental_date_input = button.text.toString()

            val item = TransactionItems(
                userid
                , cartid
                , item_name_input
                , customer_name_input
                , item_price_input
                , pickup_point_input
                , payment_input
                , phone_number_input
                , rental_duration_input
                , rental_date_input
                , image_url
            )
            saveItem(item)
            customer_name.setText("")
            phone_number_new.setText("")
            rental_duration_new.setText("")
            button.setText("")
            imageurl.setText("")

            if (phone_number_input.isEmpty()) {
                phone_number_new.error = "Please enter your phone number"
            } else {

            }

            showToast("Data succesfully moved to Cart! ...")
        }

        return v
    }

    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
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
                    "UserId" to items.UserId,
                    "CartId" to documentId,
                    "ItemName" to items.ItemName,
                    "TotalPrice" to items.TotalPrice,
                    "CustomerName" to items.CustomerName,
                    "PickupPoint" to items.PickupPoint,
                    "Payment" to items.Payment,
                    "PhoneNumber" to items.PhoneNumber,
                    "RentDuration" to items.RentDuration,
                    "RentDate" to items.RentDate,
                    "ImageUrl" to items.ImageUrl
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