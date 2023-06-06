package com.app.thesisappdemo

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore

class TransactionFragment : Fragment() {

    private val firestore = FirebaseFirestore.getInstance()
    private var layoutManager: RecyclerView.LayoutManager? = null
    private var adapter: RecyclerView.Adapter<TransactionRecyclerAdapter.ViewHolder>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        layoutManager = LinearLayoutManager(getActivity())
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val v = inflater.inflate(R.layout.fragment_transaction, container, false)
        val transaction_List = v.findViewById(R.id.RecylerViewTransaction) as RecyclerView
        transaction_List.layoutManager = layoutManager

        firestore.collection("Transaction").get().addOnSuccessListener { result ->
            val dataset = result.documents
            val adapter = TransactionRecyclerAdapter(dataset, parentFragmentManager)
            transaction_List.adapter = adapter
        }
            .addOnFailureListener { exception ->
                Log.e(TAG, "Error", exception)
            }
        return v
    }
}