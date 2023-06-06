package com.app.thesisappdemo

import android.app.DatePickerDialog
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.AttributeSet
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.app.thesisappdemo.databinding.ActivityCustomerOrderBinding
import com.app.thesisappdemo.databinding.MainCustomerBinding
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.squareup.picasso.Picasso
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class CustomerOrderActivity : AppCompatActivity() {
    private val firestore = Firebase.firestore
    private lateinit var binding : ActivityCustomerOrderBinding
    private lateinit var tvDatePicker : TextInputEditText
    private lateinit var buttonDatePicker : TextInputLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityCustomerOrderBinding.inflate(layoutInflater)
        setContentView(binding.root)

        tvDatePicker = findViewById(R.id.rentaldate)
        buttonDatePicker = findViewById(R.id.textinput5)

        val myCalendar = Calendar.getInstance()
        val datePicker = DatePickerDialog.OnDateSetListener{
            view,year,month,dayOfMonth ->
            myCalendar.set(Calendar.YEAR, year)
            myCalendar.set(Calendar.MONTH, month)
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
            updateLable(myCalendar)
        }

        binding.rentaldate.setOnClickListener{
            DatePickerDialog(this, datePicker, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
            myCalendar.get(Calendar.DAY_OF_MONTH)).show()
        }

        val product = DetailProduct()
        val bundle = product.arguments

        val kode = bundle?.getString("kode")
        val collection = "Items"
        val firestoreref = firestore.collection(collection).document(kode.toString())

        val picture = findViewById(R.id.imagevieworder) as ImageView
        val name = findViewById(R.id.productnameorder) as TextView
//        val price = findViewById(R.id.price) as TextView
//        val description = findViewById(R.id.description) as TextView
//        val rentnow = findViewById(R.id.buttontransaction) as Button
//        val backimage = findViewById(R.id.backimage) as ImageView

        firestoreref.get().addOnSuccessListener { document ->
            Picasso.get().load(document.data?.get("image_url") as? String).into(picture)
            name.setText(document.data?.get("item_name") as? String)
//            price.setText("Rp" + document.data?.get("item_price").toString() as? String + "-/day")
//            description.setText(document.data?.get("item_bio") as? String)
        }.addOnFailureListener {
                e-> println("Error showing document: ${e.message}")
        }
    }

    private fun updateLable(myCalendar: Calendar) {
        val myFormat = "dd/MM/yyyy"
        val sdf = SimpleDateFormat(myFormat, Locale.UK)
        tvDatePicker.setText(sdf.format(myCalendar.time))
    }
}