package com.app.thesisappdemo

import android.content.Intent
import android.graphics.ImageDecoder
import android.os.Build

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.EventListener
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import java.io.FileNotFoundException


class Profile : AppCompatActivity() {
    private val REQUEST_CODE = 1
    private lateinit var camera: ImageView
    private lateinit var logoutbutton : Button
    val db = FirebaseFirestore.getInstance()
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var showusername: TextView
    private lateinit var showemail: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)
        firebaseAuth = FirebaseAuth.getInstance()

        val bundle = intent.extras
        if (bundle != null) {
            val userid = bundle.getString("userid")
            val name = bundle.getString("name")
            val email = bundle.getString("email")

//        val uid = firebaseAuth.currentUser?.uid
            showusername = findViewById(R.id.showusername)
            showemail = findViewById(R.id.showemail)

            val docRef = db.collection("Users")
                .document(userid.toString())
//            .document(uid!!)
            docRef.get().addOnSuccessListener { document ->
                if (document != null) {
                    val un = document.data!!["name"].toString()
                    val email = document.data!!["email"].toString()

                    showusername.text = un
                    showemail.text = email
                }
            }
                .addOnFailureListener { exception ->
                    Toast.makeText(this, "Failed", Toast.LENGTH_SHORT).show()
                }


            camera = findViewById(R.id.camera)

            val openGalleryButton: ImageView = findViewById(R.id.camera)
            openGalleryButton.setOnClickListener {
                openImagePicker()
            }

            logoutbutton = findViewById(R.id.logoutbutton)

            logoutbutton.setOnClickListener {
                val intent = Intent(this@Profile, SignInView::class.java)
                startActivity(intent)
            }
        }
    }

//    private fun openGallery(){
//        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
//        startActivityForResult(intent, REQUEST_CODE)
//    }

//    private lateinit var camera: ImageView
    private val itemCollectionRef = Firebase.firestore.collection("Users")
    var item_id = 1
    private lateinit var current_image_url: String

    private val pickImageLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            val imageUri = result.data?.data
            imageUri?.let { uri ->
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                    val source = ImageDecoder.createSource(this.contentResolver, uri)
                    val bitmap = ImageDecoder.decodeBitmap(source)
                    camera.setImageBitmap(bitmap)
                    val storageRef = FirebaseStorage.getInstance().reference
                    val imageRef = storageRef.child("profilepicture/${System.currentTimeMillis()}.jpg")
                    val uploadTask = imageRef.putFile(uri)
                    uploadTask.addOnSuccessListener { taskSnapshot ->
                        // Image upload successful
                        imageRef.downloadUrl.addOnSuccessListener { uri ->
                            current_image_url = uri.toString()
                        }
                        Toast.makeText(this, "Image uploaded successfully", Toast.LENGTH_SHORT).show()
                    }.addOnFailureListener { exception ->
                        // Handle image upload failure
                        Toast.makeText(this, "Failed to upload image", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    val bitmap = MediaStore.Images.Media.getBitmap(this.contentResolver, uri)
                    camera.setImageBitmap(bitmap)
                    val storageRef = FirebaseStorage.getInstance().reference
                    val imageRef = storageRef.child("profilepicture/${System.currentTimeMillis()}.jpg")
                    val uploadTask = imageRef.putFile(uri)
                    uploadTask.addOnSuccessListener { taskSnapshot ->
                        // Image upload successful
                        // imageref.downloadurl digunakan agar url yang digunakan merupakan url permanen gambar dan bukan temporary
                        // sehingga gambar akan tetap muncul walaupun aplikasi di close dan buka
                        imageRef.downloadUrl.addOnSuccessListener { uri ->
                            current_image_url = uri.toString()
                        }
                        Toast.makeText(this, "Image uploaded successfully", Toast.LENGTH_SHORT).show()
                    }.addOnFailureListener { exception ->
                        // Handle image upload failure
                        Toast.makeText(this, "Failed to upload image", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    private fun openImagePicker() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        pickImageLauncher.launch(intent)
    }

//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        super.onActivityResult(requestCode, resultCode, data)
//
//        if(requestCode == REQUEST_CODE && resultCode == RESULT_OK && data != null){
//            val selectedImage = data.data
//
//            selectedImage?.let { uri ->
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
//                    camera.setImageURI(selectedImage)
//
//                    val layoutParams = camera.layoutParams
//
//                    try {
//
//                        layoutParams.width = 360
//                        layoutParams.height = 360
//                        camera.layoutParams = layoutParams
//
//                        camera.scaleType = ImageView.ScaleType.CENTER_CROP
//                    } catch (e: FileNotFoundException) {
//                        e.printStackTrace()
//                    }
//
//                    val storageRef = FirebaseStorage.getInstance().reference
//                    val imageRef =
//                        storageRef.child("profilepicture/${System.currentTimeMillis()}.jpg")
//                    val uploadTask = imageRef.putFile(uri)
//                    uploadTask.addOnSuccessListener {
//                        taskSnapshot -> Toast.makeText(this, "Profile Updated", Toast.LENGTH_SHORT).show()
//                    }.addOnFailureListener{
//                        exception -> Toast.makeText(this, "Try Again", Toast.LENGTH_SHORT).show()
//                    }
//                }
//                else{
//                    camera.setImageURI(selectedImage)
//
//                    val layoutParams = camera.layoutParams
//
//                    try {
//
//                        layoutParams.width = 260
//                        layoutParams.height = 260
//                        camera.layoutParams = layoutParams
//
//                        camera.scaleType = ImageView.ScaleType.CENTER_CROP
//                    } catch (e: FileNotFoundException) {
//                        e.printStackTrace()
//                    }
//
//                    val storageRef = FirebaseStorage.getInstance().reference
//                    val imageRef =
//                        storageRef.child("profilepicture/${System.currentTimeMillis()}.jpg")
//                    val uploadTask = imageRef.putFile(uri)
//                    uploadTask.addOnSuccessListener {
//                            taskSnapshot -> Toast.makeText(this, "Profile Updated", Toast.LENGTH_SHORT).show()
//                    }.addOnFailureListener{
//                            exception -> Toast.makeText(this, "Try Again", Toast.LENGTH_SHORT).show()
//                    }
//                }
//            }
//        }
//    }
}
