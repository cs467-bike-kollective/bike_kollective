package com.example.bikekollective

import android.app.Activity
import android.content.Context
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
import androidx.core.view.children
import com.bumptech.glide.Glide
import com.example.bikekollective.databinding.ActivityCreateBikeBinding
import com.example.bikekollective.databinding.ChipBinding
import com.example.bikekollective.models.Bike
import com.google.android.material.chip.Chip
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
    private var chosenTagList: ArrayList<String>? = null

    private lateinit var firebaseStorage: StorageReference
    private val db = Firebase.firestore
    private lateinit var auth: FirebaseAuth
    companion object {
        private const val TAG = "CreateBikeActivity"
        private const val CREATE_BIKE_IDENTIFIER = 1001
    }


    private val resultLauncherCameraActivity = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        binding.tvChoosePhoto.visibility = View.INVISIBLE
        Log.i("TAG", "HERE")
        if (result.resultCode == Activity.RESULT_OK) {
            // There are no request codes
            val data: Intent? = result.data

            photoUri = Uri.parse( data?.extras?.getString("imageUri"))

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

        binding.bikeLocation.setOnClickListener {
            startActivity(Intent(this, PickLocationMapsActivity::class.java))
        }

        //ensure that tags aren't null and add chip choices
        if ((applicationContext as ApplicationContext).bikeTagList.isNullOrEmpty()){
            (applicationContext as ApplicationContext).queryBikeTags()
        }else{
            (applicationContext as ApplicationContext).bikeTagList?.forEach{ tag ->
                binding.chipGroup.addView(createTagChip(baseContext, tag?.name.toString()))
            }

        }
        binding.progressBar.visibility = View.GONE

        // hide action bar
        supportActionBar?.hide();

        binding.bikeLocation.setOnClickListener {
            startActivity(Intent(this, PickLocationMapsActivity::class.java))
        }

        binding.ivExitCreateBike.setOnClickListener {
//            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
        binding.bikeImage.setOnClickListener {
            openCameraForResult()
        }


       // reference https://stackoverflow.com/questions/58224630/how-to-get-selected-chips-from-chipgroup
        binding.submitFormButton.setOnClickListener {

            binding.progressBar.visibility = View.VISIBLE
            val userID = auth.currentUser?.uid.toString()
            var missingFields = false

            //get tags chosen
            if (binding.chipGroup.childCount > 0) {
                chosenTagList = arrayListOf()
                binding.chipGroup.children.toList().filter { (it as Chip).isChecked }.forEach {
                    chosenTagList?.add((it as Chip).text.toString())
                }
            }
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
                            chosenTagList,

                        )

                        db.collection("bikes").add(bike).addOnSuccessListener { it ->
                            //add bike ID
                            bike.documentId = it.id

                            //add bike to global data
                            (applicationContext as ApplicationContext).userBikeList?.add(bike)

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
                                .centerCrop()
                                .into(binding.bikeImage)

                            //clear data on form and re-enable button
                            binding.bikeDescription.text.clear()
                            binding.bikeLockCombination.text.clear()
                            binding.submitFormButton.isEnabled = true
                            binding.progressBar.visibility = View.INVISIBLE

                            startActivity(Intent(this, MainActivity::class.java))
                            finish()
                        }.addOnFailureListener {
                            binding.submitFormButton.isEnabled = true
                            binding.progressBar.visibility = View.INVISIBLE

                        }
                        binding.submitFormButton.isEnabled = true
                        binding.progressBar.visibility = View.INVISIBLE

                    }
            }
            Log.i(TAG, "$descriptionInput $combinationInput")


            binding.submitFormButton.isEnabled = true
            binding.progressBar.visibility = View.INVISIBLE


        }
    }

    private fun createTagChip(baseContext: Context?, tagString: String): Chip {

        val chip = ChipBinding.inflate(layoutInflater).root
        chip.text = tagString
        return chip

    }

    private fun openCameraForResult() {
        val intentCameraActivity = Intent(this, CameraActivity::class.java)
        intentCameraActivity.putExtra("identifier", CREATE_BIKE_IDENTIFIER)
        resultLauncherCameraActivity.launch(intentCameraActivity)
    }


}