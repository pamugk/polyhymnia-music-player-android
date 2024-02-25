package com.github.pamugk.polyhymniamusicplayer

import android.Manifest.permission.READ_EXTERNAL_STORAGE
import android.Manifest.permission.READ_MEDIA_AUDIO
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.getValue
import androidx.core.content.ContextCompat
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.github.pamugk.polyhymniamusicplayer.ui.screens.main.MainScreen
import com.github.pamugk.polyhymniamusicplayer.ui.screens.forbidden.ForbiddenScreen
import kotlinx.coroutines.flow.MutableStateFlow

class MainActivity : ComponentActivity() {
    private lateinit var allowedFlow: MutableStateFlow<Boolean>
    private lateinit var requestPermissionLauncher: ActivityResultLauncher<String>

    private val storagePermission: String
        get() =
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU)
                READ_MEDIA_AUDIO
            else READ_EXTERNAL_STORAGE

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        allowedFlow = MutableStateFlow(ContextCompat.checkSelfPermission(this, storagePermission) ==
                PackageManager.PERMISSION_GRANTED)
        this.requestPermissionLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()) {
            allowedFlow.value = it
        }

        setContent {
            MaterialTheme {
                val enoughPermissions by allowedFlow.collectAsStateWithLifecycle()
                if (enoughPermissions) {
                    MainScreen()
                } else {
                    ForbiddenScreen()
                }
            }
        }
    }

    override fun onStart() {
        super.onStart()
        if (!allowedFlow.value) {
            requestPermissionLauncher.launch(storagePermission)
        }
    }

}