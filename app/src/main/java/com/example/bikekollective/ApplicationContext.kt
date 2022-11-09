package com.example.bikekollective

import android.app.Application
import android.util.Log
import com.example.bikekollective.models.Bike
import com.example.bikekollective.models.Tag
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class ApplicationContext: Application() {
    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore
    var userBikeList: MutableList<Bike?>? = null
    var bikeTagList: MutableList<Tag?>? = null

    companion object{
        private const val TAG = "ApplicationContext"
    }
    override fun onCreate() {
        Log.v("ApplicationContext", "HERE")
        super.onCreate()
        FirebaseApp.initializeApp(this)
        db = Firebase.firestore
        auth = Firebase.auth

        queryBikeTags()
    }

    fun queryBikeTags(){
        db.collection("tags").get().addOnSuccessListener { snapshot ->
            bikeTagList = snapshot.toObjects(Tag::class.java)
            Log.i(TAG, bikeTagList.toString())

        }
    }
    fun queryUserBikes(){
        db.collection("bikes").whereEqualTo("owner_id", auth.currentUser?.uid.toString())
            .get().addOnSuccessListener { snapshot ->
                userBikeList = snapshot.toObjects(Bike::class.java)


        }
        Log.i(TAG, "HERE" + userBikeList.toString())

    }
    fun addBike(newBike: Bike){
        userBikeList?.add(newBike)
    }
}

