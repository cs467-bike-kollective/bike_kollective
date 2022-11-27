package com.example.bikekollective

import android.content.DialogInterface
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import com.bumptech.glide.Glide
import com.example.bikekollective.databinding.ActivityBikeProfileBinding
import com.example.bikekollective.databinding.ActivityCreateBikeBinding
import com.example.bikekollective.databinding.ActivityLoginBinding
import com.example.bikekollective.models.Bike
import com.example.bikekollective.models.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.StorageReference

class BikeProfileActivity : AppCompatActivity() {
    private var bike: Bike? = null
    private lateinit var db : FirebaseFirestore
    private lateinit var auth: FirebaseAuth
    private lateinit var binding: ActivityBikeProfileBinding
    private var currUser: User? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBikeProfileBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        auth = Firebase.auth
        db = Firebase.firestore


        db.collection("users").document(auth.currentUser!!.uid)
            .get().addOnSuccessListener { snapshot ->
                currUser = snapshot.toObject(User::class.java)

        }
        val bikeBorrowedExists = AlertDialog.Builder(this)
            .setTitle("You currently have a bike borrowed")
            .setMessage("Return your bike before you can borrow another bike")
            .setPositiveButton("OK",
                // _, _ is used to indicate they're unused
                DialogInterface.OnClickListener { _, _ ->
                })
        binding.profileExit.setOnClickListener {
            finish()
        }

        bike = if (Build.VERSION.SDK_INT >= 33) {
            intent.getParcelableExtra("bike", Bike::class.java)

        } else {
            intent.getParcelableExtra<Bike>("bike")
        }

        if (!bike?.imagePath.isNullOrEmpty()) {
            Glide.with(baseContext)
                .load(bike?.imagePath.toString())
                .centerCrop()
                .into(binding.bikeProfileImageView)
        } else {
            Glide.with(baseContext)
                .load(R.drawable.no_image_curr_bike)
                .centerCrop()
                .into(binding.bikeProfileImageView)
        }
        binding.bikeProfileRating.rating = bike?.averageRating?.toFloat() ?: 0.0f
        binding.bikeProfileDescription.text = "Description: ${bike?.description}"
        binding.bikeProfileLocation.text = "Location: ${bike?.latitude}, ${bike?.longitude}"
        var averageRating = String.format("%.1f", bike?.averageRating).toDouble()
        binding.reviewNumber.text = "${averageRating}/5 ( ${bike?.numberOfRatings.toString()} ratings)"
        binding.buttonCheckout.setOnClickListener {
            if (currUser?.borrowedBike.isNullOrEmpty()){
                var toCheckout =  Intent(this,CheckoutActivity::class.java)
                //pass data to intent
                toCheckout.putExtra("bike", bike)
                startActivity(toCheckout)
            }else{
                bikeBorrowedExists.show()

            }

        }
    }
}