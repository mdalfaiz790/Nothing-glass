package com.example.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val NothingDarkColorScheme = darkColorScheme(
  primary = NothingRed,
  onPrimary = Color.White,
  primaryContainer = NothingRedSoft,
  onPrimaryContainer = NothingRedGlow,
  secondary = NothingWhite,
  onSecondary = NothingBlack,
  secondaryContainer = Color(0x33FFFFFF),
  onSecondaryContainer = NothingWhite,
  tertiary = GlassCyan,
  onTertiary = NothingBlack,
  background = NothingBlack,
  onBackground = NothingWhite,
  surface = NothingDarkBackground,
  onSurface = NothingWhite,
  surfaceVariant = NothingSurface,
  onSurfaceVariant = NothingOffWhite,
  outline = NothingGlassBorder,
  outlineVariant = NothingGlassBorderSubtle
)

@Composable
fun MyApplicationTheme(
  content: @Composable () -> Unit
) {
  MaterialTheme(
    colorScheme = NothingDarkColorScheme,
    typography = Typography,
    content = content
  )
}
