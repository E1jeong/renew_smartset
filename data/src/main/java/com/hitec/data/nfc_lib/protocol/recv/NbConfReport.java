/**
 * <pre>
 * NodeConfReport 메시지 Parser
 * </pre>
 */
package com.hitec.data.nfc_lib.protocol.recv;

import android.util.Log;

import com.hitec.data.nfc_lib.util.Constanse;
import com.hitec.data.nfc_lib.util.DevUtil;
import com.hitec.data.nfc_lib.util.Meter;

public class NbConfReport extends NfcRxMessage {

    private int m_nReportType = 0;
    private int m_nResultType = 0;
    private int m_nErrorCode = 0;
    private String m_strSerialNum = "";
    private int m_nBattLevel = 0;
    private int m_nSleepStatus = 0;
    private int m_nDataFormat = 0;
    private int m_nPlatformMode = 0;
    private String m_strNbImsi = "";
    private String m_strServerIp = "";
    private int m_nServerPort = 0;
    private String m_strPAN = "";
    private String m_strNWK = "";
    private int m_nActiveMode = 0;
    private int m_nFwVersion = 0;
    private String m_strFwVersion = "";
    // meter
    private int m_nMeterCount = 0;
    private int[] m_nMeterUtility = new int[MAX_METER_PER_TERMINAL];
    private int[] m_nMeterType = new int[MAX_METER_PER_TERMINAL];
    private int[] m_nMeterPort = new int[MAX_METER_PER_TERMINAL];
    private int nAmiMeteringInterval = 0;
    private int nAmiReportInterval = 0;
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

    public String GetSerialNumber() {
        return m_strSerialNum;
    }

    public String GetBattLevel() {
        return String.format("%d.%d", m_nBattLevel / 10, m_nBattLevel % 10);
    }

    public int GetSleepStatus() {
        return m_nSleepStatus;
    }

    public int GetDataFormat() {
        return m_nDataFormat;
    }

    public int GetPlatformMode() {
        return m_nPlatformMode;
    }

    public String GetNbImsi() {
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

    public String GetServerIp() {
        return m_strServerIp;
    }

    public String GetServerPort() {
        return Integer.toString(m_nServerPort);
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

    public String GetFwVersion() {
        return m_strFwVersion;
    }

    public int GetNumOfMeter() {
        return m_nMeterCount;
    }

    private void initIntArray() {
        for (int i = 0; i < MAX_METER_PER_TERMINAL; i++) {
            m_nMeterUtility[i] = 0;
            m_nMeterType[i] = 0;
            m_nMeterPort[i] = 0;
        }
        m_nMeterCount = 0;
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

    public int GetMeterType() {
        return GetMeterType(0);
    }

    public int GetMeterPort(int index) {
        return m_nMeterPort[index];
    }

    public int GetMeterPort() {
        return GetMeterPort(0);
    }

    public int GetAmiMeteringInterval() {
        return nAmiMeteringInterval;
    }

    public int GetAmiReportInterval() {
        return nAmiReportInterval;
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
    public boolean parse(byte[] rxdata) {
        if (super.parse(rxdata) == false) {
            return false;
        }

        initIntArray();

        m_nReportType = getHexData(m_nOffset++);
        m_nResultType = getHexData(m_nOffset++);
        m_nErrorCode = getHexData(m_nOffset++);


        m_strSerialNum = parseSerial(m_nOffset, 12);
        m_nOffset += 12;


        m_nSleepStatus = getHexData(m_nOffset++);

        if (m_nNodeMsgVersion < 4) {
            m_nDataFormat = getHexData(m_nOffset++);
        }

        m_strNbImsi = DevUtil.convHexToStringCode(hexData, m_nOffset, 8);
        m_nOffset += 8;
        m_strServerIp = parseServerIP(m_nOffset);
        m_nOffset += 4;
        m_nServerPort = parseServerPort(m_nOffset);
        m_nOffset += 2;

        m_nBattLevel = getHexData(m_nOffset++);
        m_strFwVersion = parseFwVersion(m_nOffset);
        m_nOffset += 4;

        //플랫폼
        m_nPlatformMode = GetNbPlatformMode(m_strFwVersion);

        nAmiMeteringInterval = getHexData(m_nOffset++);
        nAmiReportInterval = getHexData(m_nOffset++);

        if (m_nNodeMsgVersion >= 4) {
            nAmiReportRange = getHexData(m_nOffset++);
        } else {
            nAmiReportRange = nAmiReportInterval;
        }

        if (
                m_nNodeMsgVersion == 0 || m_nNodeMsgVersion == 2 || m_nNodeMsgVersion == 4
        ) { //단말기
            parserTerm();
        } else if (
                m_nNodeMsgVersion == 1 || m_nNodeMsgVersion == 3 || m_nNodeMsgVersion == 5
        ) { //보조중계기
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

        m_nMeterCount = getHexData(m_nOffset++);
        if (m_nMeterCount > MAX_METER_PER_TERMINAL) {
            m_nMeterCount = MAX_METER_PER_TERMINAL;
        }

        for (int i = 0; i < m_nMeterCount; i++) {
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

        m_nMeterCount = 0;
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
            Log.v(
                    "log",
                    "GetNbPlatformVersion ===> Exception ==> strFwVersion:" + strFwVersion
            );
            return 0;
        }
    }
}