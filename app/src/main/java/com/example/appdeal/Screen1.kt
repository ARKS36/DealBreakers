package com.example.appdeal

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.location.Location
import android.os.Looper
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng


@Composable
fun Screen1() {
    var userLocation by remember { mutableStateOf<LatLng?>(null) }
    var isPermissionGranted by remember { mutableStateOf(false) }
    val context = LocalContext.current
    val fusedLocationClient: FusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(context)
    var selectedDistance by remember { mutableStateOf(5) }
    var expanded by remember { mutableStateOf(false) }
    var zoomLevel by remember { mutableStateOf(10f) }
    val locationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            locationResult.lastLocation?.let { location ->
                userLocation = LatLng(location.latitude, location.longitude)
            }
        }
    }



    @SuppressLint("MissingPermission")
    val locationRequest = com.google.android.gms.location.LocationRequest.Builder(1000).build()

    val requestPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { isGranted: Boolean ->
            isPermissionGranted = isGranted
        }
    )

    LaunchedEffect(key1 = isPermissionGranted) {
        if (isPermissionGranted) {
            if (ContextCompat.checkSelfPermission(
                    context,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED
            ) {
               fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
                    if (location != null) {
                        userLocation = LatLng(location.latitude, location.longitude)
                        fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper())
                    }
                }
            }
        }else {
            fusedLocationClient.removeLocationUpdates(locationCallback)
            if (isPermissionGranted) {
                fusedLocationClient.requestLocationUpdates(
                    locationRequest,
                    locationCallback,
                    Looper.getMainLooper()
                )
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (!isPermissionGranted) {
            Button(onClick = { requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION) }) {
                Text("Request Location Permission")
            }
        }else if (isPermissionGranted && userLocation != null){
            Text(text = "This is Screen 1")
            Spacer(modifier = Modifier.height(16.dp))
            Box(modifier = Modifier.offset(y= -65.dp)) {
                Button(onClick = { expanded = true }) {
                    Text(text = "$selectedDistance Miles")
                }
                DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                    listOf(5, 10, 25, 50).forEach { distance ->
                        DropdownMenuItem(text = { Text("$distance Miles") }, onClick = {
                            selectedDistance = distance
                            expanded = false
                            when (distance) {
                                5 -> zoomLevel = 10f
                                10 -> zoomLevel = 9f
                                25 -> zoomLevel = 8f
                                50 -> zoomLevel = 7f
                            }
                        })
                    }
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            val cameraPositionState = rememberCameraPositionState{
                var position = CameraPosition.fromLatLngZoom(
                    userLocation!!,
                    zoomLevel
                )
            }
            GoogleMap(
                modifier = Modifier.fillMaxSize().offset(y = -65.dp),
                cameraPositionState = cameraPositionState,
            ) {
                userLocation?.let {
                    Marker(state = MarkerState(position = it), title = "You")
                    var circle = Unit
                    circle
                }
            }
        }
    }
}

fun Marker(state: MarkerState, title: String) {


}

class MarkerState(position: LatLng) {

}

fun GoogleMap(modifier: Modifier, cameraPositionState: Unit, function: () -> Unit?) {

}

fun rememberCameraPositionState(function: () -> Unit) {

}

