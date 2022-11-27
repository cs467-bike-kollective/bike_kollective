package com.example.bikekollective

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.bikekollective.adapters.BikeItemAdapter
import com.example.bikekollective.databinding.FragmentSearchBinding
import com.example.bikekollective.models.Bike
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
        // Inflate the layout for this fragment
        binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root

    }


}