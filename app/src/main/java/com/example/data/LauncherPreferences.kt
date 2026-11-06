package com.example.data

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.floatPreferencesKey
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.model.IconStyle
import com.example.model.WallpaperType
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.launcherDataStore by preferencesDataStore(name = "nothing_launcher_settings")

data class LauncherSettings(
  val wallpaperType: WallpaperType = WallpaperType.GLASS_DARK,
  val glassOpacity: Float = 0.55f,
  val glassBlurIntensity: Float = 0.70f,
  val iconStyle: IconStyle = IconStyle.NOTHING_MONO,
  val gridColumns: Int = 4,
  val showClockWidget: Boolean = true,
  val showWeatherWidget: Boolean = true,
  val showControlsWidget: Boolean = true,
  val showMusicWidget: Boolean = true,
  val showNotesWidget: Boolean = true,
  val showStepWidget: Boolean = true,
  val is24Hour: Boolean = true,
  val notesText: String = "Design review at 3 PM\nExplore Nothing widgets\nEnjoy the smooth glass UI"
)

class LauncherPreferences(private val context: Context) {

  private object Keys {
    val WALLPAPER = stringPreferencesKey("pref_wallpaper")
    val GLASS_OPACITY = floatPreferencesKey("pref_glass_opacity")
    val GLASS_BLUR = floatPreferencesKey("pref_glass_blur")
    val ICON_STYLE = stringPreferencesKey("pref_icon_style")
    val GRID_COLUMNS = intPreferencesKey("pref_grid_columns")
    val SHOW_CLOCK = booleanPreferencesKey("pref_show_clock")
    val SHOW_WEATHER = booleanPreferencesKey("pref_show_weather")
    val SHOW_CONTROLS = booleanPreferencesKey("pref_show_controls")
    val SHOW_MUSIC = booleanPreferencesKey("pref_show_music")
    val SHOW_NOTES = booleanPreferencesKey("pref_show_notes")
    val SHOW_STEPS = booleanPreferencesKey("pref_show_steps")
    val IS_24_HOUR = booleanPreferencesKey("pref_is_24_hour")
    val NOTES_TEXT = stringPreferencesKey("pref_notes_text")
  }

  val settingsFlow: Flow<LauncherSettings> = context.launcherDataStore.data.map { prefs ->
    LauncherSettings(
      wallpaperType = runCatching {
        WallpaperType.valueOf(prefs[Keys.WALLPAPER] ?: WallpaperType.GLASS_DARK.name)
      }.getOrDefault(WallpaperType.GLASS_DARK),
      glassOpacity = prefs[Keys.GLASS_OPACITY] ?: 0.55f,
      glassBlurIntensity = prefs[Keys.GLASS_BLUR] ?: 0.70f,
      iconStyle = runCatching {
        IconStyle.valueOf(prefs[Keys.ICON_STYLE] ?: IconStyle.NOTHING_MONO.name)
      }.getOrDefault(IconStyle.NOTHING_MONO),
      gridColumns = prefs[Keys.GRID_COLUMNS] ?: 4,
      showClockWidget = prefs[Keys.SHOW_CLOCK] ?: true,
      showWeatherWidget = prefs[Keys.SHOW_WEATHER] ?: true,
      showControlsWidget = prefs[Keys.SHOW_CONTROLS] ?: true,
      showMusicWidget = prefs[Keys.SHOW_MUSIC] ?: true,
      showNotesWidget = prefs[Keys.SHOW_NOTES] ?: true,
      showStepWidget = prefs[Keys.SHOW_STEPS] ?: true,
      is24Hour = prefs[Keys.IS_24_HOUR] ?: true,
      notesText = prefs[Keys.NOTES_TEXT] ?: "Design review at 3 PM\nExplore Nothing widgets\nEnjoy the smooth glass UI"
    )
  }

  suspend fun updateWallpaper(type: WallpaperType) {
    context.launcherDataStore.edit { it[Keys.WALLPAPER] = type.name }
  }

  suspend fun updateGlassOpacity(opacity: Float) {
    context.launcherDataStore.edit { it[Keys.GLASS_OPACITY] = opacity }
  }

  suspend fun updateGlassBlur(blur: Float) {
    context.launcherDataStore.edit { it[Keys.GLASS_BLUR] = blur }
  }

  suspend fun updateIconStyle(style: IconStyle) {
    context.launcherDataStore.edit { it[Keys.ICON_STYLE] = style.name }
  }

  suspend fun updateGridColumns(cols: Int) {
    context.launcherDataStore.edit { it[Keys.GRID_COLUMNS] = cols }
  }

  suspend fun toggleWidget(keyName: String, enabled: Boolean) {
    context.launcherDataStore.edit { prefs ->
      when (keyName) {
        "clock" -> prefs[Keys.SHOW_CLOCK] = enabled
        "weather" -> prefs[Keys.SHOW_WEATHER] = enabled
        "controls" -> prefs[Keys.SHOW_CONTROLS] = enabled
        "music" -> prefs[Keys.SHOW_MUSIC] = enabled
        "notes" -> prefs[Keys.SHOW_NOTES] = enabled
        "steps" -> prefs[Keys.SHOW_STEPS] = enabled
      }
    }
  }

  suspend fun updateNotesText(text: String) {
    context.launcherDataStore.edit { it[Keys.NOTES_TEXT] = text }
  }
}
