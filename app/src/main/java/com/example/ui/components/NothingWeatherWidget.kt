package com.example.ui.components

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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Air
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.WaterDrop
import androidx.compose.material.icons.filled.WbCloudy
import androidx.compose.material.icons.filled.WbSunny
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.WeatherInfo
import com.example.ui.theme.GlassAmber
import com.example.ui.theme.NothingMuted
import com.example.ui.theme.NothingOffWhite
import com.example.ui.theme.NothingWhite
import com.example.ui.theme.OutfitFontFamily
import com.example.ui.theme.SpaceMonoFontFamily

@Composable
fun NothingWeatherWidget(
  weather: WeatherInfo,
  modifier: Modifier = Modifier,
  opacity: Float = 0.55f,
  onClick: () -> Unit = {}
) {
  GlassCard(
    modifier = modifier
      .fillMaxWidth()
      .testTag("nothing_weather_widget"),
    opacity = opacity,
    borderAlpha = 0.28f,
    onClick = onClick
  ) {
    Column(
      modifier = Modifier
        .fillMaxWidth()
        .padding(20.dp)
    ) {
      // Location & Weather condition badge
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
          Icon(
            imageVector = Icons.Default.LocationOn,
            contentDescription = "Location",
            tint = NothingOffWhite,
            modifier = Modifier.size(14.dp)
          )
          Spacer(modifier = Modifier.width(4.dp))
          Text(
            text = weather.location,
            fontFamily = OutfitFontFamily,
            fontSize = 13.sp,
            fontWeight = FontWeight.Medium,
            color = NothingOffWhite
          )
        }

        Box(
          modifier = Modifier
            .clip(CircleShape)
            .background(Color.White.copy(alpha = 0.08f))
            .padding(horizontal = 10.dp, vertical = 3.dp)
        ) {
          Text(
            text = weather.condition,
            fontFamily = SpaceMonoFontFamily,
            fontSize = 11.sp,
            color = NothingOffWhite
          )
        }
      }

      Spacer(modifier = Modifier.height(12.dp))

      // Main Temp & Icon
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Row(verticalAlignment = Alignment.Top) {
          Text(
            text = "${weather.temperature}",
            fontFamily = SpaceMonoFontFamily,
            fontSize = 54.sp,
            fontWeight = FontWeight.Bold,
            color = NothingWhite,
            lineHeight = 56.sp
          )
          Text(
            text = "°C",
            fontFamily = SpaceMonoFontFamily,
            fontSize = 20.sp,
            fontWeight = FontWeight.Medium,
            color = GlassAmber,
            modifier = Modifier.padding(start = 2.dp, top = 6.dp)
          )
        }

        Icon(
          imageVector = if (weather.condition.contains("Sun", ignoreCase = true)) Icons.Default.WbSunny else Icons.Default.WbCloudy,
          contentDescription = "Weather Icon",
          tint = GlassAmber,
          modifier = Modifier.size(46.dp)
        )
      }

      Spacer(modifier = Modifier.height(10.dp))

      // Humidity & Wind
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
      ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
          Icon(
            imageVector = Icons.Default.WaterDrop,
            contentDescription = null,
            tint = NothingMuted,
            modifier = Modifier.size(13.dp)
          )
          Spacer(modifier = Modifier.width(4.dp))
          Text(
            text = "${weather.humidity}%",
            fontFamily = SpaceMonoFontFamily,
            fontSize = 11.sp,
            color = NothingMuted
          )
        }

        Row(verticalAlignment = Alignment.CenterVertically) {
          Icon(
            imageVector = Icons.Default.Air,
            contentDescription = null,
            tint = NothingMuted,
            modifier = Modifier.size(13.dp)
          )
          Spacer(modifier = Modifier.width(4.dp))
          Text(
            text = weather.windSpeed,
            fontFamily = SpaceMonoFontFamily,
            fontSize = 11.sp,
            color = NothingMuted
          )
        }
      }

      Spacer(modifier = Modifier.height(14.dp))

      // Hourly Forecast Mini Glass Pills
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
      ) {
        weather.forecast.forEach { hour ->
          Box(
            modifier = Modifier
              .weight(1f)
              .padding(horizontal = 3.dp)
              .clip(RoundedCornerShape(14.dp))
              .background(Color.White.copy(alpha = 0.05f))
              .padding(vertical = 8.dp),
            contentAlignment = Alignment.Center
          ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
              Text(
                text = hour.time,
                fontFamily = SpaceMonoFontFamily,
                fontSize = 10.sp,
                color = NothingMuted
              )
              Spacer(modifier = Modifier.height(4.dp))
              Text(
                text = "${hour.temp}°",
                fontFamily = SpaceMonoFontFamily,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = NothingWhite
              )
            }
          }
        }
      }
    }
  }
}
