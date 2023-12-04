package com.example.avianenthusiasts

import android.location.Location
import java.util.UUID

class Bird(var species: String, var comment: String, var location: Location? = null, val uuid: UUID = UUID.randomUUID()) {
}