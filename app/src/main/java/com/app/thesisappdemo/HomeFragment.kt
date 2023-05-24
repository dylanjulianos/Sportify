package com.app.thesisappdemo

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.app.thesisappdemo.databinding.FragmentHomeBinding
import kotlinx.android.synthetic.main.fragment_home.view.*

class HomeFragment : Fragment() {
private lateinit var binding : FragmentHomeBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,savedInstanceState: Bundle?):
        View? {
        binding = FragmentHomeBinding.inflate(layoutInflater)
    return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // Instead of view.findViewById(R.id.hello) as TextView
//        val url = "http://www.google.com"
        val url ="https://wa.link/8j16c2"
        val intent = Intent(Intent.ACTION_VIEW)
        binding.btnWhatsapp.setOnClickListener() {
            Toast.makeText(context,"Please wait...", Toast.LENGTH_LONG).show()
            intent.data = Uri.parse(url)
            startActivity(intent)
        }
    }
}
