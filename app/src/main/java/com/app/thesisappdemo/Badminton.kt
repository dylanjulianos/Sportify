package com.app.thesisappdemo

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import com.app.thesisappdemo.databinding.ActivityBadmintonBinding

class Badminton : AppCompatActivity() {
    private val REQUEST_CODE = 1
    private lateinit var binding : ActivityBadmintonBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_badminton)

    }
}