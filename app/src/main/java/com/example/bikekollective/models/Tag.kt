package com.example.bikekollective.models

import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.Exclude

data class Tag (
    @Exclude @DocumentId val documentId: String? = "",
    var name: String? = null
)