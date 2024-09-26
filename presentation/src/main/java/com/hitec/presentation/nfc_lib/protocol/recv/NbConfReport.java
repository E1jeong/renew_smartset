/**
 * <pre>
 * NodeConfReport 메시지 Parser
 * </pre>
 */
package com.hitec.presentation.nfc_lib.protocol.recv;

import android.util.Log;

import com.hitec.presentation.nfc_lib.util.DevUtil;
import com.hitec.presentation.nfc_lib.util.Meter;

import java.util.Locale;

public class NbConfReport extends NfcRxMessage {

    private int m_nReportType = 0;
    private int m_nResultType = 0;
    private int m_nErrorCode = 0;
    private String serialNumber = "";
    private int m_nBattLevel = 0;
    private int sleepStatus = 0;
    private int m_nDataFormat = 0;
    private int m_nPlatformMode = 0;
    private String m_strNbImsi = "";
    private String serverIp = "";
    private int serverPort = 0;
    private String m_strPAN = "";
    private String m_strNWK = "";
    private int m_nActiveMode = 0;
    private String fwVersion = "";
    // meter
    private int meterCount = 0;
    private final int[] m_nMeterUtility = new int[MAX_METER_PER_TERMINAL];
    private final int[] m_nMeterType = new int[MAX_METER_PER_TERMINAL];
    private final int[] m_nMeterPort = new int[MAX_METER_PER_TERMINAL];
    private int meterInterval = 0;
    private int reportInterval = 0;
    private int nAmiReportRange = 0;
    private int m_nSubId = 0;
    private int m_nGsmlteMode = 0;

    public int GetReportType() {
        return m_nReportType;
    }

    public int GetResultType() {
        return m_nResultType;
    }

    public int GetErrorCode() {
        return m_nErrorCode;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public String getBatteryVoltage() {
        return String.format(Locale.KOREA, "%d.%d", m_nBattLevel / 10, m_nBattLevel % 10);
    }

    public int getSleepStatus() {
        return sleepStatus;
    }

    public int GetDataFormat() {
        return m_nDataFormat;
    }

    public int GetPlatformMode() {
        return m_nPlatformMode;
    }

    public String getNbImsi() {
        String dstImsi = "";
        String[] azImsi = new String[4];
        if (m_strNbImsi.length() >= 15) {
            //450-06-1222992799
            azImsi[0] = m_strNbImsi.substring(0, 3);
            azImsi[1] = m_strNbImsi.substring(3, 5);
            azImsi[2] = m_strNbImsi.substring(5, 15);

            dstImsi = azImsi[0] + "-" + azImsi[1] + "-" + azImsi[2];
        }
        return dstImsi;
    }

    public String getServerIp() {
        return serverIp;
    }

    public String getServerPort() {
        return Integer.toString(serverPort);
    }

    public String GetPAN() {
        return m_strPAN;
    }

    public String GetNWK() {
        return m_strNWK;
    }

    public int GetActiveMode() {
        return m_nActiveMode;
    }

    public String getFwVersion() {
        return fwVersion;
    }

    public int getMeterCount() {
        return meterCount;
    }

    private void initIntArray() {
        for (int i = 0; i < MAX_METER_PER_TERMINAL; i++) {
            m_nMeterUtility[i] = 0;
            m_nMeterType[i] = 0;
            m_nMeterPort[i] = 0;
        }
        meterCount = 0;
    }

    public int GetMeterUtility(int index) {
        return m_nMeterUtility[index];
    }

    public int GetMeterUtility() {
        return GetMeterUtility(0);
    }

    public int GetMeterType(int index) {
        return m_nMeterType[index];
    }

    public int getMeterProtocol() {
        return GetMeterType(0);
    }

    public int GetMeterPort(int index) {
        return m_nMeterPort[index];
    }

    public int GetMeterPort() {
        return GetMeterPort(0);
    }

    public int getMeterInterval() {
        return meterInterval;
    }

    public int getReportInterval() {
        return reportInterval;
    }

    public int GetAmiReportRange() {
        return nAmiReportRange;
    }

    public String GetSubId() {
        return String.format("%d", m_nSubId);
    }

    public String GetNodeTime() {
        return getMessageTime();
    }

    public int GetGsmlteMode() {
        return m_nGsmlteMode;
    }

    @Override
    public boolean parse(byte[] rxData) {
        if (!super.parse(rxData)) {
            return false;
        }

        initIntArray();

        m_nReportType = getHexData(m_nOffset++);
        m_nResultType = getHexData(m_nOffset++);
        m_nErrorCode = getHexData(m_nOffset++);


        serialNumber = parseSerial(m_nOffset, 12);
        m_nOffset += 12;


        sleepStatus = getHexData(m_nOffset++);

        if (m_nNodeMsgVersion < 4) {
            m_nDataFormat = getHexData(m_nOffset++);
        }

        m_strNbImsi = DevUtil.convHexToStringCode(hexData, m_nOffset, 8);
        m_nOffset += 8;
        serverIp = parseServerIP(m_nOffset);
        m_nOffset += 4;
        serverPort = parseServerPort(m_nOffset);
        m_nOffset += 2;

        m_nBattLevel = getHexData(m_nOffset++);
        fwVersion = parseFwVersion(m_nOffset);
        m_nOffset += 4;

        //플랫폼
        m_nPlatformMode = GetNbPlatformMode(fwVersion);

        meterInterval = getHexData(m_nOffset++);
        reportInterval = getHexData(m_nOffset++);

        if (m_nNodeMsgVersion >= 4) {
            nAmiReportRange = getHexData(m_nOffset++);
        } else {
            nAmiReportRange = reportInterval;
        }

        if (m_nNodeMsgVersion == 0 || m_nNodeMsgVersion == 2 || m_nNodeMsgVersion == 4) { //단말기
            parserTerm();
        } else if (m_nNodeMsgVersion == 1 || m_nNodeMsgVersion == 3 || m_nNodeMsgVersion == 5) { //보조중계기
            parserMaster();
        }

        return parseCompleted();
    }

    private void parserTerm() {
        if (m_nNodeMsgVersion >= 4) {
            parseCompressDateTime(m_nOffset);
            m_nOffset += 4;
        } else {
            nYear = parseYear(m_nOffset);
            m_nOffset += 2;
            nMon = getHexData(m_nOffset++);
            nDay = getHexData(m_nOffset++);
            nHour = getHexData(m_nOffset++);
            nMin = getHexData(m_nOffset++);
            nSec = getHexData(m_nOffset++);
        }

//        if (protocolMode.equals(Constanse.BT_DEVICE_PROTOCOL_GSM)) {
//            m_nGsmlteMode = getHexData(m_nOffset++);
//        }

        meterCount = getHexData(m_nOffset++);
        if (meterCount > MAX_METER_PER_TERMINAL) {
            meterCount = MAX_METER_PER_TERMINAL;
        }

        for (int i = 0; i < meterCount; i++) {
            m_nMeterType[i] = getHexData(m_nOffset++);
            m_nMeterPort[i] = getHexData(m_nOffset++);
            m_nMeterUtility[i] = Meter.GetMeterUtility(m_nMeterType[i]);
        }

        m_nActiveMode = ACTIVE_MODE_AMI_AMI;
        m_nSubId = 0;
    }

    private void parserMaster() {
        m_strPAN = parsePAN(m_nOffset);
        m_nOffset += 2;
        m_strNWK = parseNWK(m_nOffset);
        m_nOffset += 4;

        m_nActiveMode = ACTIVE_MODE_AMI_AMI_M;
        m_nSubId = getHexData(m_nOffset++);

        meterCount = 0;
        m_nMeterType[0] = 0x01;
        m_nMeterPort[0] = 0x01; //uart

        parseCompressDateTime(m_nOffset);
        m_nOffset += 4;
    }

    private int GetNbPlatformMode(String strFwVersion) {
        try {
            int nParseVer;
            String strVerHead;
            String strVerBody;

            //length 가 4이하이면 오류
            if (strFwVersion.length() != 4) {
                return 0;
            }

            strVerHead = strFwVersion.substring(0, 1);
            strVerBody = strFwVersion.substring(1, 4);
            nParseVer = Integer.parseInt(strVerBody, 10);

            if (strVerHead.equals("U")) {
                return 1;
            } else {
                return 0;
            }
        } catch (Exception e) {
            Log.v("log", "GetNbPlatformVersion ===> Exception ==> strFwVersion:" + strFwVersion);
            return 0;
        }
    }
}
