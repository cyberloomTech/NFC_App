package com.example.nfcapp.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel

class NFCViewModel : ViewModel() {
    companion object {
        val sharedText = mutableStateOf("Ready to read NFC tag...")
        val isWriteMode = mutableStateOf(false)
        val textToWrite = mutableStateOf<String?>(null)
        val lockTagAfterWrite = mutableStateOf(false)
    }
}
