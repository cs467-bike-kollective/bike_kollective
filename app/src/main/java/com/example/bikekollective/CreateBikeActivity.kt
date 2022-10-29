package com.example.bikekollective

import android.app.Activity
import android.content.Intent
import android.graphics.Camera
import android.graphics.Color
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import com.example.bikekollective.databinding.ActivityCameraBinding
import com.example.bikekollective.databinding.ActivityCreateBikeBinding

class CreateBikeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCreateBikeBinding
    companion object {
        private const val TAG = "CameraXInfo"
    }

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        binding = ActivityCreateBikeBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        // hide action bar
        supportActionBar?.hide();

//        https://stackoverflow.com/a/66096319

        binding.bikeImage.setOnClickListener {
            startActivity(Intent(this, CameraActivity::class.java))
        }
        binding.submitFormButton.setOnClickListener {
            // prevent user from submitting form multiple times
            binding.submitFormButton.isEnabled = false

            val descriptionInput = binding.bikeDescription.text.toString()
            val combinationInput = binding.bikeLockCombination.text.toString()
            //check if description is entered
            if (descriptionInput.isEmpty()){
                binding.bikeDescription.hint = "Add Description"
                binding.bikeDescription.setHintTextColor(Color.RED)
            }
            // check if the combination entered is not empty
            if (combinationInput.isEmpty()){
                binding.bikeLockCombination.hint = "Add Combination"
                binding.bikeLockCombination.setHintTextColor(Color.RED)
            }

            Log.i(TAG, "$descriptionInput $combinationInput")
        }
    }

}