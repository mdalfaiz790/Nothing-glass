package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.NothingMuted
import com.example.ui.theme.NothingRed
import com.example.ui.theme.NothingWhite
import com.example.ui.theme.OutfitFontFamily

@Composable
fun NothingSearchBar(
  onClick: () -> Unit,
  onMicClick: () -> Unit,
  modifier: Modifier = Modifier,
  opacity: Float = 0.6f
) {
  GlassPill(
    modifier = modifier
      .fillMaxWidth()
      .height(54.dp)
      .testTag("nothing_search_bar"),
    opacity = opacity,
    onClick = onClick
  ) {
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .height(54.dp)
        .padding(horizontal = 16.dp),
      verticalAlignment = Alignment.CenterVertically
    ) {
      Box(
        modifier = Modifier
          .size(8.dp)
          .clip(CircleShape)
          .background(NothingRed)
      )

      Spacer(modifier = Modifier.width(12.dp))

      Icon(
        imageVector = Icons.Default.Search,
        contentDescription = "Search",
        tint = NothingMuted,
        modifier = Modifier.size(20.dp)
      )

      Spacer(modifier = Modifier.width(10.dp))

      Text(
        text = "Search apps or web...",
        fontFamily = OutfitFontFamily,
        fontSize = 14.sp,
        color = NothingMuted,
        modifier = Modifier.weight(1f)
      )

      Box(
        modifier = Modifier
          .size(36.dp)
          .clip(CircleShape)
          .clickable(
            interactionSource = remember { MutableInteractionSource() },
            indication = ripple(color = Color.White),
            onClick = onMicClick
          ),
        contentAlignment = Alignment.Center
      ) {
        Icon(
          imageVector = Icons.Default.Mic,
          contentDescription = "Voice Search",
          tint = NothingWhite,
          modifier = Modifier.size(18.dp)
        )
      }
    }
  }
}
