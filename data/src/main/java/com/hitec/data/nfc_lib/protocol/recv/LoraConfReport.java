/**
 * <pre>
 * NodeConfReport 메시지 Parser
 * </pre>
 */
package com.hitec.data.nfc_lib.protocol.recv;

import com.hitec.data.nfc_lib.util.DevUtil;
import com.hitec.data.nfc_lib.util.Meter;

public class LoraConfReport extends NfcRxMessage {

    private int m_nReportType = 0;
    private int m_nResultType = 0;
    private int m_nErrorCode = 0;
    private String m_strSerialNum = "";
    private int m_nBattLevel = 0;
    private int m_nSleepStatus = 0;
    private int m_nDataFormat = 0;
    private String m_strAppEui = "";
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
    private int m_nSubId = 0;

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

    public String GetAppEui() {
        return m_strAppEui;
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

    public String GetSubId() {
        return String.format("%d", m_nSubId);
    }

    public String GetNodeTime() {
        return getMessageTime();
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
        m_nDataFormat = getHexData(m_nOffset++);

        m_strAppEui = DevUtil.convHexToStringCode(hexData, m_nOffset, 8);
        m_nOffset += 8;

        m_nBattLevel = getHexData(m_nOffset++);
        m_strFwVersion = parseFwVersion(m_nOffset);
        m_nOffset += 4;

        nAmiMeteringInterval = getHexData(m_nOffset++);
        nAmiReportInterval = getHexData(m_nOffset++);

        if (m_nNodeMsgVersion == 0) {
            nYear = parseYear(m_nOffset);
            m_nOffset += 2;
            nMon = getHexData(m_nOffset++);
            nDay = getHexData(m_nOffset++);
            nHour = getHexData(m_nOffset++);
            nMin = getHexData(m_nOffset++);
            nSec = getHexData(m_nOffset++);

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
        } else if (m_nNodeMsgVersion == 1) {
            m_strPAN = parsePAN(m_nOffset);
            m_nOffset += 2;
            m_strNWK = parseNWK(m_nOffset);
            m_nOffset += 4;

            m_nActiveMode = ACTIVE_MODE_AMI_AMI_M;
            m_nSubId = getHexData(m_nOffset++);

            parseCompressDateTime(m_nOffset);
            m_nOffset += 4;
        }

        return parseCompleted();
    }
}
