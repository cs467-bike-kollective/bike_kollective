package com.example.bikekollective.models

import android.os.Parcelable
import com.google.firebase.firestore.IgnoreExtraProperties
import com.google.firebase.firestore.PropertyName
import kotlinx.android.parcel.Parcelize

@IgnoreExtraProperties
@Parcelize
data class User(
    var email: String? = "",
    @get:PropertyName("display_name") @set:PropertyName("display_name") var displayName: String? = "",
    @get:PropertyName("is_flagged") @set:PropertyName("is_flagged") var isFlagged: Boolean? = null,
    //id of bike the current user is using
    @get:PropertyName("display_name") @set:PropertyName("display_name") var borrowedBike:String? = null
):Parcelable

