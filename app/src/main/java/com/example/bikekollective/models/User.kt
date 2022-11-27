package com.example.bikekollective.models

import com.google.firebase.firestore.PropertyName

data class User(
    var email: String? = "",
    @get:PropertyName("display_name") @set:PropertyName("display_name") var displayName: String? = "",
    @get:PropertyName("is_flagged") @set:PropertyName("is_flagged") var isFlagged: Boolean? = null,
    @get:PropertyName("borrowed_bike") @set:PropertyName("borrowed_bike") var borrowedBike: String? = null,
    var ride:String? = null

)

