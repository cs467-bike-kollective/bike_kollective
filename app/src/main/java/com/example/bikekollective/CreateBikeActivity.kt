package com.example.bikekollective

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.result.contract.ActivityResultContracts
import com.bumptech.glide.Glide
import com.example.bikekollective.databinding.ActivityCreateBikeBinding

class CreateBikeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCreateBikeBinding
    private var longitude: Double? = null
    private var latitude: Double? = null
    private var photoUri: Uri? = null
    companion object {
        private const val TAG = "CameraXInfo"
        private const val CREATE_BIKE_IDENTIFIER = 1001
    }


    private val resultLauncherCameraActivity = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            // There are no request codes
            val data: Intent? = result.data
            photoUri = data?.extras?.get("imageUri") as Uri
            if (photoUri != null){
                Glide.with(baseContext)
                    .load(photoUri)
                    .into(binding.bikeImage)
            }


        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        binding = ActivityCreateBikeBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        // hide action bar
        supportActionBar?.hide();


        binding.bikeImage.setOnClickListener {
            openCameraForResult()
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
            binding.submitFormButton.isEnabled = true
        }
    }
    private fun openCameraForResult() {
        val intentCameraActivity = Intent(this, CameraActivity::class.java)
        intentCameraActivity.putExtra("identifier", CREATE_BIKE_IDENTIFIER)
        resultLauncherCameraActivity.launch(intentCameraActivity)
    }

}