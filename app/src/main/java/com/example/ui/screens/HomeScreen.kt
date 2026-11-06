package com.example.ui.screens

import android.content.Intent
import android.net.Uri
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.BatteryStatus
import com.example.data.LauncherSettings
import com.example.model.AppItem
import com.example.model.WallpaperType
import com.example.model.WeatherInfo
import com.example.ui.components.NothingDock
import com.example.ui.components.NothingDotClockWidget
import com.example.ui.components.NothingMusicWidget
import com.example.ui.components.NothingNotesWidget
import com.example.ui.components.NothingQuickControlsWidget
import com.example.ui.components.NothingSearchBar
import com.example.ui.components.NothingStepWidget
import com.example.ui.components.NothingWeatherWidget
import com.example.ui.theme.NothingBlack
import com.example.ui.theme.NothingOffWhite
import com.example.ui.theme.NothingRed
import com.example.ui.theme.NothingWhite
import com.example.ui.theme.SpaceMonoFontFamily

@Composable
fun HomeScreen(
  apps: List<AppItem>,
  settings: LauncherSettings,
  weather: WeatherInfo,
  battery: BatteryStatus,
  isTorchOn: Boolean,
  isWifiOn: Boolean,
  isSilent: Boolean,
  isDndOn: Boolean,
  onLaunchApp: (AppItem) -> Unit,
  onToggleFavorite: (AppItem) -> Unit,
  onTogglePinDock: (AppItem) -> Unit,
  onToggleTorch: () -> Unit,
  onToggleWifi: () -> Unit,
  onToggleSilent: () -> Unit,
  onToggleDnd: () -> Unit,
  onUpdateWallpaper: (WallpaperType) -> Unit,
  onUpdateGlassOpacity: (Float) -> Unit,
  onUpdateIconStyle: (com.example.model.IconStyle) -> Unit,
  onUpdateGridColumns: (Int) -> Unit,
  onToggleWidget: (String, Boolean) -> Unit,
  onSaveNotes: (String) -> Unit
) {
  val context = LocalContext.current
  var isDrawerOpen by remember { mutableStateOf(false) }
  var isSettingsOpen by remember { mutableStateOf(false) }

  val dockApps = remember(apps) {
    val pinned = apps.filter { it.isPinnedToDock }
    if (pinned.isNotEmpty()) pinned.take(4) else apps.take(4)
  }

  Box(modifier = Modifier.fillMaxSize()) {
    // Wallpaper Layer
    when (settings.wallpaperType) {
      WallpaperType.GLASS_DARK -> {
        Image(
          painter = painterResource(id = R.drawable.bg_glass_dark),
          contentDescription = null,
          contentScale = ContentScale.Crop,
          modifier = Modifier.fillMaxSize()
        )
      }
      WallpaperType.GLASS_GLYPH -> {
        Image(
          painter = painterResource(id = R.drawable.bg_glass_glyph),
          contentDescription = null,
          contentScale = ContentScale.Crop,
          modifier = Modifier.fillMaxSize()
        )
      }
      WallpaperType.FROSTED_MESH -> {
        Box(
          modifier = Modifier
            .fillMaxSize()
            .background(
              Brush.radialGradient(
                colors = listOf(Color(0xFF1E2838), Color(0xFF0C1018), NothingBlack),
                radius = 1200f
              )
            )
        )
      }
      WallpaperType.PURE_BLACK -> {
        Box(
          modifier = Modifier
            .fillMaxSize()
            .background(NothingBlack)
        )
      }
    }

    // Subtle dark gradient overlay to ensure widgets and text pop cleanly
    Box(
      modifier = Modifier
        .fillMaxSize()
        .background(
          Brush.verticalGradient(
            colors = listOf(
              Color.Black.copy(alpha = 0.35f),
              Color.Transparent,
              Color.Black.copy(alpha = 0.65f)
            )
          )
        )
    )

    // Main Foreground Layout
    Column(
      modifier = Modifier
        .fillMaxSize()
        .statusBarsPadding()
        .navigationBarsPadding()
    ) {
      // Top Navigation / Header Bar
      Row(
        modifier = Modifier
          .fillMaxWidth()
          .padding(horizontal = 20.dp, vertical = 12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
          Box(
            modifier = Modifier
              .size(10.dp)
              .clip(CircleShape)
              .background(NothingRed)
          )
          Spacer(modifier = Modifier.width(8.dp))
          Text(
            text = "NOTHING (G)",
            fontFamily = SpaceMonoFontFamily,
            fontSize = 15.sp,
            fontWeight = FontWeight.Bold,
            color = NothingWhite,
            letterSpacing = 2.sp
          )
        }

        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
          // Launcher Customization Button
          Box(
            modifier = Modifier
              .size(42.dp)
              .clip(CircleShape)
              .background(Color.White.copy(alpha = 0.12f))
              .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = ripple(color = Color.White),
                onClick = { isSettingsOpen = true }
              )
              .testTag("open_settings_button"),
            contentAlignment = Alignment.Center
          ) {
            Icon(
              imageVector = Icons.Default.Tune,
              contentDescription = "Customize Launcher",
              tint = NothingWhite,
              modifier = Modifier.size(20.dp)
            )
          }
        }
      }

      // Middle: Scrollable Glass Widgets Area
      LazyColumn(
        modifier = Modifier
          .weight(1f)
          .fillMaxWidth(),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 6.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
      ) {
        // Dot Clock Widget
        if (settings.showClockWidget) {
          item(key = "widget_clock") {
            NothingDotClockWidget(
              opacity = settings.glassOpacity,
              is24Hour = settings.is24Hour,
              onClick = { isDrawerOpen = true }
            )
          }
        }

        // Search Bar
        item(key = "widget_search") {
          NothingSearchBar(
            opacity = settings.glassOpacity,
            onClick = { isDrawerOpen = true },
            onMicClick = {
              val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://www.google.com"))
              intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
              runCatching { context.startActivity(intent) }
            }
          )
        }

        // Weather Widget
        if (settings.showWeatherWidget) {
          item(key = "widget_weather") {
            NothingWeatherWidget(
              weather = weather,
              opacity = settings.glassOpacity,
              onClick = { isDrawerOpen = true }
            )
          }
        }

        // Quick Controls Widget
        if (settings.showControlsWidget) {
          item(key = "widget_controls") {
            NothingQuickControlsWidget(
              battery = battery,
              isTorchOn = isTorchOn,
              isWifiOn = isWifiOn,
              isSilent = isSilent,
              isDndOn = isDndOn,
              onToggleTorch = onToggleTorch,
              onToggleWifi = onToggleWifi,
              onToggleSilent = onToggleSilent,
              onToggleDnd = onToggleDnd,
              opacity = settings.glassOpacity
            )
          }
        }

        // Music Player Widget
        if (settings.showMusicWidget) {
          item(key = "widget_music") {
            NothingMusicWidget(
              opacity = settings.glassOpacity
            )
          }
        }

        // Sticky Quick Notes
        if (settings.showNotesWidget) {
          item(key = "widget_notes") {
            NothingNotesWidget(
              initialNotes = settings.notesText,
              onSaveNotes = onSaveNotes,
              opacity = settings.glassOpacity
            )
          }
        }

        // Daily Steps Widget
        if (settings.showStepWidget) {
          item(key = "widget_steps") {
            NothingStepWidget(
              opacity = settings.glassOpacity
            )
          }
        }

        item(key = "bottom_spacer") {
          Spacer(modifier = Modifier.height(10.dp))
        }
      }

      // Bottom Pinned Glass Dock
      NothingDock(
        dockApps = dockApps,
        iconStyle = settings.iconStyle,
        onAppClick = onLaunchApp,
        onOpenDrawer = { isDrawerOpen = true },
        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
        opacity = settings.glassOpacity
      )
    }

    // App Drawer Overlay (Smooth Vertical Slide In/Out)
    AnimatedVisibility(
      visible = isDrawerOpen,
      enter = slideInVertically(
        initialOffsetY = { it },
        animationSpec = tween(320)
      ),
      exit = slideOutVertically(
        targetOffsetY = { it },
        animationSpec = tween(280)
      )
    ) {
      AppDrawerScreen(
        apps = apps,
        iconStyle = settings.iconStyle,
        gridColumns = settings.gridColumns,
        glassOpacity = settings.glassOpacity,
        onLaunchApp = onLaunchApp,
        onToggleFavorite = onToggleFavorite,
        onTogglePinDock = onTogglePinDock,
        onClose = { isDrawerOpen = false }
      )
    }

    // Settings Customization Modal
    if (isSettingsOpen) {
      LauncherSettingsSheet(
        settings = settings,
        onUpdateWallpaper = onUpdateWallpaper,
        onUpdateGlassOpacity = onUpdateGlassOpacity,
        onUpdateIconStyle = onUpdateIconStyle,
        onUpdateGridColumns = onUpdateGridColumns,
        onToggleWidget = onToggleWidget,
        onDismiss = { isSettingsOpen = false }
      )
    }
  }
}
