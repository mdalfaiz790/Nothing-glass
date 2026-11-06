package com.example.model

import android.graphics.drawable.Drawable
import androidx.compose.ui.graphics.vector.ImageVector

enum class AppCategory(val title: String) {
  ALL("All"),
  FAVORITES("Favorites"),
  TOOLS("Tools"),
  SOCIAL("Social"),
  MEDIA("Media"),
  GAMES("Games"),
  SYSTEM("System")
}

data class AppItem(
  val packageName: String,
  val activityName: String = "",
  val label: String,
  val iconDrawable: Drawable? = null,
  val fallbackIcon: ImageVector? = null,
  val category: AppCategory = AppCategory.TOOLS,
  val isFavorite: Boolean = false,
  val isPinnedToDock: Boolean = false,
  val isSystemApp: Boolean = false
)

enum class WallpaperType(val title: String, val description: String) {
  GLASS_DARK("Onyx Glass", "Frosted glass curve with dot matrix"),
  GLASS_GLYPH("Glyph Neon", "Translucent ribbon with crimson glyph dot"),
  FROSTED_MESH("Frosted Mesh", "Deep noir gradient with glass sheen"),
  PURE_BLACK("AMOLED Black", "Battery saving pitch black minimal")
}

enum class IconStyle(val title: String) {
  NOTHING_MONO("Nothing Mono"),
  NEON_GLASS("Neon Glass"),
  ORIGINAL("Original")
}

data class WeatherInfo(
  val location: String = "London, UK",
  val temperature: Int = 21,
  val condition: String = "Clear Glass",
  val humidity: Int = 54,
  val windSpeed: String = "12 km/h",
  val forecast: List<HourlyForecast> = listOf(
    HourlyForecast("12:00", 21, "Sunny"),
    HourlyForecast("15:00", 23, "Partly"),
    HourlyForecast("18:00", 20, "Cloudy"),
    HourlyForecast("21:00", 17, "Clear")
  )
)

data class HourlyForecast(
  val time: String,
  val temp: Int,
  val condition: String
)

data class NoteItem(
  val id: String,
  val text: String,
  val isCompleted: Boolean = false
)
