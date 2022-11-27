package com.example.bikekollective

import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.RatingBar
import android.widget.RatingBar.OnRatingBarChangeListener
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import android.os.Build.VERSION.SDK_INT
import androidx.appcompat.app.AlertDialog
import com.example.bikekollective.databinding.ActivityBikeProfileBinding
import com.example.bikekollective.databinding.ActivityReturnBikeBinding
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.example.bikekollective.models.Bike
import com.example.bikekollective.models.User
import com.example.bikekollective.models.Ride

class ReturnBikeActivity : AppCompatActivity() {
    private var bike: Bike? = null
    var user: User? = null
    private val db = Firebase.firestore
    private lateinit var auth: FirebaseAuth
    private var ratingTotal: Double = 0.0
    private var averageRating: Double = 0.0
    private var numberOfRatings = 0
    lateinit var binding:ActivityReturnBikeBinding


    companion object {
        private const val TAG = "ApplicationContext"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        Log.v("ApplicationContext", "HERE")
        super.onCreate(savedInstanceState)

        binding = ActivityReturnBikeBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        auth = Firebase.auth

        val errorMessage = AlertDialog.Builder(this)
            .setTitle("Error")
            .setMessage("Please try returning bike at another time")
            .setPositiveButton("OK"
            ) { _, _ -> }

        db.collection("users").document(auth.currentUser?.uid.toString()).get()
            .addOnSuccessListener { snapshot ->
                user = snapshot.toObject(User::class.java)
                Log.i(TAG, user.toString())
            }

        binding.ratingBar.numStars = 5

        // Update text view with selected rating
        binding.ratingBar.onRatingBarChangeListener =
            OnRatingBarChangeListener { _, rating, _ ->
                binding.rating.text = "" + rating.toInt() + "/5"
            }

        binding.buttonCancel.setOnClickListener {
            finish()
        }
        binding.buttonRate.setOnClickListener {
            binding.buttonRate.isEnabled = false

            db.collection("bikes").document(user?.borrowedBike.toString()).get()
                .addOnSuccessListener { snapshot ->
                    bike = snapshot.toObject(Bike::class.java)
                    numberOfRatings = bike?.numberOfRatings?.plus(1)!!
                    ratingTotal = bike?.ratingTotal?.plus(binding.ratingBar.rating.toDouble())!!
                    averageRating = ratingTotal / numberOfRatings

                    // update ride
                    db.collection("rides").document(user?.ride.toString()).update(
                        mapOf(
                            "is_active" to false,
                            "rating" to binding.ratingBar.rating.toInt()
                        )
                    ).addOnSuccessListener {
                        Log.i(TAG, "successfully added ride")
                        // Show toast with given number of stars
                        Toast.makeText(
                            this@ReturnBikeActivity, "Added rating of " +
                                    binding.ratingBar.rating.toInt() + " stars.", Toast.LENGTH_SHORT
                        ).show()

                        db.collection("users").document(auth.currentUser?.uid.toString()).update(
                            mapOf(
                                "borrowed_bike" to null,
                                "ride" to null
                            )
                        )

                        db.collection("bikes").document(bike?.documentId.toString()).update(
                            mapOf(
                                "rating_total" to ratingTotal,
                                "number_of_ratings" to numberOfRatings,
                                "average_rating" to averageRating,
                                "is_available" to true,
                            )
                        ).addOnSuccessListener {

                            binding.buttonRate.isEnabled = true
                            startActivity(Intent(this, MainActivity::class.java))
                            finish()

                        }.addOnFailureListener {
                            Log.e(TAG, "failed to added ride")
                            errorMessage.show()
                            finish()
                        }

                    }
                }


            binding.buttonRate.isEnabled = true
        }
        }
    }