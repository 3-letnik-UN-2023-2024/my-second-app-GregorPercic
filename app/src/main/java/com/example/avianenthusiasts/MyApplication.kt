package com.example.avianenthusiasts

import android.app.Application
import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import io.github.serpro69.kfaker.Faker
import java.io.File
import java.util.Locale
import java.util.UUID

class MyApplication : Application() {
    val birdList = mutableListOf<Bird>()

    var uuid: String? = null
    private val PREFS_NAME = "MyPrefs"
    private val UUID_KEY = "uuid"
    private val gson = Gson()

    override fun onCreate() {
        super.onCreate()
        initializeUUID()
    }

    private fun initializeUUID() {
        val sharedPrefs = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        uuid = sharedPrefs.getString(UUID_KEY, null)
        if (uuid == null) {
            uuid = UUID.randomUUID().toString()
            sharedPrefs.edit().putString(UUID_KEY, uuid).apply()
        }
    }

    fun saveToFile() {
        val jsonData = gson.toJson(birdList)
        val file = File(filesDir, "birds.json")
        file.writeText(jsonData)
    }

    fun loadFromFile() {
        val file = File(filesDir, "birds.json")
        if (file.exists()) {
            val jsonData = file.readText()
            val type = object : TypeToken<List<Bird>>() {}.type
            val subjectsList: List<Bird> = gson.fromJson(jsonData, type)
            birdList.clear()
            birdList.addAll(subjectsList)
        }
    }

    fun addItem(bird: Bird) {
        birdList.add(bird)
        saveToFile()
    }

    fun updateItem(birdUUID: UUID, species: String, comment: String) {
        val subjectIndex = birdList.indexOfFirst { it.uuid == birdUUID }
        if (subjectIndex != -1) {
            birdList[subjectIndex].species = species
            birdList[subjectIndex].comment = comment
            saveToFile()
        }
    }

    fun deleteItem(birdUUID: UUID) {
        birdList.removeAll { it.uuid == birdUUID }
        saveToFile()
    }

    fun deleteAll() {
        birdList.clear()
        saveToFile()
    }

    fun generateData() {
        deleteAll()
        val faker = Faker()
        for (i in 0..< 100) {
            val species = faker.animal.name()
            var sentence = ""
            for (i in 0..<15) {
                sentence += faker.lorem.words() + " "
            }
            val comment = sentence
            addItem(Bird(species, comment))
        }
    }
}

// /data/data/com.example.pora_my_first_app_gregorpercic/shared_prefs/MyPrefs.xml