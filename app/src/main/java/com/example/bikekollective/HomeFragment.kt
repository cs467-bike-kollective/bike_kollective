package com.example.bikekollective

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.bikekollective.databinding.FragmentHomeBinding
import com.example.bikekollective.databinding.FragmentUserBikeListBinding
import com.firebase.ui.auth.AuthUI


class HomeFragment : Fragment() {
    // TODO: Rename and change types of parameters

    private lateinit var binding: FragmentHomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        if((activity?.applicationContext as ApplicationContext).currBike == null){
            (activity?.applicationContext as ApplicationContext).getCurrUserInfo()


        }else{

        }
        binding = FragmentHomeBinding.inflate(inflater, container, false)
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
        return binding.root

    }

}