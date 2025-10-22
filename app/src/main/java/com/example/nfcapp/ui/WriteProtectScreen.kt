package com.example.nfcapp.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
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
    statusText: String,
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
            Text(
                text = statusText,
                fontSize = 20.sp,
                modifier = Modifier.padding(16.dp)
            )
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
            Button(
                onClick = {
                    NFCViewModel.textToWrite.value = inputText
                    NFCViewModel.lockTagAfterWrite.value = lockTag
                    NFCViewModel.isWriteMode.value = true
                    haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(60.dp)
            ) {
                Text("Write to Tag", fontSize = 18.sp)
            }
            // LOCK TAG CHECKBOX
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(16.dp)
            ) {
                Checkbox(
                    checked = lockTag,
                    onCheckedChange = onLockTagChanged,
                    colors = CheckboxDefaults.colors(checkedColor = Color.Cyan)
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
                    color = Color.Black,

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
        statusText = "Ready to write NFC tag",
        inputText = "",
        remainingBlocks = 0,
        writtenStrLength = 0,
        lockTag = false,
        onInputTextChanged = {},
        onLockTagChanged = {},
        onNavigateBack = {}
    )
}
