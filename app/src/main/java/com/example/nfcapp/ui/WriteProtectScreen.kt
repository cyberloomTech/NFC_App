package com.example.nfcapp.ui

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.nfcapp.viewmodel.NFCViewModel


@Composable
fun WriteProtectScreen(
    inputText: String,
    remainingBlocks: Int,
    writtenStrLength: Int,
    lockTag: Boolean,
    onInputTextChanged: (String) -> Unit,
    onLockTagChanged: (Boolean) -> Unit,
    onNavigateBack: () -> Unit
) {
    val haptic = LocalHapticFeedback.current
    val isWriteMode by NFCViewModel.isWriteMode
    val context = LocalContext.current

    val focusRequester = remember { FocusRequester() }
    val keyboardController = LocalSoftwareKeyboardController.current

    // Auto focus and show keyboard on screen enter
    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
        keyboardController?.show()
    }

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
            // Status text
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxWidth()
            ) {
                if (isWriteMode) {
                    CircularProgressIndicator(
                        modifier = Modifier
                            .size(22.dp)
                            .padding(end = 8.dp),
                        strokeWidth = 2.dp,
                        color = Color.Cyan
                    )
                    Text(
                        text = "Please tap an NFC tag",
                        fontSize = 18.sp,
                        textAlign = TextAlign.Center
                    )

                } else {
                    Text(
                        text = "Enter text to write",
                        fontSize = 18.sp,
                        textAlign = TextAlign.Center
                    )
                }


            }
            Column(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                // Input text field
                OutlinedTextField(
                    value = inputText,
                    onValueChange = onInputTextChanged,
                    label = { Text("Enter text to write") },
                    keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                        .padding(16.dp)
                        .focusRequester(focusRequester),
                    textStyle = LocalTextStyle.current.copy(fontSize = 20.sp),
                    singleLine = false
                )
                // Remaining 30-char blocks
                Box(
                    modifier = Modifier
                        .padding(18.dp, 10.dp)
                ) {
                    Text(
                        text = "$writtenStrLength / 888\nRemained $remainingBlocks blocks",
                        fontSize = 18.sp,
                        textAlign = TextAlign.End,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
            // WRITE / CANCEL BUTTON
            Button(
                onClick = {
                    if (isWriteMode) {
                        // Cancel write mode
                        NFCViewModel.isWriteMode.value = false
                        haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                        Toast.makeText(context, "Write canceled", Toast.LENGTH_SHORT).show()
                    } else {
                        // Enter write mode
                        if (inputText.isNotBlank()) {
                            NFCViewModel.textToWrite.value = inputText
                            NFCViewModel.lockTagAfterWrite.value = lockTag
                            NFCViewModel.isWriteMode.value = true
                            haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                            haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                        } else {
                            Toast.makeText(context, "Please enter text first", Toast.LENGTH_SHORT).show()
                        }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(60.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (isWriteMode) Color.Red else Color(0xFF6750A4)
                )
            ) {
                Text(
                    text = if (isWriteMode) "Cancel" else "Write to NFC",
                    fontSize = 20.sp
                )
            }
            // LOCK TAG CHECKBOX
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .padding(16.dp)
                    .clickable { onLockTagChanged(!lockTag) } // toggle when Row (or text) tapped
            ) {
                Checkbox(
                    checked = lockTag,
                    onCheckedChange = onLockTagChanged,
                    colors = CheckboxDefaults.colors(checkedColor = Color.Red)
                )
                Text(
                    text = "Lock tag after writing",
                    fontSize = 18.sp,
                    modifier = Modifier.padding(start = 8.dp)
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
                    text = "⚠️ IMPORTANT\nBackup your text! No data loss responsibility. Keep safe copy.",
                    fontSize = 18.sp,
                    lineHeight = 22.sp,
                    color = Color.Black
                    )
            }
            Button(
                onClick = onNavigateBack,
                modifier = Modifier.fillMaxWidth().height(60.dp)
            ) {
                Text("Back to Read/Copy", fontSize = 18.sp)
            }
        }
    }
}

@Preview
@Composable
fun WriteProtectScreenPreview() {
    WriteProtectScreen(
        inputText = "",
        remainingBlocks = 0,
        writtenStrLength = 0,
        lockTag = false,
        onInputTextChanged = {},
        onLockTagChanged = {},
        onNavigateBack = {}
    )
}
