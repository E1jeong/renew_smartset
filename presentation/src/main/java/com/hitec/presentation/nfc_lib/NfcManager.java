/**
 * <pre>
 * 모든 액티비티 부모 액티비티
 * 서버 통신 핸들러
 * 블루투스 연결및 통신 핸들러
 * 등등..
 * </pre>
 */
package com.hitec.presentation.nfc_lib;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.AudioManager;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.MifareUltralight;
import android.nfc.tech.NfcA;
import android.util.Log;
import android.widget.Toast;

import com.hitec.presentation.main.MainActivity;
import com.hitec.presentation.nfc_lib.model.NfcBufferData;
import com.hitec.presentation.nfc_lib.model.NfcMeterConfig;
import com.hitec.presentation.nfc_lib.model.NfcMeterValue;
import com.hitec.presentation.nfc_lib.protocol.NfcConstant;
import com.hitec.presentation.nfc_lib.protocol.recv.RxHeader;
import com.hitec.presentation.nfc_lib.protocol.send.AccountNoSet;
import com.hitec.presentation.nfc_lib.protocol.send.BdControlReq;
import com.hitec.presentation.nfc_lib.protocol.send.ChangeMinuteIntervalReq;
import com.hitec.presentation.nfc_lib.protocol.send.CheckSubTerm;
import com.hitec.presentation.nfc_lib.protocol.send.DeviceInfoReq;
import com.hitec.presentation.nfc_lib.protocol.send.FlashDataReq;
import com.hitec.presentation.nfc_lib.protocol.send.FlashDateListReq;
import com.hitec.presentation.nfc_lib.protocol.send.FwUpdateReq;
import com.hitec.presentation.nfc_lib.protocol.send.GsmChangeDomainReq;
import com.hitec.presentation.nfc_lib.protocol.send.MeterReq;
import com.hitec.presentation.nfc_lib.protocol.send.NbConfSet;
import com.hitec.presentation.nfc_lib.protocol.send.NbIdReq;
import com.hitec.presentation.nfc_lib.protocol.send.NbIdSet;
import com.hitec.presentation.nfc_lib.protocol.send.NfcTxMessage;
import com.hitec.presentation.nfc_lib.protocol.send.NodeConfReq;
import com.hitec.presentation.nfc_lib.protocol.send.PeriodMeterAck;
import com.hitec.presentation.nfc_lib.protocol.send.PeriodMeterReq;
import com.hitec.presentation.nfc_lib.protocol.send.SelectGsmOrLteReq;
import com.hitec.presentation.nfc_lib.protocol.send.ServerConnectReq;
import com.hitec.presentation.nfc_lib.protocol.send.SetTimeInfo;
import com.hitec.presentation.nfc_lib.protocol.send.SmartCertiCalibrationReq;
import com.hitec.presentation.nfc_lib.protocol.send.SmartCertiCalibrationSet;
import com.hitec.presentation.nfc_lib.protocol.send.SmartConfAutoStart;
import com.hitec.presentation.nfc_lib.protocol.send.SmartConfMeterReq;
import com.hitec.presentation.nfc_lib.protocol.send.SmartConfReq;
import com.hitec.presentation.nfc_lib.protocol.send.SmartConfSet;
import com.hitec.presentation.nfc_lib.protocol.send.SmartMeterReq;
import com.hitec.presentation.nfc_lib.protocol.send.SmartMeterValueSet;
import com.hitec.presentation.nfc_lib.protocol.send.SmartMeterValveControl;
import com.hitec.presentation.nfc_lib.protocol.send.SmartUltraCompSet;
import com.hitec.presentation.nfc_lib.protocol.send.SnChangeReq;
import com.hitec.presentation.nfc_lib.protocol.send.TxHeader;
import com.hitec.presentation.nfc_lib.reader.Ntag_Auth;
import com.hitec.presentation.nfc_lib.reader.Ntag_I2C_Connect;
import com.hitec.presentation.nfc_lib.util.ConstCommon;
import com.hitec.presentation.nfc_lib.util.ConstNfc;
import com.hitec.presentation.nfc_lib.util.bLog;

public class NfcManager implements ConstCommon, ConstNfc, NfcConstant {

    public Ntag_I2C_Connect ntagI2CConnect = null;
    private static int mAuthStatus; // Current authentication state
    private Activity mActivity;
    private Context mContext;
    private boolean isRunning = false;
    private static boolean isResponseConnected = false;
    private static int m_nfcType = NFC_TYPE_DEFAULT;
    private static int requestMessageType = 0;
    private static String m_consumeHouseNo = "";
    private static byte[] mPassword; // Current used password
    private static byte[] m_txBuffer;
    private static byte[] m_rxBuffer;
    private static byte[] m_rxPeriodBuffer; //기간검침
    private static int nfcRespWaitTime;
    private static int nfcStartWaitTime;
    public final int SRAM_SIZE = 64;
    private final int PERIOD_BUFFER_SIZE = SRAM_SIZE * 100 * 2; // 90일 * 2
    public NfcAdapter nfcAdapter;
    private NfcBufferData nfcSendData = null; //ami lib 송신 Data
    private NfcBufferData nfcReceiveData = null; //ami lib 수신 Data
    private PendingIntent pendingIntent;

    public static int getAuthStatus() {
        return mAuthStatus;
    }

    public static byte[] getPassword() {
        return mPassword;
    }

    public NfcManager() {
    }

    public void startNfcManager(Context context, Activity activity, int nfcType) {
        Log.i("NFC_TEST", "startNfcManager ==> start 01");
        mActivity = activity;
        mContext = context;
        m_nfcType = nfcType;
        isRunning = false;

        nfcAdapter = NfcAdapter.getDefaultAdapter(mContext);
        Log.i("NFC_TEST", "startNfcManager nfcAdapter: " + nfcAdapter.toString());

        mAuthStatus = Ntag_Auth.AuthStatus.Disabled.getValue(); // When we open the application by default we set the status to disabled (we don't know the product yet)
        mPassword = Ntag_Auth.Pwds.PWD0.getValue();
        ntagI2CConnect = new Ntag_I2C_Connect(null, mActivity, null, 0); // Initialize the demo in order to handle tab change events

        m_txBuffer = new byte[SRAM_SIZE];
        m_rxBuffer = new byte[SRAM_SIZE];
        m_rxPeriodBuffer = new byte[PERIOD_BUFFER_SIZE];
        initBuffer();

        setNfcForeground(mContext);

        viewNfcStatusImage();

        mActivity.setVolumeControlStream(AudioManager.STREAM_MUSIC);
    }

    private void initBuffer() {
        Log.i("NFC_TEST", "initBuffer ==> start 01");

        if (nfcSendData == null) {
            nfcSendData = new NfcBufferData();
            nfcSendData.stMeterConfig0 = new NfcMeterConfig();
            nfcSendData.stMeterValue0 = new NfcMeterValue();
            initNfcBuffer(nfcSendData);
        }

        if (nfcReceiveData == null) {
            nfcReceiveData = new NfcBufferData();
            nfcReceiveData.stMeterConfig0 = new NfcMeterConfig();
            nfcReceiveData.stMeterValue0 = new NfcMeterValue();
            initNfcBuffer(nfcReceiveData);
        }
    }

    public void closeNfcManager() {
        Log.i("NFC_TEST", "closeNfcManager ==> close 01");
        mAuthStatus = 0;
        mPassword = null;
        nfclib_close();
    }

    private void initNfcBuffer(NfcBufferData bufferData) {
        bufferData.serialNo = "";
        bufferData.fwVersion = "";
        bufferData.nDeviceCode = 0;
        bufferData.appEui = "";
        bufferData.nbImsi = "";
        bufferData.serverIp = "";
        bufferData.serverPort = "";
        bufferData.nbServiceCode = "";
        bufferData.nbCseId = "";
        bufferData.nbIccId = "";
    }

    public void setNfcReceiveData(NfcBufferData receiveData) {
        nfcReceiveData = receiveData;
    }

    public void viewNfcStatusImage() {
        int nStatus = NFC_USE_STATUS_DISABLE;

        if (nfcAdapter != null) {
            if (nfcAdapter.isEnabled()) {
                nStatus = NFC_USE_STATUS_ENABLE;
            }
        }

        view_NfcAdapterStatus(nStatus);
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

    public void NfcAdapterEnableForeground() {

        if (nfcAdapter != null) {
            Log.i("NFC_TEST", "NfcAdapterEnableForeground() => 01");

            IntentFilter tagDiscoverFilter = new IntentFilter(NfcAdapter.ACTION_TAG_DISCOVERED);
            IntentFilter techDiscoverFilter = new IntentFilter(NfcAdapter.ACTION_TECH_DISCOVERED);
            IntentFilter ndefDiscoveredFilter = new IntentFilter(NfcAdapter.ACTION_NDEF_DISCOVERED);
            IntentFilter[] intentFilters = new IntentFilter[]{tagDiscoverFilter, techDiscoverFilter, ndefDiscoveredFilter};

            String[][] techLists = {new String[]{NfcA.class.getName()}, new String[]{MifareUltralight.class.getName()}};
            nfcAdapter.enableForegroundDispatch(mActivity, pendingIntent, intentFilters, techLists);
        }
    }

    public void handleIntent(Intent intent) {
        Log.i("NFC_TEST", "handleIntent isRunning: " + isRunning);
        if (!isRunning) return;

        doProcess(intent);
    }

    private void doProcess(Intent intent) {
        Log.i("NFC_TEST", "doProcess");

        Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
        mAuthStatus = Ntag_Auth.AuthStatus.Disabled.getValue(); // Set the initial auth parameters
        mPassword = Ntag_Auth.Pwds.PWD0.getValue();
        isResponseConnected = false;

        ntagI2CConnect = new Ntag_I2C_Connect(tag, mActivity, mPassword, mAuthStatus);
        if (ntagI2CConnect.isReady()) {
            mAuthStatus = ntagI2CConnect.ObtainAuthStatus(); // Retrieve Auth Status before doing any operation
            ntagI2CConnect = new Ntag_I2C_Connect(tag, mActivity, mPassword, mAuthStatus);
            connectProcess();
        }
    }

    private void setNfcForeground(Context context) {
        Log.i("NFC_TEST", "setNfcForeground - 01");

        pendingIntent = PendingIntent.getActivity(
                context,
                0,
                new Intent(context, MainActivity.class).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP),
                PendingIntent.FLAG_MUTABLE
        );
    }

    //****************************************************************************
    //******************* GPS, Network
    //****************************************************************************

    private void connectNfc() {
        Log.i("NFC_TEST", "connectNfc - 01");

        if (getAuthStatus() == Ntag_Auth.AuthStatus.Authenticated.getValue()) { // This authentication is added in order to avoid authentication problems with old NFC Controllers
            Log.i("NFC_TEST", "connectNfc - 02");
            ntagI2CConnect.Auth(getPassword(), Ntag_Auth.AuthStatus.Protected_RW.getValue());
        }

        if (ntagI2CConnect != null && ntagI2CConnect.isReady()) {
            boolean authSuccess = true;
            if (!isResponseConnected) {
                authSuccess = ntagI2CConnect.Auth(getPassword(), getAuthStatus());
            }

            Log.i("NFC_TEST", "authTask = doInBackground => authSuccess:" + authSuccess);

            if (authSuccess) {
                boolean responseSuccess = false;
                try {
                    Log.i("NFC_TEST", "connectNfc => NfcDataTransive m_nReqMsgType:" + requestMessageType);

                    //설정시 수용가번호 입력
                    if (requestMessageType == NODE_SEND_LORA_CONF_SET ||
                            requestMessageType == NODE_SEND_NB_CONF_SET ||
                            requestMessageType == NODE_SEND_DISPLAY_CONF_SET) {
                        ntagI2CConnect.NfcWriteNdef(m_consumeHouseNo);
                    }

                    if (requestMessageType == NODE_SEND_PERIOD_METER_REQ || requestMessageType == NODE_SEND_FLASH_DATA_REQ) {
                        responseSuccess = ntagI2CConnect.NfcPeriodDataTransive(
                                m_nfcType,
                                isResponseConnected,
                                m_txBuffer,
                                m_rxPeriodBuffer,
                                nfcRespWaitTime,
                                nfcStartWaitTime
                        );
                    } else if (requestMessageType == NODE_SEND_ACCOUNT_NO_SET) {
                        responseSuccess = ntagI2CConnect.NfcWriteNdef(m_consumeHouseNo);
                    } else {
                        responseSuccess = ntagI2CConnect.NfcDataTransive(
                                m_nfcType,
                                isResponseConnected,
                                m_txBuffer,
                                m_rxBuffer,
                                nfcRespWaitTime,
                                nfcStartWaitTime
                        );
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    Log.i("NFC_TEST", "connectNfc => Tag_lost");
                }

                if (responseSuccess) isResponseConnected = true;

                if (requestMessageType == NODE_SEND_PERIOD_METER_REQ || requestMessageType == NODE_SEND_FLASH_DATA_REQ) {
                    NfcPeriodDataResp(responseSuccess, m_rxPeriodBuffer);
                } else if (requestMessageType == NODE_SEND_ACCOUNT_NO_SET) {
                    nfclib_EvtRecvDataEvent(NODE_RECV_ACCOUNT_NO_REPORT, m_rxBuffer);
                } else {
                    NfcDataResp(responseSuccess, m_rxBuffer);
                }
            }
        } else {
            Log.i("NFC_TEST", "connectNfc -  04");
        }
    }

    //****************************************************************************
    //******************* UI
    //****************************************************************************

    private void NfcDataResp(boolean responseSuccess, byte[] rxData) {
        Log.i("NFC_TEST", "NfcDataResp => responseSuccess:" + responseSuccess);
        if (!responseSuccess) {
            Toast.makeText(mActivity, "read tag failed", Toast.LENGTH_SHORT).show();
            return;
        }

        RxHeader rxHeader = new RxHeader();
        if (!rxHeader.parse(rxData)) {
            return;
        }

        bLog.i_hex("NFC_TEST", "NfcDataResp", m_rxBuffer, m_rxBuffer.length);

        int msgType = rxHeader.GetNodeMsgType();
        nfclib_EvtRecvDataEvent(msgType, m_rxBuffer);
    }

    //****************************************************************************
    //******************** ViewActivity ==> AmiActivity
    //*****************************************************************************

    private void NfcPeriodDataResp(boolean responseSuccess, byte[] rxData) {
        Log.i("NFC_TEST", "NfcPeriodDataResp => responseSuccess:" + responseSuccess);
        if (!responseSuccess) {
            Toast.makeText(mActivity, "read tag failed", Toast.LENGTH_SHORT).show();
            return;
        }

        int i;
        for (i = 0; i < SRAM_SIZE; i++) {
            m_rxBuffer[i] = rxData[i];
        }
        bLog.i_hex("PERIOD", "m_rxBuffer ==> ", m_rxBuffer, m_rxBuffer.length);

        if (m_rxBuffer[0] == 0x00) return;

        RxHeader rxHeader = new RxHeader();
        if (!rxHeader.parse(m_rxBuffer)) {
            return;
        }

        int msgType = rxHeader.GetNodeMsgType();
        if (msgType == NODE_RECV_FLASH_DATE_LIST_REPORT) {
            nfclib_EvtRecvDataEvent(msgType, m_rxBuffer);
        } else {
            nfclib_EvtRecvDataEvent(msgType, rxData);
        }
    }

    /**
     * <pre>
     * NFC Lib Start
     * </pre>
     */
    public boolean nfclib_start() {
        isRunning = true;
        isResponseConnected = false;
        Log.i("NFC_TEST", "nfclib_start ==> 01");
        try {
            boolean retry = false;

            NfcAdapterEnableForeground();
            if (nfcAdapter != null) {
                if (!nfcAdapter.isEnabled()) {
                    Log.i("NFC_TEST", "nfclib_start ==> 02");
                    retry = true;
                }
            } else {
                Log.i("NFC_TEST", "nfclib_start ==> 03");
                retry = true;
            }

            if (retry) {
                nfcAdapter = NfcAdapter.getDefaultAdapter(mContext);
                NfcAdapterEnableForeground();
                Log.i("NFC_TEST", "nfclib_retry ==> 03");
                if (nfcAdapter != null) {
                    Log.i("NFC_TEST", "nfclib_retry ==> 04");
                    if (!nfcAdapter.isEnabled()) {
                        Log.i("NFC_TEST", "nfclib_retry ==> 05");
                        return false;
                    }
                }
            }
        } catch (Exception e) {
            Log.i("NFC_TEST", "nfclib_start ==> Exception");
            return false;
        }

        return true;
    }

    public void nfclib_restart() {
        isRunning = true;
        isResponseConnected = false;
    }

    public void nfclib_stop() {
        isRunning = false;
        isResponseConnected = false;
        for (int i = 0; i < SRAM_SIZE; i++) {
            m_txBuffer[i] = 0x00;
        }
    }

    public void nfclib_close() {
        isRunning = false;
        isResponseConnected = false;
    }

    protected void nfclib_EvtRecvDataEvent(int msgType, byte[] rxData) {
        bLog.i_hex("NFC_TEST", "nfclib_EvtRecvDataEvent", m_rxBuffer, rxData.length);
    } //

    protected void nfclib_EvtTimeOutEvent(int nResult) {
    } //

    protected void view_NfcAdapterStatus(int nStatus) {
    } //

    private void connectProcess() {
        if (isResponseConnected) {
            CheckSendData();
        }
        connectNfc();
    }

    /*
     * 메세지에 시간정보 업데이트
     */
    public void UpdateTxTime(int msgLen, int timePos, byte[] timeData) {
        int i;
        for (i = 0; i < 7; i++) {
            m_txBuffer[timePos + i] = timeData[i];
        }

        byte checksum = 0;
        for (i = 0; i < msgLen + 2; i++) {
            checksum += m_txBuffer[i];
        }
        m_txBuffer[i] = checksum;
    }

    /*
     * 메세지중의 시간 데이터 업데이트
     */
    public void CheckSendData() {
        TxHeader txHeader = new TxHeader();
        if (!txHeader.parse(m_txBuffer)) {
            return;
        }

        int msgType = txHeader.GetNodeMsgType();
        int msgLen = txHeader.GetMsgLen();

        Log.i("NFC_TEST", "UpdateSendData ==> msgType : " + msgType + " msgLen:" + msgLen);
        bLog.i_hex("NFC_TEST", "UpdateSendData => 1", m_txBuffer, m_txBuffer.length);

        if (msgType == NODE_SEND_METER_REQ) {
            MeterReq req = new MeterReq();
            UpdateTxTime(msgLen, req.GetTimePosition(), txHeader.updateTime());
        }
    }

    /*
     * 무선 데이터 전송
     */
    private void nfcLib_SendData(
            NfcTxMessage req,
            int writeWaitTime,
            int startWaitTime
    ) {
        byte[] msg = req.GetDataFrame();
        int i;

        requestMessageType = req.GetMsgType();
        //기간검침 버퍼 초기화
        for (i = 0; i < PERIOD_BUFFER_SIZE; i++) {
            m_rxPeriodBuffer[i] = 0;
        }

        //TX Data
        for (i = 0; i < msg.length; i++) {
            m_txBuffer[i] = msg[i];
        }

        for (; i < SRAM_SIZE; i++) {
            m_txBuffer[i] = 0;
        }

        nfcRespWaitTime = writeWaitTime;
        nfcStartWaitTime = startWaitTime;
        bLog.i_hex("NFC_TEST", "SendData", msg, msg.length);

        Log.e("NFC_TEST", "SendData ==> isResponseConnected:" + isResponseConnected + " requestMessageType:" + requestMessageType);

        if (isResponseConnected && ntagI2CConnect != null) {
            connectNfc();
        }
    }

    // 장비 설정 정보 요청
    public void nfcLib_ReqNodeConfig() {
        Log.i("NFC_TEST", "nfcLib_ReqNodeConfig");
        NodeConfReq req = new NodeConfReq();
        nfcLib_SendData(req, NFC_RESP_WAIT_TIME_DEFAULT, 0);
    }

    //NB-IOT Config 설정(Version 1, 3, 5)
    public void nfcLib_SetNbConfig(
            int msgVersion,
            String consumeHouseNo,
            String serialNo,
            int sleepMode,
            int amiMeteringInterval,
            int amiReportInterval,
            int terminalProtocol,
            String strServiceCode,
            String strServerIp,
            String strServerPort,
            int meterNum,
            int meterType0,
            int meterPort0,
            int meterType1,
            int meterPort1,
            int meterType2,
            int meterPort2
    ) {
        Log.i("NFC_TEST", "nfcLib_SetNodeConfig");
        NbConfSet req = new NbConfSet(
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
        );
        m_consumeHouseNo = consumeHouseNo;
        nfcLib_SendData(req, NFC_RESP_WAIT_TIME_NODE_CONF_SET, 0);
    }

    //NB-IOT Config2 설정(Version 2, 4, 6)
    public void nfcLib_SetNbConfigMaster(
            int msgVersion,
            String consumeHouseNo,
            String serialNo,
            int sleepMode,
            int amiMeteringInterval,
            int amiReportInterval,
            int amiReportRange,
            String strServiceCode,
            String strServerIp,
            String strServerPort,
            String pan,
            String nwk,
            String strSubId
    ) {
        Log.i("NFC_TEST", "nfcLib_SetNbConfigMaster");
        NbConfSet req = new NbConfSet(
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
        );
        m_consumeHouseNo = consumeHouseNo;
        nfcLib_SendData(req, NFC_RESP_WAIT_TIME_NODE_CONF_SET, 0);
    }

    //수용가번호 설정
    public void nfcLib_SetAccountNo(String consumeHouseNo) {
        Log.i("NFC_TEST", "nfcLib_SetAccountNo");
        AccountNoSet req = new AccountNoSet(consumeHouseNo);
        m_consumeHouseNo = consumeHouseNo;
        nfcLib_SendData(req, NFC_RESP_WAIT_TIME_NODE_CONF_SET, 0);
    }

    //Meter 검침요청
    public void nfcLib_ReqMeterData(int meterPort) {
        Log.i("NFC_TEST", "nfcLib_ReqMeterData");
        MeterReq req = new MeterReq(meterPort);
        int startWaitTime;
        if (isResponseConnected) {
            startWaitTime = NFC_START_WAIT_TIME_READ_METER;
        } else {
            startWaitTime = 0;
        }
        nfcLib_SendData(req, NFC_RESP_WAIT_TIME_DEFAULT, startWaitTime);
    }

    //기간 검침요청
    public void nfcLib_ReqPeriodMeterData(
            int meterPort,
            String dateFrom,
            String dateTo
    ) {
        Log.i("NFC_TEST", "nfcLib_ReqPeriodMeterData");
        PeriodMeterReq req = new PeriodMeterReq(meterPort, dateFrom, dateTo);
        nfcLib_SendData(req, NFC_RESP_WAIT_TIME_DEFAULT, 0);
    }

    //기간 검침 응답 Ack
    public void nfcLib_AckPeriodMeterData(int totalBlock, int currentBlock) {
        Log.i("NFC_TEST", "nfcLib_AckPeriodMeterData");
        PeriodMeterAck req = new PeriodMeterAck(totalBlock, currentBlock);
        nfcLib_SendData(req, NFC_RESP_WAIT_TIME_PERIOD_ACK, 0);
    }

    //Flash Date List 요청
    public void nfcLib_ReqFlashDateList() {
        Log.i("NFC_TEST", "nfcLib_ReqFlashDateList");
        FlashDateListReq req = new FlashDateListReq();
        nfcLib_SendData(req, NFC_RESP_WAIT_TIME_DEFAULT, 0);
    }

    //Flash Data 검침요청
    public void nfcLib_ReqFlashData(String dateFrom, String dateTo) {
        Log.i("NFC_TEST", "nfcLib_ReqFlashData");
        FlashDataReq req = new FlashDataReq(dateFrom, dateTo);
        nfcLib_SendData(req, NFC_RESP_WAIT_TIME_DEFAULT, 0);
    }

    //서버 접속요청
    public void nfcLib_ReqServerConnect(int reqType) {
        Log.i("NFC_TEST", "nfcLib_ReqServerConnect");
        ServerConnectReq req = new ServerConnectReq(reqType);
        nfcLib_SendData(req, NFC_RESP_WAIT_TIME_DEFAULT, 0);
    }

    //메인리셋요청
    public void nfcLib_ReqMainReset() {
        Log.i("NFC_TEST", "nfcLib_ReqMainReset");
        BdControlReq req = new BdControlReq(
                CONF_BD_RESET_NOW,
                CONF_SLEEP_STATE_NONE
        );
        nfcLib_SendData(req, NFC_RESP_WAIT_TIME_DEFAULT, 0);
    }

    //장비 정보 요청
    public void nfcLib_ReqDeviceInfo() {
        Log.i("NFC_TEST", "nfcLib_ReqDeviceInfo");
        DeviceInfoReq req = new DeviceInfoReq();
        nfcLib_SendData(req, NFC_RESP_WAIT_TIME_DEFAULT, 0);
    }

    //하위 단말기 상태 요청
    public void nfcLib_CheckSubTerm() {
        Log.i("NFC_TEST", "nfcLib_CheckSubTerm");
        CheckSubTerm req = new CheckSubTerm();
        nfcLib_SendData(req, NFC_RESP_WAIT_TIME_DEFAULT, 0);
    }

    //NB-IoT ID 요청
    public void nfcLib_ReqNbId() {
        Log.i("NFC_TEST", "nfcLib_ReqNbId");
        NbIdReq req = new NbIdReq();
        nfcLib_SendData(req, NFC_RESP_WAIT_TIME_DEFAULT, 0);
    }

    //NB-IoT 서비스코드 설정
    public void nfcLib_WriteNbId(String serviceCode) {
        Log.i("NFC_TEST", "nfcLib_WriteNbIdSet");
        NbIdSet req = new NbIdSet(serviceCode);
        nfcLib_SendData(req, NFC_RESP_WAIT_TIME_DEFAULT, 0);
    }

    //장비 일련번호 변경 요청
    public void nfcLib_SerialChangeReq(String serialNumber, int length) {
        Log.i("NFC_TEST", "nfcLib_SerialChangeReq");
        SnChangeReq req = new SnChangeReq(serialNumber, length);
        nfcLib_SendData(req, NFC_RESP_WAIT_TIME_DEFAULT, 0);
    }

    //주기보고 분단위 변경
    public void nfcLib_ChangeMinuteInterval(int value) {
        Log.i("NFC_TEST", "nfcLib_ChangeMinuteInterval");
        ChangeMinuteIntervalReq req = new ChangeMinuteIntervalReq(value);
        nfcLib_SendData(req, NFC_RESP_WAIT_TIME_DEFAULT, 0);
    }

    //gsm 단말기에서 gsm or lte 모드 변경
    public void nfcLib_selectGsmOrLte(int value) {
        Log.i("NFC_TEST", "nfcLib_selectGsmOrLte");
        SelectGsmOrLteReq req = new SelectGsmOrLteReq(value);
        nfcLib_SendData(req, NFC_RESP_WAIT_TIME_DEFAULT, 0);
    }

    //gsm 단말기에서 도메인 변경
    public void nfcLib_changeDomain(String domain) {
        Log.i("NFC_TEST", "nfcLib_changeDomain");
        GsmChangeDomainReq req = new GsmChangeDomainReq(domain);
        nfcLib_SendData(req, NFC_RESP_WAIT_TIME_DEFAULT, 0);
    }

    //Fw Update 설정
    public void nfcLib_ReqFwUpdate(
            String serialNo,
            int reqMode,
            String fwVersion
    ) {
        Log.i("NFC_TEST", "nfcLib_ReqFwUpdate");
        FwUpdateReq req = new FwUpdateReq(serialNo, reqMode, fwVersion);
        nfcLib_SendData(req, NFC_RESP_WAIT_TIME_DEFAULT, 0);
    }

    //SmartMeter 검침요청
    public void nfcLib_ReqSmartMeterData() {
        Log.i("NFC_TEST", "nfcLib_ReqSmartMeterData");
        SmartMeterReq req = new SmartMeterReq();
        nfcLib_SendData(req, NFC_RESP_WAIT_TIME_SMART_METER, 0);
    }

    //SmartMeter 카운터 설정
    public void nfcLib_SetSmartMeterCount(int count) {
        Log.i("NFC_TEST", "nfcLib_ReqSmartMeterData");
        SmartMeterValveControl req = new SmartMeterValveControl(count);
        nfcLib_SendData(req, NFC_RESP_WAIT_TIME_SMART_METER, 0);
    }

    //SmartMeter Config 요청
    public void nfcLib_ReqSmartConfData() {
        Log.i("NFC_TEST", "nfcLib_ReqSmartConfData");
        SmartConfReq req = new SmartConfReq();
        nfcLib_SendData(req, NFC_RESP_WAIT_TIME_SMART_METER, 0);
    }

    //SmartMeter 검침 Config 요청
    public void nfcLib_ReqSmartConfMeterData() {
        Log.i("NFC_TEST", "nfcLib_ReqSmartConfMeterData");
        SmartConfMeterReq req = new SmartConfMeterReq();
        nfcLib_SendData(req, NFC_RESP_WAIT_TIME_SMART_METER, 0);
    }

    //SmartMeter Config SN 설정
    public void nfcLib_SetSmartConfSnData(
            int nFlowType,
            String strDeviceSerial,
            int nMeterCaliber,
            String strMeterSerial,
            int nMaker
    ) {
        Log.i("NFC_TEST", "nfcLib_SetSmartConfSnData strMeterSerial:" + strMeterSerial);
        SmartConfSet req = new SmartConfSet(
                SMART_METER_CHANGE_MODE_WRITE_SN,
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
        );
        nfcLib_SendData(req, NFC_RESP_WAIT_TIME_SMART_METER, 0);
    }

    //SmartMeter Config Calibration 설정
    public void nfcLib_SetSmartConfCalibrationData(
            int nFlowType,
            String strDeviceSerial,
            int nMeterCaliber,
            String strMeterSerial,
            int nQ3Value,
            int nQtValue,
            int nQsValue,
            int nQ2Value,
            int nQ1Value,
            int nTemperature,
            int nMaker
    ) {
        Log.i("NFC_TEST", "nfcLib_SetSmartConfCalibrationData");
        SmartConfSet req = new SmartConfSet(
                SMART_METER_CHANGE_MODE_WRITE_METER,
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
        );
        nfcLib_SendData(req, NFC_RESP_WAIT_TIME_SMART_METER, 0);
    }

    //SmartMeter Config 초음파 보정 설정
    public void nfcLib_SetSmartUltraCompData(
            String strDeviceSerial,
            int nCompSelect,
            int nCompOffset,
            int nCompValue
    ) {
        Log.i("NFC_TEST", "nfcLib_SetSmartUltraCompData");
        SmartUltraCompSet req = new SmartUltraCompSet(
                strDeviceSerial,
                nCompSelect,
                nCompOffset,
                nCompValue
        );
        nfcLib_SendData(req, NFC_RESP_WAIT_TIME_SMART_METER, 0);
    }

    //SmartMeter Config certification Calibration 설정
    public void nfcLib_SetSmartCertiCalibrationData(
            String strDeviceSerial,
            int nCompSelect,
            int nCompValue
    ) {
        Log.i("NFC_TEST", "nfcLib_SetSmartCertiCalibrationData");
        SmartCertiCalibrationSet req = new SmartCertiCalibrationSet(
                strDeviceSerial,
                nCompSelect,
                nCompValue
        );
        nfcLib_SendData(req, NFC_RESP_WAIT_TIME_SMART_METER, 0);
    }

    //SmartMeter Config certification Calibration 요청
    public void nfcLib_ReqSmartCertiCalibrationData() {
        Log.i("NFC_TEST", "nfcLib_ReqSmartCertiCalibrationData ");
        SmartCertiCalibrationReq req = new SmartCertiCalibrationReq();
        nfcLib_SendData(req, NFC_RESP_WAIT_TIME_SMART_METER, 0);
    }

    //SmartMeter 온도 설정
    public void nfcAct_SetSmartTemperatureData(
            int flowType,
            String strDeviceSerial,
            int meterCaliber,
            int temperature,
            int maker
    ) {
        Log.i("NFC_TEST", "nfcAct_SetSmartTemperatureData strMeterSerial:" + strDeviceSerial);
        SmartConfSet req = new SmartConfSet(
                SMART_METER_CHANGE_MODE_WRITE_METER,
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
        );
        nfcLib_SendData(req, NFC_RESP_WAIT_TIME_SMART_METER, 0);
    }

    //SmartMeter 자동보정
    public void nfcAct_SetSmartAutoStart(
            int nFlowType,
            String strDeviceSerial,
            int nCompMode,
            int nCompSelect,
            int nQnFlow
    ) {
        Log.i("NFC_TEST", "nfcAct_SetSmartAutoStart strMeterSerial:" + strDeviceSerial);
        SmartConfAutoStart req = new SmartConfAutoStart(
                nFlowType,
                strDeviceSerial,
                nCompMode,
                nCompSelect,
                nQnFlow
        );

        int writeWaitTime;
        if (!strDeviceSerial.isEmpty()) {
            writeWaitTime = NFC_RESP_WAIT_TIME_SMART_METER;
        } else {
            writeWaitTime = NFC_RESP_WAIT_TIME_DEFAULT;
        }
        nfcLib_SendData(req, writeWaitTime, 0);
    }

    //SmartMeter 검침값 설정
    public void nfcAct_SetSmartMeterValueData(
            String strDeviceSerial,
            String strMeterValue
    ) {
        Log.i("NFC_TEST", "nfcAct_SetSmartMeterValueData");
        SmartMeterValueSet req = new SmartMeterValueSet(
                strDeviceSerial,
                strMeterValue
        );
        nfcLib_SendData(req, NFC_RESP_WAIT_TIME_SMART_METER, 0);
    }

    //Sleep Mode 설정/해제
    public void nfcLib_SetSleepMode(int sleepMode) {
        Log.i("NFC_TEST", "nfcLib_SetSleepMode");
        BdControlReq req = new BdControlReq(
                CONF_BD_RESET_NONE,
                sleepMode
        );
        nfcLib_SendData(req, NFC_RESP_WAIT_TIME_DEFAULT, 0);
    }

    //Report Mode 설정/해제
    public void nfcLib_SetReportMode(int reportMode) {
        Log.i("NFC_TEST", "nfcLib_SetReportMode");
        BdControlReq req = new BdControlReq(
                CONF_BD_RESET_NONE,
                CONF_SLEEP_STATE_NONE,
                reportMode
        );
        nfcLib_SendData(req, NFC_RESP_WAIT_TIME_DEFAULT, 0);
    }

    //기간검침  설정/해제
    public void nfcLib_SetPeriodMode(int periodMode) {
        Log.i("NFC_TEST", "nfcLib_SetPeriodMode");
        BdControlReq req = new BdControlReq(
                CONF_BD_RESET_NONE,
                CONF_SLEEP_STATE_NONE,
                CONF_REPORT_MODE_NONE,
                periodMode
        );
        nfcLib_SendData(req, NFC_RESP_WAIT_TIME_DEFAULT, 0);
    }

    //디버그모드  설정/해제
    public void nfcLib_SetDebugMode(int debugMode) {
        Log.i("NFC_TEST", "nfcLib_SetDebugMode");
        BdControlReq req = new BdControlReq(
                CONF_BD_RESET_NONE,
                CONF_SLEEP_STATE_NONE,
                CONF_REPORT_MODE_NONE,
                CONF_PERIOD_MODE_NONE,
                debugMode
        );
        nfcLib_SendData(req, NFC_RESP_WAIT_TIME_DEFAULT, 0);
    }

    //Data Skip  설정/해제
    public void nfcLib_SetDataSkipMode(int dataSkipMode) {
        Log.i("NFC_TEST", "nfcLib_SetDataSkipMode");
        BdControlReq req = new BdControlReq(
                CONF_BD_RESET_NONE,
                CONF_SLEEP_STATE_NONE,
                CONF_REPORT_MODE_NONE,
                CONF_PERIOD_MODE_NONE,
                CONF_DEBUG_MODE_NONE,
                dataSkipMode
        );
        nfcLib_SendData(req, NFC_RESP_WAIT_TIME_DEFAULT, 0);
    }

    //장비 시간 설정
    public void nfcLib_SetTimeInfo() {
        Log.i("NFC_TEST", "nfcLib_SetTimeInfo");
        SetTimeInfo req = new SetTimeInfo();
        nfcLib_SendData(req, NFC_RESP_WAIT_TIME_DEFAULT, 0);
    }
}
