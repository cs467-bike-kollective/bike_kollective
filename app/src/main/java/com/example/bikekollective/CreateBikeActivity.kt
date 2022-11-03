package com.example.bikekollective

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.opengl.Visibility
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.bumptech.glide.Glide
import com.example.bikekollective.databinding.ActivityCreateBikeBinding
import com.example.bikekollective.models.Bike
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import java.util.concurrent.ThreadLocalRandom

class CreateBikeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCreateBikeBinding
    private var longitude: Double? = null
    private var latitude: Double? = null
    private var photoUri: Uri? = null

    private lateinit var firebaseStorage: StorageReference
    private val db = Firebase.firestore
    private lateinit var auth: FirebaseAuth
    companion object {
        private const val TAG = "CreateBikeActivity"
        private const val CREATE_BIKE_IDENTIFIER = 1001
    }


    private val resultLauncherCameraActivity = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        Log.i("TAG", "HERE")
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

        firebaseStorage = FirebaseStorage.getInstance().getReference();

        auth = Firebase.auth

        // hide action bar
        supportActionBar?.hide();

        binding.ivExitCreateBike.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
        binding.bikeImage.setOnClickListener {
            openCameraForResult()
        }
        binding.submitFormButton.setOnClickListener {
            val userID = auth.currentUser?.uid.toString()
            var missingFields = false
            //create temp lat and long
            latitude = ThreadLocalRandom.current().nextDouble(33.0, 34.351711,);
            longitude = ThreadLocalRandom.current().nextDouble(-118.654626,  -118.046890);

            // prevent user from submitting form multiple times
            binding.submitFormButton.isEnabled = false

            val descriptionInput = binding.bikeDescription.text.toString()
            val combinationInput = binding.bikeLockCombination.text.toString()
            //check if description is entered
            if (descriptionInput.isEmpty()){
                binding.bikeDescription.hint = "Add Description"
                binding.bikeDescription.setHintTextColor(Color.RED)
                missingFields = true
            }
            // check if the combination entered is not empty
            if (combinationInput.isEmpty()){
                binding.bikeLockCombination.hint = "Add Combination"
                binding.bikeLockCombination.setHintTextColor(Color.RED)
                missingFields = true
            }
            if (photoUri == null){
                missingFields = true
                binding.tvChoosePhoto.visibility = View.VISIBLE
            }else{
                binding.tvChoosePhoto.visibility = View.INVISIBLE
            }

            if (!missingFields){
                val imageRef = firebaseStorage.child("$userID/bikes/${System.currentTimeMillis()}_photo.jpg")
                imageRef.putFile(photoUri!!)
                    .continueWithTask { photoUploadTask ->
                        imageRef.downloadUrl

                    }.continueWith { downloadUrl ->
                        Log.i(TAG, downloadUrl.result.toString())
                        var bike = Bike(
                            userID,
                            true,
                            latitude,
                            longitude,
                            downloadUrl.result.toString(),
                            descriptionInput,
                            combinationInput,
                            0.0,
                            0,
                            0.0,
                            null,
                            ""
                        )
                        db.collection("bikes").add(bike).addOnSuccessListener {
                            Toast.makeText(
                            baseContext,
                            "Bike Added.",
                            Toast.LENGTH_SHORT
                            ).show()
                            //            reset the input
                            binding.bikeDescription.hint = "Description"
                            binding.bikeLockCombination.hint = "Combination"
                            Glide.with(baseContext)
                                .load(R.drawable.click_to_take_photo)
                                .into(binding.bikeImage)

                            //clear data on form and re-enable button
                            binding.bikeDescription.text.clear()
                            binding.bikeLockCombination.text.clear()
                            binding.submitFormButton.isEnabled = true
                            startActivity(Intent(this, MainActivity::class.java))
                            finish()
                        }.addOnFailureListener {

                        }
                        binding.submitFormButton.isEnabled = true

                    }
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