package com.example.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
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
import androidx.compose.material.icons.filled.DirectionsWalk
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.GlassCyan
import com.example.ui.theme.NothingMuted
import com.example.ui.theme.NothingOffWhite
import com.example.ui.theme.NothingRed
import com.example.ui.theme.NothingWhite
import com.example.ui.theme.SpaceMonoFontFamily

@Composable
fun NothingStepWidget(
  steps: Int = 7480,
  goal: Int = 10000,
  modifier: Modifier = Modifier,
  opacity: Float = 0.55f
) {
  val progress = (steps.toFloat() / goal.toFloat()).coerceIn(0f, 1f)

  GlassCard(
    modifier = modifier
      .fillMaxWidth()
      .testTag("nothing_step_widget"),
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
      // Progress Arc with Step count
      Box(
        modifier = Modifier.size(68.dp),
        contentAlignment = Alignment.Center
      ) {
        Canvas(modifier = Modifier.size(64.dp)) {
          // Track
          drawArc(
            color = Color.White.copy(alpha = 0.1f),
            startAngle = -90f,
            sweepAngle = 360f,
            useCenter = false,
            style = Stroke(width = 5.dp.toPx(), cap = StrokeCap.Round)
          )
          // Progress
          drawArc(
            color = NothingRed,
            startAngle = -90f,
            sweepAngle = progress * 360f,
            useCenter = false,
            style = Stroke(width = 5.dp.toPx(), cap = StrokeCap.Round)
          )
        }

        Icon(
          imageVector = Icons.Default.DirectionsWalk,
          contentDescription = "Steps",
          tint = NothingWhite,
          modifier = Modifier.size(24.dp)
        )
      }

      Spacer(modifier = Modifier.width(16.dp))

      // Stats Column
      Column(modifier = Modifier.weight(1f)) {
        Row(verticalAlignment = Alignment.Bottom) {
          Text(
            text = "$steps",
            fontFamily = SpaceMonoFontFamily,
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            color = NothingWhite
          )
          Text(
            text = " / ${goal} STEPS",
            fontFamily = SpaceMonoFontFamily,
            fontSize = 11.sp,
            color = NothingMuted,
            modifier = Modifier.padding(bottom = 2.dp, start = 4.dp)
          )
        }

        Spacer(modifier = Modifier.height(6.dp))

        Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
              modifier = Modifier
                .size(6.dp)
                .clip(CircleShape)
                .background(GlassCyan)
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
              text = "5.6 KM",
              fontFamily = SpaceMonoFontFamily,
              fontSize = 11.sp,
              color = NothingOffWhite
            )
          }

          Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
              imageVector = Icons.Default.LocalFireDepartment,
              contentDescription = null,
              tint = NothingRed,
              modifier = Modifier.size(12.dp)
            )
            Spacer(modifier = Modifier.width(2.dp))
            Text(
              text = "382 KCAL",
              fontFamily = SpaceMonoFontFamily,
              fontSize = 11.sp,
              color = NothingOffWhite
            )
          }
        }
      }
    }
  }
}
