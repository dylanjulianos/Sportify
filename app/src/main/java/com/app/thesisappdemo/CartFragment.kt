package com.app.thesisappdemo

import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.app.thesisappdemo.databinding.FragmentCartBinding
import com.google.firebase.firestore.FirebaseFirestore

class CartFragment : Fragment() {

    private val firestore = FirebaseFirestore.getInstance()
    private var layoutManager: RecyclerView.LayoutManager? = null
    private var adapter: RecyclerView.Adapter<CartRecyclerAdapter.ViewHolder>? =  null
    private lateinit var binding: FragmentCartBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        layoutManager = LinearLayoutManager(getActivity())
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val v = inflater.inflate(R.layout.fragment_cart, container, false)
        val cart_List = v.findViewById(R.id.RecylerViewCart) as RecyclerView
        cart_List.layoutManager = layoutManager

        firestore.collection("Cart").get().addOnSuccessListener { result ->
            val dataset = result.documents
            val adapter = CartRecyclerAdapter(dataset, parentFragmentManager)
            cart_List.adapter = adapter
        }
            .addOnFailureListener { exception ->
                Log.e(TAG, "Error", exception)
            }

        return v
    }
}