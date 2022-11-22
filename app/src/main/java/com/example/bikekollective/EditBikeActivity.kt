package com.example.bikekollective
import android.app.Activity
import android.content.Context
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.bikekollective.databinding.ActivityEditBikeBinding
import android.graphics.Color
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Build.VERSION.SDK_INT
import android.os.Parcelable
import android.os.PersistableBundle
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.view.children
import androidx.core.view.forEach
import com.bumptech.glide.Glide
import com.example.bikekollective.databinding.ChipBinding
import com.example.bikekollective.models.Bike
import com.google.android.material.chip.Chip
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.StorageReference

class EditBikeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityEditBikeBinding
    private var bike: Bike? = null
    private lateinit var firebaseStorage: StorageReference
    private val db = Firebase.firestore
    private lateinit var auth: FirebaseAuth
    private var photoUri: Uri? = null
    private var longitude: Double? = null
    private var latitude: Double? = null
    private var newImage: Boolean = false
    private var editTagList: ArrayList<String>? = null
    companion object {
        private const val TAG = "EditBikeActivity"

    }
    private val resultLauncherCameraActivity = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        Log.i("TAG", "HERE")
        if (result.resultCode == Activity.RESULT_OK) {
            // There are no request codes
            val data: Intent? = result.data

            photoUri = Uri.parse( data?.extras?.getString("imageUri"))

            if (photoUri != null){
                Glide.with(baseContext)
                    .load(photoUri)
                    .into(binding.editBikeImage)
                newImage = true
            }


        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityEditBikeBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        auth = Firebase.auth

        bike = if (SDK_INT >= 33) {
            intent.getParcelableExtra("bike", Bike::class.java)

        } else {
            intent.getParcelableExtra<Bike>("bike")
        }


        latitude = bike?.latitude
        longitude = bike?.longitude
        addDataToForm(bike)

        // hide action bar
        supportActionBar?.hide()
        binding.editProgressBar.visibility = View.GONE

        Log.i(TAG, (applicationContext as ApplicationContext).bikeTagList.toString())
        if ((applicationContext as ApplicationContext).bikeTagList.isNullOrEmpty()){
            (applicationContext as ApplicationContext).queryUserBikes()
        }else{
            (applicationContext as ApplicationContext).bikeTagList?.forEach{ tag ->
                binding.editChipGroup.addView(createTagChip(baseContext, tag?.name.toString()))
            }

        }

        binding.editBikeImage.setOnClickListener{
            openCameraForResult()
        }
        binding.editSubmitFormButton.setOnClickListener {
            //disable button to prevent multiple clicks
            binding.editProgressBar.visibility = View.VISIBLE
            val userID = auth.currentUser?.uid.toString()
            var missingFields = false

            binding.editSubmitFormButton.isEnabled = false

            val descriptionInput = binding.editBikeDescription.text.toString()
            val combinationInput = binding.editBikeLockCombination.text.toString()

            //check if there's a string in description
            if (descriptionInput.isEmpty()){
                binding.editBikeDescription.hint = "Add Description"
                binding.editBikeDescription.setHintTextColor(Color.RED)
                missingFields = true
            }

            // check if there's a string in combination
            if (combinationInput.isEmpty()){
                binding.editBikeLockCombination.hint = "Add Combination"
                binding.editBikeLockCombination.setHintTextColor(Color.RED)
                missingFields = true

            }
            if (!missingFields){
                if (newImage){
                    updateBikeAndImage(userID)
                }
            }



            Log.i(TAG, "$descriptionInput $combinationInput")
            binding.editSubmitFormButton.isEnabled = true
            binding.editProgressBar.visibility = View.GONE

        }

        binding.editIvExitCamera.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
        }
    }


    private fun addDataToForm(bike: Bike?) {
        if (bike != null) {
            binding.editBikeDescription.setText(bike.description)
            binding.editBikeLockCombination.setText(bike.combination)
            val location = "Location: ${bike.latitude}, ${bike.longitude}"
            binding.editBikeLocation.setText(location)
            if (!bike.imagePath.isNullOrEmpty()){
                Glide.with(baseContext)
                    .load(bike.imagePath)
                    .into(binding.editBikeImage)
            }else{
                Glide.with(baseContext)
                    .load(R.drawable.click_to_take_photo)
                    .into(binding.editBikeImage)
            }
        }
    }


    private fun createTagChip(baseContext: Context?, tagString: String): Chip {
        val chip = ChipBinding.inflate(layoutInflater).root
        chip.text = tagString

        Log.i(TAG, bike?.tag.toString())
        if (bike?.tag?.contains(tagString) == true){
            chip.isChecked = true
            Log.i(TAG, tagString)
        }
        return chip

    }
    private fun openCameraForResult() {
        val intentCameraActivity = Intent(this, CameraActivity::class.java)
        resultLauncherCameraActivity.launch(intentCameraActivity)
    }

    private fun updateBikeAndImage(userID:String){

        val imageRef = firebaseStorage.child("$userID/bikes/${System.currentTimeMillis()}_photo.jpg")
        imageRef.putFile(photoUri!!)
            .continueWithTask { photoUploadTask ->
                imageRef.downloadUrl

            }.continueWith{ downloadUrl ->
                bike?.imagePath = downloadUrl.result.toString()
                updateBike()

            }
    }
    private fun updateBike(){
        bike?.description = binding.editBikeDescription.text.toString()
        bike?.combination = binding.editBikeLockCombination.text.toString()
        bike?.latitude = latitude
        bike?.longitude = longitude

        if (binding.editChipGroup.childCount > 0) {
            editTagList = arrayListOf()
            binding.editChipGroup.children.toList().filter { (it as Chip).isChecked }.forEach {
                editTagList?.add((it as Chip).text.toString())
            }
        }
        bike?.tag = editTagList

        db.collection("bikes").document(bike?.documentId.toString())
                //todo update only the information changed and not the whole bike
            .update(
                mapOf(
                    "description" to bike?.description,
                    "combination" to bike?.combination,
                    "image_path" to bike?.imagePath,
                    "latitude" to bike?.latitude,
                    "longitude" to bike?.longitude,
                    )).addOnSuccessListener {

                (applicationContext as ApplicationContext).userBikeList?.forEachIndexed { index, bikeItem ->
                    if (bikeItem?.ownerId == bike?.ownerId) {
                        (applicationContext as ApplicationContext).userBikeList?.get(index)?.imagePath = bike?.imagePath
                        (applicationContext as ApplicationContext).userBikeList?.get(index)?.combination = bike?.combination
                        (applicationContext as ApplicationContext).userBikeList?.get(index)?.latitude = bike?.latitude
                        (applicationContext as ApplicationContext).userBikeList?.get(index)?.longitude = bike?.longitude
                        (applicationContext as ApplicationContext).userBikeList?.get(index)?.description = bike?.description
                        (applicationContext as ApplicationContext).userBikeList?.get(index)?.tag = bike?.tag
                    }
                }
                //  reset the input
                binding.editBikeDescription.hint = "Description"
                binding.editBikeLockCombination.hint = "Combination"


                binding.editSubmitFormButton.isEnabled = true
                binding.editProgressBar.visibility = View.INVISIBLE

                startActivity(Intent(this, MainActivity::class.java))
                finish()


            }.addOnFailureListener { e->
                Log.e(TAG, e.message.toString())
                binding.editSubmitFormButton.isEnabled = true
                binding.editProgressBar.visibility = View.GONE
            }
    }


}