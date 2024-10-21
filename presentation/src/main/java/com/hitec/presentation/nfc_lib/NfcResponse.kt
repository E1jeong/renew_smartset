package com.hitec.presentation.nfc_lib

import android.util.Log
import com.hitec.presentation.nfc_lib.protocol.recv.BdControlAck
import com.hitec.presentation.nfc_lib.protocol.recv.MeterDataReport
import com.hitec.presentation.nfc_lib.protocol.recv.NbConfReport
import com.hitec.presentation.nfc_lib.protocol.recv.NbIdReport
import com.hitec.presentation.nfc_lib.protocol.recv.SnChangeReport
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale
import javax.inject.Inject

class NfcResponse @Inject constructor(
    private val nfcManager: NfcManager,
    private val nfcRequest: NfcRequest,
) {
    companion object {
        private const val TAG = "NfcResponse"

        //this flag is used at DeviceDetailViewModel, and handleBoardControlAck()
        var boardControlAckFlag = 0
        const val BOARD_ACK_FLAG_SLEEP = 1
        const val BOARD_ACK_FLAG_ACTIVE = 2
        const val BOARD_ACK_FLAG_RESET = 3
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

    fun handleNbConfReport(nfcResponse: ByteArray) {
        val response = NbConfReport()
        if (!response.parse(nfcResponse)) {
            return
        }

        when (response.GetReportType()) {
            0 -> readConfig(nfcResponse)
            1 -> writeConfig(nfcResponse)
        }
    }

    private fun readConfig(nfcResponse: ByteArray) {
        val nbConfReportResponse = NbConfReport()
        if (!nbConfReportResponse.parse(nfcResponse)) {
            return
        }

        nfcRequest.reqNbId()
        val nbIdReportResponse = NbIdReport()
        if (!nbIdReportResponse.parse(nfcResponse)) {
            return
        }

        nfcManager.stop()

        val currentTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
        val state = if (nbConfReportResponse.sleepStatus == 1) "sleep" else "active"
        val meterProtocol = parseMeterProtocol(nbConfReportResponse.meterProtocol)

        val resultFlow = StringBuilder()
        resultFlow.append("current time: $currentTime\n")
        resultFlow.append("terminal time: ${nbConfReportResponse.messageTime}\n\n")
        resultFlow.append("state: $state\n\n")
        resultFlow.append("serial number: ${nbConfReportResponse.serialNumber}\n")
        resultFlow.append("imei: ${nbConfReportResponse.GetHeaderSrcDevId()}\n")
        resultFlow.append("imsi: ${nbConfReportResponse.getNbImsi()}\n")
        resultFlow.append("firmware: ${nbConfReportResponse.fwVersion}\n")
        resultFlow.append("battery: ${nbConfReportResponse.batteryVoltage}V\n\n")
        resultFlow.append("service code: ${nbIdReportResponse.serviceCode}\n")
        resultFlow.append("iccid: ${nbIdReportResponse.getIccId()}\n")
        resultFlow.append("server ip/port: ${nbConfReportResponse.serverIp}:${nbConfReportResponse.serverPort}\n\n")
        resultFlow.append("report interval: ${nbConfReportResponse.reportInterval}H\n")
        resultFlow.append("meter interval: ${nbConfReportResponse.meterInterval}H\n")
        resultFlow.append("meter count: ${nbConfReportResponse.meterCount}ea\n")
        resultFlow.append("meter protocol: $meterProtocol")

        updateStateFlow(resultFlow.toString())
        Log.i(TAG, "readConfig ==> result:$resultFlow")
    }

    private fun writeConfig(nfcResponse: ByteArray) {
        nfcManager.stop()

        val response = NbConfReport()
        if (!response.parse(nfcResponse)) {
            return
        }

        val resultFlow = StringBuilder("Write config\n")

        val errorCode = response.GetErrorCode()
        when (errorCode) {
            0 -> resultFlow.append("Success")
            1 -> resultFlow.append("Error: Selected device in app is different from tagged device")
            2 -> resultFlow.append("Error: Meter or Report interval")
            3 -> resultFlow.append("Error: Meter type")
            4 -> resultFlow.append("Error: Meter port")
            else -> resultFlow.append("")
        }

        updateStateFlow(resultFlow.toString())
        Log.i(TAG, "writeConfig ==> result:$resultFlow")
    }

    //handle: set sleep, set active, reset device etc
    fun handleBoardControlAck(nfcResponse: ByteArray?) {
        nfcManager.stop()

        val response = BdControlAck()
        if (!response.parse(nfcResponse)) {
            return
        }

        var resultFlow = ""
        when (boardControlAckFlag) {
            BOARD_ACK_FLAG_SLEEP -> resultFlow = if (response.sleepOrActive == 1) "Sleep success" else "Fail"
            BOARD_ACK_FLAG_ACTIVE -> resultFlow = if (response.sleepOrActive == 2) "Active success" else "Fail"
            BOARD_ACK_FLAG_RESET -> resultFlow = "Reset success"
        }

        updateStateFlow(resultFlow)
        Log.i(TAG, "setSleep ==> result:$resultFlow")
        boardControlAckFlag = 0 //init flag
    }

    fun readMeter(nfcResponse: ByteArray?) {
        nfcManager.stop()

        val response = MeterDataReport()
        if (!response.parse(nfcResponse)) {
            return
        }

        if (response.GetMeterState() == 2) {
            updateStateFlow("Modem is working. Please do later")
            return
        }

        val meterCount = response.GetNumOfMeter()
        val meterValue = response.GetMeterValue(0)
        val meterSerial = response.GetMeterSerial(0)
        val meterCaliber = response.GetMeterCaliber(0)
        val meterProtocol = parseMeterProtocol(response.GetMeterType(0))

        val resultFlow = StringBuilder("Metering Success\n\n")
        resultFlow.append("meter serial: $meterSerial\n")
        resultFlow.append("meter value: ${meterValue}t\n")
        resultFlow.append("meter caliber: ${meterCaliber}mm\n")
        resultFlow.append("meter protocol: $meterProtocol")

        updateStateFlow(resultFlow.toString())
        Log.i(TAG, "readMeter ==> result:$resultFlow")
    }

    private fun parseMeterProtocol(protocol: Int): String {
        val hexProtocol = Integer.toHexString(protocol).uppercase(Locale.US).padStart(2, '0')
        return when (hexProtocol) {
            "00" -> "Unknown"
            "01" -> "STD Digital"
            "10" -> "HT Digital"
            "11" -> "HT-SH Digital"
            "20" -> "SH Digital"
            "22" -> "Big SH Digital"
            "30" -> "MnS Digital"
            "43" -> "Modbus YK Flow"
            "50" -> "OneTL Digital"
            "B2" -> "ICM DPLC"
            else -> "Unknown"
        }
    }
}