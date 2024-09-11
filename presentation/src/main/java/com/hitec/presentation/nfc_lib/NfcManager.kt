package com.hitec.presentation.nfc_lib

import android.app.Activity
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.media.AudioManager
import android.nfc.NfcAdapter
import android.nfc.Tag
import android.nfc.tech.MifareUltralight
import android.nfc.tech.NfcA
import android.util.Log
import android.widget.Toast
import com.hitec.presentation.main.MainActivity
import com.hitec.presentation.nfc_lib.protocol.NfcConstant.NODE_RECV_ACCOUNT_NO_REPORT
import com.hitec.presentation.nfc_lib.protocol.NfcConstant.NODE_RECV_FLASH_DATE_LIST_REPORT
import com.hitec.presentation.nfc_lib.protocol.NfcConstant.NODE_RECV_SN_CHANGE_REPORT
import com.hitec.presentation.nfc_lib.protocol.NfcConstant.NODE_SEND_ACCOUNT_NO_SET
import com.hitec.presentation.nfc_lib.protocol.NfcConstant.NODE_SEND_FLASH_DATA_REQ
import com.hitec.presentation.nfc_lib.protocol.NfcConstant.NODE_SEND_METER_REQ
import com.hitec.presentation.nfc_lib.protocol.NfcConstant.NODE_SEND_NB_CONF_SET
import com.hitec.presentation.nfc_lib.protocol.NfcConstant.NODE_SEND_PERIOD_METER_REQ
import com.hitec.presentation.nfc_lib.protocol.recv.RxHeader
import com.hitec.presentation.nfc_lib.protocol.send.MeterReq
import com.hitec.presentation.nfc_lib.protocol.send.NfcTxMessage
import com.hitec.presentation.nfc_lib.protocol.send.TxHeader
import com.hitec.presentation.nfc_lib.reader.Ntag_Auth
import com.hitec.presentation.nfc_lib.reader.Ntag_I2C_Connect
import com.hitec.presentation.nfc_lib.util.ConstNfc
import com.hitec.presentation.nfc_lib.util.bLog
import dagger.Lazy
import javax.inject.Inject

class NfcManager @Inject constructor(
    private val nfcResponse: Lazy<NfcResponse>
) {
    companion object {
        @JvmField
        var authStatus: Int = 0

        @JvmField
        var password: ByteArray? = null

        private const val SRAM_SIZE = 64
        private const val PERIOD_BUFFER_SIZE = SRAM_SIZE * 100 * 2
    }

    private lateinit var ntagI2CConnect: Ntag_I2C_Connect

    private lateinit var mActivity: Activity
    private lateinit var mContext: Context
    lateinit var nfcAdapter: NfcAdapter

    private var isRunning = false
    var isResponseConnected = false
    private var requestMessageType = 0
    private var nfcType = ConstNfc.NFC_TYPE_DEFAULT
    var consumeHouseNo = ""

    private lateinit var txBuffer: ByteArray
    private lateinit var rxBuffer: ByteArray
    private lateinit var rxPeriodBuffer: ByteArray
    private lateinit var pendingIntent: PendingIntent

    private var nfcRespWaitTime = 0
    private var nfcStartWaitTime = 0

    fun startNfcManager(context: Context, activity: Activity, nfcType: Int) {
        Log.i("NFC_TEST", "startNfcManager ==> start 01")
        mActivity = activity
        mContext = context
        this.nfcType = nfcType
        isRunning = false

        nfcAdapter = NfcAdapter.getDefaultAdapter(mContext)
        Log.i("NFC_TEST", "startNfcManager nfcAdapter: $nfcAdapter")

        authStatus =
            Ntag_Auth.AuthStatus.Disabled.value // When we open the application by default we set the status to disabled (we don't know the product yet)
        password = Ntag_Auth.Pwds.PWD0.value
        ntagI2CConnect =
            Ntag_I2C_Connect(null, mActivity, null, 0) // Initialize the demo in order to handle tab change events

        txBuffer = ByteArray(SRAM_SIZE)
        rxBuffer = ByteArray(SRAM_SIZE)
        rxPeriodBuffer = ByteArray(PERIOD_BUFFER_SIZE)

        setForeground(mContext)

        mActivity.volumeControlStream = AudioManager.STREAM_MUSIC
    }

    fun closeNfcManager() {
        Log.i("NFC_TEST", "closeNfcManager ==> close 01")
        authStatus = 0
        password = null
        close()
    }

    //    @SuppressLint("InlinedApi")
    //    public void checkNFC() {
    //        if (m_nNfcType == NFC_TYPE_NO) {
    //            return;
    //        }
    //
    //        String strTitle;
    //        String strMsg;
    //        String strBtnOk = "확인";
    //        String strBtnCancel = "취소";
    //
    //        if (nfcAdapter != null) {
    //            if (!nfcAdapter.isEnabled()) {
    //                strTitle = "nfc 미사용";
    //                strMsg = "설정화면으로 이동하시겠습니까?";
    //                new AlertDialog.Builder(mActivity)
    //                        .setTitle(strTitle)
    //                        .setMessage(strMsg)
    //                        .setPositiveButton(
    //                                strBtnOk,
    //                                (dialog, which) -> {
    //                                    if (android.os.Build.VERSION.SDK_INT >= 16) {
    //                                        startActivity(new Intent(android.provider.Settings.ACTION_NFC_SETTINGS));
    //                                    } else {
    //                                        startActivity(new Intent(android.provider.Settings.ACTION_WIRELESS_SETTINGS));
    //                                    }
    //                                }
    //                        )
    //                        .setNegativeButton(
    //                                strBtnCancel,
    //                                (dialog, which) -> {
    //                                    //System.exit(0);
    //                                }
    //                        )
    //                        .show();
    //            }
    //        } else {
    //            //NFC를 사용할 수 없습니다.
    //            strTitle = "nfc를 사용할수 없습니다.";
    //            new AlertDialog.Builder(mActivity)
    //                    .setTitle(strTitle)
    //                    .setNeutralButton(
    //                            strBtnOk,
    //                            (dialog, which) -> {
    //                                //System.exit(0);
    //                            }
    //                    )
    //                    .show();
    //        }
    //    }
    fun nfcAdapterEnableForeground() {
        Log.i("NFC_TEST", "NfcAdapterEnableForeground() => 01")

        val tagDiscoverFilter = IntentFilter(NfcAdapter.ACTION_TAG_DISCOVERED)
        val techDiscoverFilter = IntentFilter(NfcAdapter.ACTION_TECH_DISCOVERED)
        val ndefDiscoveredFilter = IntentFilter(NfcAdapter.ACTION_NDEF_DISCOVERED)
        val intentFilters = arrayOf(tagDiscoverFilter, techDiscoverFilter, ndefDiscoveredFilter)

        val techLists = arrayOf(
            arrayOf(NfcA::class.java.name), arrayOf(MifareUltralight::class.java.name)
        )
        nfcAdapter.enableForegroundDispatch(mActivity, pendingIntent, intentFilters, techLists)
    }

    fun handleIntent(intent: Intent) {
        Log.i("NFC_TEST", "handleIntent isRunning: $isRunning")
        if (!isRunning) return

        val tag = intent.getParcelableExtra<Tag>(NfcAdapter.EXTRA_TAG)
        authStatus = Ntag_Auth.AuthStatus.Disabled.value // Set the initial auth parameters
        password = Ntag_Auth.Pwds.PWD0.value
        isResponseConnected = false

        ntagI2CConnect = Ntag_I2C_Connect(tag, mActivity, password, authStatus)
        if (ntagI2CConnect.isReady) {
            authStatus = ntagI2CConnect.ObtainAuthStatus() // Retrieve Auth Status before doing any operation
            ntagI2CConnect = Ntag_I2C_Connect(tag, mActivity, password, authStatus)
            connectProcess()
        }
    }

    private fun doProcess(intent: Intent) {
        Log.i("NFC_TEST", "doProcess")


    }

    private fun setForeground(context: Context?) {
        Log.i("NFC_TEST", "setNfcForeground - 01")

        pendingIntent = PendingIntent.getActivity(
            context,
            0,
            Intent(context, MainActivity::class.java).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP),
            PendingIntent.FLAG_MUTABLE
        )
    }

    //****************************************************************************
    //******************* GPS, Network
    //****************************************************************************
    private fun connect() {
        Log.i("NFC_TEST", "connectNfc - 01")

        if (authStatus == Ntag_Auth.AuthStatus.Authenticated.value) { // This authentication is added in order to avoid authentication problems with old NFC Controllers
            Log.i("NFC_TEST", "connectNfc - 02")
            ntagI2CConnect.Auth(password, Ntag_Auth.AuthStatus.Protected_RW.value)
        }

        if (ntagI2CConnect.isReady) {
            var authSuccess = true
            if (!isResponseConnected) {
                authSuccess = ntagI2CConnect.Auth(password, authStatus)
            }

            Log.i("NFC_TEST", "authTask = doInBackground => authSuccess:$authSuccess")

            if (authSuccess) {
                var responseSuccess = false
                try {
                    Log.i("NFC_TEST", "connectNfc => NfcDataTransive m_nReqMsgType:$requestMessageType")

                    //설정시 수용가번호 입력
                    if (requestMessageType == NODE_SEND_NB_CONF_SET) {
                        ntagI2CConnect.NfcWriteNdef(consumeHouseNo)
                    }

                    responseSuccess = when (requestMessageType) {
                        NODE_SEND_PERIOD_METER_REQ, NODE_SEND_FLASH_DATA_REQ -> {
                            ntagI2CConnect.NfcPeriodDataTransive(
                                nfcType,
                                isResponseConnected,
                                txBuffer,
                                rxPeriodBuffer,
                                nfcRespWaitTime,
                                nfcStartWaitTime
                            )
                        }

                        NODE_SEND_ACCOUNT_NO_SET -> {
                            ntagI2CConnect.NfcWriteNdef(consumeHouseNo)
                        }

                        else -> {
                            ntagI2CConnect.NfcDataTransive(
                                nfcType,
                                isResponseConnected,
                                txBuffer,
                                rxBuffer,
                                nfcRespWaitTime,
                                nfcStartWaitTime
                            )
                        }
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                    Log.i("NFC_TEST", "connectNfc => Tag_lost")
                }

                if (responseSuccess) isResponseConnected = true

                when (requestMessageType) {
                    NODE_SEND_PERIOD_METER_REQ, NODE_SEND_FLASH_DATA_REQ -> {
                        receivePeriodData(responseSuccess, rxPeriodBuffer)
                    }

                    NODE_SEND_ACCOUNT_NO_SET -> {
                        receiveData(NODE_RECV_ACCOUNT_NO_REPORT, rxBuffer)
                    }

                    else -> {
                        NfcDataResp(responseSuccess, rxBuffer)
                    }
                }
            }
        } else {
            Log.i("NFC_TEST", "connectNfc -  04")
        }
    }

    //****************************************************************************
    //******************* UI
    //****************************************************************************
    private fun NfcDataResp(responseSuccess: Boolean, rxData: ByteArray) {
        Log.i("NFC_TEST", "NfcDataResp => responseSuccess:$responseSuccess")
        if (!responseSuccess) {
            Toast.makeText(mActivity, "read tag failed", Toast.LENGTH_SHORT).show()
            return
        }

        val rxHeader = RxHeader()
        if (!rxHeader.parse(rxData)) {
            return
        }

        bLog.i_hex("NFC_TEST", "NfcDataResp", rxBuffer, rxBuffer.size)

        val msgType = rxHeader.GetNodeMsgType()
        receiveData(msgType, rxBuffer)
    }

    private fun receivePeriodData(responseSuccess: Boolean, rxData: ByteArray) {
        Log.i("NFC_TEST", "NfcPeriodDataResp => responseSuccess:$responseSuccess")
        if (!responseSuccess) {
            Toast.makeText(mActivity, "read tag failed", Toast.LENGTH_SHORT).show()
            return
        }
        var i = 0
        while (i < SRAM_SIZE) {
            rxBuffer[i] = rxData[i]
            i++
        }
        bLog.i_hex("PERIOD", "m_rxBuffer ==> ", rxBuffer, rxBuffer.size)

        if (rxBuffer[0].toInt() == 0x00) return

        val rxHeader = RxHeader()
        if (!rxHeader.parse(rxBuffer)) {
            return
        }

        val msgType = rxHeader.GetNodeMsgType()
        if (msgType == NODE_RECV_FLASH_DATE_LIST_REPORT) {
            receiveData(msgType, rxBuffer)
        } else {
            receiveData(msgType, rxData)
        }
    }

    /**
     * <pre>
     * NFC Lib Start
    </pre> *
     */
    fun start(): Boolean {
        isRunning = true
        isResponseConnected = false
        Log.i("NFC_TEST", "nfclib_start ==> 01")
        try {
            var retry = false

            nfcAdapterEnableForeground()
            if (!nfcAdapter.isEnabled) {
                Log.i("NFC_TEST", "nfclib_start ==> 02")
                retry = true
            }

            if (retry) {
                nfcAdapter = NfcAdapter.getDefaultAdapter(mContext)
                nfcAdapterEnableForeground()
                Log.i("NFC_TEST", "nfclib_retry ==> 03")
                if (!nfcAdapter.isEnabled) {
                    Log.i("NFC_TEST", "nfclib_retry ==> 04")
                    return false
                }
            }
        } catch (e: Exception) {
            Log.i("NFC_TEST", "nfclib_start ==> Exception")
            return false
        }

        return true
    }

    fun restart() {
        isRunning = true
        isResponseConnected = false
    }

    fun stop() {
        isRunning = false
        isResponseConnected = false
        for (i in 0 until SRAM_SIZE) {
            txBuffer[i] = 0x00
        }
    }

    fun close() {
        isRunning = false
        isResponseConnected = false
    }

    private fun receiveData(msgType: Int, rxData: ByteArray) {
        Log.i("NFC", "nfcReceiveEvent ==> 01 msgType:$msgType")

        when (msgType) {
            NODE_RECV_SN_CHANGE_REPORT -> nfcResponse.get().changeSerial(rxData)
            else -> {}
        }
    } //

    protected fun nfclib_EvtTimeOutEvent(nResult: Int) {
    } //

    private fun connectProcess() {
        if (isResponseConnected) {
            checkSendData()
        }
        connect()
    }

    /*
     * 메세지에 시간정보 업데이트
     */
    private fun updateTxTime(msgLen: Int, timePos: Int, timeData: ByteArray) {
        var i = 0
        while (i < 7) {
            txBuffer[timePos + i] = timeData[i]
            i++
        }

        var checksum: Byte = 0
        i = 0
        while (i < msgLen + 2) {
            checksum = (checksum + txBuffer[i]).toByte()
            i++
        }
        txBuffer[i] = checksum
    }

    /*
     * 메세지중의 시간 데이터 업데이트
     */
    private fun checkSendData() {
        val txHeader = TxHeader()
        if (!txHeader.parse(txBuffer)) {
            return
        }

        val msgType = txHeader.GetNodeMsgType()
        val msgLen = txHeader.GetMsgLen()

        Log.i("NFC_TEST", "UpdateSendData ==> msgType : $msgType msgLen:$msgLen")
        bLog.i_hex("NFC_TEST", "UpdateSendData => 1", txBuffer, txBuffer.size)

        if (msgType == NODE_SEND_METER_REQ) {
            val req = MeterReq()
            updateTxTime(msgLen, req.GetTimePosition(), txHeader.updateTime())
        }
    }

    /*
     * 무선 데이터 전송
     */
    fun sendData(
        req: NfcTxMessage,
        writeWaitTime: Int,
        startWaitTime: Int
    ) {
        val msg = req.GetDataFrame()

        requestMessageType = req.GetMsgType()
        //기간검침 버퍼 초기화
        var i = 0
        while (i < PERIOD_BUFFER_SIZE) {
            rxPeriodBuffer[i] = 0
            i++
        }

        //TX Data
        i = 0
        while (i < msg.size) {
            txBuffer[i] = msg[i]
            i++
        }

        while (i < SRAM_SIZE) {
            txBuffer[i] = 0
            i++
        }

        nfcRespWaitTime = writeWaitTime
        nfcStartWaitTime = startWaitTime
        bLog.i_hex("NFC_TEST", "nfcSendData", msg, msg.size)
        Log.e(
            "NFC_TEST",
            "nfcSendData ==> isResponseConnected:$isResponseConnected requestMessageType:$requestMessageType"
        )

        if (isResponseConnected) {
            connect()
        }
    }
}