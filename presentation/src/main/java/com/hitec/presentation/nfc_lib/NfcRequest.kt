package com.hitec.presentation.nfc_lib

import android.util.Log
import com.hitec.presentation.nfc_lib.protocol.NfcConstant
import com.hitec.presentation.nfc_lib.protocol.send.AccountNoSet
import com.hitec.presentation.nfc_lib.protocol.send.BdControlReq
import com.hitec.presentation.nfc_lib.protocol.send.ChangeMinuteIntervalReq
import com.hitec.presentation.nfc_lib.protocol.send.CheckSubTerm
import com.hitec.presentation.nfc_lib.protocol.send.FlashDataReq
import com.hitec.presentation.nfc_lib.protocol.send.FlashDateListReq
import com.hitec.presentation.nfc_lib.protocol.send.FwUpdateReq
import com.hitec.presentation.nfc_lib.protocol.send.GsmChangeDomainReq
import com.hitec.presentation.nfc_lib.protocol.send.MeterReq
import com.hitec.presentation.nfc_lib.protocol.send.NbConfSet
import com.hitec.presentation.nfc_lib.protocol.send.NbIdReq
import com.hitec.presentation.nfc_lib.protocol.send.NbIdSet
import com.hitec.presentation.nfc_lib.protocol.send.NodeConfReq
import com.hitec.presentation.nfc_lib.protocol.send.PeriodMeterAck
import com.hitec.presentation.nfc_lib.protocol.send.PeriodMeterReq
import com.hitec.presentation.nfc_lib.protocol.send.SelectGsmOrLteReq
import com.hitec.presentation.nfc_lib.protocol.send.ServerConnectReq
import com.hitec.presentation.nfc_lib.protocol.send.SetTimeInfo
import com.hitec.presentation.nfc_lib.protocol.send.SmartCertiCalibrationReq
import com.hitec.presentation.nfc_lib.protocol.send.SmartCertiCalibrationSet
import com.hitec.presentation.nfc_lib.protocol.send.SmartConfAutoStart
import com.hitec.presentation.nfc_lib.protocol.send.SmartConfMeterReq
import com.hitec.presentation.nfc_lib.protocol.send.SmartConfReq
import com.hitec.presentation.nfc_lib.protocol.send.SmartConfSet
import com.hitec.presentation.nfc_lib.protocol.send.SmartMeterReq
import com.hitec.presentation.nfc_lib.protocol.send.SmartMeterValueSet
import com.hitec.presentation.nfc_lib.protocol.send.SmartMeterValveControl
import com.hitec.presentation.nfc_lib.protocol.send.SmartUltraCompSet
import com.hitec.presentation.nfc_lib.protocol.send.SnChangeReq
import com.hitec.presentation.nfc_lib.util.ConstNfc
import javax.inject.Inject

class NfcRequest @Inject constructor(
    private val nfcManager: NfcManager
) {
    companion object {
        private const val TAG = "NfcRequest"
    }

    // 장비 설정 정보 요청
    fun nodeConfig() {
        Log.v(TAG, "nodeConfig")
        val req = NodeConfReq()
        nfcManager.sendData(req, ConstNfc.NFC_RESP_WAIT_TIME_DEFAULT, 0)
    }

    //NB-IOT Config 설정(Version 1, 3, 5)
    fun setNbConfig(
        msgVersion: Int,
        consumeHouseNo: String,
        serialNo: String?,
        sleepMode: Int,
        amiMeteringInterval: Int,
        amiReportInterval: Int,
        terminalProtocol: Int,
        strServiceCode: String?,
        strServerIp: String?,
        strServerPort: String?,
        meterNum: Int,
        meterType0: Int,
        meterPort0: Int,
        meterType1: Int,
        meterPort1: Int,
        meterType2: Int,
        meterPort2: Int
    ) {
        Log.v(TAG, "setNbConfig")
        val req = NbConfSet(
            msgVersion,
            serialNo,
            sleepMode,
            amiMeteringInterval,
            amiReportInterval,
            terminalProtocol,
            strServiceCode,
            strServerIp,
            strServerPort,
            meterNum,
            meterType0,
            meterPort0,
            meterType1,
            meterPort1,
            meterType2,
            meterPort2
        )
        nfcManager.consumeHouseNo = consumeHouseNo
        nfcManager.sendData(req, ConstNfc.NFC_RESP_WAIT_TIME_NODE_CONF_SET, 0)
    }

    //NB-IOT Config2 설정(Version 2, 4, 6)
    fun setNbConfigMaster(
        msgVersion: Int,
        consumeHouseNo: String,
        serialNo: String?,
        sleepMode: Int,
        amiMeteringInterval: Int,
        amiReportInterval: Int,
        amiReportRange: Int,
        strServiceCode: String?,
        strServerIp: String?,
        strServerPort: String?,
        pan: String?,
        nwk: String?,
        strSubId: String?
    ) {
        Log.v(TAG, "setNbConfigMaster")
        val req = NbConfSet(
            msgVersion,
            serialNo,
            sleepMode,
            amiMeteringInterval,
            amiReportInterval,
            amiReportRange,
            strServiceCode,
            strServerIp,
            strServerPort,
            pan,
            nwk,
            strSubId
        )
        nfcManager.consumeHouseNo = consumeHouseNo
        nfcManager.sendData(req, ConstNfc.NFC_RESP_WAIT_TIME_NODE_CONF_SET, 0)
    }

    //수용가번호 설정
    fun setAccountNo(consumeHouseNo: String) {
        Log.v(TAG, "setAccountNo")
        val req = AccountNoSet(consumeHouseNo)
        nfcManager.consumeHouseNo = consumeHouseNo
        nfcManager.sendData(req, ConstNfc.NFC_RESP_WAIT_TIME_NODE_CONF_SET, 0)
    }

    //Meter 검침요청
    fun readMeter(meterPort: Int = 1) {
        Log.v(TAG, "readMeter")
        val req = MeterReq(meterPort)
        val startWaitTime = if (nfcManager.isResponseConnected) {
            ConstNfc.NFC_START_WAIT_TIME_READ_METER
        } else {
            0
        }
        nfcManager.sendData(req, ConstNfc.NFC_RESP_WAIT_TIME_DEFAULT, startWaitTime)
    }

    //기간 검침요청
    fun reqPeriodMeterData(
        meterPort: Int,
        dateFrom: String?,
        dateTo: String?
    ) {
        Log.v(TAG, "reqPeriodMeterData")
        val req = PeriodMeterReq(meterPort, dateFrom, dateTo)
        nfcManager.sendData(req, ConstNfc.NFC_RESP_WAIT_TIME_DEFAULT, 0)
    }

    //기간 검침 응답 Ack
    fun ackPeriodMeterData(totalBlock: Int, currentBlock: Int) {
        Log.v(TAG, "ackPeriodMeterData")
        val req = PeriodMeterAck(totalBlock, currentBlock)
        nfcManager.sendData(req, ConstNfc.NFC_RESP_WAIT_TIME_PERIOD_ACK, 0)
    }

    //Flash Date List 요청
    fun reqFlashDateList() {
        Log.v(TAG, "reqFlashDateList")
        val req = FlashDateListReq()
        nfcManager.sendData(req, ConstNfc.NFC_RESP_WAIT_TIME_DEFAULT, 0)
    }

    //Flash Data 검침요청
    fun reqFlashData(dateFrom: String?, dateTo: String?) {
        Log.v(TAG, "reqFlashData")
        val req = FlashDataReq(dateFrom, dateTo)
        nfcManager.sendData(req, ConstNfc.NFC_RESP_WAIT_TIME_DEFAULT, 0)
    }

    //서버 접속요청
    fun reqServerConnect(reqType: Int) {
        Log.v(TAG, "reqServerConnect")
        val req = ServerConnectReq(reqType)
        nfcManager.sendData(req, ConstNfc.NFC_RESP_WAIT_TIME_DEFAULT, 0)
    }

    fun resetDevice() {
        Log.v(TAG, "resetDevice")
        val req = BdControlReq(NfcConstant.CONF_BD_RESET_NOW, NfcConstant.CONF_SLEEP_STATE_NONE)
        nfcManager.sendData(req, ConstNfc.NFC_RESP_WAIT_TIME_DEFAULT, 0)
    }

    //장비 정보 요청
    fun reqDeviceInfo() {
        Log.v(TAG, "reqDeviceInfo")
        val req = SmartMeterReq()
        nfcManager.sendData(req, ConstNfc.NFC_RESP_WAIT_TIME_DEFAULT, 0)
    }

    //하위 단말기 상태 요청
    fun checkSubTerm() {
        Log.v(TAG, "checkSubTerm")
        val req = CheckSubTerm()
        nfcManager.sendData(req, ConstNfc.NFC_RESP_WAIT_TIME_DEFAULT, 0)
    }

    //NB-IoT ID 요청
    fun reqNbId() {
        Log.v(TAG, "reqNbId")
        val req = NbIdReq()
        nfcManager.sendData(req, ConstNfc.NFC_RESP_WAIT_TIME_DEFAULT, 0)
    }

    //NB-IoT 서비스코드 설정
    fun writeNbId(serviceCode: String?) {
        Log.v(TAG, "writeNbId")
        val req = NbIdSet(serviceCode)
        nfcManager.sendData(req, ConstNfc.NFC_RESP_WAIT_TIME_DEFAULT, 0)
    }

    //장비 일련번호 변경 요청
    fun changeSerial(serialNumber: String?, length: Int) {
        Log.v(TAG, "changeSerial")
        val req = SnChangeReq(serialNumber, length)
        nfcManager.sendData(req, ConstNfc.NFC_RESP_WAIT_TIME_DEFAULT, 0)
    }

    //주기보고 분단위 변경
    fun changeMinuteInterval(value: Int) {
        Log.v(TAG, "changeMinuteInterval")
        val req = ChangeMinuteIntervalReq(value)
        nfcManager.sendData(req, ConstNfc.NFC_RESP_WAIT_TIME_DEFAULT, 0)
    }

    //gsm 단말기에서 gsm or lte 모드 변경
    fun selectGsmOrLte(value: Int) {
        Log.v(TAG, "selectGsmOrLte")
        val req = SelectGsmOrLteReq(value)
        nfcManager.sendData(req, ConstNfc.NFC_RESP_WAIT_TIME_DEFAULT, 0)
    }

    //gsm 단말기에서 도메인 변경
    fun changeDomain(domain: String?) {
        Log.v(TAG, "changeDomain")
        val req = GsmChangeDomainReq(domain)
        nfcManager.sendData(req, ConstNfc.NFC_RESP_WAIT_TIME_DEFAULT, 0)
    }

    //Fw Update 설정
    fun reqFwUpdate(
        serialNo: String?,
        reqMode: Int,
        fwVersion: String?
    ) {
        Log.v(TAG, "reqFwUpdate")
        val req = FwUpdateReq(serialNo, reqMode, fwVersion)
        nfcManager.sendData(req, ConstNfc.NFC_RESP_WAIT_TIME_DEFAULT, 0)
    }

    //SmartMeter 검침요청
    fun reqSmartMeterData() {
        Log.v(TAG, "reqSmartMeterData")
        val req = SmartMeterReq()
        nfcManager.sendData(req, ConstNfc.NFC_RESP_WAIT_TIME_SMART_METER, 0)
    }

    //SmartMeter 카운터 설정
    fun setSmartMeterCount(count: Int) {
        Log.v(TAG, "setSmartMeterCount")
        val req = SmartMeterValveControl(count)
        nfcManager.sendData(req, ConstNfc.NFC_RESP_WAIT_TIME_SMART_METER, 0)
    }

    //SmartMeter Config 요청
    fun reqSmartConfData() {
        Log.v(TAG, "reqSmartConfData")
        val req = SmartConfReq()
        nfcManager.sendData(req, ConstNfc.NFC_RESP_WAIT_TIME_SMART_METER, 0)
    }

    //SmartMeter 검침 Config 요청
    fun reqSmartConfMeterData() {
        Log.v(TAG, "reqSmartConfMeterData")
        val req = SmartConfMeterReq()
        nfcManager.sendData(req, ConstNfc.NFC_RESP_WAIT_TIME_SMART_METER, 0)
    }

    //SmartMeter Config SN 설정
    fun setSmartConfSnData(
        nFlowType: Int,
        strDeviceSerial: String?,
        nMeterCaliber: Int,
        strMeterSerial: String,
        nMaker: Int
    ) {
        Log.v(TAG, "setSmartConfSnData strMeterSerial:$strMeterSerial")
        val req = SmartConfSet(
            NfcConstant.SMART_METER_CHANGE_MODE_WRITE_SN,
            nFlowType,
            strDeviceSerial,
            nMeterCaliber,
            strMeterSerial,
            0,
            0,
            0,
            0,
            0,
            0,
            nMaker
        )
        nfcManager.sendData(req, ConstNfc.NFC_RESP_WAIT_TIME_SMART_METER, 0)
    }

    //SmartMeter Config Calibration 설정
    fun setSmartConfCalibrationData(
        nFlowType: Int,
        strDeviceSerial: String?,
        nMeterCaliber: Int,
        strMeterSerial: String?,
        nQ3Value: Int,
        nQtValue: Int,
        nQsValue: Int,
        nQ2Value: Int,
        nQ1Value: Int,
        nTemperature: Int,
        nMaker: Int
    ) {
        Log.v(TAG, "setSmartConfCalibrationData")
        val req = SmartConfSet(
            NfcConstant.SMART_METER_CHANGE_MODE_WRITE_METER,
            nFlowType,
            strDeviceSerial,
            nMeterCaliber,
            strMeterSerial,
            nQ3Value,
            nQtValue,
            nQsValue,
            nQ2Value,
            nQ1Value,
            nTemperature,
            nMaker
        )
        nfcManager.sendData(req, ConstNfc.NFC_RESP_WAIT_TIME_SMART_METER, 0)
    }

    //SmartMeter Config 초음파 보정 설정
    fun setSmartUltraCompData(
        strDeviceSerial: String?,
        nCompSelect: Int,
        nCompOffset: Int,
        nCompValue: Int
    ) {
        Log.v(TAG, "setSmartUltraCompData")
        val req = SmartUltraCompSet(
            strDeviceSerial,
            nCompSelect,
            nCompOffset,
            nCompValue
        )
        nfcManager.sendData(req, ConstNfc.NFC_RESP_WAIT_TIME_SMART_METER, 0)
    }

    //SmartMeter Config certification Calibration 설정
    fun setSmartCertiCalibrationData(
        strDeviceSerial: String?,
        nCompSelect: Int,
        nCompValue: Int
    ) {
        Log.v(TAG, "setSmartCertiCalibrationData")
        val req = SmartCertiCalibrationSet(
            strDeviceSerial,
            nCompSelect,
            nCompValue
        )
        nfcManager.sendData(req, ConstNfc.NFC_RESP_WAIT_TIME_SMART_METER, 0)
    }

    //SmartMeter Config certification Calibration 요청
    fun reqSmartCertiCalibrationData() {
        Log.v(TAG, "reqSmartCertiCalibrationData ")
        val req = SmartCertiCalibrationReq()
        nfcManager.sendData(req, ConstNfc.NFC_RESP_WAIT_TIME_SMART_METER, 0)
    }

    //SmartMeter 온도 설정
    fun setSmartTemperatureData(
        flowType: Int,
        strDeviceSerial: String,
        meterCaliber: Int,
        temperature: Int,
        maker: Int
    ) {
        Log.v(TAG, "setSmartTemperatureData strDeviceSerial:$strDeviceSerial")
        val req = SmartConfSet(
            NfcConstant.SMART_METER_CHANGE_MODE_WRITE_METER,
            flowType,
            strDeviceSerial,
            meterCaliber,
            "",
            0,
            0,
            0,
            0,
            0,
            temperature,
            maker
        )
        nfcManager.sendData(req, ConstNfc.NFC_RESP_WAIT_TIME_SMART_METER, 0)
    }

    //SmartMeter 자동보정
    fun setSmartAutoStart(
        nFlowType: Int,
        strDeviceSerial: String,
        nCompMode: Int,
        nCompSelect: Int,
        nQnFlow: Int
    ) {
        Log.v(TAG, "setSmartAutoStart strDeviceSerial:$strDeviceSerial")
        val req = SmartConfAutoStart(
            nFlowType,
            strDeviceSerial,
            nCompMode,
            nCompSelect,
            nQnFlow
        )
        val writeWaitTime = if (strDeviceSerial.isNotEmpty()) {
            ConstNfc.NFC_RESP_WAIT_TIME_SMART_METER
        } else {
            ConstNfc.NFC_RESP_WAIT_TIME_DEFAULT
        }
        nfcManager.sendData(req, writeWaitTime, 0)
    }

    //SmartMeter 검침값 설정
    fun setSmartMeterValueData(
        strDeviceSerial: String?,
        strMeterValue: String?
    ) {
        Log.v(TAG, "setSmartMeterValueData")
        val req = SmartMeterValueSet(
            strDeviceSerial,
            strMeterValue
        )
        nfcManager.sendData(req, ConstNfc.NFC_RESP_WAIT_TIME_SMART_METER, 0)
    }

    fun setSleepOrActive(sleepOrActive: Int) {
        Log.v(TAG, "setSleepOrActive")
        val req = BdControlReq(NfcConstant.CONF_BD_RESET_NONE, sleepOrActive)
        nfcManager.sendData(req, ConstNfc.NFC_RESP_WAIT_TIME_DEFAULT, 0)
    }

    //Report Mode 설정/해제
    fun setReportMode(reportMode: Int) {
        Log.v(TAG, "setReportMode")
        val req = BdControlReq(
            NfcConstant.CONF_BD_RESET_NONE,
            NfcConstant.CONF_SLEEP_STATE_NONE,
            reportMode
        )
        nfcManager.sendData(req, ConstNfc.NFC_RESP_WAIT_TIME_DEFAULT, 0)
    }

    //기간검침  설정/해제
    fun setPeriodMode(periodMode: Int) {
        Log.v(TAG, "setPeriodMode")
        val req = BdControlReq(
            NfcConstant.CONF_BD_RESET_NONE,
            NfcConstant.CONF_SLEEP_STATE_NONE,
            NfcConstant.CONF_REPORT_MODE_NONE,
            periodMode
        )
        nfcManager.sendData(req, ConstNfc.NFC_RESP_WAIT_TIME_DEFAULT, 0)
    }

    //디버그모드  설정/해제
    fun setDebugMode(debugMode: Int) {
        Log.v(TAG, "setDebugMode")
        val req = BdControlReq(
            NfcConstant.CONF_BD_RESET_NONE,
            NfcConstant.CONF_SLEEP_STATE_NONE,
            NfcConstant.CONF_REPORT_MODE_NONE,
            NfcConstant.CONF_PERIOD_MODE_NONE,
            debugMode
        )
        nfcManager.sendData(req, ConstNfc.NFC_RESP_WAIT_TIME_DEFAULT, 0)
    }

    //Data Skip  설정/해제
    fun setDataSkipMode(dataSkipMode: Int) {
        Log.v(TAG, "setDataSkipMode")
        val req = BdControlReq(
            NfcConstant.CONF_BD_RESET_NONE,
            NfcConstant.CONF_SLEEP_STATE_NONE,
            NfcConstant.CONF_REPORT_MODE_NONE,
            NfcConstant.CONF_PERIOD_MODE_NONE,
            NfcConstant.CONF_DEBUG_MODE_NONE,
            dataSkipMode
        )
        nfcManager.sendData(req, ConstNfc.NFC_RESP_WAIT_TIME_DEFAULT, 0)
    }

    //장비 시간 설정
    fun setTimeInfo() {
        Log.v(TAG, "setTimeInfo")
        val req = SetTimeInfo()
        nfcManager.sendData(req, ConstNfc.NFC_RESP_WAIT_TIME_DEFAULT, 0)
    }
}