/**
 * <pre>
 * FwVerReport 메시지 Parser
 * </pre>
 */
package com.hitec.presentation.nfc_lib.protocol.recv;

public class SmartMeterErrorResult extends NfcRxMessage {

    private String strFwVer = "";
    private String m_strSerialNum = "";
    private int m_nErrorResult = 0;

    public String GetFwVersion() {
        return strFwVer;
    }

    public String GetSerialNumber() {
        return m_strSerialNum;
    }

    public int GetErrorResult() {
        return m_nErrorResult;
    }

    @Override
    public boolean parse(byte[] rxdata) {
        if (super.parse(rxdata) == false) {
            return false;
        }

        m_strSerialNum = parseSerial(m_nOffset, 12);
        m_nOffset += 12;


        strFwVer = parseFwVersion(m_nOffset);
        m_nOffset += 4;

        m_nErrorResult = getHexData(m_nOffset++);

        return parseCompleted();
    }
}
