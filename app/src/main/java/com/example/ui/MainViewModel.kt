package com.example.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.AppRepository
import com.example.data.BatteryStatus
import com.example.data.LauncherPreferences
import com.example.data.LauncherSettings
import com.example.model.AppItem
import com.example.model.IconStyle
import com.example.model.WallpaperType
import com.example.model.WeatherInfo
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class MainViewModel(application: Application) : AndroidViewModel(application) {

  private val repository = AppRepository(application)
  private val preferences = LauncherPreferences(application)

  val settings: StateFlow<LauncherSettings> = preferences.settingsFlow.stateIn(
    scope = viewModelScope,
    started = SharingStarted.WhileSubscribed(5000),
    initialValue = LauncherSettings()
  )

  val battery: StateFlow<BatteryStatus> = repository.observeBattery().stateIn(
    scope = viewModelScope,
    started = SharingStarted.WhileSubscribed(5000),
    initialValue = BatteryStatus(85, false)
  )

  private val _apps = MutableStateFlow<List<AppItem>>(emptyList())
  val apps: StateFlow<List<AppItem>> = _apps.asStateFlow()

  private val _weather = MutableStateFlow(WeatherInfo())
  val weather: StateFlow<WeatherInfo> = _weather.asStateFlow()

  private val _isTorchOn = MutableStateFlow(false)
  val isTorchOn: StateFlow<Boolean> = _isTorchOn.asStateFlow()

  private val _isWifiOn = MutableStateFlow(true)
  val isWifiOn: StateFlow<Boolean> = _isWifiOn.asStateFlow()

  private val _isSilent = MutableStateFlow(false)
  val isSilent: StateFlow<Boolean> = _isSilent.asStateFlow()

  private val _isDndOn = MutableStateFlow(false)
  val isDndOn: StateFlow<Boolean> = _isDndOn.asStateFlow()

  init {
    loadApps()
  }

  fun loadApps() {
    viewModelScope.launch {
      _apps.value = repository.getInstalledApps()
    }
  }

  fun launchApp(app: AppItem) {
    repository.launchApp(app.packageName)
  }

  fun toggleTorch() {
    val newState = repository.toggleTorch()
    _isTorchOn.value = newState
  }

  fun toggleWifi() {
    _isWifiOn.value = !_isWifiOn.value
  }

  fun toggleSilent() {
    _isSilent.value = !_isSilent.value
  }

  fun toggleDnd() {
    _isDndOn.value = !_isDndOn.value
  }

  fun toggleFavorite(app: AppItem) {
    val current = _apps.value
    _apps.value = current.map {
      if (it.packageName == app.packageName) it.copy(isFavorite = !it.isFavorite) else it
    }
  }

  fun togglePinDock(app: AppItem) {
    val current = _apps.value
    _apps.value = current.map {
      if (it.packageName == app.packageName) it.copy(isPinnedToDock = !it.isPinnedToDock) else it
    }
  }

  fun updateWallpaper(type: WallpaperType) {
    viewModelScope.launch { preferences.updateWallpaper(type) }
  }

  fun updateGlassOpacity(opacity: Float) {
    viewModelScope.launch { preferences.updateGlassOpacity(opacity) }
  }

  fun updateIconStyle(style: IconStyle) {
    viewModelScope.launch { preferences.updateIconStyle(style) }
  }

  fun updateGridColumns(cols: Int) {
    viewModelScope.launch { preferences.updateGridColumns(cols) }
  }

  fun toggleWidget(widgetKey: String, enabled: Boolean) {
    viewModelScope.launch { preferences.toggleWidget(widgetKey, enabled) }
  }

  fun saveNotes(notes: String) {
    viewModelScope.launch { preferences.updateNotesText(notes) }
  }
}
