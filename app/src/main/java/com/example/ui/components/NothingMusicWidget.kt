package com.example.ui.components

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
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
import androidx.compose.material.icons.filled.GraphicEq
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.SkipNext
import androidx.compose.material.icons.filled.SkipPrevious
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.NothingMuted
import com.example.ui.theme.NothingOffWhite
import com.example.ui.theme.NothingRed
import com.example.ui.theme.NothingWhite
import com.example.ui.theme.OutfitFontFamily
import com.example.ui.theme.SpaceMonoFontFamily

@Composable
fun NothingMusicWidget(
  modifier: Modifier = Modifier,
  opacity: Float = 0.55f
) {
  var isPlaying by remember { mutableStateOf(false) }
  var trackIndex by remember { mutableStateOf(0) }

  val playlist = listOf(
    Pair("Glyph Synesthesia", "Nothing Sounds • 02:45"),
    Pair("Monochrome Atmosphere", "Ear (2) Spatial • 03:12"),
    Pair("Transparent Architecture", "Carl Pei Beats • 01:58")
  )

  val currentTrack = playlist[trackIndex % playlist.size]

  val infiniteTransition = rememberInfiniteTransition(label = "waveform")
  val wave1 by infiniteTransition.animateFloat(
    initialValue = 4.dp.value,
    targetValue = 18.dp.value,
    animationSpec = infiniteRepeatable(
      animation = tween(380, easing = FastOutSlowInEasing),
      repeatMode = RepeatMode.Reverse
    ),
    label = "wave1"
  )
  val wave2 by infiniteTransition.animateFloat(
    initialValue = 16.dp.value,
    targetValue = 6.dp.value,
    animationSpec = infiniteRepeatable(
      animation = tween(450, easing = FastOutSlowInEasing),
      repeatMode = RepeatMode.Reverse
    ),
    label = "wave2"
  )
  val wave3 by infiniteTransition.animateFloat(
    initialValue = 8.dp.value,
    targetValue = 22.dp.value,
    animationSpec = infiniteRepeatable(
      animation = tween(320, easing = FastOutSlowInEasing),
      repeatMode = RepeatMode.Reverse
    ),
    label = "wave3"
  )

  GlassCard(
    modifier = modifier
      .fillMaxWidth()
      .testTag("nothing_music_widget"),
    opacity = opacity,
    borderAlpha = 0.28f
  ) {
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .padding(18.dp),
      verticalAlignment = Alignment.CenterVertically,
      horizontalArrangement = Arrangement.SpaceBetween
    ) {
      // Album Art / Visualizer Box
      Box(
        modifier = Modifier
          .size(52.dp)
          .clip(CircleShape)
          .background(Color.White.copy(alpha = 0.08f)),
        contentAlignment = Alignment.Center
      ) {
        if (isPlaying) {
          Row(
            horizontalArrangement = Arrangement.spacedBy(3.dp),
            verticalAlignment = Alignment.CenterVertically
          ) {
            Box(modifier = Modifier.width(3.dp).height(wave1.dp).clip(CircleShape).background(NothingRed))
            Box(modifier = Modifier.width(3.dp).height(wave2.dp).clip(CircleShape).background(NothingWhite))
            Box(modifier = Modifier.width(3.dp).height(wave3.dp).clip(CircleShape).background(NothingRed))
          }
        } else {
          Icon(
            imageVector = Icons.Default.GraphicEq,
            contentDescription = "Music",
            tint = NothingMuted,
            modifier = Modifier.size(24.dp)
          )
        }
      }

      Spacer(modifier = Modifier.width(14.dp))

      // Track metadata
      Column(modifier = Modifier.weight(1f)) {
        Text(
          text = currentTrack.first,
          fontFamily = OutfitFontFamily,
          fontSize = 14.sp,
          fontWeight = FontWeight.SemiBold,
          color = NothingWhite,
          maxLines = 1
        )
        Spacer(modifier = Modifier.height(2.dp))
        Text(
          text = currentTrack.second,
          fontFamily = SpaceMonoFontFamily,
          fontSize = 11.sp,
          color = NothingMuted,
          maxLines = 1
        )
      }

      // Playback Controls
      Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(6.dp)
      ) {
        Box(
          modifier = Modifier
            .size(36.dp)
            .clip(CircleShape)
            .clickable(
              interactionSource = remember { MutableInteractionSource() },
              indication = ripple(),
              onClick = { if (trackIndex > 0) trackIndex-- }
            ),
          contentAlignment = Alignment.Center
        ) {
          Icon(
            imageVector = Icons.Default.SkipPrevious,
            contentDescription = "Previous Track",
            tint = NothingOffWhite,
            modifier = Modifier.size(20.dp)
          )
        }

        Box(
          modifier = Modifier
            .size(42.dp)
            .clip(CircleShape)
            .background(if (isPlaying) NothingRed else Color.White.copy(alpha = 0.15f))
            .clickable(
              interactionSource = remember { MutableInteractionSource() },
              indication = ripple(color = Color.White),
              onClick = { isPlaying = !isPlaying }
            ),
          contentAlignment = Alignment.Center
        ) {
          Icon(
            imageVector = if (isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
            contentDescription = if (isPlaying) "Pause" else "Play",
            tint = if (isPlaying) Color.White else NothingWhite,
            modifier = Modifier.size(24.dp)
          )
        }

        Box(
          modifier = Modifier
            .size(36.dp)
            .clip(CircleShape)
            .clickable(
              interactionSource = remember { MutableInteractionSource() },
              indication = ripple(),
              onClick = { trackIndex++ }
            ),
          contentAlignment = Alignment.Center
        ) {
          Icon(
            imageVector = Icons.Default.SkipNext,
            contentDescription = "Next Track",
            tint = NothingOffWhite,
            modifier = Modifier.size(20.dp)
          )
        }
      }
    }
  }
}
