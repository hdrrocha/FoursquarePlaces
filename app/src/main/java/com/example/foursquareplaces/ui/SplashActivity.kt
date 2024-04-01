package com.example.foursquareplaces.ui
import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import androidx.compose.runtime.*
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import com.example.foursquareplaces.R
import com.example.foursquareplaces.ui.SplashActivity.Companion.SPLASH_DURATION_MS

class SplashActivity : ComponentActivity() {
    companion object {
         const val LOCATION_PERMISSION_REQUEST_CODE = 123
         const val SPLASH_DURATION_MS = 4000L
    }
    private var locationPermissionGranted by mutableStateOf(false)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (hasLocationPermission()) {
            locationPermissionGranted = true
            startMainActivity()
        } else {
            requestLocationPermission()
        }

    }
    private fun hasLocationPermission(): Boolean {
        return ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
    }
    private fun requestLocationPermission() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
            LOCATION_PERMISSION_REQUEST_CODE
        )
    }
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            locationPermissionGranted =
                grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED
            if (locationPermissionGranted) {
                startMainActivity()
            } else {
               finish()
            }
        }
    }
    private fun startMainActivity() {
        setContent {
            SplashScreen()
        }
        lifecycleScope.launch {
            delay(SPLASH_DURATION_MS)
            if (locationPermissionGranted) {
                val intent = Intent(this@SplashActivity, MainActivity::class.java)
                startActivity(intent)
            }
            finish()
        }
    }

}
@SuppressLint("RememberReturnType")
@Composable
fun SplashScreen() {
    val imageVisible = remember { mutableStateOf(false) }

    val fadeInAlpha = remember { Animatable(0f) }

    LaunchedEffect(Unit) {
        fadeInAlpha.animateTo(
            targetValue = 1f,
            animationSpec = tween(durationMillis = 1000)
        )
    }
    LaunchedEffect(Unit) {
        delay(SPLASH_DURATION_MS - 1000)
        imageVisible.value = true
    }

    val rotationAngle by animateFloatAsState(
        targetValue = if (imageVisible.value) 360f else 0f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1000),
            repeatMode = RepeatMode.Restart
        )
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(colorResource(id = R.color.background_button_dark)),
        contentAlignment = Alignment.Center
    ) {
        if (imageVisible.value) {
            Image(
                painter = painterResource(id = R.drawable.is_splash),
                contentDescription = "Splash Image",
                modifier = Modifier
                    .padding(16.dp)
                    .size(250.dp)
                    .alpha(fadeInAlpha.value)
                    .rotate(rotationAngle)
            )
        }
    }
}

