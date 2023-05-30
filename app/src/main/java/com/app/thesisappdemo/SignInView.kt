package com.app.thesisappdemo

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.FirebaseFirestore

class SignInView: AppCompatActivity() {

    lateinit var etEmail: EditText
    lateinit var etPassword: EditText
    lateinit var btnSignIn: Button
    lateinit var btnCreateAccount: TextView
    lateinit var firestore: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.signin_view)

        etEmail = findViewById(R.id.textfield_EmailSignIn)
        etPassword = findViewById(R.id.textfield_PasswordSignIn)
        btnSignIn = findViewById(R.id.signinButton)
        btnCreateAccount = findViewById(R.id.createAccountButtonInSignIn)

        firestore = FirebaseFirestore.getInstance()

        btnCreateAccount.setOnClickListener {
            val intent = Intent(this, RegistrationView::class.java)
            startActivity(intent)
        }

        btnSignIn.setOnClickListener {
            val email = etEmail.text.toString()
            val password = etPassword.text.toString()

            val usersCollection = firestore.collection("Users")
            val query = usersCollection
                .whereEqualTo("email", email)
                .whereEqualTo("password", password)

            query.get()
                .addOnSuccessListener { querySnapshot ->
                    if (!querySnapshot.isEmpty) {

                        //login success
                        val userDocument = querySnapshot.documents[0]
                        val userId = userDocument.id

                        getUserRole(userId)
//                        val intent = Intent(this, MainActivity::class.java)
//                        startActivity(intent)
                    } else {
                        Toast.makeText(this, "Incorrect Email or Password, please try again", Toast.LENGTH_LONG).show()
                    }
                }
        }
    }

    fun getUserRole(userId: String) {
        val firestore = FirebaseFirestore.getInstance()

        firestore.collection("Users").document(userId)
            .get()
            .addOnSuccessListener { documentSnapshot ->
                val role = documentSnapshot.getString("role")
                // Use the role as needed
                if (role == "Admin") {
                    val intent = Intent(this@SignInView, AdminView::class.java)
                    startActivity(intent)
                } else if (role == "Customer") {
                    val intent = Intent(this@SignInView, CustomerView::class.java)
                    startActivity(intent)
                }
            }
//            .addOnFailureListener { exception ->
//                // Handle failure to retrieve user role
//            }
    }

//    fun getUserRole(userId: String) {
//        val cRef = firestore.collection("Users").document("role")
//
//        cRef.get()
//            .addOnSuccessListener { querySnapshot ->
//                for (documentSnapshot in querySnapshot) {
//                    val data = documentSnapshot.data
//                }
//            }
//            .addOnFailureListener {
//
//            }
//    }

}