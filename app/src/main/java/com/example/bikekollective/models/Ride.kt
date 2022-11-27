package com.example.bikekollective.models

import android.os.Parcelable
import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.Exclude
import com.google.firebase.firestore.IgnoreExtraProperties
import com.google.firebase.firestore.PropertyName
import com.google.type.DateTime
import kotlinx.parcelize.Parcelize

@Parcelize
@IgnoreExtraProperties
data class Ride(
    @get:PropertyName("user_id") @set:PropertyName("user_id") var userId:String? = "",
    @get:PropertyName("bike_id") @set:PropertyName("bike_id") var bikeId:String? = "",
    @get:PropertyName("is_active") @set:PropertyName("is_active") var isActive:Boolean? = true,
    @get:PropertyName("rented_at") @set:PropertyName("rented_at") var rentedAt:Timestamp? = null,
    var rating: Double? = null,
    @Exclude @DocumentId var documentId: String? = ""

): Parcelable {
    @Exclude
    fun toMap(): Map<String, Any?> {
        return mapOf(
            "user_id" to userId,
            "bike_id" to bikeId,
            "is_active" to isActive,
            "rented_at" to rentedAt,
            "rating" to rating
        )
    }
}