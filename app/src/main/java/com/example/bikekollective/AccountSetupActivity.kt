package com.example.bikekollective

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import com.example.bikekollective.databinding.ActivityAccountSetupBinding
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.PropertyName
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class AccountSetupActivity : AppCompatActivity() {
    companion object {
        private val TAG = "AccountSetupActivity"
    }
    private lateinit var binding: ActivityAccountSetupBinding
    private var db = Firebase.firestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityAccountSetupBinding.inflate(layoutInflater)
        setContentView(binding.root)


        val auth = Firebase.auth
        val currUser = auth.currentUser

        val userData = hashMapOf<String, Any?>(
            "email" to currUser?.email.toString(),
            "display_name" to currUser?.displayName.toString(),
            "is_flagged" to false,
            "borrowed_bike" to null,
            "ride" to null
        )
        db.collection("users").document(currUser?.uid.toString())
            .set(userData).addOnCompleteListener {
                Log.i(TAG, "user data created")
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            }




    }


}