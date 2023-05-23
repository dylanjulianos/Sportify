package com.app.thesisappdemo

import android.content.ClipData.Item
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [admin_add_item.newInstance] factory method to
 * create an instance of this fragment.
 */
class admin_add_item : Fragment() {
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
    private val itemCollectionRef = Firebase.firestore.collection("Items")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        val v = inflater.inflate(R.layout.fragment_admin_add_item, container, false)

        val save_item_button = v.findViewById(R.id.save_item_button) as Button
        val item_name = v.findViewById(R.id.item_name) as EditText
        val item_price = v.findViewById(R.id.item_price) as EditText
        val item_sport_type = v.findViewById(R.id.item_sport_type) as EditText
        val item_bio = v.findViewById(R.id.item_description) as EditText

        save_item_button.setOnClickListener{
            val item_name_input = item_name.text.toString()
            val item_price_input = item_name.text.toString().toFloat()
            val item_sport_type_input = item_name.text.toString()
            val item_bio_input = item_name.text.toString()
            val item = Items(item_name_input, item_price_input, item_sport_type_input, item_bio_input)
            saveItem(item)
        }

        return v

    }

    private fun saveItem (items: Items) = CoroutineScope(Dispatchers.IO).launch {
        try {
            itemCollectionRef.add(items).await()
            withContext(Dispatchers.Main){
                Toast.makeText(activity, "Successfully Added Item", Toast.LENGTH_LONG).show()
            }
        }catch (e: Exception){
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
         * @return A new instance of fragment admin_add_item.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            admin_add_item().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}