package com.example.nfcapp.ui

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
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
    onInputTextChanged: (String) -> Unit,
    onNavigateBack: () -> Unit
) {
    val haptic = LocalHapticFeedback.current
    val isWriteMode by NFCViewModel.isWriteMode
    val lockTagAfterWrite by NFCViewModel.lockTagAfterWrite
    val context = LocalContext.current
    var showConfirmDialog by remember { mutableStateOf(false) }
    val onLockTagChanged = { newState: Boolean -> NFCViewModel.lockTagAfterWrite.value = newState }
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
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                // Status text
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(40.dp)
                ) {
                    if (isWriteMode) {
                        CircularProgressIndicator(
                            modifier = Modifier
                                .size(36.dp)
                                .padding(end = 8.dp),
                            strokeWidth = 3.dp,
                            color = Color(0xFF1F95F2)
                        )
                        Text(
                            text = "Please tap to NFC tag",
                            fontSize = 22.sp,
                            textAlign = TextAlign.Center
                        )
                    } else {
                        Text(
                            text = "Enter text to write",
                            fontSize = 22.sp,
                            textAlign = TextAlign.Center
                        )
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

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
//                Box(
//                    modifier = Modifier
//                        .padding(18.dp, 10.dp)
//                ) {
//                    Text(
//                        text = "$writtenStrLength / 888\nRemained $remainingBlocks blocks",
//                        fontSize = 18.sp,
//                        textAlign = TextAlign.End,
//                        modifier = Modifier.fillMaxWidth()
//                    )
//                }

                Spacer(modifier = Modifier.height(15.dp))

                // WRITE / CANCEL BUTTON
                Button(
                    onClick = {
                        if (isWriteMode) {
                            // Cancel write mode
                            NFCViewModel.isWriteMode.value = false
                            haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                            Toast.makeText(context, "CANCELED", Toast.LENGTH_SHORT).show()
                        } else {
                            // Enter write mode
                            if (inputText.isNotBlank()) {
                                NFCViewModel.textToWrite.value = inputText
                                NFCViewModel.isWriteMode.value = true
                                haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                                haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                            } else {
                                Toast.makeText(
                                    context,
                                    "Please enter text first",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                    },
                    shape = RoundedCornerShape(20.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (isWriteMode) Color.Red else Color(0xFF1F95F2)
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(60.dp),
                ) {
                    Text(
                        text = if (isWriteMode) "CANCEL" else "WRITE NFC TAG",
                        fontSize = 28.sp
                    )
                }

                Spacer(modifier = Modifier.height(15.dp))

                // LOCK TAG CHECKBOX
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null,
                            onClick = {
                                if (!lockTagAfterWrite) {
                                    showConfirmDialog = true
                                } else {
                                    onLockTagChanged(false)
                                }
                            }
                        )
                ) {
                    Checkbox(
                        checked = lockTagAfterWrite,
                        onCheckedChange = null,
                        colors = CheckboxDefaults.colors(checkedColor = Color.Red)
                    )
                    Text(
                        text = "LOCK NFC TAG",
                        fontSize = 22.sp,
                        modifier = Modifier.padding(start = 8.dp)
                    )
                }
            }
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp)
                    .border(
                        width = 2.dp,
                        color = Color(0xFFFF9800), // orange
                        shape = RoundedCornerShape(20.dp)
                    )
                    .background(
                        color = Color(0xFFFFF59D), // light yellow
                        shape = RoundedCornerShape(20.dp)
                    )
                    .padding(10.dp)
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "IMPORTANT",
                        fontSize = 22.sp,
                        lineHeight = 22.sp,
                        textAlign = TextAlign.Center,
                        color = Color(0xFF3D3D3C),
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Back up your text first.\nSave a copy on paper or in a secure place. It cannot be recovered.",
                        fontSize = 20.sp,
                        lineHeight = 22.sp,
                        textAlign = TextAlign.Center,
                        color = Color(0xFF3D3D3C)
                    )
                }
            }
        }
        ConfirmDialog(
            show = showConfirmDialog,
            onDismiss = { showConfirmDialog = false },
            onConfirm = {
                onLockTagChanged(true) // This is where the checkbox state is actually changed
            },
            title = "Permanent Action",
            content = "Locking a tag is irreversible and the data cannot be changed again. Are you sure you want to proceed?",
            confirmText = "Yes, Lock It",
            dismissText = "Cancel"
        )
    }
}

@Composable
fun ConfirmDialog(
    show: Boolean,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit,
    title: String,
    content: String,
    confirmText: String = "Yes",
    dismissText: String = "No"
) {
    if (show) {
        AlertDialog(
            onDismissRequest = onDismiss,
            title = { Text(text = title, fontWeight = FontWeight.Bold, fontSize = 20.sp) },
            text = { Text(text = content, fontSize = 18.sp) },
            confirmButton = {
                Button(
                    onClick = {
                        onConfirm()
                        onDismiss()
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Red)
                ) {
                    Text(confirmText)
                }
            },
            dismissButton = {
                Button(
                    onClick = onDismiss,
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Gray)
                ) {
                    Text(dismissText)
                }
            }
        )
    }
}

@Preview
@Composable
fun WriteProtectScreenPreview() {
    WriteProtectScreen(
        inputText = "",
        remainingBlocks = 0,
        writtenStrLength = 0,
        onInputTextChanged = {},
        onNavigateBack = {}
    )
}
