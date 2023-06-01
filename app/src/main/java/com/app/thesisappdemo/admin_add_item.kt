package com.app.thesisappdemo

import android.content.ClipData.Item
import android.Manifest
import android.app.Activity.RESULT_OK
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.ImageDecoder
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Spinner
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toBitmap
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.ktx.storage
import kotlinx.android.synthetic.main.fragment_admin_add_item.item_photo
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

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

    private lateinit var pick_image_button: ImageView
    private val itemCollectionRef = Firebase.firestore.collection("Items")
    var item_id = 1
    private lateinit var current_image_url: String

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            openImagePicker()
        } else {

        }
    }

    private val pickImageLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            val imageUri = result.data?.data
            imageUri?.let { uri ->
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                    val source = ImageDecoder.createSource(requireContext().contentResolver, uri)
                    val bitmap = ImageDecoder.decodeBitmap(source)
                    pick_image_button.setImageBitmap(bitmap)
                    val storageRef = FirebaseStorage.getInstance().reference
                    val imageRef = storageRef.child("images/${System.currentTimeMillis()}.jpg")
                    val uploadTask = imageRef.putFile(uri)
                    uploadTask.addOnSuccessListener { taskSnapshot ->
                        // Image upload successful
                        imageRef.downloadUrl.addOnSuccessListener { uri ->
                            current_image_url = uri.toString()
                        }
                        Toast.makeText(requireContext(), "Image uploaded successfully", Toast.LENGTH_SHORT).show()
                    }.addOnFailureListener { exception ->
                        // Handle image upload failure
                        Toast.makeText(requireContext(), "Failed to upload image", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    val bitmap = MediaStore.Images.Media.getBitmap(requireContext().contentResolver, uri)
                    pick_image_button.setImageBitmap(bitmap)
                    val storageRef = FirebaseStorage.getInstance().reference
                    val imageRef = storageRef.child("images/${System.currentTimeMillis()}.jpg")
                    val uploadTask = imageRef.putFile(uri)
                    uploadTask.addOnSuccessListener { taskSnapshot ->
                        // Image upload successful
                        // imageref.downloadurl digunakan agar url yang digunakan merupakan url permanen gambar dan bukan temporary
                        // sehingga gambar akan tetap muncul walaupun aplikasi di close dan buka
                        imageRef.downloadUrl.addOnSuccessListener { uri ->
                            current_image_url = uri.toString()
                        }
                        Toast.makeText(requireContext(), "Image uploaded successfully", Toast.LENGTH_SHORT).show()
                    }.addOnFailureListener { exception ->
                        // Handle image upload failure
                        Toast.makeText(requireContext(), "Failed to upload image", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        val v = inflater.inflate(R.layout.fragment_admin_add_item, container, false)
        pick_image_button = v.findViewById(R.id.item_photo)
        val save_item_button = v.findViewById(R.id.save_item_button) as Button
        val item_name = v.findViewById(R.id.item_name) as EditText
        val item_price = v.findViewById(R.id.item_price) as EditText
        val item_sport_type = v.findViewById(R.id.item_sport_type) as EditText
        val item_bio = v.findViewById(R.id.item_description) as EditText
        val db = FirebaseFirestore.getInstance()


        pick_image_button.setOnClickListener{
            val permission = Manifest.permission.READ_EXTERNAL_STORAGE
            if (ContextCompat.checkSelfPermission(requireContext(), permission) == PackageManager.PERMISSION_GRANTED
            ) {

                openImagePicker()

            } else {

                requestPermissionLauncher.launch(permission)

            }
        }

        save_item_button.setOnClickListener{
            val kode_item = "IT" + item_id
            val item_name_input = item_name.text.toString()
            val item_price_input = item_price.text.toString().toFloat()
            val item_sport_type_input = item_sport_type.text.toString()
            val item_bio_input = item_bio.text.toString()
            val item = Items(current_image_url, kode_item, item_name_input, item_price_input, item_sport_type_input, item_bio_input)
            saveItem(item)
            item_name.setText("")
            item_price.setText("")
            item_sport_type.setText("")
            item_bio.setText("")
            pick_image_button.setImageResource(R.drawable.baseline_camera_alt_24)
        }

        return v

    }

    private fun openImagePicker() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        pickImageLauncher.launch(intent)
    }

    private fun saveItem (items: Items) = CoroutineScope(Dispatchers.IO).launch {
        try {
            itemCollectionRef.add(items).addOnSuccessListener { documentReference ->
                val documentId = documentReference.id

                val updatedDocument = hashMapOf(
                    "image_url" to items.image_url,
                    "item_name" to items.item_name,
                    "item_price" to items.price,
                    "item_bio" to items.item_bio,
                    "sport_category" to items.sport_category,
                    "item_code" to documentId
                )

                // Update the document with the new data
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