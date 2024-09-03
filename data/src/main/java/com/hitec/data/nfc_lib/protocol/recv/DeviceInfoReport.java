/**
 * <pre>
 * FwVerReport 메시지 Parser
 * </pre>
 */
package com.hitec.data.nfc_lib.protocol.recv;

public class DeviceInfoReport extends NfcRxMessage {

    private String strFwVer = "";
    private String m_strSerialNum = "";
    private int m_nDeviceCode = 0;
    private int m_nDeviceType = 0;

    public String GetFwVersion() {
        return strFwVer;
    }

    public String GetSerialNumber() {
        return m_strSerialNum;
    }

    public int GetDeviceCode() {
        return m_nDeviceCode;
    }

    public int GetDeviceType() {
        return m_nDeviceType;
    }

    @Override
    public boolean parse(byte[] rxdata) {
        if (super.parse(rxdata) == false) {
            return false;
        }

        strFwVer = parseFwVersion(m_nOffset);
        m_nOffset += 4;


        m_strSerialNum = parseSerial(m_nOffset, 12);
        m_nOffset += 12;


        m_nDeviceCode = getHexData(m_nOffset++);
        m_nDeviceType = getHexData(m_nOffset++);

        return parseCompleted();
    }
}
