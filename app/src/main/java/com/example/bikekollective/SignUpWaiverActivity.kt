package com.example.bikekollective

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import com.example.bikekollective.databinding.ActivitySignUpWaiverBinding
import com.firebase.ui.auth.AuthUI
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class SignUpWaiverActivity : AppCompatActivity() {
    companion object {
        private const val TAG = "SignUpWaiverActivity"
    }
    private lateinit var binding: ActivitySignUpWaiverBinding
    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = Firebase.auth

        binding = ActivitySignUpWaiverBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val cancelDialog = AlertDialog.Builder(this)
            .setTitle("Are you sure you want to cancel?")
            .setPositiveButton("Yes",
                // _, _ is used to indicate they're unused
                DialogInterface.OnClickListener { _, _ ->
                    deleteUserLogout()
                })
            .setNegativeButton("No",
                DialogInterface.OnClickListener { _, _ ->
                })
            .create()

        binding.btnSignupAgree.setOnClickListener {
            startActivity(Intent(this, AccountSetupActivity::class.java))
            finish()
        }

        binding.btnSignupDisagree.setOnClickListener {
            cancelDialog.show()

        }


    }
    private fun deleteUserLogout(){
        AuthUI.getInstance()
            .delete(this)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d(TAG, "User account deleted.")
                    val logoutIntent = Intent(this, LoginActivity::class.java)
                    logoutIntent.flags =
                        Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                    startActivity(logoutIntent)
                    finish()
                }
            }
    }

}