/**
 * <pre>
 * NodeConfReport 메시지 Parser
 * </pre>
 */
package com.hitec.data.nfc_lib.protocol.recv;

public class LoraAppEuiKeyReport extends NfcRxMessage {

    private int m_nReportType = 0;
    private int m_nResultType = 0;
    private int m_nErrorCode = 0;
    private String m_strSerialNum = "";
    private String m_strAppEui = "";
    private String m_strAppKey = "";
    private int m_nFwVersion = 0;
    private String m_strFwVersion = "";

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

    public String GetAppEui() {
        return m_strAppEui;
    }

    public String GetAppKey() {
        return m_strAppKey;
    }

    public String GetFwVersion() {
        return m_strFwVersion;
    }

    public String GetNodeTime() {
        return getMessageTime();
    }

    @Override
    public boolean parse(byte[] rxdata) {
        if (super.parse(rxdata) == false) {
            return false;
        }

        m_nReportType = getHexData(m_nOffset++);
        m_nResultType = getHexData(m_nOffset++);
        m_nErrorCode = getHexData(m_nOffset++);

        m_strSerialNum = parseSerial(m_nOffset, 12);
        m_nOffset += 12;

        m_strFwVersion = parseFwVersion(m_nOffset);
        m_nOffset += 4;

        m_strAppEui = parseHexData(m_nOffset, 8);
        m_nOffset += 8;

        m_strAppKey = parseHexData(m_nOffset, 16);
        m_nOffset += 16;

        return parseCompleted();
    }
}
