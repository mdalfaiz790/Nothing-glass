package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.ui.MainViewModel
import com.example.ui.screens.HomeScreen
import com.example.ui.theme.MyApplicationTheme

class MainActivity : ComponentActivity() {

  private val viewModel: MainViewModel by viewModels()

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    enableEdgeToEdge()
    setContent {
      MyApplicationTheme {
        val apps by viewModel.apps.collectAsStateWithLifecycle()
        val settings by viewModel.settings.collectAsStateWithLifecycle()
        val weather by viewModel.weather.collectAsStateWithLifecycle()
        val battery by viewModel.battery.collectAsStateWithLifecycle()
        val isTorchOn by viewModel.isTorchOn.collectAsStateWithLifecycle()
        val isWifiOn by viewModel.isWifiOn.collectAsStateWithLifecycle()
        val isSilent by viewModel.isSilent.collectAsStateWithLifecycle()
        val isDndOn by viewModel.isDndOn.collectAsStateWithLifecycle()

        Surface(
          modifier = Modifier.fillMaxSize(),
          color = Color.Black
        ) {
          HomeScreen(
            apps = apps,
            settings = settings,
            weather = weather,
            battery = battery,
            isTorchOn = isTorchOn,
            isWifiOn = isWifiOn,
            isSilent = isSilent,
            isDndOn = isDndOn,
            onLaunchApp = { viewModel.launchApp(it) },
            onToggleFavorite = { viewModel.toggleFavorite(it) },
            onTogglePinDock = { viewModel.togglePinDock(it) },
            onToggleTorch = { viewModel.toggleTorch() },
            onToggleWifi = { viewModel.toggleWifi() },
            onToggleSilent = { viewModel.toggleSilent() },
            onToggleDnd = { viewModel.toggleDnd() },
            onUpdateWallpaper = { viewModel.updateWallpaper(it) },
            onUpdateGlassOpacity = { viewModel.updateGlassOpacity(it) },
            onUpdateIconStyle = { viewModel.updateIconStyle(it) },
            onUpdateGridColumns = { viewModel.updateGridColumns(it) },
            onToggleWidget = { key, enabled -> viewModel.toggleWidget(key, enabled) },
            onSaveNotes = { viewModel.saveNotes(it) }
          )
        }
      }
    }
  }

  override fun onResume() {
    super.onResume()
    viewModel.loadApps()
  }
}
