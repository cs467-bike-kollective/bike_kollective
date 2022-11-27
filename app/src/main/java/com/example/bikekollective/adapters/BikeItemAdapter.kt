package com.example.bikekollective.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide // for images

import com.example.bikekollective.databinding.SearchBikeItemBinding

import com.example.bikekollective.models.Bike
import java.text.DecimalFormat


class BikeItemAdapter(
    val context:Context?,
    private var userBikeList: MutableList<Bike?>?

) : RecyclerView.Adapter<BikeItemAdapter.ViewHolder>(){


    inner class ViewHolder(val binding: SearchBikeItemBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BikeItemAdapter.ViewHolder {
        val binding = SearchBikeItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        val bike : Bike? = userBikeList?.get(position)
        if (bike != null) {
            var bikeAvailable : String = "Yes"

            // parse the geocoding
            val latitude = bike.latitude
            val longitude = bike.longitude

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
            val tags = "Tags: ${bike.tags}"

            // bind the text to the view
            viewHolder.binding.searchBikeRatings.text = ratings
            viewHolder.binding.searchBikeAvailable.text = isAvailable
            viewHolder.binding.searchBikeDescription.text = description
            viewHolder.binding.searchBikeLocation.text = location
            viewHolder.binding.searchBikeCombo.text = combination
            viewHolder.binding.searchBikeTags.text = tags

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
        return userBikeList?.size ?: 0
    }
}