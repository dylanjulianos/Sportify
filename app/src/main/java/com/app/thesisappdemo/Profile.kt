package com.app.thesisappdemo

import android.content.Intent
import android.os.Build

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
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

        val uid = firebaseAuth.currentUser?.uid
        showusername = findViewById(R.id.showusername)
        showemail = findViewById(R.id.showemail)

        val docRef = db.collection("Users")
            .document("LHGCSJ8AIc7XPXprVqWx")
//            .document(uid!!)
        docRef.get()
            .addOnSuccessListener { document ->
                if(document != null){
                    val un = document.data!!["name"].toString()
                    val email = document.data!!["email"].toString()

                    showusername.text = un
                    showemail.text = email
                }
            }
            .addOnFailureListener{
                exception -> Toast.makeText(this, "Failed", Toast.LENGTH_SHORT).show()
            }


        camera = findViewById(R.id.camera)

        val openGalleryButton: ImageView = findViewById(R.id.camera)
        openGalleryButton.setOnClickListener{
            openGallery()
        }

        logoutbutton = findViewById(R.id.logoutbutton)

        logoutbutton.setOnClickListener{
            val intent = Intent(this@Profile, SignInView::class.java)
            startActivity(intent)
        }

    }

    private fun openGallery(){
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent, REQUEST_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(requestCode == REQUEST_CODE && resultCode == RESULT_OK && data != null){
            val selectedImage = data.data

            selectedImage?.let { uri ->
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                    camera.setImageURI(selectedImage)

                    val layoutParams = camera.layoutParams

                    try {

                        layoutParams.width = 260
                        layoutParams.height = 260
                        camera.layoutParams = layoutParams

                        camera.scaleType = ImageView.ScaleType.CENTER_CROP
                    } catch (e: FileNotFoundException) {
                        e.printStackTrace()
                    }

                    val storageRef = FirebaseStorage.getInstance().reference
                    val imageRef =
                        storageRef.child("profilepicture/${System.currentTimeMillis()}.jpg")
                    val uploadTask = imageRef.putFile(uri)
                    uploadTask.addOnSuccessListener {
                        taskSnapshot -> Toast.makeText(this, "Profile Updated", Toast.LENGTH_SHORT).show()
                    }.addOnFailureListener{
                        exception -> Toast.makeText(this, "Try Again", Toast.LENGTH_SHORT).show()
                    }
                }
                else{
                    camera.setImageURI(selectedImage)

                    val layoutParams = camera.layoutParams

                    try {

                        layoutParams.width = 260
                        layoutParams.height = 260
                        camera.layoutParams = layoutParams

                        camera.scaleType = ImageView.ScaleType.CENTER_CROP
                    } catch (e: FileNotFoundException) {
                        e.printStackTrace()
                    }

                    val storageRef = FirebaseStorage.getInstance().reference
                    val imageRef =
                        storageRef.child("profilepicture/${System.currentTimeMillis()}.jpg")
                    val uploadTask = imageRef.putFile(uri)
                    uploadTask.addOnSuccessListener {
                            taskSnapshot -> Toast.makeText(this, "Profile Updated", Toast.LENGTH_SHORT).show()
                    }.addOnFailureListener{
                            exception -> Toast.makeText(this, "Try Again", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }
}
