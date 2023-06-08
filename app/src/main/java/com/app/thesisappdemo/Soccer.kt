package com.app.thesisappdemo

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Recycler
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.firestore.FirebaseFirestore

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"


class Soccer : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private val firestore = FirebaseFirestore.getInstance()
    private var gridLayoutManager: GridLayoutManager? = null

    //    private var layoutManager: RecyclerView.LayoutManager? = null
    private var adapter: RecyclerView.Adapter<MyAdapter.ViewHolder>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        gridLayoutManager = GridLayoutManager(context, 2, LinearLayoutManager.VERTICAL, false)
//        layoutManager = LinearLayoutManager(getActivity())
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val v = inflater.inflate(R.layout.fragment_soccer, container, false)
        val collectionRef = firestore.collection("Items")
        val fieldName = "sport_category"
        val desiredValue = "Soccer"
        val query = collectionRef.whereEqualTo(fieldName,desiredValue)

        val item_list = v.findViewById(R.id.RecyclerView) as RecyclerView
        item_list.layoutManager = gridLayoutManager

        query
            .get().addOnSuccessListener { result ->
                val dataset = result.documents
                val adapter = MyAdapter(dataset, parentFragmentManager)
                item_list.adapter = adapter
            }
            .addOnFailureListener { exception ->
                // Handle any errors
                Log.e(TAG, "Error getting documents: ", exception)
            }

        return v
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment admin_home.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            Soccer().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}