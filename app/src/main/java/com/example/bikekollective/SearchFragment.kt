package com.example.bikekollective

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.bikekollective.adapters.BikeItemAdapter
import com.example.bikekollective.databinding.ChipBinding
import com.example.bikekollective.databinding.FragmentSearchBinding
import com.example.bikekollective.models.Bike
import com.google.android.material.chip.Chip
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase


class SearchFragment : Fragment() {
    private lateinit var binding: FragmentSearchBinding
    private var recyclerView: RecyclerView? = null
    private val db = Firebase.firestore
    var bikeList: MutableList<Bike?>? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        db.collection("bikes").get().addOnSuccessListener { snapshot ->
            bikeList = snapshot.toObjects(Bike::class.java)
            // Log.d(javaClass.simpleName, "List: ${bikeList.toString()}")

            val bikeItemAdapter = BikeItemAdapter(context, bikeList)
            recyclerView = binding.rvSearchBikeList
            recyclerView!!.adapter = bikeItemAdapter
            recyclerView!!.layoutManager = LinearLayoutManager(context)
        }

        /**
        db.collection("bikes").whereEqualTo("is_available", true).whereArrayContains("tag", "Cruiser")
            .get().addOnSuccessListener { snapshot ->
                bikeList = snapshot.toObjects(Bike::class.java)
                Log.d(javaClass.simpleName, bikeList.toString())
                val bikeItemAdapter = BikeItemAdapter(context, bikeList)
                recyclerView = binding.rvSearchBikeList
                recyclerView!!.adapter = bikeItemAdapter
                recyclerView!!.layoutManager = LinearLayoutManager(context)
            }
        **/

        // Inflate the layout for this fragment
        binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Click on each chip to view the filtered search result
        // Road
        binding.chip1.setOnClickListener {
            Log.d(
                javaClass.simpleName, "Display the search result for " +
                        "${binding.choiceChips.childCount}"
            )
            db.collection("bikes").whereEqualTo("is_available", true).whereArrayContains("tag", mutableListOf(tag))
                .get().addOnSuccessListener { snapshot ->
                    bikeList = snapshot.toObjects(Bike::class.java)
                    Log.d(javaClass.simpleName, bikeList.toString())
                }
        }

    }



}