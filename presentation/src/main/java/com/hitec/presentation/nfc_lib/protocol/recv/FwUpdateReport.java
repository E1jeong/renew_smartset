/**
 * <pre>
 * NodeConfReport 메시지 Parser
 * </pre>
 */
package com.hitec.presentation.nfc_lib.protocol.recv;

public class FwUpdateReport extends NfcRxMessage {


    private int m_nResultCode = 0;
    private int m_nStateCode = 0;
    private String m_strSerialNum = "";
    private String m_strFwVersion = "";

    public int GetResultCode() {
        return m_nResultCode;
    }

    public int GetStateCode() {
        return m_nStateCode;
    }

    public String GetSerialNumber() {
        return m_strSerialNum;
    }

    public String GetFwVersion() {
        return m_strFwVersion;
    }

    @Override
    public boolean parse(byte[] rxdata) {
        if (super.parse(rxdata) == false) {
            return false;
        }

        m_nResultCode = getHexData(m_nOffset++);
        m_nStateCode = getHexData(m_nOffset++);

        m_strSerialNum = parseSerial(m_nOffset, 12);
        m_nOffset += 12;


        m_strFwVersion = parseFwVersion(m_nOffset);
        m_nOffset += 4;

        return parseCompleted();
    }
}
