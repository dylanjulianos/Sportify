package com.app.thesisappdemo

import android.text.Layout
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class RecyclerAdapter: RecyclerView.Adapter<RecyclerAdapter.ViewHolder>() {

    private var name = arrayOf("nama1", "nama2", "nama3", "nama4", "hanzel gay")
    private var bio = arrayOf("det1", "det2", "det3", "det4", "gay")
    private var images = intArrayOf(R.drawable.baseline_person_24, R.drawable.baseline_person_24, R.drawable.baseline_person_24, R.drawable.baseline_person_24, R.drawable.baseline_person_24)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerAdapter.ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.card_layout, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: RecyclerAdapter.ViewHolder, position: Int) {
        holder.itemName.text = name[position]
        holder.itemBio.text = bio[position]
        holder.itemImage.setImageResource(images[position])
    }

    override fun getItemCount(): Int {
        return name.size
    }

    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        var itemImage : ImageView
        var itemName : TextView
        var itemBio : TextView

        init {
            itemImage = itemView.findViewById(R.id.item_display_picture)
            itemName = itemView.findViewById(R.id.item_display_name)
            itemBio = itemView.findViewById(R.id.item_display_bio)
        }
    }
}