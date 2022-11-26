package com.example.bikekollective

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
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.example.bikekollective.models.Bike
import com.example.bikekollective.models.User
import com.example.bikekollective.models.Ride

class ReturnBikeActivity : AppCompatActivity() {
    lateinit var ratingBar: RatingBar
    lateinit var button: Button
    lateinit var textView: TextView
    private var bike: Bike? = null
    var user: User? = null
    var ride: Ride? = null
    private val db = Firebase.firestore
    private lateinit var auth: FirebaseAuth
    private var ratingTotal: Double = 0.0
    private var averageRating: Double = 0.0
    private var numberOfRatings = 0


    companion object {
        private const val TAG = "ApplicationContext"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        Log.v("ApplicationContext", "HERE")
        super.onCreate(savedInstanceState)
        auth = Firebase.auth
        setContentView(R.layout.activity_return_bike)
        ratingBar = findViewById(R.id.ratingBar)
        button = findViewById(R.id.button_rate)
        textView = findViewById<TextView>(R.id.rating)
        ratingBar.numStars = 5

        // Update text view with selected rating
        ratingBar.onRatingBarChangeListener =
            OnRatingBarChangeListener { _, rating, _ ->
                textView.text = "" + rating.toInt() + "/5"
            }

        button.setOnClickListener {


            // Grab user information
            db.collection("users").document(auth.currentUser?.uid.toString()).get()
                .addOnSuccessListener { snapshot ->
                    var user = snapshot.toObject(User::class.java)
                    Log.i(TAG, user.toString())
                }

            // Grab bike information
            bike = if (SDK_INT >= 33) {
                intent.getParcelableExtra("bike", Bike::class.java)

            } else {
                intent.getParcelableExtra<Bike>("bike")
            }

            db.collection("bikes").document(bike?.documentId.toString()).get()
                .addOnSuccessListener { snapshot ->
                    bike = snapshot.toObject(Bike::class.java)
                    numberOfRatings = bike?.numberOfRatings?.plus(1)!!
                    ratingTotal = bike?.ratingTotal?.plus(ratingBar.rating.toDouble())!!
                    averageRating = ratingTotal / numberOfRatings
                }

            db.collection("rides").document(ride?.documentId.toString()).update(
                mapOf(
                    "is_active" to false,
                    "rating" to ratingBar.rating.toInt()
                )
            ).addOnSuccessListener {
                Log.i(TAG, "successfully added ride")
                // Show toast with given number of stars
                Toast.makeText(
                    this@ReturnBikeActivity, "Added rating of " +
                            ratingBar.rating.toInt() + " stars.", Toast.LENGTH_SHORT
                ).show()


                db.collection("bikes").document(bike?.documentId.toString()).update(
                    mapOf(
                        "rating_total" to ratingTotal,
                        "number_of_ratings" to numberOfRatings,
                        "average_rating" to averageRating,
                        "is_available" to true,
                    )
                ).addOnSuccessListener {

                    startActivity(Intent(this, MainActivity::class.java))
                    finish()

                }.addOnFailureListener {
                    Log.e(TAG, "failed to added ride")
                }

            }
        }
        }
    }