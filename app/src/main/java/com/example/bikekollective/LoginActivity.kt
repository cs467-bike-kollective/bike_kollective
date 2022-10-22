package com.example.bikekollective

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.bikekollective.databinding.ActivityLoginBinding
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract
import com.firebase.ui.auth.data.model.FirebaseAuthUIAuthenticationResult
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.Identity
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
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


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // initialize Firebase Auth
        auth = Firebase.auth

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
        if (result.resultCode == RESULT_OK) {
            Log.w(TAG, "Result ok")
            // Successfully signed in
            val user = FirebaseAuth.getInstance().currentUser
            Log.w(TAG, user?.metadata.toString())

            val signInIntent = Intent(this, SignUpWaiverActivity::class.java)
            startActivity(signInIntent)
            if (result?.idpResponse?.isNewUser == true){
                Log.w(TAG, "new user")
//                direct user to sign up user
            }else{
                //check if user exists in db, if not then user needs to sign waiver

                //send user to home page
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





}

//override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//    super.onActivityResult(requestCode, resultCode, data)
//
//    when (requestCode) {
//        REQ_ONE_TAP -> {
//            try {
//                val credential = oneTapClient.getSignInCredentialFromIntent(data)
//                val idToken = credential.googleIdToken
//                when {
//                    idToken != null -> {
//                        // Got an ID token from Google. Use it to authenticate
//                        // with Firebase.
//                        Log.d(TAG, "Got ID token.")
//                    }
//                    else -> {
//                        // Shouldn't happen.
//                        Log.d(TAG, "No ID token!")
//                    }
//                }
//            } catch (e: ApiException) {
//                // ...
//            }
//        }
//    }