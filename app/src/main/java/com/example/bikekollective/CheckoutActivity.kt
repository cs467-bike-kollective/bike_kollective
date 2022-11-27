package com.example.bikekollective

import android.content.DialogInterface
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import com.example.bikekollective.databinding.ActivityBikeProfileBinding
import com.example.bikekollective.databinding.ActivityCheckoutBinding
import com.example.bikekollective.models.Bike
import com.google.android.material.dialog.MaterialDialogs
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.StorageReference

class CheckoutActivity : AppCompatActivity() {
    private var bike: Bike? = null
    private lateinit var firebaseStorage: StorageReference
    private lateinit var db : FirebaseFirestore
    private lateinit var auth: FirebaseAuth
    private lateinit var binding: ActivityCheckoutBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCheckoutBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)


        binding.btnCheckoutCancel.setOnClickListener {
            finish()
        }
        bike = if (Build.VERSION.SDK_INT >= 33) {
            intent.getParcelableExtra("bike", Bike::class.java)

        } else {
            intent.getParcelableExtra<Bike>("bike")
        }

        val comboDialog = AlertDialog.Builder(this)
            .setTitle("Bike Successfully Checked out")
            .setMessage("The Bike Combination Lock\n \n ${bike?.combination}\n " +
                    "you can find the combination again on your home page")
            .setPositiveButton("OK",
                // _, _ is used to indicate they're unused
                DialogInterface.OnClickListener { _, _ ->
                    startActivity(Intent(this, MainActivity::class.java))

                })

        binding.btnCheckoutAgree.setOnClickListener {
            updateUserBikeInfo()
            comboDialog.show()

        }
    }

    private fun updateUserBikeInfo() {
        TODO("Not yet implemented")
//        create a ride document

    }
}