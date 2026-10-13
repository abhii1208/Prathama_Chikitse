package com.aidassist.aid_assist_flutter

import android.content.Context
import android.hardware.camera2.CameraCharacteristics
import android.hardware.camera2.CameraManager
import android.media.AudioManager
import android.media.ToneGenerator
import android.os.Handler
import android.os.Looper
import android.net.Uri
import org.json.JSONObject
import java.net.HttpURLConnection
import java.net.URL
import io.flutter.embedding.android.FlutterActivity
import io.flutter.embedding.engine.FlutterEngine
import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel

class MainActivity : FlutterActivity() {
    private val channelName = "aid_assist/device"
    private val handler = Handler(Looper.getMainLooper())
    private var whistleRunning = false
    private var toneGenerator: ToneGenerator? = null
    private var burstIndex = 0
    private val whistlePulse = object : Runnable {
        override fun run() {
            if (!whistleRunning) return
            toneGenerator?.startTone(ToneGenerator.TONE_SUP_RINGTONE, 180)
            burstIndex += 1
            handler.postDelayed(this, if (burstIndex % 3 == 0) 800 else 280)
        }
    }

    override fun configureFlutterEngine(flutterEngine: FlutterEngine) {
        super.configureFlutterEngine(flutterEngine)
        MethodChannel(flutterEngine.dartExecutor.binaryMessenger, channelName).setMethodCallHandler { call, result ->
            when (call.method) {
                "setTorch" -> result.success(setTorch(call.argument<Boolean>("enabled") == true))
                "setWhistle" -> { if (call.argument<Boolean>("enabled") == true) startWhistle() else stopWhistle(); result.success(true) }
                "hasGoogleMapsApiKey" -> result.success(BuildConfig.GOOGLE_MAPS_API_KEY.isNotBlank())
                "searchNearbyHospitals" -> searchNearbyHospitals(call, result)
                else -> result.notImplemented()
            }
        }
    }

    private fun searchNearbyHospitals(call: MethodCall, result: MethodChannel.Result) {
        val latitude = call.argument<Double>("latitude")
        val longitude = call.argument<Double>("longitude")
        if (latitude == null || longitude == null || BuildConfig.GOOGLE_MAPS_API_KEY.isBlank()) {
            result.success(emptyList<Map<String, Any?>>())
            return
        }
        Thread {
            val request = runCatching {
                val connection = (URL("https://places.googleapis.com/v1/places:searchNearby")
                    .openConnection() as HttpURLConnection).apply {
                    requestMethod = "POST"
                    connectTimeout = 10000
                    readTimeout = 10000
                    doOutput = true
                    setRequestProperty("Content-Type", "application/json")
                    setRequestProperty(
                        "X-Goog-Api-Key",
                        BuildConfig.GOOGLE_MAPS_API_KEY
                    )
                    setRequestProperty(
                        "X-Goog-FieldMask",
                        "places.displayName,places.formattedAddress,places.location,places.googleMapsUri,places.nationalPhoneNumber,places.currentOpeningHours,places.rating"
                    )
                }
                val body = JSONObject().apply {
                    put("includedTypes", org.json.JSONArray().put("hospital"))
                    put("maxResultCount", 20)
                    put("rankPreference", "DISTANCE")
                    put("locationRestriction", JSONObject().apply {
                        put("circle", JSONObject().apply {
                            put("center", JSONObject().apply {
                                put("latitude", latitude)
                                put("longitude", longitude)
                            })
                            put("radius", 25000.0)
                        })
                    })
                }.toString()
                connection.outputStream.use { it.write(body.toByteArray()) }
                if (connection.responseCode !in 200..299) {
                    throw IllegalStateException("Places request failed")
                }
                val response = connection.inputStream.bufferedReader().use { it.readText() }
                val places = JSONObject(response).optJSONArray("places") ?: return@runCatching emptyList()
                (0 until places.length()).mapNotNull { index ->
                    val place = places.optJSONObject(index) ?: return@mapNotNull null
                    val location = place.optJSONObject("location") ?: return@mapNotNull null
                    val displayName = place.optJSONObject("displayName")?.optString("text").orEmpty()
                    if (displayName.isBlank()) return@mapNotNull null
                    hashMapOf<String, Any?>(
                        "name" to displayName,
                        "address" to place.optString("formattedAddress"),
                        "phone" to place.optString("nationalPhoneNumber"),
                        "latitude" to location.optDouble("latitude"),
                        "longitude" to location.optDouble("longitude"),
                        "mapsUri" to place.optString("googleMapsUri"),
                        "rating" to if (place.has("rating")) place.optDouble("rating") else null,
                        "open" to place.optJSONObject("currentOpeningHours")?.optBoolean("openNow")
                    )
                }
            }
            runOnUiThread {
                request.fold(
                    onSuccess = { result.success(it) },
                    onFailure = {
                        result.error(
                            "PLACES_REQUEST_FAILED",
                            "Unable to load nearby hospitals right now.",
                            null
                        )
                    }
                )
            }
        }.start()
    }

    private fun setTorch(enabled: Boolean): Boolean {
        return try {
            val manager = getSystemService(Context.CAMERA_SERVICE) as CameraManager
            val cameraId = manager.cameraIdList.firstOrNull { id ->
                manager.getCameraCharacteristics(id)
                    .get(CameraCharacteristics.FLASH_INFO_AVAILABLE) == true
            } ?: return false
            manager.setTorchMode(cameraId, enabled)
            true
        } catch (_: Exception) {
            false
        }
    }

    private fun startWhistle() { if (whistleRunning) return; whistleRunning = true; burstIndex = 0; toneGenerator = ToneGenerator(AudioManager.STREAM_ALARM, 100); handler.post(whistlePulse) }
    private fun stopWhistle() { whistleRunning = false; handler.removeCallbacks(whistlePulse); toneGenerator?.release(); toneGenerator = null }
    override fun onStop() { stopWhistle(); super.onStop() }
    override fun onDestroy() { stopWhistle(); super.onDestroy() }
}
