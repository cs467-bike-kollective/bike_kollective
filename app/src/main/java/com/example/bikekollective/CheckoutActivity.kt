package com.example.bikekollective

import android.content.DialogInterface
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AlertDialog
import com.example.bikekollective.databinding.ActivityBikeProfileBinding
import com.example.bikekollective.databinding.ActivityCheckoutBinding
import com.example.bikekollective.models.Bike
import com.example.bikekollective.models.Ride
import com.google.android.material.dialog.MaterialDialogs
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.StorageReference

class CheckoutActivity : AppCompatActivity() {
    private var bike: Bike? = null
    private lateinit var firebaseStorage: StorageReference
    private lateinit var db : FirebaseFirestore
    private lateinit var auth: FirebaseAuth
    private lateinit var binding: ActivityCheckoutBinding
    private lateinit var comboDialog: AlertDialog.Builder
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCheckoutBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        db = Firebase.firestore
        auth = Firebase.auth

        binding.btnCheckoutCancel.setOnClickListener {
            finish()
        }
        bike = if (Build.VERSION.SDK_INT >= 33) {
            intent.getParcelableExtra("bike", Bike::class.java)

        } else {
            intent.getParcelableExtra<Bike>("bike")
        }

        Log.i("Checkout", bike.toString())
        comboDialog = AlertDialog.Builder(this)
            .setTitle("Bike Successfully Checked out")
            .setMessage("The Bike Combination Lock\n \n ${bike?.combination}\n " +
                    "you can find the combination again on your home page")
            .setPositiveButton("OK") { _, _ ->
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            }

        binding.btnCheckoutAgree.setOnClickListener {
            binding.btnCheckoutAgree.isEnabled = false
            updateUserBikeInfo()

        }
    }

    private fun updateUserBikeInfo() {
        var ride = Ride(
            auth.currentUser?.uid.toString(),
            bike?.documentId.toString(),
            true,
            Timestamp.now(),
            0.0,
        )
        db.collection("rides").add(ride).addOnSuccessListener {
            ride.documentId = it.id
            db.collection("users").document(auth.currentUser?.uid.toString()).update(
                mapOf(
                    "borrowed_bike" to bike?.documentId.toString(),
                    "ride" to it.id
                )
            ).addOnSuccessListener {
                db.collection("bikes").document(bike?.documentId.toString()).update(
                    mapOf(
                        "is_available" to false
                    )
                ).addOnSuccessListener {
                    comboDialog.show()
                    binding.btnCheckoutAgree.isEnabled = true
                }
            }.addOnFailureListener{
                binding.btnCheckoutAgree.isEnabled = true
            }
        }


        binding.btnCheckoutAgree.isEnabled = true
    }
}