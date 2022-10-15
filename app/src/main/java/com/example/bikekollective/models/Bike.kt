package com.example.bikekollective.models

import android.os.Parcelable
import com.google.firebase.firestore.*
import kotlinx.parcelize.Parcelize

@Parcelize
@IgnoreExtraProperties
data class Bike(
    var ownerId: String? = null,
    @get:PropertyName("is_available") @set:PropertyName("is_available")var isAvailable: Boolean? = null,
    var latitude: Double? = null,
    var longitude: Double? = null,
    @get:PropertyName("image_path") @set:PropertyName("image_path") var imagePath: String? = "",
    var description: String? = "",
    var combination: String? = "",
    @get:PropertyName("rating_total") @set:PropertyName("rating_total") var ratingTotal: Double? = null,
    @get:PropertyName("number_of_ratings") @set:PropertyName("number_of_ratings")var numberOfRatings: Int? = null,
    var tags: ArrayList<String>? = null,
    @Exclude @DocumentId val documentId: String? = ""
):Parcelable{
    @Exclude
    fun toMap(): Map<String, Any?> {
        return mapOf(
            "owner_id" to ownerId,
            "is_available" to isAvailable,
            "latitude" to latitude,
            "longitude" to longitude,
            "image_path" to imagePath,
            "description" to description,
            "combination" to combination,
            "rating_total" to ratingTotal,
            "number_of_ratings" to numberOfRatings,
            "tag" to tags

        )
    }
}