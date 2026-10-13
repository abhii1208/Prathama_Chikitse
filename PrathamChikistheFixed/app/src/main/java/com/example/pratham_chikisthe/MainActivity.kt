package com.example.pratham_chikisthe

import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.core.content.ContextCompat
import com.example.pratham_chikisthe.services.EmergencyCallManager
import com.example.pratham_chikisthe.services.FlashlightManager
import com.example.pratham_chikisthe.services.TtsManager
import com.example.pratham_chikisthe.services.WhistleManager
import com.example.pratham_chikisthe.ui.navigation.AppNavigation
import com.example.pratham_chikisthe.ui.theme.PrathamChikistheTheme
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices

class MainActivity : ComponentActivity() {

    private lateinit var preferenceManager: PreferenceManager
    private lateinit var ttsManager: TtsManager
    private lateinit var flashlightManager: FlashlightManager
    private lateinit var whistleManager: WhistleManager
    private lateinit var callManager: EmergencyCallManager
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        preferenceManager = PreferenceManager(this)
        ttsManager = TtsManager(this) { success ->
            Log.d("MainActivity", "TTS Init status: $success")
        }
        flashlightManager = FlashlightManager(this)
        whistleManager = WhistleManager()
        callManager = EmergencyCallManager(this)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        setContent {
            var userLat by remember { mutableStateOf<Double?>(null) }
            var userLng by remember { mutableStateOf<Double?>(null) }

            val locationPermissionLauncher = rememberLauncherForActivityResult(
                contract = ActivityResultContracts.RequestMultiplePermissions()
            ) { permissions ->
                if (permissions[Manifest.permission.ACCESS_FINE_LOCATION] == true ||
                    permissions[Manifest.permission.ACCESS_COARSE_LOCATION] == true
                ) {
                    fetchLastLocation { lat, lng ->
                        userLat = lat
                        userLng = lng
                    }
                }
            }

            PrathamChikistheTheme {
                AppNavigation(
                    preferenceManager = preferenceManager,
                    ttsManager = ttsManager,
                    flashlightManager = flashlightManager,
                    whistleManager = whistleManager,
                    callManager = callManager,
                    userLat = userLat,
                    userLng = userLng,
                    onRequestLocation = {
                        val hasFine = ContextCompat.checkSelfPermission(
                            this, Manifest.permission.ACCESS_FINE_LOCATION
                        ) == PackageManager.PERMISSION_GRANTED
                        val hasCoarse = ContextCompat.checkSelfPermission(
                            this, Manifest.permission.ACCESS_COARSE_LOCATION
                        ) == PackageManager.PERMISSION_GRANTED

                        if (hasFine || hasCoarse) {
                            fetchLastLocation { lat, lng ->
                                userLat = lat
                                userLng = lng
                            }
                        } else {
                            locationPermissionLauncher.launch(
                                arrayOf(
                                    Manifest.permission.ACCESS_FINE_LOCATION,
                                    Manifest.permission.ACCESS_COARSE_LOCATION
                                )
                            )
                        }
                    }
                )
            }
        }
    }

    private fun fetchLastLocation(onLocationResult: (Double, Double) -> Unit) {
        try {
            if (ContextCompat.checkSelfPermission(
                    this, Manifest.permission.ACCESS_FINE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(
                    this, Manifest.permission.ACCESS_COARSE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
                    if (location != null) {
                        onLocationResult(location.latitude, location.longitude)
                    }
                }
            }
        } catch (e: Exception) {
            Log.e("MainActivity", "Location fetch error: ${e.message}")
        }
    }

    override fun onStop() {
        whistleManager.stopWhistle()
        ttsManager.stop()
        super.onStop()
    }

    override fun onDestroy() {
        ttsManager.shutdown()
        whistleManager.stopWhistle()
        super.onDestroy()
    }
}
