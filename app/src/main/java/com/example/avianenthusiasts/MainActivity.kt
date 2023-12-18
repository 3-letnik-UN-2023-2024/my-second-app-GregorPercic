package com.example.avianenthusiasts

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

import android.Manifest
import android.content.Context
import android.location.LocationListener
import android.location.LocationManager

import com.example.avianenthusiasts.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    lateinit var app: MyApplication
    private lateinit var binding: ActivityMainBinding
    var getBird =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val data: Intent? = result.data

                getCurrentLocation()

                val newBird = Bird(
                    species = data?.getStringExtra("species")!!,
                    comment = data.getStringExtra("comment")!!,
                    imageUri = data.getStringExtra("imageUri") ?: "",
                    latitude = lastKnownLatitude ?: 0.0,
                    longitude = lastKnownLongitude ?: 0.0
                )

                Toast.makeText(this, "${newBird.latitude}, ${newBird.longitude}", Toast.LENGTH_SHORT).show()

                app.loadFromFile()
                app.addItem(newBird)
            }
        }

    private lateinit var locationManager: LocationManager
    private var locationListener: LocationListener? = null

    private var lastKnownLatitude: Double? = null
    private var lastKnownLongitude: Double? = null

    companion object {
        private const val LOCATION_PERMISSION_REQUEST_CODE = 1
    }

    private fun getCurrentLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
            && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION), LOCATION_PERMISSION_REQUEST_CODE)
            return
        }
        Toast.makeText(this, "made it", Toast.LENGTH_SHORT).show()
        fetchLocation()
    }

    private fun fetchLocation() {
        locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        locationListener = LocationListener { location ->
            lastKnownLatitude = location.latitude
            lastKnownLongitude = location.longitude
        }
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return
        }
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0L, 0f, locationListener!!)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                Toast.makeText(this, "made it", Toast.LENGTH_SHORT).show()
                fetchLocation()
            } else {
                Toast.makeText(this, "Location permission denied", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        app = application as MyApplication

        app.generateData()

        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        binding.buttonExit.setOnClickListener {
            finish()
        }
        binding.buttonInput.setOnClickListener {
            val intent = Intent(this, InputActivity::class.java)
            getBird.launch(intent)
        }
        binding.buttonList.setOnClickListener {
            val intent = Intent(this, ListActivity::class.java)
            startActivity(intent)
        }

        getCurrentLocation()
    }
}