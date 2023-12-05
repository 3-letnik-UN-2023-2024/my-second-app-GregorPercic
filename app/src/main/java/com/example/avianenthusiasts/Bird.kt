package com.example.avianenthusiasts

import android.location.Location
import android.net.Uri
import java.util.UUID

class Bird(var species: String, var comment: String, var location: Location? = null, val uuid: UUID = UUID.randomUUID(),  val imageUri: String = "") {
}