package com.example.bikekollective.adapters

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide // for images
import com.example.bikekollective.BikeProfileActivity

import com.example.bikekollective.databinding.SearchBikeItemBinding

import com.example.bikekollective.models.Bike


class BikeItemAdapter(
    val context:Context?,
    private var bikeList: MutableList<Bike?>?

) : RecyclerView.Adapter<BikeItemAdapter.ViewHolder>(){


    inner class ViewHolder(val binding: SearchBikeItemBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BikeItemAdapter.ViewHolder {
        val binding = SearchBikeItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        val bike : Bike? = bikeList?.get(position)
        if (bike != null) {
            var bikeAvailable : String = "Yes"

            // parse the geocoding
            val latitude = bike.latitude
            val longitude = bike.longitude

            // get the user's location



            val availability = bike.isAvailable
            if (availability == false) {
                bikeAvailable = "No"
            }

            val ratings = "Ratings: ${String.format(("%.2f"), (bike.ratingTotal!!/ bike.numberOfRatings!!))}"
            // String.format(("%.2f"), rating)
            val isAvailable = "Available: $bikeAvailable"
            val description = "Description: ${bike.description}"
            val combination = "Combination: ${bike.combination}"
            val location = "Location: ${bike.latitude}, ${bike.longitude}"

            // bind the text to the view
            viewHolder.binding.searchBikeRatings.text = ratings
            viewHolder.binding.searchBikeAvailable.text = isAvailable
            viewHolder.binding.searchBikeDescription.text = description
            viewHolder.binding.searchBikeLocation.text = location
            viewHolder.binding.searchBikeCombo.text = combination
            if (bike.tag != null) {
                val tags = "Tags: ${bike.tag.toString()}"
                viewHolder.binding.searchBikeTags.text = tags
            }

            viewHolder.binding.bikeSearchLayout.setOnClickListener{
                var intent = Intent(context, BikeProfileActivity::class.java)
                intent.putExtra("bike", bike)
                Log.i("AdapterSearch", bike.toString())
                context?.startActivity(intent)
            }
            // images for the bike
            if (!bike.imagePath.isNullOrEmpty()){
                if (context != null) {
                    Glide.with(context)
                        .load(bike.imagePath)
                        .centerCrop()
                        .into(viewHolder.binding.searchBikeImage)
                }
            }

        }


    }

    override fun getItemCount(): Int {
        return bikeList?.size ?: 0
    }
}