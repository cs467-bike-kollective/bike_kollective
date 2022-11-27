package com.example.bikekollective

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.bikekollective.adapters.BikeItemAdapter
import com.example.bikekollective.databinding.FragmentSearchBinding


class SearchFragment : Fragment() {
    private lateinit var binding: FragmentSearchBinding
    private var recyclerView: RecyclerView? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentSearchBinding.inflate(inflater, container, false)

        if ((activity?.applicationContext as ApplicationContext).userBikeList.isNullOrEmpty()){
            (activity?.applicationContext as ApplicationContext).queryUserBikes()
        }
        val bikeItemAdapter = BikeItemAdapter(context, (activity?.applicationContext as ApplicationContext).userBikeList)


        recyclerView = binding.rvSearchBikeList
        recyclerView!!.adapter = bikeItemAdapter
        recyclerView!!.layoutManager = LinearLayoutManager(context)
        return binding.root

    }


}