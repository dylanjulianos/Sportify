package com.app.thesisappdemo

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.lang.Exception

class RegistrationView: AppCompatActivity() {

    lateinit var etName: EditText
    lateinit var etEmail: EditText
    lateinit var etPassword: EditText
    lateinit var etRole: EditText
    lateinit var btnCreateAccountRegister: Button
    lateinit var btnSignIn: TextView
    lateinit var firestore: FirebaseFirestore
    val userCollection = Firebase.firestore.collection("Users")
    var user_id = 1
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.registration_view)

        etName = findViewById(R.id.textfield_NameRegister)
        etEmail = findViewById(R.id.textfield_EmailRegister)
        etPassword = findViewById(R.id.textfield_PasswordRegister)
//        etRole = findViewById(R.id.textfield_RoleRegister)
        btnSignIn = findViewById(R.id.signInButtonInRegister)
        btnCreateAccountRegister = findViewById(R.id.createAccountButton)
        val etRole = "Customer"

        firestore = FirebaseFirestore.getInstance()

        btnSignIn.setOnClickListener {
            val intent = Intent(this, SignInView::class.java)
            startActivity(intent)
        }

        btnCreateAccountRegister.setOnClickListener {
            val cartid = "uid" + user_id
            val name = etName.text.toString()
            val email = etEmail.text.toString()
            val password = etPassword.text.toString()
            val role = etRole
            val user = User(cartid, name, email, password, role)

            registerUser(user)
        }
    }

//    fun registerUser(user: User) {
//        val userCollection = firestore.collection("Users")
//        val userDocument = userCollection.document(user.email)
//
//        userDocument.set(user)
//            .addOnSuccessListener {
//                Toast.makeText(this@RegistrationView, "Successfully creating account!", Toast.LENGTH_LONG).show()
//                val intent = Intent(this@RegistrationView, MainActivity::class.java)
//                startActivity(intent)
//            }
//            .addOnFailureListener { exception ->
//                Toast.makeText(this@RegistrationView, exception.localizedMessage, Toast.LENGTH_LONG).show()
//            }
//    }

    fun registerUser(user: User) = CoroutineScope(Dispatchers.IO).launch {
        try {
//            userCollection.add(user).await()
//            withContext(Dispatchers.Main) {
//                Toast.makeText(this@RegistrationView, "Successfully creating account!", Toast.LENGTH_LONG).show()
//                val intent = Intent(this@RegistrationView, SignInView::class.java)
//                startActivity(intent)
//
//                //finish() biar user gabisa back ke register screen lagi
//                finish()
//            }
            userCollection.add(user).addOnSuccessListener { documentReference ->
                val documentId = documentReference.id
                val updatedDocument = hashMapOf(
                    "userid" to documentId,
                    "name" to user.name,
                    "email" to user.email,
                    "password" to user.password,
                    "role" to user.role,
                )
                documentReference.set(updatedDocument).addOnSuccessListener {
                    Toast.makeText(this@RegistrationView, "Successfully creating account!", Toast.LENGTH_LONG).show()
                    val intent = Intent(this@RegistrationView, SignInView::class.java)
                    startActivity(intent)
                    finish()

                }.addOnFailureListener { e ->
                    // Handle any errors that occurred during the update
                    println("Error adding user: ${e.message}")
                }
            }
        } catch (e: Exception) {
            withContext(Dispatchers.Main) {
                Toast.makeText(this@RegistrationView, e.message, Toast.LENGTH_LONG).show()
            }
        }
    }
}