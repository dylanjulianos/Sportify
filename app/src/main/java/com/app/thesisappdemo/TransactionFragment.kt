package com.app.thesisappdemo

import android.content.ContentValues.TAG
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.app.thesisappdemo.databinding.FragmentTransactionBinding
import com.google.firebase.firestore.FirebaseFirestore

class TransactionFragment : Fragment() {

    private val firestore = FirebaseFirestore.getInstance()
    private var layoutManager: RecyclerView.LayoutManager? = null
    private var adapter: RecyclerView.Adapter<TransactionRecyclerAdapter.ViewHolder>? = null
    private lateinit var binding: FragmentTransactionBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        layoutManager = LinearLayoutManager(getActivity())
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val collectionRef = firestore.collection("Transaction")
        val uid = arguments?.getString("userid")

        val v = inflater.inflate(R.layout.fragment_transaction, container, false)
        val transaction_List = v.findViewById(R.id.RecylerViewTransaction) as RecyclerView
        transaction_List.layoutManager = layoutManager

        val fieldName = "UserId"
        val desiredValue = uid.toString()
        val query = collectionRef.whereEqualTo(fieldName,desiredValue)

        query.get().addOnSuccessListener { result ->
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