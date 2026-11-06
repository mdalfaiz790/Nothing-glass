package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Apps
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.ColorMatrix
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.model.AppItem
import com.example.model.IconStyle
import com.example.ui.theme.NothingMuted
import com.example.ui.theme.NothingRed
import com.example.ui.theme.NothingWhite
import com.example.ui.theme.SpaceMonoFontFamily

@Composable
fun NothingDock(
  dockApps: List<AppItem>,
  iconStyle: IconStyle,
  onAppClick: (AppItem) -> Unit,
  onOpenDrawer: () -> Unit,
  modifier: Modifier = Modifier,
  opacity: Float = 0.6f
) {
  Column(
    modifier = modifier.fillMaxWidth(),
    horizontalAlignment = Alignment.CenterHorizontally
  ) {
    // Swipe-up hint indicator
    Box(
      modifier = Modifier
        .clip(CircleShape)
        .clickable(
          interactionSource = remember { MutableInteractionSource() },
          indication = ripple(),
          onClick = onOpenDrawer
        )
        .padding(horizontal = 16.dp, vertical = 6.dp),
      contentAlignment = Alignment.Center
    ) {
      Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(
          imageVector = Icons.Default.KeyboardArrowUp,
          contentDescription = "Swipe up for all apps",
          tint = NothingMuted,
          modifier = Modifier.size(16.dp)
        )
        Spacer(modifier = Modifier.width(4.dp))
        Text(
          text = "ALL APPS",
          fontFamily = SpaceMonoFontFamily,
          fontSize = 10.sp,
          fontWeight = FontWeight.Medium,
          color = NothingMuted,
          letterSpacing = 1.sp
        )
      }
    }

    Spacer(modifier = Modifier.height(6.dp))

    // Floating Glass Dock Bar
    GlassPill(
      modifier = Modifier
        .fillMaxWidth()
        .height(76.dp)
        .testTag("nothing_dock_bar"),
      opacity = opacity
    ) {
      Row(
        modifier = Modifier
          .fillMaxWidth()
          .height(76.dp)
          .padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
      ) {
        dockApps.take(4).forEach { app ->
          DockAppIcon(
            app = app,
            iconStyle = iconStyle,
            onClick = { onAppClick(app) }
          )
        }

        // Dedicated App Drawer Button
        Box(
          modifier = Modifier
            .size(54.dp)
            .clip(CircleShape)
            .background(Color.White.copy(alpha = 0.12f))
            .clickable(
              interactionSource = remember { MutableInteractionSource() },
              indication = ripple(color = Color.White),
              onClick = onOpenDrawer
            )
            .testTag("open_drawer_button"),
          contentAlignment = Alignment.Center
        ) {
          Icon(
            imageVector = Icons.Default.Apps,
            contentDescription = "All Apps Drawer",
            tint = NothingWhite,
            modifier = Modifier.size(26.dp)
          )
        }
      }
    }
  }
}

@Composable
fun DockAppIcon(
  app: AppItem,
  iconStyle: IconStyle,
  onClick: () -> Unit
) {
  Box(
    modifier = Modifier
      .size(54.dp)
      .clip(CircleShape)
      .background(Color.White.copy(alpha = 0.08f))
      .clickable(
        interactionSource = remember { MutableInteractionSource() },
        indication = ripple(color = Color.White),
        onClick = onClick
      )
      .testTag("dock_app_${app.label}"),
    contentAlignment = Alignment.Center
  ) {
    if (app.iconDrawable != null) {
      val colorFilter = when (iconStyle) {
        IconStyle.NOTHING_MONO -> {
          val matrix = ColorMatrix().apply { setToSaturation(0f) }
          ColorFilter.colorMatrix(matrix)
        }
        IconStyle.NEON_GLASS -> ColorFilter.tint(NothingWhite)
        IconStyle.ORIGINAL -> null
      }

      AsyncImage(
        model = app.iconDrawable,
        contentDescription = app.label,
        modifier = Modifier.size(32.dp),
        colorFilter = colorFilter
      )
    } else if (app.fallbackIcon != null) {
      Icon(
        imageVector = app.fallbackIcon,
        contentDescription = app.label,
        tint = if (iconStyle == IconStyle.NOTHING_MONO) NothingWhite else NothingWhite,
        modifier = Modifier.size(26.dp)
      )
    }

    // Nothing red dot accent for active dock app
    if (app.isFavorite) {
      Box(
        modifier = Modifier
          .align(Alignment.TopEnd)
          .padding(4.dp)
          .size(6.dp)
          .clip(CircleShape)
          .background(NothingRed)
      )
    }
  }
}
