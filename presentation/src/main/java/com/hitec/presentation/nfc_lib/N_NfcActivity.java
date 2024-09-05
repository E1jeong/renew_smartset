/**
 * <pre>
 * 모든 액티비티 부모 액티비티
 * 서버 통신 핸들러
 * 블루투스 연결및 통신 핸들러
 * 등등..
 * </pre>
 */
package com.hitec.presentation.nfc_lib;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.AudioManager;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.MifareUltralight;
import android.nfc.tech.NfcA;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.util.Log;
import android.widget.Toast;

import com.hitec.presentation.nfc_lib.model.N_AmiBufferData;
import com.hitec.presentation.nfc_lib.model.N_MeterConfigT;
import com.hitec.presentation.nfc_lib.model.N_MeterValueT;
import com.hitec.presentation.nfc_lib.protocol.NfcConstant;
import com.hitec.presentation.nfc_lib.protocol.recv.RxHeader;
import com.hitec.presentation.nfc_lib.protocol.send.AccountNoSet;
import com.hitec.presentation.nfc_lib.protocol.send.BdControlReq;
import com.hitec.presentation.nfc_lib.protocol.send.ChangeMinuteIntervalReq;
import com.hitec.presentation.nfc_lib.protocol.send.CheckSubTerm;
import com.hitec.presentation.nfc_lib.protocol.send.DeviceInfoReq;
import com.hitec.presentation.nfc_lib.protocol.send.DisplayConfSet;
import com.hitec.presentation.nfc_lib.protocol.send.FlashDataReq;
import com.hitec.presentation.nfc_lib.protocol.send.FlashDateListReq;
import com.hitec.presentation.nfc_lib.protocol.send.FwUpdateReq;
import com.hitec.presentation.nfc_lib.protocol.send.GsmChangeDomainReq;
import com.hitec.presentation.nfc_lib.protocol.send.LoraAppEuiKeyReq;
import com.hitec.presentation.nfc_lib.protocol.send.LoraAppEuiKeySet;
import com.hitec.presentation.nfc_lib.protocol.send.LoraConfSet;
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
import com.hitec.presentation.nfc_lib.util.ConstDB;
import com.hitec.presentation.nfc_lib.util.ConstMeter;
import com.hitec.presentation.nfc_lib.util.ConstNfc;
import com.hitec.presentation.nfc_lib.util.Constanse;
import com.hitec.presentation.nfc_lib.util.bLog;

public class N_NfcActivity extends Activity implements Constanse, ConstDB, ConstMeter, ConstCommon, ConstNfc {

    //Intent request codes
    public static final int AUTH_REQUEST = 0;
    public static Ntag_I2C_Connect mNtagConnect = null;
    // Current authentication state
    private static int mAuthStatus;
    private static boolean m_fNfcConnect = false;
    private static int m_nNfcType = NFC_TYPE_DEFAULT;
    private static int m_nReqMsgType = 0;
    private static String m_consumeHouseNo = "";
    // Current used password
    private static byte[] mPassword;
    private static byte[] m_txBuffer;
    private static byte[] m_rxBuffer;
    private static byte[] m_rxPeriodBuffer; //기간검침
    private static int m_nNfcRespWaitTime;
    private static int m_nNfcStartWaitTime;
    public final int SRAM_SIZE = 64;
    private final int PERIOD_BUFFER_SIZE = SRAM_SIZE * 100 * 2; // 90일 * 2
    private NfcAdapter nfcAdapter;
    private Tag mNfcTag;
    private N_AmiBufferData mAmiSendData = null; //ami lib 송신 Data
    private N_AmiBufferData mAmiRecvData = null; //ami lib 수신 Data
    private Activity mActivity;
    private Context mContext;
    private boolean m_isAmiRun = false;
    private PendingIntent mPendingIntent;
    /**
     * <pre>
     * nfc command 핸들러
     * </pre>
     *
     * @param command
     */
    private Runnable commandRunnable = null;
    private Handler commandHandler = null;
    private int mCommandTimeDelay = 1000;

    public static int getAuthStatus() {
        return mAuthStatus;
    }

    public static void setAuthStatus(int status) {
        mAuthStatus = status;
    }

    public static byte[] getPassword() {
        return mPassword;
    }

    public static void setPassword(byte[] pwd) {
        mPassword = pwd;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.i("NFC_TEST", "NfcActivity ==> onCreate 01");

        setVolumeControlStream(AudioManager.STREAM_MUSIC);

        // When we open the application by default we set the status to disabled (we don't know the product yet)
        mAuthStatus = Ntag_Auth.AuthStatus.Disabled.getValue();

        m_txBuffer = new byte[SRAM_SIZE];
        m_rxBuffer = new byte[SRAM_SIZE];
        m_rxPeriodBuffer = new byte[PERIOD_BUFFER_SIZE];

        mActivity = this;
        mContext = this;

        Log.i("NFC_TEST", "NfcActivity - start05");
    }

    public void startNfcActivity(Activity act, Context cont, int nNfcType) {
        Log.i("NFC_TEST", "startNfcActivity ==> start 01");
        //mActivity = act;
        //mContext = cont;

        m_nNfcType = nNfcType;
        m_isAmiRun = false;

        Log.i("NFC_TEST", "startNfcActivity - start02");
        // Initialize the demo in order to handle tab change events
        mNtagConnect = new Ntag_I2C_Connect(null, mActivity, null, 0);
        nfcAdapter = NfcAdapter.getDefaultAdapter(mContext);
        N_NfcActivity.setPassword(Ntag_Auth.Pwds.PWD0.getValue());

        Log.i("NFC_TEST", "startNfcActivity - start03");
        setNfcForeground();

        Log.i("NFC_TEST", "startNfcActivity - start04");
        //checkNFC();
        viewNfcStatusImage();

        mActivity.setVolumeControlStream(AudioManager.STREAM_MUSIC);
    }

    public void initNfcActivity() {
        Log.i("NFC_TEST", "initNfcActivity ==> start 01");

        if (mAmiSendData == null) {
            mAmiSendData = new N_AmiBufferData();
            mAmiSendData.stMeterConfig0 = new N_MeterConfigT();

            mAmiSendData.stMeterValue0 = new N_MeterValueT();
            init_AmiBufferData(mAmiSendData);
        }

        if (mAmiRecvData == null) {
            mAmiRecvData = new N_AmiBufferData();
            mAmiRecvData.stMeterConfig0 = new N_MeterConfigT();

            mAmiRecvData.stMeterValue0 = new N_MeterValueT();
            init_AmiBufferData(mAmiRecvData);
        }
    }

    public void closeNfcActivity() {
        Log.i("NFC_TEST", "AmiActivity ==> close 01");
        nfclib_close();
        Log.i("NFC_TEST", "AmiActivity ==> close 02");
    }

    public void init_AmiBufferData(N_AmiBufferData bufferData) {
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

    public void copyAmiRecvBufferData(N_AmiBufferData dstBuffer) {
        dstBuffer = mAmiRecvData;
    }

    public void startSettingNfc() {
        if (android.os.Build.VERSION.SDK_INT >= 16) {
            startActivity(new Intent(android.provider.Settings.ACTION_NFC_SETTINGS));
        } else {
            startActivity(
                    new Intent(android.provider.Settings.ACTION_WIRELESS_SETTINGS)
            );
        }
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

    //////////////////////////////////////

    @SuppressLint("InlinedApi")
    public void checkNFC() {
        if (m_nNfcType == NFC_TYPE_NO) {
            return;
        }

        String strTitle = "";
        String strMsg = "";
        String strBtnOk;
        String strBtnCancel;

        //"확인";
        strBtnOk = "확인";
        //"취소";
        strBtnCancel = "취소";

        if (nfcAdapter != null) {
            if (!nfcAdapter.isEnabled()) {
                //NFC 미사용
                strTitle = "nfc 미사용";
                //NFC 설정화면으로 이동하시겠습니까?
                strMsg = "설정화면으로 이동하시겠습니까?";
                new AlertDialog.Builder(mActivity)
                        .setTitle(strTitle)
                        .setMessage(strMsg)
                        .setPositiveButton(
                                strBtnOk,
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        if (android.os.Build.VERSION.SDK_INT >= 16) {
                                            startActivity(
                                                    new Intent(android.provider.Settings.ACTION_NFC_SETTINGS)
                                            );
                                        } else {
                                            startActivity(
                                                    new Intent(
                                                            android.provider.Settings.ACTION_WIRELESS_SETTINGS
                                                    )
                                            );
                                        }
                                    }
                                }
                        )
                        .setNegativeButton(
                                strBtnCancel,
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        //System.exit(0);
                                    }
                                }
                        )
                        .show();
            }
        } else {
            //NFC를 사용할 수 없습니다.
            strTitle = "nfc를 사용할수 없습니다.";
            new AlertDialog.Builder(mActivity)
                    .setTitle(strTitle)
                    .setNeutralButton(
                            strBtnOk,
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    //System.exit(0);
                                }
                            }
                    )
                    .show();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.i("NFC_TEST", "onPause => 01");

        if (nfcAdapter != null) {
            nfcAdapter.disableForegroundDispatch(mActivity);
        }
    }

    /*
      @Override
      public void onResume() {
          super.onResume();

          // Update the Auth Status Icon
          //updateAuthIcon(mAuthStatus);
          Log.i("NFC_TEST", "onResume : updateAuthIcon" );

          if (mAdapter != null) {
              mAdapter.enableForegroundDispatch(mActivity, mPendingIntent, null, null);
          }
      }
  */
    public void NfcAdapterEnableForeground() {
        Log.i("NFC_TEST", "NfcAdapterEnableForeground() => 01");

        if (nfcAdapter != null) {

            IntentFilter tagDiscoverFilter = new IntentFilter(NfcAdapter.ACTION_TAG_DISCOVERED);
            IntentFilter techDiscoverFilter = new IntentFilter(NfcAdapter.ACTION_TECH_DISCOVERED);
            IntentFilter[] intentFilters = new IntentFilter[]{tagDiscoverFilter, techDiscoverFilter};

            String[][] techLists = {new String[]{NfcA.class.getName()}, new String[]{MifareUltralight.class.getName()}};
            nfcAdapter.enableForegroundDispatch(mActivity, mPendingIntent, intentFilters, techLists);
        }
    }

    // ===========================================================================
    // NTAG I2C Plus getters and setters
    // ===========================================================================

    @Override
    public void onDestroy() {
        super.onDestroy();
        mAuthStatus = 0;
        mPassword = null;
    }

    @Override
    protected void onNewIntent(Intent nfc_intent) {
        super.onNewIntent(nfc_intent);

        if (m_isAmiRun == false) return;

        // Set the pattern for vibration
        long pattern[] = {0, 100};

        Log.i("NFC_TEST", "onNewIntent - 01");
        // Set the initial auth parameters
        mAuthStatus = Ntag_Auth.AuthStatus.Disabled.getValue();

        Log.i("NFC_TEST", "onNewIntent - 03");
        // Vibrate on new Intent
        Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        vibrator.vibrate(pattern, -1);

        doProcess(nfc_intent);
        Log.i("NFC_TEST", "onNewIntent - 04");
    }

    public void doProcess(Intent nfc_intent) {
        //Tag tag = nfc_intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
        mNfcTag = nfc_intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
        Log.i("NFC_TEST", "doProcess - 01: NfcTag: " + mNfcTag);
        setPassword(Ntag_Auth.Pwds.PWD0.getValue());
        m_fNfcConnect = false;
        //@@ NFC Tag와 I2C 통신 수행
        mNtagConnect = new Ntag_I2C_Connect(mNfcTag, mActivity, mPassword, mAuthStatus);
        //@@yslee : 여기에서 false를 리턴하여 더 진행이 안됨.
        if (mNtagConnect.isReady()) {
            // Retrieve Auth Status before doing any operation
            Log.i("NFC_TEST", "doProcess - 02");
            mAuthStatus = mNtagConnect.ObtainAuthStatus();
            // AuthStatus만 갱신하여 다시 연결 시도
            mNtagConnect = new Ntag_I2C_Connect(mNfcTag, mActivity,
                    N_NfcActivity.getPassword(), N_NfcActivity.getAuthStatus());
            Log.i("NFC_TEST", "doProcess - 03");
            connectProcess();
            Log.i("NFC_TEST", "doProcess - 04");
        }
    }

    public void setNfcForeground() {
        Log.i("NFC_TEST", "setNfcForeground - 01");
        // Create a generic PendingIntent that will be delivered to this
        // activity. The NFC stack will fill
        // in the intent with the details of the discovered tag before
        // delivering it to this activity.
        //		mPendingIntent = PendingIntent.getActivity(mActivity, 0, new Intent(
        //				getApplicationContext(), getClass())
        //				.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);

        Intent new_intent = new Intent(getApplicationContext(), getClass());
        //Intent new_intent = new Intent(mContext, getClass());
//    mPendingIntent = PendingIntent.getActivity(mActivity, 0,
//        new_intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP),
//        PendingIntent.FLAG_IMMUTABLE
//      );

        mPendingIntent = PendingIntent.getActivity(mActivity, 0,
                new Intent(getApplicationContext(), getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP),
                PendingIntent.FLAG_MUTABLE);

    }

    //****************************************************************************
    //******************* GPS, Network
    //****************************************************************************

    private void connectNfc() {
        Log.i("NFC_TEST", "connectNfc - 01");
        // This authentication is added in order to avoid authentication problems with old NFC Controllers
        if (N_NfcActivity.getAuthStatus() == Ntag_Auth.AuthStatus.Authenticated.getValue()) {
            Log.i("NFC_TEST", "connectNfc - 02");
            mNtagConnect.Auth(N_NfcActivity.getPassword(), Ntag_Auth.AuthStatus.Protected_RW.getValue());
        }

        if (mNtagConnect != null && mNtagConnect.isReady()) {
            boolean success = true;
            if (m_fNfcConnect == false) {
                success = mNtagConnect.Auth(N_NfcActivity.getPassword(), N_NfcActivity.getAuthStatus());
            }

            Log.i("AUTH", "authTask = doInBackground => success:" + success);

            if (success == true) {
                boolean fSuccess = false;
                try {
                    Log.i("NFC_TEST", "connectNfc => NfcDataTransive m_nReqMsgType:" + m_nReqMsgType);

                    //설정시 수용가번호 입력
                    if (m_nReqMsgType == NfcConstant.NODE_SEND_LORA_CONF_SET ||
                            m_nReqMsgType == NfcConstant.NODE_SEND_NB_CONF_SET ||
                            m_nReqMsgType == NfcConstant.NODE_SEND_DISPLAY_CONF_SET) {
                        mNtagConnect.NfcWriteNdef(m_consumeHouseNo);
                    }

                    if (m_nReqMsgType == NfcConstant.NODE_SEND_PERIOD_METER_REQ ||
                            m_nReqMsgType == NfcConstant.NODE_SEND_FLASH_DATA_REQ) {
                        fSuccess = mNtagConnect.NfcPeriodDataTransive(
                                m_nNfcType,
                                m_fNfcConnect,
                                m_txBuffer,
                                m_rxPeriodBuffer,
                                m_nNfcRespWaitTime,
                                m_nNfcStartWaitTime
                        );
                    } else if (m_nReqMsgType == NfcConstant.NODE_SEND_ACCOUNT_NO_SET) {
                        fSuccess = mNtagConnect.NfcWriteNdef(m_consumeHouseNo);
                    } else {
                        fSuccess = mNtagConnect.NfcDataTransive(
                                m_nNfcType,
                                m_fNfcConnect,
                                m_txBuffer,
                                m_rxBuffer,
                                m_nNfcRespWaitTime,
                                m_nNfcStartWaitTime
                        );
                    }
                    //int devCode = (int)(m_rxBuffer[0] & 0xFF);
                    //if(devCode == DEVICE_CODE_SMART){
                    //	fSuccess = false;
                    //}

                } catch (Exception e) {
                    e.printStackTrace();
                    Log.i("NFC_TEST", "connectNfc => Tag_lost");
                }

                if (fSuccess == true) m_fNfcConnect = true;

                if (m_nReqMsgType == NfcConstant.NODE_SEND_PERIOD_METER_REQ ||
                        m_nReqMsgType == NfcConstant.NODE_SEND_FLASH_DATA_REQ) {
                    NfcPeriodDataResp(fSuccess, m_rxPeriodBuffer);
                } else if (m_nReqMsgType == NfcConstant.NODE_SEND_ACCOUNT_NO_SET) {
                    nfclib_EvtRecvDataEvent(
                            NfcConstant.NODE_RECV_ACCOUNT_NO_REPORT,
                            m_rxBuffer
                    );
                } else {
                    NfcDataResp(fSuccess, m_rxBuffer);
                }
            }
        } else {
            Log.i("NFC_TEST", "connectNfc -  04");
        }
    }

    //****************************************************************************
    //******************* UI
    //****************************************************************************

    private void NfcDataResp(boolean fSuccess, byte[] rxData) {
        Log.i("NFC_TEST", "NfcDataResp => fSuccess:" + fSuccess);
        if (fSuccess == false) {
            Toast.makeText(mActivity, "read tag failed", Toast.LENGTH_SHORT).show();
            return;
        }

        RxHeader rHeader = new RxHeader();
        if (rHeader.parse(rxData) == false) {
            return;
        }

        bLog.i_hex("NFC_TEST", "NfcDataResp", m_rxBuffer, m_rxBuffer.length);

        int msgType = rHeader.GetNodeMsgType();
        nfclib_EvtRecvDataEvent(msgType, m_rxBuffer);
    }

    //****************************************************************************
    //******************** ViewActivity ==> AmiActivity
    //*****************************************************************************

    private void NfcPeriodDataResp(boolean fSuccess, byte[] rxData) {
        Log.i("NFC_TEST", "NfcPeriodDataResp => fSuccess:" + fSuccess);
        if (fSuccess == false) {
            Toast.makeText(mActivity, "read tag failed", Toast.LENGTH_SHORT).show();
            return;
        }

        int i;
        for (i = 0; i < SRAM_SIZE; i++) {
            m_rxBuffer[i] = rxData[i];
        }
        bLog.i_hex("PERIOD", "m_rxBuffer ==> ", m_rxBuffer, m_rxBuffer.length);

        if (m_rxBuffer[0] == 0x00) return;

        RxHeader rHeader = new RxHeader();
        if (rHeader.parse(m_rxBuffer) == false) {
            return;
        }

        int msgType = rHeader.GetNodeMsgType();
        if (msgType == NfcConstant.NODE_RECV_FLASH_DATE_LIST_REPORT) {
            nfclib_EvtRecvDataEvent(msgType, m_rxBuffer);
        } else {
            nfclib_EvtRecvDataEvent(msgType, rxData);
        }
    }

    @Override
    protected void onActivityResult(
            int requestCode,
            int resultCode,
            Intent intent
    ) {
        super.onActivityResult(requestCode, resultCode, intent);

        //Log.i("NFC_TEST", "onActivityResult ==> requestCode:" + requestCode + " resultCode:" + resultCode);

        if (resultCode != Activity.RESULT_OK) return;

        switch (requestCode) {
            case AUTH_REQUEST:
                // When the request to enable Bluetooth returns
                if (resultCode == Activity.RESULT_OK) {
                    // Bluetooth is now enabled, so set up a chat session
                    //setupBtCommand();
                }
                break;
        }
    }

    /**
     * <pre>
     * NFC Lib Start
     * </pre>
     */
    public boolean nfclib_start() {
        m_isAmiRun = true;
        m_fNfcConnect = false;
        Log.i("NFC_TEST", "nfclib_start ==> 01");
        try {
            boolean restart = false;

            NfcAdapterEnableForeground();
            if (nfcAdapter != null) {
                Log.i("NFC_TEST", "nfclib_start ==> 02");
                if (!nfcAdapter.isEnabled()) {
                    Log.i("NFC_TEST", "nfclib_start ==> 03");
                    restart = true;
                }
            } else {
                Log.i("NFC_TEST", "nfclib_start ==> 04");
                restart = true;
            }

            if (restart == true) {
                nfcAdapter = NfcAdapter.getDefaultAdapter(mContext);
                NfcAdapterEnableForeground();
                if (nfcAdapter != null) {
                    Log.i("NFC_TEST", "nfclib_start ==> 02");
                    if (!nfcAdapter.isEnabled()) {
                        Log.i("NFC_TEST", "nfclib_start ==> 03");
                        restart = true;
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

    //////////////////////////////////////////////
    //****************************************************************************
    //******************** NfcActivity ==> ViewActivity
    //*****************************************************************************

    /**
     * <pre>
     * NFC Lib start
     * 변수만 초기화
     * </pre>
     */
    public void nfclib_restart() {
        m_isAmiRun = true;
        m_fNfcConnect = false;
    }

    /**
     * <pre>
     * NFC Lib Stop
     * </pre>
     */
    public void nfclib_stop() {
        m_isAmiRun = false;
        m_fNfcConnect = false;
        for (int i = 0; i < SRAM_SIZE; i++) {
            m_txBuffer[i] = 0x00;
        }
        stopCommandHandler();
    }

    /**
     * <pre>
     * NFC Lib Close
     * </pre>
     */
    public void nfclib_close() {
        m_isAmiRun = false;
        m_fNfcConnect = false;
        stopCommandHandler();
    }

    //****************************************************************************
    //******************** App ==> NFC Lib
    // *****************************************************************************

    protected void nfclib_EvtRecvDataEvent(int msgType, byte[] rxData) {
    } //

    protected void nfclib_EvtTimeOutEvent(int nResult) {
    } //

    protected void view_NfcAdapterStatus(int nStatus) {
    } //

    protected void startCommandHandler(int timeDelay) {
        mCommandTimeDelay = timeDelay;
        commandHandler = new Handler();
        commandRunnable =
                new Runnable() {
                    @Override
                    public void run() {
                        Log.i(
                                "NFC_TEST",
                                "===> startCommandHandler mCommandTimeDelay:" + mCommandTimeDelay
                        );
                    }
                };

        commandHandler.postDelayed(commandRunnable, mCommandTimeDelay);
    }

    /**
     * <pre>
     * Command 중지
     * </pre>
     */
    public void stopCommandHandler() {
        if (commandHandler != null) {
            Log.i("NFC_TEST", "===> stopCommandHandler");
            commandHandler.removeCallbacks(commandRunnable);
        }
    }

    public void restartCommandHandler(int timeDelay) {
        stopCommandHandler();
        startCommandHandler(timeDelay);
    }

    private void connectProcess() {
        if (m_fNfcConnect == true) {
            connectNfc();
        } else {
            CheckSendData();
            connectNfc();
        }
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
        TxHeader rHeader = new TxHeader();
        if (rHeader.parse(m_txBuffer) == false) {
            return;
        }

        int msgType = rHeader.GetNodeMsgType();
        int msgLen = rHeader.GetMsgLen();

        Log.i(
                "NFC_TEST",
                "UpdateSendData ==> msgType : " + msgType + " msgLen:" + msgLen
        );
        bLog.i_hex("NFC_TEST", "UpdateSendData => 1", m_txBuffer, m_txBuffer.length);

        if (msgType == NfcConstant.NODE_SEND_LORA_CONF_SET) {
        } else if (
                msgType == NfcConstant.NODE_SEND_NB_CONF_SET
        ) {
        } else if (msgType == NfcConstant.NODE_SEND_METER_REQ) {
            MeterReq req = new MeterReq();
            UpdateTxTime(msgLen, req.GetTimePosition(), rHeader.updateTime());
        } else if (msgType == NfcConstant.NODE_SEND_PERIOD_METER_REQ) {
        } else if (
                msgType == NfcConstant.NODE_SEND_PULSE_METER_VALUE_SET
        ) {
        }
    }

    /*
     * 무선 데이터 전송
     */
    public void nfcLib_SendData(
            NfcTxMessage req,
            int writeWaitTime,
            int startWaitTime
    ) {
        byte[] msg = req.GetDataFrame();
        int i;

        m_nReqMsgType = req.GetMsgType();
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

        m_nNfcRespWaitTime = writeWaitTime;
        m_nNfcStartWaitTime = startWaitTime;
        bLog.i_hex("NFC_TEST", "SendData", msg, msg.length);

        Log.e("NFC_TEST", "SendData ==> m_fNfcConnect:" + m_fNfcConnect
                + " m_nReqMsgType:" + m_nReqMsgType);

        if (m_fNfcConnect == true && mNtagConnect != null) {
            connectNfc();
        }
    }

    /**
     * <pre>
     * 장비 설정 정보 요청
     * </pre>
     *
     * @param
     */
    public void nfcLib_ReqNodeConfig() {
        Log.i("NFC_TEST", "nfcLib_ReqNodeConfig ");
        NodeConfReq req = new NodeConfReq();
        nfcLib_SendData(req, NFC_RESP_WAIT_TIME_DEFAULT, 0);
    }

    /**
     * <pre>
     * Lora Config 설정
     * </pre>
     *
     * @param
     */
    public void nfcLib_SetLoraConfig(
            String consumeHouseNo,
            String serialNo,
            int sleepMode,
            int amiMeteringInterval,
            int amiReportInterval,
            int dataFormat,
            int meterNum,
            int meterType0,
            int meterPort0,
            int meterType1,
            int meterPort1,
            int meterType2,
            int meterPort2
    ) {
        Log.i("NFC_TEST", "nfcLib_SetLoraConfig ");
        LoraConfSet req = new LoraConfSet(
                serialNo,
                sleepMode,
                amiMeteringInterval,
                amiReportInterval,
                dataFormat,
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

    /**
     * <pre>
     * Lora Config2 설정
     * </pre>
     *
     * @param
     */
    public void nfcLib_SetLoraConfig2(
            String consumeHouseNo,
            String serialNo,
            int sleepMode,
            int amiMeteringInterval,
            int amiReportInterval,
            int dataFormat,
            String pan,
            String nwk,
            String strSubId
    ) {
        Log.i("NFC_TEST", "nfcLib_SetLoraConfig2 ");
        LoraConfSet req = new LoraConfSet(
                serialNo,
                sleepMode,
                amiMeteringInterval,
                amiReportInterval,
                dataFormat,
                pan,
                nwk,
                strSubId
        );

        m_consumeHouseNo = consumeHouseNo;
        nfcLib_SendData(req, NFC_RESP_WAIT_TIME_NODE_CONF_SET, 0);
    }

    /**
     * <pre>
     * NB-IOT Config 설정(Version 1, 3, 5)
     * </pre>
     *
     * @param
     */
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
        Log.i("NFC_TEST", "nfcLib_SetNodeConfig ");
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

    /**
     * <pre>
     * NB-IOT Config2 설정(Version 2, 4, 6)
     * </pre>
     *
     * @param
     */
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
        Log.i("NFC_TEST", "nfcLib_SetNbConfigMaster ");
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

    /**
     * <pre>
     * 옥외표시 Config 설정
     * </pre>
     *
     * @param
     */
    public void nfcLib_SetDisplayConfig(
            String consumeHouseNo,
            String serialNo,
            int dataFormat,
            int meterNum,
            int meterType0,
            int meterPort0,
            int meterType1,
            int meterPort1,
            int meterType2,
            int meterPort2
    ) {
        Log.i("NFC_TEST", "nfcLib_SetDisplayConfig ");
        DisplayConfSet req = new DisplayConfSet(
                serialNo,
                dataFormat,
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

    /**
     * <pre>
     * 수용가번호 설정
     * </pre>
     *
     * @param
     */
    public void nfcLib_SetAccountNo(String consumeHouseNo) {
        Log.i("NFC_TEST", "nfcLib_SetAccountNo ");
        AccountNoSet req = new AccountNoSet(consumeHouseNo);

        m_consumeHouseNo = consumeHouseNo;
        nfcLib_SendData(req, NFC_RESP_WAIT_TIME_NODE_CONF_SET, 0);
    }

    /**
     * <pre>
     * Meter 검침요청
     * </pre>
     *
     * @param
     */
    public void nfcLib_ReqMeterData(int meterPort) {
        MeterReq req = new MeterReq(meterPort);
        int startWaitTime = 0;
        if (m_fNfcConnect == true) {
            startWaitTime = NFC_START_WAIT_TIME_READ_METER;
        } else {
            startWaitTime = 0;
        }
        Log.i("NFC_TEST", "nfcLib_ReqMeterData ");
        //yikim	2018.01.17		NFC_START_WAIT_TIME_READ_METER => NFC_RESP_WAIT_TIME_DEFAULT
        nfcLib_SendData(req, NFC_RESP_WAIT_TIME_DEFAULT, startWaitTime);
    }

    /**
     * <pre>
     * 기간 검침요청
     * </pre>
     *
     * @param
     */
    public void nfcLib_ReqPeriodMeterData(
            int meterPort,
            String dateFrom,
            String dateTo
    ) {
        Log.i("NFC_TEST", "nfcLib_ReqPeriodMeterData ");
        PeriodMeterReq req = new PeriodMeterReq(meterPort, dateFrom, dateTo);

        nfcLib_SendData(req, NFC_RESP_WAIT_TIME_DEFAULT, 0);
    }

    /**
     * <pre>
     * 기간 검침 응답 Ack
     * </pre>
     *
     * @param
     */
    public void nfcLib_AckPeriodMeterData(int nTotalBlock, int nCurrentblock) {
        Log.i("NFC_TEST", "nfcLib_AckPeriodMeterData ");

        PeriodMeterAck req = new PeriodMeterAck(nTotalBlock, nCurrentblock);

        nfcLib_SendData(req, NFC_RESP_WAIT_TIME_PERIOD_ACK, 0);
    }

    /**
     * <pre>
     * Flash Date List 요청
     * </pre>
     *
     * @param
     */
    public void nfcLib_ReqFlashDateList() {
        Log.i("NFC_TEST", "nfcLib_ReqFlashDataList ");
        FlashDateListReq req = new FlashDateListReq();

        nfcLib_SendData(req, NFC_RESP_WAIT_TIME_DEFAULT, 0);
    }

    /**
     * <pre>
     * Flash Data 검침요청
     * </pre>
     *
     * @param
     */
    public void nfcLib_ReqFlashData(String dateFrom, String dateTo) {
        Log.i("NFC_TEST", "nfcLib_ReqFlashData ");
        FlashDataReq req = new FlashDataReq(dateFrom, dateTo);

        nfcLib_SendData(req, NFC_RESP_WAIT_TIME_DEFAULT, 0);
    }

    /**
     * <pre>
     * 서버 접속요청
     * </pre>
     *
     * @param
     */
    public void nfcLib_ReqServerConnect(int reqType) {
        Log.i("NFC_TEST", "nfcLib_ReqServerConnect ");
        ServerConnectReq req = new ServerConnectReq(reqType);
        int startWaitTime = 0;
        if (m_fNfcConnect == true) {
            startWaitTime = NFC_START_WAIT_TIME_READ_METER;
        } else {
            startWaitTime = 0;
        }

        nfcLib_SendData(req, NFC_RESP_WAIT_TIME_DEFAULT, 0);
    }

    /**
     * <pre>
     * 메인리셋요청
     * </pre>
     *
     * @param
     */
    public void nfcLib_ReqMainReset() {
        Log.i("NFC_TEST", "nfcLib_ReqMainReset ");
        BdControlReq req = new BdControlReq(
                NfcConstant.CONF_BD_RESET_NOW,
                NfcConstant.CONF_SLEEP_STATE_NONE
        );

        nfcLib_SendData(req, NFC_RESP_WAIT_TIME_DEFAULT, 0);
    }

    /**
     * <pre>
     * 장비 정보 요청
     * </pre>
     *
     * @param
     */
    public void nfcLib_ReqDeviceInfo() {
        Log.i("NFC_TEST", "nfcLib_ReqDeviceInfo ");
        DeviceInfoReq req = new DeviceInfoReq();

        nfcLib_SendData(req, NFC_RESP_WAIT_TIME_DEFAULT, 0);
    }

    /**
     * <pre>
     * 하위 단말기 상태 요청
     * </pre>
     *
     * @param
     */
    public void nfcLib_CheckSubTerm() {
        Log.i("NFC_TEST", "nfcLib_CheckSubTerm ");
        CheckSubTerm req = new CheckSubTerm();

        nfcLib_SendData(req, NFC_RESP_WAIT_TIME_DEFAULT, 0);
    }

    /**
     * <pre>
     * NB-IoT ID 요청
     * </pre>
     *
     * @param
     */
    public void nfcLib_ReqNbId() {
        Log.i("NFC_TEST", "nfcLib_ReqNbId ");
        NbIdReq req = new NbIdReq();

        nfcLib_SendData(req, NFC_RESP_WAIT_TIME_DEFAULT, 0);
    }

    /**
     * <pre>
     * NB-IoT 서비스코드 설정
     * </pre>
     *
     * @param
     */
    public void nfcLib_WriteNbId(String serviceCode) {
        Log.i("NFC_TEST", "nfcLib_WriteNbIdSet ");
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
    public void nfcLib_ChangeMinuteInterval(int Value) {
        Log.i("NFC_TEST", "nfcLib_ChangeReportIntervalToMinute");
        ChangeMinuteIntervalReq req = new ChangeMinuteIntervalReq(Value);

        nfcLib_SendData(req, NFC_RESP_WAIT_TIME_DEFAULT, 0);
    }

    //gsm 단말기에서 gsm or lte 모드 변경
    public void nfcLib_selectGsmOrLte(int Value) {
        Log.i("NFC_TEST", "nfcLib_selectGsmOrLte");
        SelectGsmOrLteReq req = new SelectGsmOrLteReq(Value);

        nfcLib_SendData(req, NFC_RESP_WAIT_TIME_DEFAULT, 0);
    }

    //gsm 단말기에서 도메인 변경
    public void nfcLib_changeDomain(String domain) {
        Log.i("NFC_TEST", "nfcLib_changeDomain");
        GsmChangeDomainReq req = new GsmChangeDomainReq(domain);
        nfcLib_SendData(req, NFC_RESP_WAIT_TIME_DEFAULT, 0);
    }

    /**
     * <pre>
     * Fw Update 설정
     * </pre>
     *
     * @param
     */
    public void nfcLib_ReqFwUpdate(
            String serialNo,
            int reqMode,
            String fwVersion
    ) {
        Log.i("NFC_TEST", "nfcLib_ReqFwUpdate ");
        FwUpdateReq req = new FwUpdateReq(serialNo, reqMode, fwVersion);

        nfcLib_SendData(req, NFC_RESP_WAIT_TIME_DEFAULT, 0);
    }

    /**
     * <pre>
     * Lora App Key 정보 요청
     * </pre>
     *
     * @param
     */
    public void nfcLib_ReqLoraAppEuiKey() {
        Log.i("NFC_TEST", "nfcLib_ReqLoraAppEuiKey ");
        LoraAppEuiKeyReq req = new LoraAppEuiKeyReq();

        nfcLib_SendData(req, NFC_RESP_WAIT_TIME_DEFAULT, 0);
    }

    /**
     * <pre>
     * Lora App Eui 정보 설정
     * </pre>
     *
     * @param
     */
    public void nfcLib_SetLoraAppEuiKey(
            String strDeviceSn,
            int nJoinMode,
            String strAppEui,
            String strAppKey
    ) {
        Log.i("NFC_TEST", "nfcLib_SetLoraAppEuiKey ");
        LoraAppEuiKeySet req = new LoraAppEuiKeySet(
                strDeviceSn,
                nJoinMode,
                strAppEui,
                strAppKey
        );

        nfcLib_SendData(req, NFC_RESP_WAIT_TIME_DEFAULT, 0);
    }

    /**
     * <pre>
     * SmartMeter 검침요청
     * </pre>
     *
     * @param
     */
    public void nfcLib_ReqSmartMeterData() {
        Log.i("NFC_TEST", "nfcLib_ReqSmartMeterData ");
        SmartMeterReq req = new SmartMeterReq();

        nfcLib_SendData(req, NFC_RESP_WAIT_TIME_SMART_METER, 0);
    }

    /**
     * <pre>
     * SmartMeter 카운터 설정
     * </pre>
     *
     * @param
     */
    public void nfcLib_SetSmartMeterCount(int nControl) {
        Log.i("NFC_TEST", "nfcLib_ReqSmartMeterData ");
        SmartMeterValveControl req = new SmartMeterValveControl(nControl);

        nfcLib_SendData(req, NFC_RESP_WAIT_TIME_SMART_METER, 0);
    }

    /**
     * <pre>
     * SmartMeter Config 요청
     * </pre>
     *
     * @param
     */
    public void nfcLib_ReqSmartConfData() {
        Log.i("NFC_TEST", "nfcLib_ReqSmartConfData ");
        SmartConfReq req = new SmartConfReq();

        nfcLib_SendData(req, NFC_RESP_WAIT_TIME_SMART_METER, 0);
    }

    /**
     * <pre>
     * SmartMeter 검침 Config 요청
     * </pre>
     *
     * @param
     */
    public void nfcLib_ReqSmartConfMeterData() {
        Log.i("NFC_TEST", "nfcLib_ReqSmartConfMeterData ");
        SmartConfMeterReq req = new SmartConfMeterReq();

        nfcLib_SendData(req, NFC_RESP_WAIT_TIME_SMART_METER, 0);
    }

    /**
     * <pre>
     * SmartMeter Config SN 설정
     * </pre>
     *
     * @param
     */
    public void nfcLib_SetSmartConfSnData(
            int nFlowType,
            String strDeviceSerial,
            int nMeterCaliber,
            String strMeterSerial,
            int nMaker
    ) {
        Log.i(
                "NFC_TEST",
                "nfcLib_SetSmartConfSnData strMeterSerial:" + strMeterSerial
        );
        SmartConfSet req = new SmartConfSet(
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
        );

        nfcLib_SendData(req, NFC_RESP_WAIT_TIME_SMART_METER, 0);
    }

    /**
     * <pre>
     * SmartMeter Config Calibration 설정
     * </pre>
     *
     * @param
     */
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
        Log.i("NFC_TEST", "nfcLib_SetSmartConfCalibrationData ");
        SmartConfSet req = new SmartConfSet(
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
        );

        nfcLib_SendData(req, NFC_RESP_WAIT_TIME_SMART_METER, 0);
    }

    /**
     * <pre>
     * SmartMeter Config 초음파 보정 설정
     * </pre>
     *
     * @param
     */
    public void nfcLib_SetSmartUltraCompData(
            String strDeviceSerial,
            int nCompSelect,
            int nCompOffset,
            int nCompValue
    ) {
        Log.i("NFC_TEST", "nfcLib_SetSmartUltraCompData ");
        SmartUltraCompSet req = new SmartUltraCompSet(
                strDeviceSerial,
                nCompSelect,
                nCompOffset,
                nCompValue
        );

        nfcLib_SendData(req, NFC_RESP_WAIT_TIME_SMART_METER, 0);
    }

    /**
     * <pre>
     * SmartMeter Config certification Calibration 설정
     * </pre>
     *
     * @param
     */
    public void nfcLib_SetSmartCertiCalibrationData(
            String strDeviceSerial,
            int nCompSelect,
            int nCompValue
    ) {
        Log.i("NFC_TEST", "nfcLib_SetSmartCertiCompData ");
        SmartCertiCalibrationSet req = new SmartCertiCalibrationSet(
                strDeviceSerial,
                nCompSelect,
                nCompValue
        );

        nfcLib_SendData(req, NFC_RESP_WAIT_TIME_SMART_METER, 0);
    }

    /**
     * <pre>
     * SmartMeter Config certification Calibration 요청
     * </pre>
     *
     * @param
     */
    public void nfcLib_ReqSmartCertiCalibrationData() {
        Log.i("NFC_TEST", "nfcLib_ReqSmartCertiCompData ");
        SmartCertiCalibrationReq req = new SmartCertiCalibrationReq();

        nfcLib_SendData(req, NFC_RESP_WAIT_TIME_SMART_METER, 0);
    }

    /**
     * <pre>
     * SmartMeter 온도 설정
     * </pre>
     *
     * @param
     */
    public void nfcAct_SetSmartTemperatureData(
            int nFlowType,
            String strDeviceSerial,
            int nMeterCaliber,
            int nTemperature,
            int nMaker
    ) {
        Log.i(
                "NFC_TEST",
                "nfcAct_SetSmartTempearatureData strMeterSerial:" + strDeviceSerial
        );
        SmartConfSet req = new SmartConfSet(
                NfcConstant.SMART_METER_CHANGE_MODE_WRITE_METER,
                nFlowType,
                strDeviceSerial,
                nMeterCaliber,
                "",
                0,
                0,
                0,
                0,
                0,
                nTemperature,
                nMaker
        );

        nfcLib_SendData(req, NFC_RESP_WAIT_TIME_SMART_METER, 0);
    }

    /**
     * <pre>
     * SmartMeter 자동보정
     * </pre>
     *
     * @param
     */
    public void nfcAct_SetSmartAutoStart(
            int nFlowType,
            String strDeviceSerial,
            int nCompMode,
            int nCompSelect,
            int nQnFlow
    ) {
        Log.i(
                "NFC_TEST",
                "nfcAct_SetSmartAutoStart strMeterSerial:" + strDeviceSerial
        );
        SmartConfAutoStart req = new SmartConfAutoStart(
                nFlowType,
                strDeviceSerial,
                nCompMode,
                nCompSelect,
                nQnFlow
        );

        int writeWaitTime = NFC_RESP_WAIT_TIME_SMART_METER;

        if (strDeviceSerial.length() > 0) {
            writeWaitTime = NFC_RESP_WAIT_TIME_SMART_METER;
        } else {
            writeWaitTime = NFC_RESP_WAIT_TIME_DEFAULT;
        }
        nfcLib_SendData(req, writeWaitTime, 0);
    }

    /**
     * <pre>
     * SmartMeter 검침값 설정
     * </pre>
     *
     * @param
     */
    public void nfcAct_SetSmartMeterValueData(
            String strDeviceSerial,
            String strMeterValue
    ) {
        Log.i("NFC_TEST", "nfcAct_SetSmartMeterValueData ");
        SmartMeterValueSet req = new SmartMeterValueSet(
                strDeviceSerial,
                strMeterValue
        );

        nfcLib_SendData(req, NFC_RESP_WAIT_TIME_SMART_METER, 0);
    }

    /**
     * <pre>
     * Sleep Mode 설정/해제
     * </pre>
     *
     * @param
     */
    public void nfcLib_SetSleepMode(int sleepMode) {
        Log.i("NFC_TEST", "nfcLib_SetSleepMode ");
        BdControlReq req = new BdControlReq(
                NfcConstant.CONF_BD_RESET_NONE,
                sleepMode
        );

        nfcLib_SendData(req, NFC_RESP_WAIT_TIME_DEFAULT, 0);
    }

    /**
     * <pre>
     * Report Mode 설정/해제
     * </pre>
     *
     * @param
     */
    public void nfcLib_SetReportMode(int reportMode) {
        Log.i("NFC_TEST", "nfcLib_SetReportMode ");
        BdControlReq req = new BdControlReq(
                NfcConstant.CONF_BD_RESET_NONE,
                NfcConstant.CONF_SLEEP_STATE_NONE,
                reportMode
        );

        nfcLib_SendData(req, NFC_RESP_WAIT_TIME_DEFAULT, 0);
    }

    /**
     * <pre>
     * 기간검침  설정/해제
     * </pre>
     *
     * @param
     */
    public void nfcLib_SetPeriodMode(int periodMode) {
        Log.i("NFC_TEST", "nfcLib_SetPeriodMode ");
        BdControlReq req = new BdControlReq(
                NfcConstant.CONF_BD_RESET_NONE,
                NfcConstant.CONF_SLEEP_STATE_NONE,
                NfcConstant.CONF_REPORT_MODE_NONE,
                periodMode
        );

        nfcLib_SendData(req, NFC_RESP_WAIT_TIME_DEFAULT, 0);
    }

    /**
     * <pre>
     * 디버그모드  설정/해제
     * </pre>
     *
     * @param
     */
    public void nfcLib_SetDebugMode(int debugMode) {
        Log.i("NFC_TEST", "nfcLib_SetDebugMode ");
        BdControlReq req = new BdControlReq(
                NfcConstant.CONF_BD_RESET_NONE,
                NfcConstant.CONF_SLEEP_STATE_NONE,
                NfcConstant.CONF_REPORT_MODE_NONE,
                NfcConstant.CONF_PERIOD_MODE_NONE,
                debugMode
        );

        nfcLib_SendData(req, NFC_RESP_WAIT_TIME_DEFAULT, 0);
    }

    /**
     * <pre>
     * Data Skip  설정/해제
     * </pre>
     *
     * @param
     */
    public void nfcLib_SetDataSkipMode(int dataSkipMode) {
        Log.i("NFC_TEST", "nfcLib_SetDataSkipMode ");
        BdControlReq req = new BdControlReq(
                NfcConstant.CONF_BD_RESET_NONE,
                NfcConstant.CONF_SLEEP_STATE_NONE,
                NfcConstant.CONF_REPORT_MODE_NONE,
                NfcConstant.CONF_PERIOD_MODE_NONE,
                NfcConstant.CONF_DEBUG_MODE_NONE,
                dataSkipMode
        );

        nfcLib_SendData(req, NFC_RESP_WAIT_TIME_DEFAULT, 0);
    }

    /**
     * <pre>
     * 장비 시간 설정
     * </pre>
     *
     * @param
     */
    public void nfcLib_SetTimeInfo() {
        Log.i("NFC_TEST", "nfcLib_SetDevTime ");
        SetTimeInfo req = new SetTimeInfo();

        nfcLib_SendData(req, NFC_RESP_WAIT_TIME_DEFAULT, 0);
    }
}
