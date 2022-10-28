package com.example.bikekollective

import android.content.Intent
import android.graphics.Camera
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.bikekollective.databinding.ActivityCameraBinding
import com.example.bikekollective.databinding.ActivityCreateBikeBinding

class CreateBikeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCreateBikeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreateBikeBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        // hide action bar
        supportActionBar?.hide();

        binding.bikeImage.setOnClickListener {
            startActivity(Intent(this, CameraActivity::class.java))
        }
    }

}