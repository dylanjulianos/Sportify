package com.app.thesisappdemo

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.ImageDecoder
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.app.thesisappdemo.databinding.FragmentDetailProductBinding
import com.app.thesisappdemo.databinding.MainCustomerBinding
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.squareup.picasso.Picasso
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.NumberFormat

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [DetailProduct.newInstance] factory method to
 * create an instance of this fragment.
 */
class DetailProduct : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    private val firestore = Firebase.firestore

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val v = inflater.inflate(R.layout.fragment_detail_product, container, false)
        val bundle = arguments

        val kode = bundle?.getString("kode")
        val collection = "Items"
        val firestoreref = firestore.collection(collection).document(kode.toString())

        val picture = v.findViewById(R.id.imageView) as ImageView
        val name = v.findViewById(R.id.productname) as TextView
        val price = v.findViewById(R.id.price) as TextView
        val description = v.findViewById(R.id.description) as TextView
        val rentnow = v.findViewById(R.id.buttontransaction) as Button
        val backimage = v.findViewById(R.id.backimage) as ImageView

        firestoreref.get().addOnSuccessListener { document ->
            Picasso.get().load(document.data?.get("image_url") as? String).into(picture)
            name.setText(document.data?.get("item_name") as? String)
            price.setText("Rp" + document.data?.get("item_price").toString() as? String + "-/day")
            description.setText(document.data?.get("item_bio") as? String)
        }.addOnFailureListener {
            e-> println("Error showing document: ${e.message}")
        }


        rentnow.setOnClickListener{
            val bundle = Bundle()
            bundle.putString("kode", kode.toString())
            val hal_layout_detail = CustomerOrder()
            hal_layout_detail.arguments = bundle
            val transaction: FragmentTransaction = parentFragmentManager.beginTransaction()
            transaction.replace(R.id.frame_layout, hal_layout_detail)
            transaction.commit()

//            val customer = CustomerOrder()
//            val transaction: FragmentTransaction = parentFragmentManager.beginTransaction()
//            transaction.replace(R.id.frame_layout, customer)
//            transaction.commit()
//            navigateToActivity()

        }

        backimage.setOnClickListener{
            val home = HomeFragment()
            val transaction: FragmentTransaction = parentFragmentManager.beginTransaction()
            transaction.replace(R.id.frame_layout, home)
            transaction.commit()
        }

        return v
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    fun navigateToActivity() {

        val intent = Intent(activity, CustomerOrderActivity::class.java)
        startActivity(intent)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment DetailProduct.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            DetailProduct().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}