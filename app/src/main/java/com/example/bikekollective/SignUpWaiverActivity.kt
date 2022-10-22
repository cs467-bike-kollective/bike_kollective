package com.example.bikekollective

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import com.example.bikekollective.databinding.ActivitySignUpWaiverBinding

class SignUpWaiverActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySignUpWaiverBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySignUpWaiverBinding.inflate(layoutInflater)
        setContentView(binding.root)


    }

}