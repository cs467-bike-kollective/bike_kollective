package com.example.bikekollective

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.bikekollective.adapters.UserBikeListAdapter
import com.example.bikekollective.databinding.FragmentUserBikeListBinding


class UserBikeList : Fragment() {
    private lateinit var binding: FragmentUserBikeListBinding
    private var recyclerView: RecyclerView? = null
    private lateinit var userBikeAdapter: UserBikeListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentUserBikeListBinding.inflate(inflater, container, false)

        if ((activity?.applicationContext as ApplicationContext).userBikeList.isNullOrEmpty()){
            (activity?.applicationContext as ApplicationContext).queryUserBikes()
        }
        binding.fabAddBike.setOnClickListener {
            startActivity(Intent(context, CreateBikeActivity::class.java))
        }
        userBikeAdapter = UserBikeListAdapter(context, (activity?.applicationContext as ApplicationContext).userBikeList)



        recyclerView = binding.rvUserBikeList
        recyclerView!!.adapter = userBikeAdapter
        recyclerView!!.layoutManager = LinearLayoutManager(context)
        return binding.root

    }

    override fun onResume() {
        super.onResume()
        userBikeAdapter?.notifyDataSetChanged()
    }

}