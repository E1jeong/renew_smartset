package com.hitec.presentation.nfc_lib

import android.util.Log
import com.hitec.presentation.nfc_lib.model.ChangeSerial
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

    private val _nfcResponseFlow = MutableStateFlow<Any?>(null)
    val nfcResponseFlow: StateFlow<Any?> = _nfcResponseFlow

    fun changeSerial(nfcResponse: ByteArray?) {
        val response = SnChangeReport()
        if (!response.parse(nfcResponse)) {
            return
        }

        nfcManager.stop()

        val result = ChangeSerial(response.result)
        _nfcResponseFlow.value = result
        Log.i(TAG, "changeSerial ==> result:$result")
    }
}
