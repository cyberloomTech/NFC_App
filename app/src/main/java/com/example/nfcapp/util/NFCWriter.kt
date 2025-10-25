package com.example.nfcapp.util

import android.content.Context
import android.nfc.NdefMessage
import android.nfc.NdefRecord
import android.nfc.Tag
import android.nfc.tech.Ndef
import android.nfc.tech.NdefFormatable
import android.widget.Toast
import java.nio.charset.Charset
import java.util.Locale

object NFCWriter {

    fun writeNdefText(
        context: Context,
        tag: Tag,
        text: String,
        lockAfterWrite: Boolean
    ): Boolean {
        try {
            val lang = Locale.getDefault().language
            val langBytes = lang.toByteArray(Charset.forName("US-ASCII"))
            val textBytes = text.toByteArray(Charset.forName("UTF-8"))
            val payload = ByteArray(1 + langBytes.size + textBytes.size)
            payload[0] = langBytes.size.toByte()
            System.arraycopy(langBytes, 0, payload, 1, langBytes.size)
            System.arraycopy(textBytes, 0, payload, 1 + langBytes.size, textBytes.size)

            val record = NdefRecord(
                NdefRecord.TNF_WELL_KNOWN,
                NdefRecord.RTD_TEXT,
                ByteArray(0),
                payload
            )

            val ndefMessage = NdefMessage(arrayOf(record))

            val ndef = Ndef.get(tag)
            if (ndef != null) {
                ndef.connect()
                if (!ndef.isWritable) {
                    Toast.makeText(context, "This NFC tag is read-only.", Toast.LENGTH_SHORT).show()
                    return false
                }

                ndef.writeNdefMessage(ndefMessage)

                if (lockAfterWrite) {
                    ndef.makeReadOnly()
                }

                ndef.close()
                return true
            } else {
                val format = NdefFormatable.get(tag)
                format?.connect()
                format?.format(ndefMessage)
                format?.close()
                return true
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return false
    }
}
