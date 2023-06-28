package com.app.thesisappdemo

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.PersistableBundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.app.thesisappdemo.databinding.MainCustomerBinding
import com.google.firebase.firestore.FirebaseFirestore
import com.squareup.picasso.Picasso
//import kotlinx.android.synthetic.main.fragment_home.view.btnProfile
import kotlinx.coroutines.flow.merge
import java.text.SimpleDateFormat
import java.util.Calendar.getInstance
import java.util.Date

class MainCustomer : AppCompatActivity() {

    lateinit var firestore: FirebaseFirestore
    private lateinit var binding : MainCustomerBinding
//    val sdf = SimpleDateFormat("dd/M/yyyy hh:mm:ss")
//    val currentDate = sdf.format(Date())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = MainCustomerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firestore = FirebaseFirestore.getInstance()

        val bundle = intent.extras
        if (bundle != null) {
            val userid = bundle.getString("userid")
            val name = bundle.getString("name")
            val email = bundle.getString("email")

            val useridview = findViewById<TextView>(R.id.userid)
            useridview.text = "User id: $userid"

            val bundlehome1 = Bundle()
            bundlehome1.putString("userid", userid)
            val fragment = HomeFragment()
            fragment.arguments = bundlehome1
            replaceFragment(fragment)

            binding.bottomNavigationView.setOnItemSelectedListener {
                val bundlehome = Bundle()
                bundlehome.putString("userid", userid)
                when (it.itemId) {
                    R.id.home -> {
                        val fragment = HomeFragment()
                        fragment.arguments = bundlehome
                        replaceFragment(fragment)
                    }
                    R.id.cart -> replaceFragment(CartFragment())
                    R.id.transaction -> replaceFragment(TransactionFragment())
                    else -> {
                    }
                }
                true
            }

            binding.btnProfile.setOnClickListener {
                Toast.makeText(applicationContext, "Please wait...", Toast.LENGTH_LONG).show()
//                val bundlehome = Bundle()
                    bundlehome1.putString("userid", userid)
                    bundlehome1.putString("name", name)
                    bundlehome1.putString("email", email)
//            val intent = Intent(this@SignInView, MainCustomer::class.java)
//                    val intent = Intent(this, Coba::class.java)
                val intent = Intent(this, Profile::class.java)
                intent.putExtras(bundlehome1)
                startActivity(intent)
//            startActivity(intent)
            }
        }
    }

    private fun replaceFragment(fragment: Fragment)
    {
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.frame_layout, fragment)
        fragmentTransaction.commit()
    }
}