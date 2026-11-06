package com.example.ui.components

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.material.icons.filled.Alarm
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.NothingMuted
import com.example.ui.theme.NothingOffWhite
import com.example.ui.theme.NothingRed
import com.example.ui.theme.NothingRedGlow
import com.example.ui.theme.NothingWhite
import com.example.ui.theme.SpaceMonoFontFamily
import kotlinx.coroutines.delay
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun NothingDotClockWidget(
  modifier: Modifier = Modifier,
  opacity: Float = 0.55f,
  is24Hour: Boolean = true,
  onClick: () -> Unit = {}
) {
  var currentTime by remember { mutableStateOf(Date()) }

  LaunchedEffect(Unit) {
    while (true) {
      currentTime = Date()
      delay(1000L)
    }
  }

  val timeFormatPattern = if (is24Hour) "HH:mm" else "hh:mm"
  val timeFormatter = remember(is24Hour) { SimpleDateFormat(timeFormatPattern, Locale.getDefault()) }
  val secFormatter = remember { SimpleDateFormat("ss", Locale.getDefault()) }
  val dateFormatter = remember { SimpleDateFormat("EEE, dd MMM", Locale.getDefault()) }

  val formattedTime = timeFormatter.format(currentTime)
  val seconds = secFormatter.format(currentTime)
  val formattedDate = dateFormatter.format(currentTime).uppercase()

  // Split hours and minutes
  val parts = formattedTime.split(":")
  val hours = parts.getOrNull(0) ?: "10"
  val minutes = parts.getOrNull(1) ?: "30"

  // Pulsing animation for the signature Nothing Red second indicator
  val infiniteTransition = rememberInfiniteTransition(label = "nothing_pulse")
  val pulseScale by infiniteTransition.animateFloat(
    initialValue = 0.85f,
    targetValue = 1.25f,
    animationSpec = infiniteRepeatable(
      animation = tween(1000, easing = FastOutSlowInEasing),
      repeatMode = RepeatMode.Reverse
    ),
    label = "dot_scale"
  )

  GlassCard(
    modifier = modifier
      .fillMaxWidth()
      .testTag("nothing_dot_clock_widget"),
    opacity = opacity,
    borderAlpha = 0.28f,
    onClick = onClick
  ) {
    Column(
      modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 22.dp, vertical = 20.dp)
    ) {
      // Header row: Date and Alarm pill
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
          Box(
            modifier = Modifier
              .size(8.dp)
              .scale(pulseScale)
              .clip(CircleShape)
              .background(NothingRed)
          )
          Spacer(modifier = Modifier.width(8.dp))
          Text(
            text = formattedDate,
            fontFamily = SpaceMonoFontFamily,
            fontSize = 13.sp,
            fontWeight = FontWeight.Medium,
            color = NothingOffWhite,
            letterSpacing = 1.5.sp
          )
        }

        Row(
          verticalAlignment = Alignment.CenterVertically,
          modifier = Modifier
            .clip(CircleShape)
            .background(Color.White.copy(alpha = 0.08f))
            .padding(horizontal = 8.dp, vertical = 3.dp)
        ) {
          Icon(
            imageVector = Icons.Default.Alarm,
            contentDescription = "Next Alarm",
            tint = NothingMuted,
            modifier = Modifier.size(12.dp)
          )
          Spacer(modifier = Modifier.width(4.dp))
          Text(
            text = "07:00",
            fontFamily = SpaceMonoFontFamily,
            fontSize = 11.sp,
            color = NothingMuted
          )
        }
      }

      Spacer(modifier = Modifier.height(8.dp))

      // Main Dot Clock Display
      Row(
        verticalAlignment = Alignment.Bottom,
        horizontalArrangement = Arrangement.Start
      ) {
        Text(
          text = hours,
          fontFamily = SpaceMonoFontFamily,
          fontSize = 62.sp,
          fontWeight = FontWeight.Bold,
          color = NothingWhite,
          lineHeight = 64.sp,
          letterSpacing = (-2).sp
        )

        // Pulsing colon
        Box(
          modifier = Modifier
            .padding(horizontal = 4.dp, vertical = 14.dp)
            .size(10.dp)
            .scale(pulseScale)
            .clip(CircleShape)
            .background(NothingRedGlow)
        )

        Text(
          text = minutes,
          fontFamily = SpaceMonoFontFamily,
          fontSize = 62.sp,
          fontWeight = FontWeight.Bold,
          color = NothingWhite,
          lineHeight = 64.sp,
          letterSpacing = (-2).sp
        )

        Spacer(modifier = Modifier.width(10.dp))

        // Seconds indicator badge
        Box(
          modifier = Modifier
            .padding(bottom = 12.dp)
            .clip(CircleShape)
            .background(NothingRed.copy(alpha = 0.15f))
            .padding(horizontal = 8.dp, vertical = 2.dp)
        ) {
          Text(
            text = ":$seconds",
            fontFamily = SpaceMonoFontFamily,
            fontSize = 15.sp,
            fontWeight = FontWeight.Bold,
            color = NothingRedGlow
          )
        }
      }
    }
  }
}
