package com.example.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.ui.theme.NothingGlassBorder
import com.example.ui.theme.NothingGlassBorderSubtle
import com.example.ui.theme.NothingRed

@Composable
fun GlassCard(
  modifier: Modifier = Modifier,
  shape: Shape = RoundedCornerShape(26.dp),
  opacity: Float = 0.55f,
  borderAlpha: Float = 0.25f,
  hasRedAccent: Boolean = false,
  onClick: (() -> Unit)? = null,
  content: @Composable BoxScope.() -> Unit
) {
  val backgroundBrush = remember(opacity) {
    Brush.verticalGradient(
      colors = listOf(
        Color(0xFF20242D).copy(alpha = opacity),
        Color(0xFF0E1015).copy(alpha = (opacity + 0.18f).coerceAtMost(0.96f))
      )
    )
  }

  val borderBrush = remember(borderAlpha) {
    Brush.linearGradient(
      colors = listOf(
        Color.White.copy(alpha = borderAlpha),
        Color.White.copy(alpha = (borderAlpha * 0.2f))
      ),
      start = Offset(0f, 0f),
      end = Offset(400f, 600f)
    )
  }

  val clickableModifier = if (onClick != null) {
    Modifier.clickable(
      interactionSource = remember { MutableInteractionSource() },
      indication = ripple(color = Color.White.copy(alpha = 0.2f)),
      onClick = onClick
    )
  } else Modifier

  Box(
    modifier = modifier
      .shadow(
        elevation = 8.dp,
        shape = shape,
        ambientColor = Color.Black.copy(alpha = 0.6f),
        spotColor = Color.White.copy(alpha = 0.05f)
      )
      .clip(shape)
      .background(backgroundBrush)
      .border(BorderStroke(1.dp, borderBrush), shape)
      .then(clickableModifier)
  ) {
    content()

    if (hasRedAccent) {
      Box(
        modifier = Modifier
          .align(Alignment.TopEnd)
          .padding(14.dp)
          .clip(CircleShape)
          .background(NothingRed)
          .drawBehind {
            drawCircle(
              color = Color.White.copy(alpha = 0.4f),
              radius = size.minDimension / 4f,
              center = Offset(size.width * 0.35f, size.height * 0.35f)
            )
          }
      )
    }
  }
}

/**
 * Pill-shaped glass container for badges, controls, search bar
 */
@Composable
fun GlassPill(
  modifier: Modifier = Modifier,
  opacity: Float = 0.6f,
  onClick: (() -> Unit)? = null,
  content: @Composable BoxScope.() -> Unit
) {
  GlassCard(
    modifier = modifier,
    shape = CircleShape,
    opacity = opacity,
    borderAlpha = 0.3f,
    onClick = onClick,
    content = content
  )
}
