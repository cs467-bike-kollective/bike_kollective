package com.example.bikekollective

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.bikekollective.databinding.ActivityLoginBinding
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

    private lateinit var binding: ActivityLoginBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var oneTapClient: SignInClient
    private lateinit var signInRequest: BeginSignInRequest

    private var showOneTapUI = true
    companion object {
        private val TAG = "LoginActivity"
    }

//    https://stackoverflow.com/questions/69212531/how-to-use-google-activity-result-api-in-one-tap-sign-in/

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // initialize Firebase Auth
        auth = Firebase.auth

        // set up oneTapClient for Google Authentication
        oneTapClient = Identity.getSignInClient(this)
        signInRequest = BeginSignInRequest.builder()
            .setPasswordRequestOptions(BeginSignInRequest.PasswordRequestOptions.builder()
                .setSupported(true)
                .build())
            .setGoogleIdTokenRequestOptions(
                BeginSignInRequest.GoogleIdTokenRequestOptions.builder()
                    .setSupported(true)
                    // Your server's client ID, not your Android client ID.
                    .setServerClientId(getString(R.string.default_web_client_id))
                    // Only show accounts previously used to sign in.
                    .setFilterByAuthorizedAccounts(true)
                    .build())
            // Automatically sign in when exactly one credential is retrieved.
            .setAutoSelectEnabled(true)
            .build()

        binding.loginButton.setOnClickListener {
            // Use the Kotlin extension in activity-ktx
            // passing it the Intent you want to start
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