package com.example.bikekollective.adapters

import android.R
import android.content.Context
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.Target
import com.example.bikekollective.databinding.UserBikeItemBinding
import com.example.bikekollective.models.Bike


class UserBikeListAdapter(
    val context:Context?,
    var userBikeList: MutableList<Bike?>?

    ) : RecyclerView.Adapter<UserBikeListAdapter.ViewHolder>(){


    inner class ViewHolder(val binding: UserBikeItemBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserBikeListAdapter.ViewHolder {
        val binding = UserBikeItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        val bike : Bike? = userBikeList?.get(position)
        if (bike != null) {
            val description = "Description:\n ${bike.description}"
            val combination = "Combination: ${bike.combination}"
            val location = "Location: ${bike.latitude}, ${bike.longitude}"
            viewHolder.binding.userBikeDescription.text = description
            viewHolder.binding.userBikeCombo.text = combination
            viewHolder.binding.userBikeCurrentLocation.text = location

            if (!bike.imagePath.isNullOrEmpty()){
                if (context != null) {
                    Glide.with(context)
                        .load(bike.imagePath)
                        .centerCrop()
                        .into(viewHolder.binding.bikeProfileImage)
                }
            }

        }


    }

    override fun getItemCount(): Int {
       return userBikeList?.size ?: 0
    }
}