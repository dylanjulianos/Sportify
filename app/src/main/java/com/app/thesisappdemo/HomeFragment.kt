package com.app.thesisappdemo

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.app.thesisappdemo.databinding.FragmentHomeBinding
import java.util.concurrent.Executors

class HomeFragment : Fragment() {
    private lateinit var binding : FragmentHomeBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,savedInstanceState: Bundle?):
            View? {
        binding = FragmentHomeBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //val url = "http://www.google.com"
        val url = "https://wa.link/8j16c2"
        val intent = Intent(Intent.ACTION_VIEW)

        binding.btnWhatsapp.setOnClickListener() {
            Toast.makeText(context, "Please wait...", Toast.LENGTH_LONG).show()
            intent.data = Uri.parse(url)
            startActivity(intent)
        }

        binding.badmintoncat.setOnClickListener {
            showToast("Loading, Please wait...")
            val destinationFragment = Badminton()
            val transaction: FragmentTransaction = parentFragmentManager.beginTransaction()

            transaction.replace(R.id.frame_layout, destinationFragment)
            transaction.commit()
        }

        binding.soccercat.setOnClickListener {
            showToast("Loading, Please wait...")
            val destinationFragment = Soccer()
            val transaction: FragmentTransaction = parentFragmentManager.beginTransaction()

            transaction.replace(R.id.frame_layout, destinationFragment)
            transaction.commit()
        }

        binding.basketballcat.setOnClickListener {
            showToast("Loading, Please wait...")
            val destinationFragment = Basketball()
            val transaction: FragmentTransaction = parentFragmentManager.beginTransaction()

            transaction.replace(R.id.frame_layout, destinationFragment)
            transaction.commit()
        }

        binding.golfcat.setOnClickListener {
            showToast("Loading, Please wait...")
            val destinationFragment = Golf()
            val transaction: FragmentTransaction = parentFragmentManager.beginTransaction()

            transaction.replace(R.id.frame_layout, destinationFragment)
            transaction.commit()
        }

        val executor = Executors.newSingleThreadExecutor()
        val handler = Handler(Looper.getMainLooper())

        var image: Bitmap? = null
        var image1: Bitmap? = null
        var imager: Bitmap? = null
        var imager1: Bitmap? = null

        executor.execute {
            val imageURL =
                "https://betv.disway.id/upload/6b0095211002da431557f99811a6f0a8.jpg" //badmintan
            val imageURL1 =
                "https://i.pinimg.com/564x/5f/fc/7a/5ffc7ad8882000ea75232001865b1e7d.jpg" //basket
            val imageURL2 =
                "https://i.pinimg.com/564x/cd/f8/e4/cdf8e45e45b8460d05879219fee34547.jpg" //soccer
            val imageURL3 =
                "https://i.pinimg.com/564x/00/57/98/005798806ff4fc1b554356757f7b1b28.jpg" //golf

            try {
                val badminton = java.net.URL(imageURL).openStream()
                val basket = java.net.URL(imageURL1).openStream()
                val soccer = java.net.URL(imageURL2).openStream()
                val golf = java.net.URL(imageURL3).openStream()

                image = BitmapFactory.decodeStream(badminton)
                image1 = BitmapFactory.decodeStream(basket)
                imager = BitmapFactory.decodeStream(soccer)
                imager1 = BitmapFactory.decodeStream(golf)

                handler.post {
                    binding.badmintoncat.setImageBitmap(image)
                    binding.basketballcat.setImageBitmap(image1)
                    binding.soccercat.setImageBitmap(imager)
                    binding.golfcat.setImageBitmap(imager1)
                }

            }catch (e:java.lang.Exception){
                e.printStackTrace()
            }
        }
    }
    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }
}