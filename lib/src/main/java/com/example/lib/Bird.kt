package com.example.lib

import javax.xml.stream.Location

class Bird(val species: String, val comment: String, var location: Location? = null, val imageUri: String = "") {
}