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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.EditNote
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.NoteItem
import com.example.ui.theme.GlassCyan
import com.example.ui.theme.NothingDarkBackground
import com.example.ui.theme.NothingGlassBorder
import com.example.ui.theme.NothingMuted
import com.example.ui.theme.NothingOffWhite
import com.example.ui.theme.NothingRed
import com.example.ui.theme.NothingWhite
import com.example.ui.theme.OutfitFontFamily
import com.example.ui.theme.SpaceMonoFontFamily

@Composable
fun NothingNotesWidget(
  initialNotes: String,
  onSaveNotes: (String) -> Unit,
  modifier: Modifier = Modifier,
  opacity: Float = 0.55f
) {
  val notesList = remember {
    mutableStateListOf(
      NoteItem("1", "Check launcher glass performance", true),
      NoteItem("2", "Customize Nothing widgets grid", false),
      NoteItem("3", "Enjoy sleek dot-matrix design", false)
    )
  }

  var isDialogOpen by remember { mutableStateOf(false) }
  var newNoteInput by remember { mutableStateOf("") }

  GlassCard(
    modifier = modifier
      .fillMaxWidth()
      .testTag("nothing_notes_widget"),
    opacity = opacity,
    borderAlpha = 0.28f
  ) {
    Column(
      modifier = Modifier
        .fillMaxWidth()
        .padding(18.dp)
    ) {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
          Icon(
            imageVector = Icons.Default.EditNote,
            contentDescription = "Notes",
            tint = NothingOffWhite,
            modifier = Modifier.size(18.dp)
          )
          Spacer(modifier = Modifier.width(6.dp))
          Text(
            text = "QUICK NOTES",
            fontFamily = SpaceMonoFontFamily,
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            color = NothingOffWhite,
            letterSpacing = 1.sp
          )
        }

        Box(
          modifier = Modifier
            .size(30.dp)
            .clip(CircleShape)
            .background(Color.White.copy(alpha = 0.08f))
            .clickable(
              interactionSource = remember { MutableInteractionSource() },
              indication = ripple(),
              onClick = { isDialogOpen = true }
            ),
          contentAlignment = Alignment.Center
        ) {
          Icon(
            imageVector = Icons.Default.Add,
            contentDescription = "Add Note",
            tint = NothingWhite,
            modifier = Modifier.size(18.dp)
          )
        }
      }

      Spacer(modifier = Modifier.height(12.dp))

      Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        notesList.take(3).forEachIndexed { index, note ->
          Row(
            modifier = Modifier
              .fillMaxWidth()
              .clip(RoundedCornerShape(12.dp))
              .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = ripple(),
                onClick = {
                  notesList[index] = note.copy(isCompleted = !note.isCompleted)
                  onSaveNotes(notesList.joinToString("\n") { it.text })
                }
              )
              .padding(vertical = 4.dp, horizontal = 2.dp),
            verticalAlignment = Alignment.CenterVertically
          ) {
            Box(
              modifier = Modifier
                .size(20.dp)
                .clip(CircleShape)
                .background(if (note.isCompleted) GlassCyan else Color.White.copy(alpha = 0.1f)),
              contentAlignment = Alignment.Center
            ) {
              if (note.isCompleted) {
                Icon(
                  imageVector = Icons.Default.Check,
                  contentDescription = null,
                  tint = Color.Black,
                  modifier = Modifier.size(13.dp)
                )
              }
            }

            Spacer(modifier = Modifier.width(10.dp))

            Text(
              text = note.text,
              fontFamily = OutfitFontFamily,
              fontSize = 13.sp,
              color = if (note.isCompleted) NothingMuted else NothingWhite,
              textDecoration = if (note.isCompleted) TextDecoration.LineThrough else TextDecoration.None,
              maxLines = 1
            )
          }
        }
      }
    }
  }

  if (isDialogOpen) {
    AlertDialog(
      onDismissRequest = { isDialogOpen = false },
      containerColor = NothingDarkBackground,
      title = {
        Text(
          text = "New Quick Note",
          fontFamily = SpaceMonoFontFamily,
          color = NothingWhite
        )
      },
      text = {
        OutlinedTextField(
          value = newNoteInput,
          onValueChange = { newNoteInput = it },
          placeholder = { Text("Enter task or thought...", color = NothingMuted) },
          colors = TextFieldDefaults.colors(
            focusedTextColor = NothingWhite,
            unfocusedTextColor = NothingOffWhite,
            focusedContainerColor = Color.Transparent,
            unfocusedContainerColor = Color.Transparent,
            focusedIndicatorColor = NothingRed,
            unfocusedIndicatorColor = NothingGlassBorder
          ),
          modifier = Modifier.fillMaxWidth()
        )
      },
      confirmButton = {
        TextButton(
          onClick = {
            if (newNoteInput.isNotBlank()) {
              notesList.add(0, NoteItem(System.currentTimeMillis().toString(), newNoteInput.trim(), false))
              onSaveNotes(notesList.joinToString("\n") { it.text })
              newNoteInput = ""
            }
            isDialogOpen = false
          }
        ) {
          Text("Add", color = NothingRed, fontFamily = SpaceMonoFontFamily, fontWeight = FontWeight.Bold)
        }
      },
      dismissButton = {
        TextButton(onClick = { isDialogOpen = false }) {
          Text("Cancel", color = NothingMuted, fontFamily = SpaceMonoFontFamily)
        }
      }
    )
  }
}
