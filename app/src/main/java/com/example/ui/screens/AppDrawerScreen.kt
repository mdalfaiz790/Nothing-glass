package com.example.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.VerticalAlignTop
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.ColorMatrix
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.model.AppCategory
import com.example.model.AppItem
import com.example.model.IconStyle
import com.example.ui.components.GlassCard
import com.example.ui.components.GlassPill
import com.example.ui.theme.GlassCyan
import com.example.ui.theme.NothingBlack
import com.example.ui.theme.NothingGlassBorder
import com.example.ui.theme.NothingMuted
import com.example.ui.theme.NothingOffWhite
import com.example.ui.theme.NothingRed
import com.example.ui.theme.NothingWhite
import com.example.ui.theme.OutfitFontFamily
import com.example.ui.theme.SpaceMonoFontFamily
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppDrawerScreen(
  apps: List<AppItem>,
  iconStyle: IconStyle,
  gridColumns: Int,
  glassOpacity: Float,
  onLaunchApp: (AppItem) -> Unit,
  onToggleFavorite: (AppItem) -> Unit,
  onTogglePinDock: (AppItem) -> Unit,
  onClose: () -> Unit
) {
  BackHandler {
    onClose()
  }

  var searchQuery by remember { mutableStateOf("") }
  var selectedCategory by remember { mutableStateOf(AppCategory.ALL) }
  var selectedAppForMenu by remember { mutableStateOf<AppItem?>(null) }

  val coroutineScope = rememberCoroutineScope()
  val gridState = rememberLazyGridState()

  val filteredApps by remember(apps, searchQuery, selectedCategory) {
    derivedStateOf {
      apps.filter { app ->
        val matchesSearch = searchQuery.isBlank() ||
            app.label.contains(searchQuery, ignoreCase = true) ||
            app.packageName.contains(searchQuery, ignoreCase = true)

        val matchesCategory = when (selectedCategory) {
          AppCategory.ALL -> true
          AppCategory.FAVORITES -> app.isFavorite
          else -> app.category == selectedCategory
        }

        matchesSearch && matchesCategory
      }
    }
  }

  // Dark Frosted Glass backdrop
  Box(
    modifier = Modifier
      .fillMaxSize()
      .background(
        Brush.verticalGradient(
          listOf(
            Color(0xF00A0C10),
            Color(0xF8050608),
            NothingBlack
          )
        )
      )
      .statusBarsPadding()
      .navigationBarsPadding()
      .testTag("app_drawer_screen")
  ) {
    Column(modifier = Modifier.fillMaxSize()) {
      // Top Search and Close bar
      Row(
        modifier = Modifier
          .fillMaxWidth()
          .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
      ) {
        OutlinedTextField(
          value = searchQuery,
          onValueChange = { searchQuery = it },
          placeholder = {
            Text(
              text = "Search all apps...",
              fontFamily = OutfitFontFamily,
              fontSize = 14.sp,
              color = NothingMuted
            )
          },
          leadingIcon = {
            Icon(
              imageVector = Icons.Default.Search,
              contentDescription = null,
              tint = NothingOffWhite,
              modifier = Modifier.size(20.dp)
            )
          },
          trailingIcon = {
            if (searchQuery.isNotEmpty()) {
              Icon(
                imageVector = Icons.Default.Close,
                contentDescription = "Clear search",
                tint = NothingMuted,
                modifier = Modifier
                  .size(18.dp)
                  .clickable { searchQuery = "" }
              )
            }
          },
          singleLine = true,
          shape = CircleShape,
          colors = TextFieldDefaults.colors(
            focusedTextColor = NothingWhite,
            unfocusedTextColor = NothingWhite,
            focusedContainerColor = Color.White.copy(alpha = 0.08f),
            unfocusedContainerColor = Color.White.copy(alpha = 0.05f),
            focusedIndicatorColor = NothingRed,
            unfocusedIndicatorColor = NothingGlassBorder
          ),
          modifier = Modifier
            .weight(1f)
            .height(52.dp)
            .testTag("app_drawer_search_input")
        )

        Spacer(modifier = Modifier.width(10.dp))

        // Close Drawer Button
        Box(
          modifier = Modifier
            .size(52.dp)
            .clip(CircleShape)
            .background(Color.White.copy(alpha = 0.08f))
            .clickable(
              interactionSource = remember { MutableInteractionSource() },
              indication = ripple(color = Color.White),
              onClick = onClose
            )
            .testTag("close_drawer_button"),
          contentAlignment = Alignment.Center
        ) {
          Icon(
            imageVector = Icons.Default.Close,
            contentDescription = "Close",
            tint = NothingWhite,
            modifier = Modifier.size(22.dp)
          )
        }
      }

      // Categories Row
      LazyRow(
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 6.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
      ) {
        items(AppCategory.entries.toTypedArray()) { cat ->
          val isSelected = selectedCategory == cat
          Box(
            modifier = Modifier
              .clip(CircleShape)
              .background(
                if (isSelected) NothingRed else Color.White.copy(alpha = 0.07f)
              )
              .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = ripple(),
                onClick = { selectedCategory = cat }
              )
              .padding(horizontal = 14.dp, vertical = 7.dp)
              .testTag("category_pill_${cat.title}")
          ) {
            Text(
              text = cat.title.uppercase(),
              fontFamily = SpaceMonoFontFamily,
              fontSize = 11.sp,
              fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
              color = if (isSelected) Color.White else NothingOffWhite,
              letterSpacing = 1.sp
            )
          }
        }
      }

      Spacer(modifier = Modifier.height(8.dp))

      // Main Apps Grid
      Box(modifier = Modifier.weight(1f)) {
        if (filteredApps.isEmpty()) {
          Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
          ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
              Text(
                text = "NO APPS FOUND",
                fontFamily = SpaceMonoFontFamily,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = NothingOffWhite
              )
              Spacer(modifier = Modifier.height(6.dp))
              Text(
                text = "Try searching with another keyword",
                fontFamily = OutfitFontFamily,
                fontSize = 13.sp,
                color = NothingMuted
              )
            }
          }
        } else {
          LazyVerticalGrid(
            columns = GridCells.Fixed(gridColumns),
            state = gridState,
            contentPadding = PaddingValues(start = 16.dp, end = 28.dp, top = 8.dp, bottom = 24.dp),
            verticalArrangement = Arrangement.spacedBy(18.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.fillMaxSize()
          ) {
            items(filteredApps, key = { it.packageName + it.label }) { app ->
              AppGridItem(
                app = app,
                iconStyle = iconStyle,
                onLaunch = { onLaunchApp(app) },
                onLongClick = { selectedAppForMenu = app }
              )
            }
          }
        }

        // Quick A-Z Alphabet Scrubber on right edge
        AlphabetScrubber(
          onLetterSelected = { char ->
            val index = filteredApps.indexOfFirst {
              it.label.firstOrNull()?.uppercaseChar() == char
            }
            if (index != -1) {
              coroutineScope.launch {
                gridState.scrollToItem(index)
              }
            }
          },
          modifier = Modifier
            .align(Alignment.CenterEnd)
            .padding(end = 4.dp)
        )
      }
    }

    // App Options Bottom Sheet on long-press
    if (selectedAppForMenu != null) {
      val app = selectedAppForMenu!!
      ModalBottomSheet(
        onDismissRequest = { selectedAppForMenu = null },
        sheetState = rememberModalBottomSheetState(),
        containerColor = Color(0xFF14171E),
        dragHandle = {
          Box(
            modifier = Modifier
              .padding(vertical = 12.dp)
              .size(width = 36.dp, height = 4.dp)
              .clip(CircleShape)
              .background(NothingGlassBorder)
          )
        }
      ) {
        Column(
          modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp, vertical = 12.dp)
            .navigationBarsPadding()
        ) {
          Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
          ) {
            Box(
              modifier = Modifier
                .size(46.dp)
                .clip(CircleShape)
                .background(Color.White.copy(alpha = 0.08f)),
              contentAlignment = Alignment.Center
            ) {
              if (app.iconDrawable != null) {
                AsyncImage(
                  model = app.iconDrawable,
                  contentDescription = null,
                  modifier = Modifier.size(30.dp)
                )
              } else if (app.fallbackIcon != null) {
                Icon(
                  imageVector = app.fallbackIcon,
                  contentDescription = null,
                  tint = NothingWhite,
                  modifier = Modifier.size(24.dp)
                )
              }
            }

            Spacer(modifier = Modifier.width(14.dp))

            Column {
              Text(
                text = app.label,
                fontFamily = OutfitFontFamily,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = NothingWhite
              )
              Text(
                text = app.packageName,
                fontFamily = SpaceMonoFontFamily,
                fontSize = 11.sp,
                color = NothingMuted,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
              )
            }
          }

          Spacer(modifier = Modifier.height(20.dp))

          // Option: Launch
          SheetOptionRow(
            icon = Icons.Default.VerticalAlignTop,
            label = "Open Application",
            onClick = {
              selectedAppForMenu = null
              onLaunchApp(app)
            }
          )

          // Option: Favorite
          SheetOptionRow(
            icon = Icons.Default.Star,
            label = if (app.isFavorite) "Remove from Favorites" else "Add to Favorites",
            active = app.isFavorite,
            onClick = {
              onToggleFavorite(app)
              selectedAppForMenu = null
            }
          )

          Spacer(modifier = Modifier.height(16.dp))
        }
      }
    }
  }
}

@Composable
fun AppGridItem(
  app: AppItem,
  iconStyle: IconStyle,
  onLaunch: () -> Unit,
  onLongClick: () -> Unit
) {
  Column(
    horizontalAlignment = Alignment.CenterHorizontally,
    modifier = Modifier
      .clip(RoundedCornerShape(16.dp))
      .clickable(
        interactionSource = remember { MutableInteractionSource() },
        indication = ripple(),
        onClick = onLaunch
      )
      .padding(vertical = 8.dp, horizontal = 4.dp)
      .testTag("app_item_${app.label}")
  ) {
    Box(
      modifier = Modifier
        .size(56.dp)
        .clip(RoundedCornerShape(18.dp))
        .background(Color.White.copy(alpha = 0.08f)),
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
          modifier = Modifier.size(34.dp),
          colorFilter = colorFilter
        )
      } else if (app.fallbackIcon != null) {
        Icon(
          imageVector = app.fallbackIcon,
          contentDescription = app.label,
          tint = NothingWhite,
          modifier = Modifier.size(28.dp)
        )
      }

      if (app.isFavorite) {
        Box(
          modifier = Modifier
            .align(Alignment.TopEnd)
            .padding(4.dp)
            .size(7.dp)
            .clip(CircleShape)
            .background(NothingRed)
        )
      }
    }

    Spacer(modifier = Modifier.height(6.dp))

    Text(
      text = app.label,
      fontFamily = OutfitFontFamily,
      fontSize = 12.sp,
      fontWeight = FontWeight.Medium,
      color = NothingWhite,
      textAlign = TextAlign.Center,
      maxLines = 1,
      overflow = TextOverflow.Ellipsis
    )
  }
}

@Composable
fun AlphabetScrubber(
  onLetterSelected: (Char) -> Unit,
  modifier: Modifier = Modifier
) {
  val letters = remember { ('A'..'Z').toList() }

  Column(
    modifier = modifier
      .clip(CircleShape)
      .background(Color.Black.copy(alpha = 0.4f))
      .padding(vertical = 6.dp, horizontal = 3.dp),
    horizontalAlignment = Alignment.CenterHorizontally,
    verticalArrangement = Arrangement.SpaceBetween
  ) {
    letters.filterIndexed { index, _ -> index % 2 == 0 }.forEach { char ->
      Text(
        text = char.toString(),
        fontFamily = SpaceMonoFontFamily,
        fontSize = 8.sp,
        fontWeight = FontWeight.Bold,
        color = NothingMuted,
        modifier = Modifier
          .clickable { onLetterSelected(char) }
          .padding(vertical = 1.dp)
      )
    }
  }
}

@Composable
fun SheetOptionRow(
  icon: androidx.compose.ui.graphics.vector.ImageVector,
  label: String,
  active: Boolean = false,
  onClick: () -> Unit
) {
  Row(
    modifier = Modifier
      .fillMaxWidth()
      .clip(RoundedCornerShape(12.dp))
      .clickable(onClick = onClick)
      .padding(vertical = 12.dp, horizontal = 8.dp),
    verticalAlignment = Alignment.CenterVertically
  ) {
    Icon(
      imageVector = icon,
      contentDescription = label,
      tint = if (active) NothingRed else NothingWhite,
      modifier = Modifier.size(20.dp)
    )
    Spacer(modifier = Modifier.width(14.dp))
    Text(
      text = label,
      fontFamily = OutfitFontFamily,
      fontSize = 14.sp,
      color = if (active) NothingRed else NothingWhite
    )
  }
}
