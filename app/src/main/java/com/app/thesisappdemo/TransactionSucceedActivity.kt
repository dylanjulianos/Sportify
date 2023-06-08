package com.app.thesisappdemo

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.app.thesisappdemo.databinding.ActivityTransactionSucceedBinding

class TransactionSucceedActivity : AppCompatActivity() {

    private lateinit var binding: ActivityTransactionSucceedBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTransactionSucceedBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.continueButton.setOnClickListener {
            val intent = Intent(this, MainCustomer::class.java)
            startActivity(intent)
        }
    }
}