package com.example.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.Canvas
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.FlashlightOn
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.NotificationsOff
import androidx.compose.material.icons.filled.VolumeOff
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material.icons.filled.Wifi
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.BatteryStatus
import com.example.ui.theme.GlassCyan
import com.example.ui.theme.NothingMuted
import com.example.ui.theme.NothingOffWhite
import com.example.ui.theme.NothingRed
import com.example.ui.theme.NothingWhite
import com.example.ui.theme.SpaceMonoFontFamily

@Composable
fun NothingQuickControlsWidget(
  battery: BatteryStatus,
  isTorchOn: Boolean,
  isWifiOn: Boolean,
  isSilent: Boolean,
  isDndOn: Boolean,
  onToggleTorch: () -> Unit,
  onToggleWifi: () -> Unit,
  onToggleSilent: () -> Unit,
  onToggleDnd: () -> Unit,
  modifier: Modifier = Modifier,
  opacity: Float = 0.55f
) {
  GlassCard(
    modifier = modifier
      .fillMaxWidth()
      .testTag("nothing_quick_controls_widget"),
    opacity = opacity,
    borderAlpha = 0.28f
  ) {
    Column(
      modifier = Modifier
        .fillMaxWidth()
        .padding(18.dp)
    ) {
      // Top row: Battery Gauge Card + Torch Action Pill
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        // Battery status block
        Row(verticalAlignment = Alignment.CenterVertically) {
          Box(
            modifier = Modifier.size(54.dp),
            contentAlignment = Alignment.Center
          ) {
            val sweepAngle = (battery.level / 100f) * 360f
            val ringColor = when {
              battery.isCharging -> GlassCyan
              battery.level < 20 -> NothingRed
              else -> NothingWhite
            }

            Canvas(modifier = Modifier.size(50.dp)) {
              // Background track
              drawArc(
                color = Color.White.copy(alpha = 0.12f),
                startAngle = -90f,
                sweepAngle = 360f,
                useCenter = false,
                style = Stroke(width = 4.dp.toPx(), cap = StrokeCap.Round)
              )
              // Filled progress
              drawArc(
                color = ringColor,
                startAngle = -90f,
                sweepAngle = sweepAngle,
                useCenter = false,
                style = Stroke(width = 4.dp.toPx(), cap = StrokeCap.Round)
              )
            }

            if (battery.isCharging) {
              Icon(
                imageVector = Icons.Default.Bolt,
                contentDescription = "Charging",
                tint = GlassCyan,
                modifier = Modifier.size(20.dp)
              )
            } else {
              Text(
                text = "${battery.level}",
                fontFamily = SpaceMonoFontFamily,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = NothingWhite
              )
            }
          }

          Spacer(modifier = Modifier.width(12.dp))

          Column {
            Text(
              text = "BATTERY",
              fontFamily = SpaceMonoFontFamily,
              fontSize = 11.sp,
              color = NothingMuted,
              letterSpacing = 1.sp
            )
            Text(
              text = if (battery.isCharging) "CHARGING • ${battery.level}%" else "${battery.level}% OPTIMIZED",
              fontFamily = SpaceMonoFontFamily,
              fontSize = 12.sp,
              fontWeight = FontWeight.SemiBold,
              color = if (battery.isCharging) GlassCyan else NothingOffWhite
            )
          }
        }

        // Quick Torch Pill
        QuickTogglePill(
          icon = Icons.Default.FlashlightOn,
          label = if (isTorchOn) "TORCH ON" else "TORCH",
          isActive = isTorchOn,
          activeColor = NothingRed,
          onClick = onToggleTorch,
          testTag = "toggle_torch_button"
        )
      }

      Spacer(modifier = Modifier.height(14.dp))

      // Bottom Row: 3 Quick Settings Tiles (Wi-Fi, Sound, DND)
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(10.dp)
      ) {
        QuickControlTile(
          icon = Icons.Default.Wifi,
          label = if (isWifiOn) "CONNECTED" else "WI-FI OFF",
          isActive = isWifiOn,
          activeColor = GlassCyan,
          onClick = onToggleWifi,
          modifier = Modifier.weight(1f),
          testTag = "toggle_wifi_tile"
        )

        QuickControlTile(
          icon = if (isSilent) Icons.Default.VolumeOff else Icons.Default.VolumeUp,
          label = if (isSilent) "MUTE" else "SOUND",
          isActive = !isSilent,
          activeColor = NothingWhite,
          onClick = onToggleSilent,
          modifier = Modifier.weight(1f),
          testTag = "toggle_sound_tile"
        )

        QuickControlTile(
          icon = if (isDndOn) Icons.Default.NotificationsOff else Icons.Default.Notifications,
          label = if (isDndOn) "DND ON" else "DND OFF",
          isActive = isDndOn,
          activeColor = NothingRed,
          onClick = onToggleDnd,
          modifier = Modifier.weight(1f),
          testTag = "toggle_dnd_tile"
        )
      }
    }
  }
}

@Composable
fun QuickTogglePill(
  icon: ImageVector,
  label: String,
  isActive: Boolean,
  activeColor: Color,
  onClick: () -> Unit,
  modifier: Modifier = Modifier,
  testTag: String = ""
) {
  val bgCol by animateColorAsState(
    targetValue = if (isActive) activeColor.copy(alpha = 0.22f) else Color.White.copy(alpha = 0.08f),
    label = "pill_bg"
  )
  val iconCol by animateColorAsState(
    targetValue = if (isActive) activeColor else NothingMuted,
    label = "pill_icon"
  )

  Row(
    verticalAlignment = Alignment.CenterVertically,
    modifier = modifier
      .testTag(testTag)
      .clip(CircleShape)
      .background(bgCol)
      .clickable(
        interactionSource = remember { MutableInteractionSource() },
        indication = ripple(color = activeColor),
        onClick = onClick
      )
      .padding(horizontal = 14.dp, vertical = 8.dp)
  ) {
    Icon(
      imageVector = icon,
      contentDescription = label,
      tint = iconCol,
      modifier = Modifier.size(16.dp)
    )
    Spacer(modifier = Modifier.width(6.dp))
    Text(
      text = label,
      fontFamily = SpaceMonoFontFamily,
      fontSize = 11.sp,
      fontWeight = FontWeight.Bold,
      color = iconCol
    )
  }
}

@Composable
fun QuickControlTile(
  icon: ImageVector,
  label: String,
  isActive: Boolean,
  activeColor: Color,
  onClick: () -> Unit,
  modifier: Modifier = Modifier,
  testTag: String = ""
) {
  val bgCol by animateColorAsState(
    targetValue = if (isActive) activeColor.copy(alpha = 0.16f) else Color.White.copy(alpha = 0.05f),
    label = "tile_bg"
  )
  val tintCol by animateColorAsState(
    targetValue = if (isActive) activeColor else NothingMuted,
    label = "tile_tint"
  )

  Box(
    modifier = modifier
      .testTag(testTag)
      .clip(RoundedCornerShape(18.dp))
      .background(bgCol)
      .clickable(
        interactionSource = remember { MutableInteractionSource() },
        indication = ripple(color = activeColor),
        onClick = onClick
      )
      .padding(vertical = 12.dp, horizontal = 8.dp),
    contentAlignment = Alignment.Center
  ) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
      Icon(
        imageVector = icon,
        contentDescription = label,
        tint = tintCol,
        modifier = Modifier.size(20.dp)
      )
      Spacer(modifier = Modifier.height(6.dp))
      Text(
        text = label,
        fontFamily = SpaceMonoFontFamily,
        fontSize = 9.sp,
        fontWeight = FontWeight.Medium,
        color = tintCol,
        maxLines = 1
      )
    }
  }
}
