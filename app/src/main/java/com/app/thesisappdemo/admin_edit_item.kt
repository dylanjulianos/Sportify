package com.app.thesisappdemo

import android.Manifest
import android.app.Activity
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
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentTransaction
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.squareup.picasso.Picasso
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [admin_edit_item.newInstance] factory method to
 * create an instance of this fragment.
 */
class admin_edit_item() : Fragment() {
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
    private lateinit var update_picture_input: ImageView
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
        if (result.resultCode == Activity.RESULT_OK) {
            val imageUri = result.data?.data
            imageUri?.let { uri ->
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                    val source = ImageDecoder.createSource(requireContext().contentResolver, uri)
                    val bitmap = ImageDecoder.decodeBitmap(source)
                    update_picture_input.setImageBitmap(bitmap)
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
                    update_picture_input.setImageBitmap(bitmap)
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
        val bundle = arguments
        val kode = bundle?.getString("kode")
        val collection = "Items"
        val firestoreref = firestore.collection(collection).document(kode.toString())
        val v = inflater.inflate(R.layout.fragment_admin_edit_item, container, false)
        val old_picture = v.findViewById(R.id.old_item_photo) as ImageView
        val old_name = v.findViewById(R.id.old_item_name) as EditText
        val old_price = v.findViewById(R.id.old_item_price) as EditText
        val old_sport_type = v.findViewById(R.id.old_item_sport_type) as EditText
        val old_description = v.findViewById(R.id.old_item_description) as EditText
        update_picture_input = v.findViewById(R.id.updated_item_photo)
        val update_name_input = v.findViewById(R.id.updated_item_name) as EditText
        val update_price_input = v.findViewById(R.id.updated_item_price) as EditText
        val update_sport_type_input = v.findViewById(R.id.updated_item_sport_type) as EditText
        val update_description_input = v.findViewById(R.id.updated_item_description) as EditText
        val update_button = v.findViewById(R.id.update_item_button) as Button

        firestoreref.get().addOnSuccessListener { document ->
            Picasso.get().load(document.data?.get("image_url")as? String).into(old_picture)
            old_name.setText(document.data?.get("item_name") as? String)
            old_price.setText(document.data?.get("item_price").toString() as? String)
            old_sport_type.setText(document.data?.get("sport_category") as? String)
            old_description.setText(document.data?.get("item_bio") as? String)
        }.addOnFailureListener { e ->
            // Handle any errors that occurred during the addition of the document
            println("Error showing document: ${e.message}")
        }

        update_picture_input.setOnClickListener{
            val permission = Manifest.permission.READ_EXTERNAL_STORAGE
            if (ContextCompat.checkSelfPermission(requireContext(), permission) == PackageManager.PERMISSION_GRANTED
            ) {

                openImagePicker()

            } else {

                requestPermissionLauncher.launch(permission)

            }
        }

        update_button.setOnClickListener{
            val updated_name = update_name_input.text.toString()
            val updated_price = update_price_input.text.toString().toFloat()
            val updated_sport_type = update_sport_type_input.text.toString()
            val updated_description = update_description_input.text.toString()
            firestoreref.update("image_url", current_image_url)
            firestoreref.update("item_name", updated_name)
            firestoreref.update("item_bio", updated_description)
            firestoreref.update("item_code", kode.toString())
            firestoreref.update("item_price", updated_price)
            firestoreref.update("sport_category", updated_sport_type)
            update_name_input.setText("")
            update_price_input.setText("")
            update_sport_type_input.setText("")
            update_description_input.setText("")
            update_picture_input.setImageResource(R.drawable.baseline_camera_alt_24)
            Toast.makeText(activity, "Data Updated Succesfully", Toast.LENGTH_SHORT).show()
            val halaman_utama = admin_home()
            val transaction: FragmentTransaction = parentFragmentManager.beginTransaction()
            transaction.replace(R.id.frame_layout, halaman_utama)
            transaction.commit()
        }

        return v
    }

    private fun openImagePicker() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        pickImageLauncher.launch(intent)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment admin_edit_item.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            admin_edit_item().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}