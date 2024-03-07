package com.github.pamugk.polyhymniamusicplayer

import android.Manifest.permission.READ_EXTERNAL_STORAGE
import android.Manifest.permission.READ_MEDIA_AUDIO
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.core.content.ContextCompat
import com.github.pamugk.polyhymniamusicplayer.data.controller.rememberMediaBrowser
import com.github.pamugk.polyhymniamusicplayer.ui.screens.forbidden.ForbiddenScreen
import com.github.pamugk.polyhymniamusicplayer.ui.screens.main.MainScreen

class MainActivity : ComponentActivity() {

    private val storagePermission: String
        get() =
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU)
                READ_MEDIA_AUDIO
            else READ_EXTERNAL_STORAGE

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            var enoughPermissions by remember {
                mutableStateOf(ContextCompat.checkSelfPermission(
                    this,
                    storagePermission) == PackageManager.PERMISSION_GRANTED)
            }
            val permissionLauncher = rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) {
                enoughPermissions = it
            }
            LaunchedEffect(Unit) {
                permissionLauncher.launch(storagePermission)
            }

            MaterialTheme {
                if (enoughPermissions) {
                    val controller by rememberMediaBrowser()
                    MainScreen(controller)
                } else {
                    ForbiddenScreen()
                }
            }
        }
    }
}