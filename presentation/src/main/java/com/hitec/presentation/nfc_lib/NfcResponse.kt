package com.hitec.presentation.nfc_lib

import android.util.Log
import com.hitec.presentation.nfc_lib.protocol.recv.SnChangeReport
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

class NfcResponse @Inject constructor(
    private val nfcManager: NfcManager
) {
    companion object {
        private const val TAG = "NfcResponse"
    }

    private val _nfcResultFlow = MutableStateFlow("Tag Nfc")
    val nfcResultFlow: StateFlow<String> = _nfcResultFlow

    // Force to emit a value if it is equal to the current value
    private fun updateStateFlow(resultFlow: String) {
        if (_nfcResultFlow.value == resultFlow) {
            _nfcResultFlow.value = ""
            _nfcResultFlow.value = resultFlow
        } else {
            _nfcResultFlow.value = resultFlow
        }
    }

    fun changeSerial(nfcResponse: ByteArray?) {
        nfcManager.stop()

        val response = SnChangeReport()
        if (!response.parse(nfcResponse)) {
            return
        }

        val resultFlow = when (response.resultCode) {
            0 -> "Success"
            3 -> "The input serial number is same"
            else -> "Fail"
        }

        updateStateFlow(resultFlow)
        Log.i(TAG, "changeSerial ==> result:$resultFlow")
    }
}