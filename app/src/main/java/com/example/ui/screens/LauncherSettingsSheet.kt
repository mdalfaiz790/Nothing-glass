package com.example.ui.screens

import android.content.Context
import android.content.Intent
import android.provider.Settings
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material.icons.filled.Wallpaper
import androidx.compose.material.icons.filled.Widgets
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.LauncherSettings
import com.example.model.IconStyle
import com.example.model.WallpaperType
import com.example.ui.components.GlassCard
import com.example.ui.theme.GlassCyan
import com.example.ui.theme.NothingGlassBorder
import com.example.ui.theme.NothingMuted
import com.example.ui.theme.NothingOffWhite
import com.example.ui.theme.NothingRed
import com.example.ui.theme.NothingRedSoft
import com.example.ui.theme.NothingWhite
import com.example.ui.theme.OutfitFontFamily
import com.example.ui.theme.SpaceMonoFontFamily

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LauncherSettingsSheet(
  settings: LauncherSettings,
  onUpdateWallpaper: (WallpaperType) -> Unit,
  onUpdateGlassOpacity: (Float) -> Unit,
  onUpdateIconStyle: (IconStyle) -> Unit,
  onUpdateGridColumns: (Int) -> Unit,
  onToggleWidget: (String, Boolean) -> Unit,
  onDismiss: () -> Unit
) {
  val context = LocalContext.current
  val scrollState = rememberScrollState()

  ModalBottomSheet(
    onDismissRequest = onDismiss,
    sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
    containerColor = Color(0xFF101319),
    dragHandle = {
      Box(
        modifier = Modifier
          .padding(vertical = 12.dp)
          .size(width = 40.dp, height = 4.dp)
          .clip(CircleShape)
          .background(NothingGlassBorder)
      )
    }
  ) {
    Column(
      modifier = Modifier
        .fillMaxWidth()
        .verticalScroll(scrollState)
        .padding(horizontal = 20.dp, vertical = 8.dp)
        .navigationBarsPadding()
        .testTag("launcher_settings_sheet")
    ) {
      // Header
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Column {
          Text(
            text = "NOTHING GLASS",
            fontFamily = SpaceMonoFontFamily,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = NothingWhite
          )
          Text(
            text = "Launcher Customization & Engine",
            fontFamily = OutfitFontFamily,
            fontSize = 13.sp,
            color = NothingMuted
          )
        }

        Box(
          modifier = Modifier
            .clip(CircleShape)
            .background(NothingRedSoft)
            .padding(horizontal = 10.dp, vertical = 4.dp)
        ) {
          Text(
            text = "v2.0 GLASS",
            fontFamily = SpaceMonoFontFamily,
            fontSize = 10.sp,
            color = NothingRed,
            fontWeight = FontWeight.Bold
          )
        }
      }

      Spacer(modifier = Modifier.height(16.dp))

      // Button: Set as Default Phone Launcher
      GlassCard(
        modifier = Modifier.fillMaxWidth(),
        opacity = 0.7f,
        hasRedAccent = true,
        onClick = { openDefaultHomeSettings(context) }
      ) {
        Row(
          modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
          verticalAlignment = Alignment.CenterVertically
        ) {
          Box(
            modifier = Modifier
              .size(42.dp)
              .clip(CircleShape)
              .background(NothingRed),
            contentAlignment = Alignment.Center
          ) {
            Icon(
              imageVector = Icons.Default.Home,
              contentDescription = null,
              tint = Color.White,
              modifier = Modifier.size(22.dp)
            )
          }

          Spacer(modifier = Modifier.width(14.dp))

          Column(modifier = Modifier.weight(1f)) {
            Text(
              text = "Set as Default Launcher",
              fontFamily = OutfitFontFamily,
              fontSize = 15.sp,
              fontWeight = FontWeight.Bold,
              color = NothingWhite
            )
            Text(
              text = "Make Nothing Glass your primary home screen on this mobile",
              fontFamily = OutfitFontFamily,
              fontSize = 12.sp,
              color = NothingOffWhite
            )
          }
        }
      }

      Spacer(modifier = Modifier.height(20.dp))

      // Section: Wallpaper Selection
      Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(
          imageVector = Icons.Default.Wallpaper,
          contentDescription = null,
          tint = NothingRed,
          modifier = Modifier.size(16.dp)
        )
        Spacer(modifier = Modifier.width(6.dp))
        Text(
          text = "GLASS WALLPAPER",
          fontFamily = SpaceMonoFontFamily,
          fontSize = 12.sp,
          fontWeight = FontWeight.Bold,
          color = NothingOffWhite,
          letterSpacing = 1.sp
        )
      }

      Spacer(modifier = Modifier.height(10.dp))

      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(10.dp)
      ) {
        WallpaperType.entries.forEach { wp ->
          val isSelected = settings.wallpaperType == wp
          Box(
            modifier = Modifier
              .weight(1f)
              .clip(RoundedCornerShape(16.dp))
              .border(
                width = if (isSelected) 2.dp else 1.dp,
                color = if (isSelected) NothingRed else NothingGlassBorder,
                shape = RoundedCornerShape(16.dp)
              )
              .background(
                when (wp) {
                  WallpaperType.GLASS_DARK -> Color(0xFF141720)
                  WallpaperType.GLASS_GLYPH -> Color(0xFF1A1520)
                  WallpaperType.FROSTED_MESH -> Color(0xFF0F1A24)
                  WallpaperType.PURE_BLACK -> Color(0xFF000000)
                }
              )
              .clickable { onUpdateWallpaper(wp) }
              .padding(10.dp),
            contentAlignment = Alignment.Center
          ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
              if (isSelected) {
                Icon(
                  imageVector = Icons.Default.Check,
                  contentDescription = null,
                  tint = NothingRed,
                  modifier = Modifier.size(18.dp)
                )
              } else {
                Box(
                  modifier = Modifier
                    .size(18.dp)
                    .clip(CircleShape)
                    .background(Color.White.copy(alpha = 0.1f))
                )
              }
              Spacer(modifier = Modifier.height(6.dp))
              Text(
                text = wp.title,
                fontFamily = SpaceMonoFontFamily,
                fontSize = 10.sp,
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                color = if (isSelected) NothingWhite else NothingMuted,
                maxLines = 1
              )
            }
          }
        }
      }

      Spacer(modifier = Modifier.height(20.dp))

      // Section: Glass Translucency Opacity Slider
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Text(
          text = "FROSTED GLASS OPACITY",
          fontFamily = SpaceMonoFontFamily,
          fontSize = 12.sp,
          fontWeight = FontWeight.Bold,
          color = NothingOffWhite,
          letterSpacing = 1.sp
        )
        Text(
          text = "${(settings.glassOpacity * 100).toInt()}%",
          fontFamily = SpaceMonoFontFamily,
          fontSize = 12.sp,
          color = GlassCyan
        )
      }

      Slider(
        value = settings.glassOpacity,
        onValueChange = onUpdateGlassOpacity,
        valueRange = 0.25f..0.85f,
        colors = SliderDefaults.colors(
          thumbColor = NothingRed,
          activeTrackColor = NothingRed,
          inactiveTrackColor = Color.White.copy(alpha = 0.12f)
        ),
        modifier = Modifier.fillMaxWidth()
      )

      Spacer(modifier = Modifier.height(14.dp))

      // Section: Icon Style (Nothing Mono vs Neon vs Original)
      Text(
        text = "APP ICON STYLE",
        fontFamily = SpaceMonoFontFamily,
        fontSize = 12.sp,
        fontWeight = FontWeight.Bold,
        color = NothingOffWhite,
        letterSpacing = 1.sp
      )

      Spacer(modifier = Modifier.height(8.dp))

      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(10.dp)
      ) {
        IconStyle.entries.forEach { style ->
          val isSelected = settings.iconStyle == style
          Box(
            modifier = Modifier
              .weight(1f)
              .clip(RoundedCornerShape(14.dp))
              .background(if (isSelected) NothingRed else Color.White.copy(alpha = 0.08f))
              .clickable { onUpdateIconStyle(style) }
              .padding(vertical = 10.dp),
            contentAlignment = Alignment.Center
          ) {
            Text(
              text = style.title,
              fontFamily = SpaceMonoFontFamily,
              fontSize = 11.sp,
              fontWeight = FontWeight.Bold,
              color = if (isSelected) Color.White else NothingOffWhite
            )
          }
        }
      }

      Spacer(modifier = Modifier.height(20.dp))

      // Section: Grid Layout Columns (4 vs 5)
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Text(
          text = "GRID LAYOUT",
          fontFamily = SpaceMonoFontFamily,
          fontSize = 12.sp,
          fontWeight = FontWeight.Bold,
          color = NothingOffWhite,
          letterSpacing = 1.sp
        )

        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
          listOf(4, 5).forEach { cols ->
            val isSelected = settings.gridColumns == cols
            Box(
              modifier = Modifier
                .clip(CircleShape)
                .background(if (isSelected) NothingRed else Color.White.copy(alpha = 0.08f))
                .clickable { onUpdateGridColumns(cols) }
                .padding(horizontal = 14.dp, vertical = 6.dp)
            ) {
              Text(
                text = "${cols}x${cols + 1}",
                fontFamily = SpaceMonoFontFamily,
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = if (isSelected) Color.White else NothingOffWhite
              )
            }
          }
        }
      }

      Spacer(modifier = Modifier.height(20.dp))

      // Section: Active Glass Widgets
      Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(
          imageVector = Icons.Default.Widgets,
          contentDescription = null,
          tint = NothingRed,
          modifier = Modifier.size(16.dp)
        )
        Spacer(modifier = Modifier.width(6.dp))
        Text(
          text = "ACTIVE GLASS WIDGETS",
          fontFamily = SpaceMonoFontFamily,
          fontSize = 12.sp,
          fontWeight = FontWeight.Bold,
          color = NothingOffWhite,
          letterSpacing = 1.sp
        )
      }

      Spacer(modifier = Modifier.height(10.dp))

      WidgetToggleRow("Dot Clock & Date", settings.showClockWidget) { onToggleWidget("clock", it) }
      WidgetToggleRow("Frosted Weather Widget", settings.showWeatherWidget) { onToggleWidget("weather", it) }
      WidgetToggleRow("Quick Controls & Battery Ring", settings.showControlsWidget) { onToggleWidget("controls", it) }
      WidgetToggleRow("Media Controller", settings.showMusicWidget) { onToggleWidget("music", it) }
      WidgetToggleRow("Quick Notes Sticky Tile", settings.showNotesWidget) { onToggleWidget("notes", it) }
      WidgetToggleRow("Daily Step Ring", settings.showStepWidget) { onToggleWidget("steps", it) }

      Spacer(modifier = Modifier.height(20.dp))

      // Speed & Performance Banner
      Box(
        modifier = Modifier
          .fillMaxWidth()
          .clip(RoundedCornerShape(16.dp))
          .background(Color.White.copy(alpha = 0.04f))
          .padding(14.dp)
      ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
          Icon(
            imageVector = Icons.Default.Speed,
            contentDescription = null,
            tint = GlassCyan,
            modifier = Modifier.size(20.dp)
          )
          Spacer(modifier = Modifier.width(10.dp))
          Column {
            Text(
              text = "OPTIMIZED FOR ALL MOBILES",
              fontFamily = SpaceMonoFontFamily,
              fontSize = 11.sp,
              fontWeight = FontWeight.Bold,
              color = NothingWhite
            )
            Text(
              text = "Hardware acceleration active • 120Hz smooth scrolling • Zero background drain",
              fontFamily = OutfitFontFamily,
              fontSize = 11.sp,
              color = NothingMuted
            )
          }
        }
      }

      Spacer(modifier = Modifier.height(16.dp))
    }
  }
}

@Composable
fun WidgetToggleRow(
  title: String,
  checked: Boolean,
  onCheckedChange: (Boolean) -> Unit
) {
  Row(
    modifier = Modifier
      .fillMaxWidth()
      .padding(vertical = 4.dp),
    horizontalArrangement = Arrangement.SpaceBetween,
    verticalAlignment = Alignment.CenterVertically
  ) {
    Text(
      text = title,
      fontFamily = OutfitFontFamily,
      fontSize = 14.sp,
      color = NothingWhite
    )
    Switch(
      checked = checked,
      onCheckedChange = onCheckedChange,
      colors = SwitchDefaults.colors(
        checkedThumbColor = Color.White,
        checkedTrackColor = NothingRed,
        uncheckedThumbColor = NothingMuted,
        uncheckedTrackColor = Color.White.copy(alpha = 0.1f)
      )
    )
  }
}

private fun openDefaultHomeSettings(context: Context) {
  val intent = Intent(Settings.ACTION_HOME_SETTINGS).apply {
    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
  }
  runCatching {
    context.startActivity(intent)
  }.onFailure {
    // Fallback to application details or general settings
    val alt = Intent(Settings.ACTION_SETTINGS).apply {
      addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    }
    runCatching { context.startActivity(alt) }
  }
}
