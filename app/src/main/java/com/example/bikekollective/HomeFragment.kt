package com.example.bikekollective
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.bumptech.glide.Glide
import android.widget.Button

import com.example.bikekollective.databinding.FragmentHomeBinding
import com.example.bikekollective.databinding.FragmentUserBikeListBinding
import com.example.bikekollective.models.Bike
import com.example.bikekollective.models.User
import com.firebase.ui.auth.AuthUI
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase


class HomeFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore
    private lateinit var binding: FragmentHomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        db = Firebase.firestore
        auth = Firebase.auth
        arguments?.let {
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentHomeBinding.inflate(inflater, container, false)
        showBorrowedBike()
        binding.logoutBtn.setOnClickListener {
            activity?.let { it1 ->
                AuthUI.getInstance()
                    .signOut(it1.applicationContext)
                    .addOnCompleteListener {
                        val logoutIntent = Intent(activity, LoginActivity::class.java)
                        logoutIntent.flags =
                            Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                        startActivity(logoutIntent)
                        requireActivity().finish()
                    }
            }
        }

        binding.returnBikeBut.setOnClickListener {
            startActivity(Intent(context, ReturnBikeActivity::class.java))
        }

        return binding.root

    }
    private fun showBorrowedBike() {
        db.collection("users").document(auth.currentUser?.uid.toString())
            .get().addOnSuccessListener { snapshot ->
                var currUser = snapshot.toObject(User::class.java)
                if (currUser != null) {
                    if (!currUser!!.borrowedBike.isNullOrEmpty()) {
                        db.collection("bikes").document(currUser?.borrowedBike.toString())
                            .get().addOnSuccessListener { snapshot ->
                                var bike = snapshot.toObject(Bike::class.java)
                                if (!bike?.imagePath.isNullOrEmpty()) {
                                    Glide.with(requireActivity().baseContext)
                                        .load(bike?.imagePath.toString())
                                        .centerCrop()
                                        .into(binding.bikeImage)
                                } else {
                                    Glide.with(requireActivity().baseContext)
                                        .load(R.drawable.no_image_curr_bike)
                                        .centerCrop()
                                        .into(binding.bikeImage)
                                }


                            }
                    }
                }

            }

        }



}