package com.example.bikekollective

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.bikekollective.databinding.FragmentUserBikeListBinding


class UserBikeList : Fragment() {
    private lateinit var binding: FragmentUserBikeListBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentUserBikeListBinding.inflate(inflater, container, false)

        binding.fabAddBike.setOnClickListener {
            startActivity(Intent(context, CreateBikeActivity::class.java))
        }
        return binding.root

    }


}