package com.example.nfcapp.ui

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.nfcapp.viewmodel.NFCViewModel

@Composable
fun ReadCopyScreen(
    statusText: String,
    nfcText: String,
    onNavigateToWrite: () -> Unit
) {
    val nfcText by NFCViewModel.sharedText
    val context = LocalContext.current
    val haptic = LocalHapticFeedback.current
    val scrollState = rememberScrollState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(24.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceAround
        ) {
            // Display NFC Text
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp) // roughly 4 text lines (you can adjust)
                    .padding(15.dp)
                    .border(
                        width = 2.dp,
                        color = Color(0xFF9E9E9E),
                        shape = RoundedCornerShape(10.dp)
                    )
                    .verticalScroll(scrollState) // enable vertical scroll
                    .padding(15.dp)
            ) {
                Text(
                    text = nfcText,
                    fontSize = 20.sp,
                    textAlign = TextAlign.Start, // align text left
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.TopStart)
                        .clickable {
                            haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                            Toast.makeText(context, "Tag simulated", Toast.LENGTH_SHORT).show()
                        }
                )
            }
            // Copy Button
            Button(
                onClick = {
                    if (nfcText.isNotBlank() && nfcText != "No tag scanned yet.") {
                        val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                        val clip = ClipData.newPlainText("NFC Text", nfcText)
                        clipboard.setPrimaryClip(clip)

                        haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                        Toast.makeText(context, "Copied to clipboard", Toast.LENGTH_SHORT).show()
                    } else {
                        haptic.performHapticFeedback(HapticFeedbackType.Reject)
                        Toast.makeText(context, "Nothing to copy", Toast.LENGTH_SHORT).show()
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(60.dp)
            ) {
                Text("Copy Text", fontSize = 20.sp)
            }
            // Clear Clipboard Button
            Button(
                onClick = {
                    val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                    clipboard.setPrimaryClip(ClipData.newPlainText("", "")) // overwrite with empty clip

                    haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                    Toast.makeText(context, "Clipboard has been cleared.", Toast.LENGTH_SHORT).show()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(60.dp)
            ) {
                Text("Clear Clipboard", fontSize = 20.sp)
            }
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp)
                    .border(
                        width = 2.dp,
                        color = Color(0xFF2196F3), // blue border
                        shape = RoundedCornerShape(10.dp)
                    )
                    .background(
                        color = Color(0xFFBBDEFB), // light powder blue background
                        shape = RoundedCornerShape(10.dp)
                    )
                    .padding(10.dp)
            ) {
                Text(
                    text = "● Ready to read NFC tag...\n● Auto-clear in 60s\n● Paste immediately!",
                    fontSize = 18.sp,
                    lineHeight = 22.sp,
                    color = Color.Black
                )
            }
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp)
                    .border(
                        width = 2.dp,
                        color = Color(0xFFFF9800), // orange
                        shape = RoundedCornerShape(10.dp)
                    )
                    .background(
                        color = Color(0xFFFFF59D), // light yellow
                        shape = RoundedCornerShape(10.dp)
                    )
                    .padding(10.dp)
            ) {
                Text(
                    text = "⚠️ IMPORTANT\nKeep text backup in safe place!\nLoopO not responsible for loss",
                    fontSize = 18.sp,
                    lineHeight = 22.sp,
                    color = Color.Black
                )
            }
            Button(
                onClick = {
                    haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                    onNavigateToWrite()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(60.dp)
            ) {
                Text("Go to Write & Protect", fontSize = 20.sp)
            }
        }
    }
}

@Preview
@Composable
fun ReadPreview() {
    ReadCopyScreen(
        statusText = "Ready to scan NFC tag",
        nfcText = "(No scanned text)",
        onNavigateToWrite = {}
    )
}
