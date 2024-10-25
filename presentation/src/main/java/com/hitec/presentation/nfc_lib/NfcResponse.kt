package com.hitec.presentation.nfc_lib

import android.util.Log
import com.hitec.presentation.main.device_detail.DeviceDetailViewModel
import com.hitec.presentation.nfc_lib.model.WriteConfig
import com.hitec.presentation.nfc_lib.protocol.recv.BdControlAck
import com.hitec.presentation.nfc_lib.protocol.recv.ChangeMinuteIntervalReport
import com.hitec.presentation.nfc_lib.protocol.recv.FwUpdateReport
import com.hitec.presentation.nfc_lib.protocol.recv.MeterDataReport
import com.hitec.presentation.nfc_lib.protocol.recv.NbConfReport
import com.hitec.presentation.nfc_lib.protocol.recv.NbIdReport
import com.hitec.presentation.nfc_lib.protocol.recv.ServerConnectReport
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

        var serverCommFlag = 0
        const val SERVER_COMM_FLAG_REQUEST = 1
        const val SERVER_COMM_FLAG_CHECK = 2
    }

    private val _nfcResultFlow = MutableStateFlow("Tag Nfc")
    val nfcResultFlow: StateFlow<String> = _nfcResultFlow

    private var _nfcWriteConfigFlow = MutableStateFlow(
        WriteConfig(
            serialNumber = "",
            terminalProtocol = 0,
            imsi = "",
            serverIp = "",
            serverPort = "",
            firmwareVersion = "",
            reportInterval = 0,
            meterInterval = 0,
            meterCount = 0,
            meterInfo = emptyList()
        )
    )
    val nfcWriteConfigFlow: StateFlow<WriteConfig> = _nfcWriteConfigFlow

    // Force to emit a value if it is equal to the current value
    private fun updateResultFlow(resultFlow: String) {
        if (_nfcResultFlow.value == resultFlow) {
            _nfcResultFlow.value = ""
            _nfcResultFlow.value = resultFlow
        } else {
            _nfcResultFlow.value = resultFlow
        }
    }

    private fun updateWriteConfigFlow(resultFlow: WriteConfig) {
        if (_nfcWriteConfigFlow.value == resultFlow) {
            _nfcWriteConfigFlow.value = WriteConfig(
                serialNumber = "",
                terminalProtocol = 0,
                imsi = "",
                serverIp = "",
                serverPort = "",
                firmwareVersion = "",
                reportInterval = 0,
                meterInterval = 0,
                meterCount = 0,
                meterInfo = emptyList()
            )
            _nfcWriteConfigFlow.value = resultFlow
        } else {
            _nfcWriteConfigFlow.value = resultFlow
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

        updateResultFlow(resultFlow)
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

        updateResultFlow(resultFlow.toString())
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
        val errorMessage = when (errorCode) {
            0 -> "Success"
            1 -> "Error: Selected device in app is different from tagged device"
            2 -> "Error: Meter or Report interval"
            3 -> "Error: Meter type"
            4 -> "Error: Meter port"
            else -> ""
        }
        resultFlow.append(errorMessage)
        updateResultFlow(resultFlow.toString())
        Log.i(TAG, "writeConfig ==> result:$resultFlow")

        if (errorCode != 0) {
            return  // Error가 있을 경우 즉시 종료
        }

        val serialNumber = response.serialNumber
        val terminalProtocol = response.GetDataFormat()
        val imsi = response.nbImsi
        val firmwareVersion = response.fwVersion
        val serverIp = response.serverIp
        val serverPort = response.serverPort
        val reportInterval = response.reportInterval
        val meterInterval = response.meterInterval
        val meterCount = response.meterCount

        // this is for UI update in DeviceDetailScreen
        val writeConfigFlow = WriteConfig(
            serialNumber,
            terminalProtocol,
            imsi,
            serverIp,
            serverPort,
            firmwareVersion,
            reportInterval,
            meterInterval,
            meterCount,
            emptyList()
        )
        updateWriteConfigFlow(writeConfigFlow)
        Log.i(TAG, "write config UI update:\n$writeConfigFlow")
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

        updateResultFlow(resultFlow)
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
            updateResultFlow("Modem is working. Please do later")
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

        updateResultFlow(resultFlow.toString())
        Log.i(TAG, "readMeter ==> result:$resultFlow")
    }

    fun handleServerCommunication(nfcResponse: ByteArray) {
        when (serverCommFlag) {
            SERVER_COMM_FLAG_REQUEST -> requestCommunication(nfcResponse)
            SERVER_COMM_FLAG_CHECK -> checkCommunication(nfcResponse)
        }

        serverCommFlag = 0 // init flag
    }

    private fun requestCommunication(nfcResponse: ByteArray) {
        nfcManager.stop()

        val response = ServerConnectReport()
        if (!response.parse(nfcResponse)) {
            return
        }

        val serverAccessCode = response.GetServerAccess()
        val resultFlow = if (serverAccessCode == 2) "Please try again later" else "Communication start"

        updateResultFlow(resultFlow)
        Log.i(TAG, "requestCommunication ==> result:$resultFlow")
    }

    private fun checkCommunication(nfcResponse: ByteArray) {
        nfcManager.stop()

        val response = ServerConnectReport()
        if (!response.parse(nfcResponse)) {
            return
        }

        val serverAccessCode = response.GetServerAccess()
        val serverResultCode = response.GetServerResult()
        val resultMessage = when (serverAccessCode) {
            0, 2 -> {
                when (serverResultCode) {
                    0 -> "idle"
                    1 -> "Error modem response"
                    2 -> "Error read SIM"
                    4 -> "Fail to connect server"
                    5 -> "Fail to receive time"
                    6 -> "Fail to connect network"
                    7 -> "Fail to connect platform"
                    else -> ""
                }
            }

            1 -> {
                when (serverResultCode) {
                    0 -> "idle"
                    1 -> "Check modem"
                    2 -> "Check SIM"
                    4 -> "Communication is in progress"
                    else -> ""
                }
            }

            else -> {
                ""
            }
        }

        val resultFlow = StringBuilder("Check communication\n\n")
        resultFlow.append("terminal time: ${response.messageTime}\n")
        resultFlow.append("last comm time: ${response.GetLastTime()}\n\n")
        resultFlow.append("result: $resultMessage\n\n")
        resultFlow.append("RSSI: -${response.GetModemRssi()}dBm\n")
        resultFlow.append("RSRP: -${response.GetModemRsrp()}dBm\n")
        resultFlow.append("SNR: ${response.GetModemSnr()}")

        updateResultFlow(resultFlow.toString())
        Log.i(TAG, "checkCommunication ==> result:$resultFlow")
    }

    fun updateFirmware(nfcResponse: ByteArray) {
        val response = FwUpdateReport()
        if (!response.parse(nfcResponse)) {
            return
        }

        when (response.GetResultCode()) {
            2 -> {
                updateResultFlow("Please try again later. communication is in progress")
                return
            }

            3 -> {
                updateResultFlow("Don't support BSL")
                return
            }

            else -> {}
        }

        //In DeviceDetailViewModel nfcRequestUpdateFirmware(), (reqMode = 0) == (response.GetStateCode() == 0)
        //This follows the sequence in the terminal firmware
        if (response.GetStateCode() == 0) {
            nfcRequest.reqFwUpdate(
                response.GetSerialNumber(),
                1,
                DeviceDetailViewModel.userInputFirmware
            )
        }

        nfcManager.stop()

        val resultFlow = "Firmware update start"
        updateResultFlow(resultFlow)
        Log.i(TAG, "updateFirmware ==> result:$resultFlow")

        DeviceDetailViewModel.userInputFirmware = "" // init value
    }

    fun changeRiHourToMinute(nfcResponse: ByteArray) {
        nfcManager.stop()

        val response = ChangeMinuteIntervalReport()
        if (!response.parse(nfcResponse)) {
            return
        }

        val resultFlow = StringBuilder("Change ri hour to minute\n")
        val resultCode = response.result
        val resultMessage = when (resultCode) {
            0 -> "Success"
            1 -> "Error: wrong number"
            2 -> "Error: duplicated number"
            3 -> "changed minute to hour"
            else -> ""
        }
        resultFlow.append(resultMessage)
        updateResultFlow(resultFlow.toString())
        Log.i(TAG, "changeRiHourToMinute ==> result:$resultFlow")
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