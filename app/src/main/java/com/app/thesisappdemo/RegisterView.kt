package com.app.thesisappdemo

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import org.w3c.dom.Text
import java.lang.Exception

class RegisterView : AppCompatActivity() {

//    lateinit var binding : FragmentRegisterViewBinding

    lateinit var etName: EditText
    lateinit var etEmail: EditText
    lateinit var etPassword: EditText
    lateinit var btnCreateAccountRegister: Button
    lateinit var btnSignIn: TextView
//    lateinit var auth : FirebaseAuth

    private val customerCollectionRef = Firebase.firestore.collection("customers")

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_register_view)

        etName = findViewById(R.id.textfield_NameRegister)
        etEmail = findViewById(R.id.textfield_EmailRegister)
        etPassword = findViewById(R.id.textfield_PasswordRegister)
        btnCreateAccountRegister = findViewById(R.id.createAccountButton)
        btnSignIn = findViewById(R.id.signInButtonInRegister)

        btnCreateAccountRegister.setOnClickListener {
            val name = etName.text.toString()
            val email = etEmail.text.toString()
            val password = etPassword.text.toString()
            val customer = Customer(name, email, password)
            registerCustomer(customer)
        }

        btnSignIn.setOnClickListener {
            val intent = Intent(this, SignInView::class.java)
            startActivity(intent)
        }
    }

//    override fun onCreate(savedInstanceState: Bundle?) {
//
//        binding = FragmentRegisterViewBinding.inflate(layoutInflater)
//        super.onCreate(savedInstanceState)
//        setContentView(binding.root)
//
//        auth = FirebaseAuth.getInstance()
//
//        //Kalau teks sign in di halaman register diklik
//        binding.signInButtonInRegister.setOnClickListener {
//            val intent = Intent(this, SignInView::class.java)
//            startActivity(intent)
//        }
//
//        //Kalau tombol create account di halaman register diklik
//        binding.createAccountButton.setOnClickListener {
//            val email = binding.textfieldEmailRegister.text.toString()
//            val password = binding.textfieldPasswordRegister.text.toString()
//
//            //Kalo email kosong (validasi email)
//            if (email.isEmpty()) {
//                binding.textfieldEmailRegister.error = "Field Email tidak boleh kosong!"
//                binding.textfieldEmailRegister.requestFocus()
//                return@setOnClickListener
//            }
//
//            //Kalo email tidak valid (validasi email kalo tidak sesuai formatnya)
//            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
//                binding.textfieldEmailRegister.error = "Format email tidak valid"
//                binding.textfieldEmailRegister.requestFocus()
//                return@setOnClickListener
//            }
//
//            //Kalo password kosong (validasi password)
//            if (password.isEmpty()) {
//                binding.textfieldPasswordRegister.error = "Field Passsword tidak boleh kosong!"
//                binding.textfieldPasswordRegister.requestFocus()
//                return@setOnClickListener
//            }
//
//            //Validasi panjang password
//            if (password.length < 5) {
//                binding.textfieldPasswordRegister.error = "Password wajib terdiri dari minimal 5 karakter!"
//                binding.textfieldPasswordRegister.requestFocus()
//                return@setOnClickListener
//            }
//            RegisterFirebase(email, password)
//        }
//    }

    //Function untuk register
//    private fun RegisterFirebase(email: String, password: String) {
//        auth.createUserWithEmailAndPassword(email, password)
//            .addOnCompleteListener(this) {
//                //Kalau berhasil register
//                if (it.isSuccessful) {
//                    Toast.makeText(this, "Register Berhasil", Toast.LENGTH_SHORT).show()
//                    val intent = Intent(this, MainActivity::class.java)
//                    startActivity(intent)
//                } else {
//                    //Kalau gagal register
//                    Toast.makeText(this,"${it.exception?.message}", Toast.LENGTH_SHORT).show()
//                }
//            }
//    }

    private fun registerCustomer(customer: Customer) = CoroutineScope(Dispatchers.IO).launch {
        try {
            customerCollectionRef.add(customer).await()
            withContext(Dispatchers.Main) {
                Toast.makeText(this@RegisterView, "Successfully creating account!", Toast.LENGTH_LONG).show()
                val intent = Intent(this@RegisterView, MainActivity::class.java)
                startActivity(intent)

                //finish() biar user gabisa back ke register screen lagi
                finish()
            }
        } catch (e: Exception) {
            withContext(Dispatchers.Main) {
                Toast.makeText(this@RegisterView, e.message, Toast.LENGTH_LONG).show()
            }
        }
    }
}