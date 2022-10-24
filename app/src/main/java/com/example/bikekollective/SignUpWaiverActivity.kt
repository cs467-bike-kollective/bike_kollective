package com.example.bikekollective

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import com.example.bikekollective.databinding.ActivitySignUpWaiverBinding
import com.google.firebase.auth.FirebaseAuth

class SignUpWaiverActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySignUpWaiverBinding
    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySignUpWaiverBinding.inflate(layoutInflater)
        setContentView(binding.root)


        binding.btnSignupAgree.setOnClickListener {
            startActivity(Intent(this, AccountSetupActivity::class.java))
        }
//        delete user
        binding.btnSignupDisagree.setOnClickListener {
            //todo: Set up dialog button to confirm,

            //delete user
            startActivity(Intent(this, LoginActivity::class.java))

        }


    }

}