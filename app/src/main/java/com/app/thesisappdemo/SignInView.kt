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

class SignInView : AppCompatActivity() {

//    lateinit var binding : FragmentSignInViewBinding
//    lateinit var auth : FirebaseAuth

    lateinit var firestore: FirebaseFirestore

    lateinit var etEmail: EditText
    lateinit var etPassword: EditText
    lateinit var btnSignIn: Button
    lateinit var btnCreateOne: TextView

    private var db = Firebase.firestore

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_sign_in_view)

//        auth = FirebaseAuth.getInstance()

        firestore = FirebaseFirestore.getInstance()

        etEmail = findViewById(R.id.textfield_EmailSignIn)
        etPassword = findViewById(R.id.textfield_PasswordSignIn)
        btnSignIn = findViewById(R.id.signinButton)
        btnCreateOne = findViewById(R.id.createAccountButtonInSignIn)

//        val userId = FirebaseFirestore.getInstance()
//        val reference = db.collection("customers").document(userId.toString())

        btnSignIn.setOnClickListener {
            val email = etEmail.text.toString()
            val password = etPassword.text.toString()

            if (email.isEmpty() && password.isEmpty()) {
                etEmail.error = "Field tidak boleh kosong"
                etEmail.requestFocus()
                return@setOnClickListener
            }

            //Validasi user
//            reference.get().addOnSuccessListener {
//                if (it != null) {
//                    val name = it.data?.get("name").toString()
//                    val email = it.data?.get("email").toString()
//                    val password = it.data?.get("password").toString()
//
//                    etEmail.text = email
//                    etPassword.text = password
//                }
//            }
//                .addOnFailureListener {
//                    Toast.makeText(this, "Failed to signing in!", Toast.LENGTH_SHORT).show()
//                }

            signIn(email, password)
        }

        btnCreateOne.setOnClickListener {
            val intent = Intent(this, RegisterView::class.java)
            startActivity(intent)
        }

//        binding = FragmentSignInViewBinding.inflate(layoutInflater)
//        super.onCreate(savedInstanceState)
//        setContentView(binding.root)
//
//        //Untuk menuju dari halaman sign in ke halaman register
//        binding.createAccountButtonInSignIn.setOnClickListener {
//            val intent = Intent(this, RegisterView::class.java)
//            startActivity(intent)
//        }
//
//        binding.signinButton.setOnClickListener {
//            val email = binding.textfieldEmailSignIn.text.toString()
//            val password = binding.textfieldPasswordSignIn.text.toString()
//
//            //Kalo email kosong (validasi email)
//            if (email.isEmpty()) {
//                binding.textfieldEmailSignIn.error = "Field Email tidak boleh kosong!"
//                binding.textfieldEmailSignIn.requestFocus()
//                return@setOnClickListener
//            }
//
//            //Kalo email tidak valid (validasi email kalo tidak sesuai formatnya)
//            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
//                binding.textfieldEmailSignIn.error = "Format email tidak valid"
//                binding.textfieldEmailSignIn.requestFocus()
//                return@setOnClickListener
//            }
//
//            //Kalo password kosong (validasi password)
//            if (password.isEmpty()) {
//                binding.textfieldPasswordSignIn.error = "Field Passsword tidak boleh kosong!"
//                binding.textfieldPasswordSignIn.requestFocus()
//                return@setOnClickListener
//            }
//            SignInFirebase(email, password)
//        }
    }

    //Function untuk sign in user
    private fun signIn(email: String, password: String) {
        firestore.collection("customers").get().addOnSuccessListener {

            Toast.makeText(this, "Succeed", Toast.LENGTH_LONG).show()
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
            .addOnFailureListener {

                it.printStackTrace()
                Toast.makeText(this, "Failed", Toast.LENGTH_LONG).show()
            }
    }
}

    //    private fun SignInFirebase(email: String, password: String) {
//        auth.signInWithEmailAndPassword(email, password)
//            .addOnCompleteListener(this) {
//                //Kalau berhasil sign in
//                if (it.isSuccessful) {
//                    Toast.makeText(this, "Selamat datang $email", Toast.LENGTH_SHORT).show()
//                    val intent = Intent(this, MainActivity::class.java)
//                    startActivity(intent)
//                } else {
//                    //Kalau gagal sign in
//                    Toast.makeText(this,"${it.exception?.message}", Toast.LENGTH_SHORT).show()
//                }
//            }
//    }
