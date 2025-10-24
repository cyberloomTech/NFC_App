package com.example.nfcapp

import android.app.PendingIntent
import android.content.Intent
import android.content.IntentFilter
import android.nfc.NfcAdapter
import android.nfc.tech.Ndef
import android.nfc.tech.NdefFormatable
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.example.nfcapp.ui.navigation.PagerNavigation
import com.example.nfcapp.util.NFCWriter
import com.example.nfcapp.viewmodel.NFCViewModel

class MainActivity : ComponentActivity() {

    private var nfcAdapter: NfcAdapter? = null
    private val maxCapacity = 888 // NTAG216 max capacity
    private val _inputText = mutableStateOf("")
    private val _remainingBlocks = mutableIntStateOf(0)
    private val _writtenStrLength = mutableIntStateOf(0)
    private var lockTag by mutableStateOf(false)
    private lateinit var pendingIntent: PendingIntent
    private lateinit var intentFiltersArray: Array<IntentFilter>
    private lateinit var techListsArray: Array<Array<String>>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        nfcAdapter = NfcAdapter.getDefaultAdapter(this)
//        if (nfcAdapter == null) {
//            Toast.makeText(this, "NFC not supported on this device", Toast.LENGTH_LONG).show()
//            finish()
//            return
//        }
        pendingIntent = PendingIntent.getActivity(
            this, 0,
            Intent(this, javaClass).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP),
            PendingIntent.FLAG_MUTABLE
        )

        val ndefDetected = IntentFilter(NfcAdapter.ACTION_TECH_DISCOVERED)
        ndefDetected.addCategory(Intent.CATEGORY_DEFAULT)
        intentFiltersArray = arrayOf(ndefDetected)
        techListsArray = arrayOf(
            arrayOf(Ndef::class.java.name),
            arrayOf(NdefFormatable::class.java.name)
        )

        setContent {
            PagerNavigation(
                inputText = _inputText.value,
                remainingBlocks = _remainingBlocks.intValue,
                writtenStrLength = _writtenStrLength.intValue,
                lockTag = lockTag,
                onInputTextChanged = { newText ->
                    _inputText.value = newText
                    _writtenStrLength.intValue = newText.length
                    _remainingBlocks.intValue = (maxCapacity - newText.length) / 30
                },
                onLockTagChanged = { lockTag = it }
            )
        }
    }

    override fun onResume() {
        super.onResume()
        val intent = Intent(this, javaClass).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
        val pendingIntent = PendingIntent.getActivity(
            this, 0, intent, PendingIntent.FLAG_MUTABLE
        )
        val filters = arrayOf(IntentFilter(NfcAdapter.ACTION_NDEF_DISCOVERED), IntentFilter(NfcAdapter.ACTION_TAG_DISCOVERED))
        val techList = arrayOf(arrayOf<String>(Ndef::class.java.name))
        nfcAdapter?.enableForegroundDispatch(this, pendingIntent, filters, techList)
    }

    override fun onPause() {
        super.onPause()
        nfcAdapter?.disableForegroundDispatch(this)
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        setIntent(intent)

        val tag = intent.getParcelableExtra<android.nfc.Tag>(NfcAdapter.EXTRA_TAG)
        if (tag != null) {
            val ndef = Ndef.get(tag)

            if (NFCViewModel.isWriteMode.value) {
                // Write to NFC Tag
                NFCViewModel.textToWrite.value?.let { text ->
                    NFCWriter.writeNdefText(this, tag, text, NFCViewModel.lockTagAfterWrite.value)
                }
                NFCViewModel.isWriteMode.value = false
                Toast.makeText(this, "Write protected! Ready to use.", Toast.LENGTH_SHORT).show()
            } else {
                // Read from NFC Tag
                ndef?.connect()
                val message = ndef?.cachedNdefMessage
                val records = message?.records
                if (records != null && records.isNotEmpty()) {
                    val payload = records[0].payload
                    val textEncoding =
                        if ((payload[0].toInt() and 128) == 0) Charsets.UTF_8 else Charsets.UTF_16
                    val languageCodeLength = payload[0].toInt() and 63
                    val text = String(
                        payload,
                        languageCodeLength + 1,
                        payload.size - languageCodeLength - 1,
                        textEncoding
                    )
                    NFCViewModel.sharedText.value = text
                }
                ndef?.close()
            }
        }
    }
}
