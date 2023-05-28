package com.app.thesisappdemo

import android.content.Intent

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.FirebaseApp
import java.io.FileNotFoundException


class Profile : AppCompatActivity() {
    private val REQUEST_CODE = 1
    private lateinit var camera: ImageView


    val db = FirebaseFirestore.getInstance()

    //variabel untuk mengambil data profile//
    private lateinit var username: EditText
    private lateinit var email: EditText

    private lateinit var showusername: TextView
    private lateinit var showemail: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        camera = findViewById(R.id.camera)

        val openGalleryButton: ImageView = findViewById(R.id.camera)
        openGalleryButton.setOnClickListener{
            openGallery()
        }

//        val uid = fire
    }

    private fun openGallery(){
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent, REQUEST_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(requestCode == REQUEST_CODE && resultCode == RESULT_OK && data != null){
            val selectedImage = data.data
            camera.setImageURI(selectedImage)

            val layoutParams = camera.layoutParams

            try {

                layoutParams.width = 260
                layoutParams.height = 260
                camera.layoutParams = layoutParams

                camera.scaleType = ImageView.ScaleType.CENTER_CROP
            }
                catch (e: FileNotFoundException)
            {
                e.printStackTrace()
            }
        }
    }
}
