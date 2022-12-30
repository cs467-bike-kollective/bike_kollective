package com.example.bikekollective

import android.content.Context
import android.graphics.Color
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

        // Comment out the code above and un-comment the code below to
        // display only the bikes with "Road Tag"
        // This feature is in our future improvement list
        /**
        db.collection("bikes").whereEqualTo("is_available", true).whereArrayContains("tag", "Road")
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

        val roadChip = binding.chip1
        roadChip.setOnClickListener {
            Log.d(
                javaClass.simpleName, "Display the search result for " +
                        "${roadChip.text}"
            )
            roadChip.text = "✅ Road"
        }

        val bmxChip = binding.chip6
        bmxChip.setOnClickListener {
            Log.d(
                javaClass.simpleName, "Display the search result for " +
                        "${bmxChip.text}"
            )
            bmxChip.text = "✅ BMX"
            roadChip.text = "Road"
        }


        /**
         binding.searchBut.setOnClickListener {
        searchTerm = if(searchTerm.isEmpty() ) {
        binding.searchBut.setBackgroundColor(Color.YELLOW)
        "con"
        } else {
        binding.searchBut.setBackgroundColor(Color.WHITE)
        ""
        }
        viewModel.setSearchTerm(searchTerm)
        }
         **/

    }



}