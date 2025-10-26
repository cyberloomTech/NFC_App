package com.example.nfcapp.ui

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.nfcapp.R
import com.example.nfcapp.viewmodel.NFCViewModel

@Composable
fun ReadCopyScreen(
    onNavigateToWrite: () -> Unit
) {
    val nfcText by NFCViewModel.sharedText
    val context = LocalContext.current
    val haptic = LocalHapticFeedback.current
    val scrollState = rememberScrollState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(colorResource(R.color.light_grey))
            .padding(30.dp, 24.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // Display NFC Text
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp) // roughly 4 text lines (you can adjust)
                    .border(
                        width = 2.dp,
                        color = colorResource(R.color.gray_700),
                        shape = RoundedCornerShape(10.dp)
                    )
                    .verticalScroll(scrollState) // enable vertical scroll
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                        .padding(17.dp),
                    verticalArrangement = Arrangement.SpaceBetween
                ) {
                    // Top box with scrollable text
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f) // fills remaining space above bottom box
                    ) {
                        val scrollState = rememberScrollState()

                        Text(
                            text = nfcText,
                            fontSize = 28.sp,
                            textAlign = TextAlign.Start,
                            color = colorResource(R.color.gray_900),
                            modifier = Modifier
                                .fillMaxSize()
                                .verticalScroll(scrollState) // scroll if text overflows
                                .clickable {
                                    haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                                }
                        )
                    }

                    // Bottom fixed text box
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(10.dp, 0.dp),
                        contentAlignment = Alignment.CenterStart
                    ) {
                        if (nfcText.isNotBlank() && nfcText != "Ready to read NFC tag...") {
                            Text(
                                text = "→ Copy Text to Clipboard.\n→ Paste in Target App.\n→ Clear Clipboard.",
                                color = colorResource(R.color.gray_900),
                                fontSize = 18.sp,
                                textAlign = TextAlign.Start
                            )
                        } else {
                            Text(
                                text = "→ Hold the back of your phone near\nLoopO. Make sure NFC is activated.",
                                color = colorResource(R.color.gray_900),
                                fontSize = 18.sp,
                                textAlign = TextAlign.Start
                            )
                        }
                    }
                }
            }
            // Copy Button
            Button(
                onClick = {
                    if (nfcText.isNotBlank() && nfcText != "Ready to read NFC tag...") {
                        val clipboard =
                            context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                        val clip = ClipData.newPlainText("NFC Text", nfcText)
                        clipboard.setPrimaryClip(clip)

                        haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                        Toast.makeText(context, "Copied to clipboard", Toast.LENGTH_SHORT).show()
                    } else {
                        haptic.performHapticFeedback(HapticFeedbackType.Reject)
                        Toast.makeText(context, "Nothing to copy", Toast.LENGTH_SHORT).show()
                    }
                },
                shape = RoundedCornerShape(20.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = colorResource(R.color.green_400)
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(60.dp)
            ) {
                Text(
                    text = "COPY TEXT",
                    fontWeight = FontWeight.Light,
                    fontSize = 28.sp
                )
            }
            // Clear Clipboard Button
            Button(
                onClick = {
                    val clipboard =
                        context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                    clipboard.setPrimaryClip(
                        ClipData.newPlainText(
                            "",
                            ""
                        )
                    ) // overwrite with empty clip

                    haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                    Toast.makeText(context, "Clipboard has been cleared.", Toast.LENGTH_SHORT)
                        .show()
                },
                shape = RoundedCornerShape(20.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = colorResource(R.color.gray_900)// Set the text color to be visible on the dark background
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(60.dp)
            ) {
                Text(
                    text = "CLEAR CLIPBOARD",
                    fontWeight = FontWeight.Light,
                    fontSize = 28.sp
                )
            }
            Column(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "LoopO",
                    fontSize = 24.sp,
                    color = colorResource(id = R.color.gray_700)
                )
                Text(
                    text = "Your strong password - simplified!",
                    fontSize = 20.sp,
                    color = colorResource(id = R.color.gray_700)
                )

                Image(
                    painter = painterResource(id = R.drawable.loopo_nfc_logo),
                    contentDescription = stringResource(id = R.string.nfc_logo),
                    modifier = Modifier
                        .size(150.dp, 150.dp)
                        .padding(end = 12.dp)
                )
                Text(
                    text = "www.loopo.ch\ninfo@loopo.ch",
                    fontSize = 18.sp,
                    textAlign = TextAlign.Center,
                    color = colorResource(id = R.color.gray_700)
                )
            }
        }
    }
}

@Preview
@Composable
fun ReadPreview() {
    ReadCopyScreen(
        onNavigateToWrite = {}
    )
}
