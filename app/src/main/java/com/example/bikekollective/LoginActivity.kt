package com.example.bikekollective

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.bikekollective.databinding.ActivityLoginBinding
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract
import com.firebase.ui.auth.data.model.FirebaseAuthUIAuthenticationResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class LoginActivity : AppCompatActivity() {
    companion object {
        private val TAG = "LoginActivity"
    }
    private lateinit var binding: ActivityLoginBinding
    private lateinit var auth: FirebaseAuth


    private val providers = arrayListOf(
        AuthUI.IdpConfig.GoogleBuilder().build())

    private val signInLauncher = registerForActivityResult(
        FirebaseAuthUIActivityResultContract()
    ) { res ->
        this.onSignInResult(res)
    }

    @SuppressLint("RestrictedApi")
    override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        // initialize Firebase Auth
        val user = AuthUI.getInstance().auth.currentUser

        updateUI(user)

    }
    @SuppressLint("RestrictedApi")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // initialize Firebase Auth
        auth = AuthUI.getInstance().auth

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)



        // Create and launch sign-in intent
        val signInIntent = AuthUI.getInstance()
            .createSignInIntentBuilder()
            .setAvailableProviders(providers)
            .build()

        binding.loginButton.setOnClickListener{
            signInLauncher.launch(signInIntent)
        }


}

    private fun onSignInResult(result: FirebaseAuthUIAuthenticationResult) {
        val response = result.idpResponse
        val auth = Firebase.auth
        if (result.resultCode == RESULT_OK) {
            Log.w(TAG, "Result ok")
            // Successfully signed in


            if (result?.idpResponse?.isNewUser == true){
                Log.w(TAG, "new user")
                // direct user to sign up user
                toSignWaiverActivity()
                finish()

            }else{

                val user = auth.currentUser
                //temp to sign in waiver
                Log.i(TAG, "not new")
                updateUI(user)
            }
            // ...
        } else {
            // Sign in failed. If response is null the user canceled the
            // sign-in flow using the back button. Otherwise check
            // response.getError().getErrorCode() and handle the error.
            // ...
            Log.w(TAG, "Result Error")
        }
    }

    private fun updateUI(user: FirebaseUser?) {
        if(user==null){
            Log.i(TAG, "null user!")
            return
        }

        startActivity(Intent(this, MainActivity::class.java))
        finish()


    }

    private fun toSignWaiverActivity() {
        val signInIntent = Intent(this, SignUpWaiverActivity::class.java)
        startActivity(signInIntent)
    }
//


}

