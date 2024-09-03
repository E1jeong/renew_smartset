/**
 * <pre>
 * NodeConfReport 메시지 Parser
 * </pre>
 */
package com.hitec.data.nfc_lib.protocol.recv;

public class SlaveCheckReport extends NfcRxMessage {

    private int m_nCheckState = 0;
    private String m_strSerial = "";
    private String m_strNWK = "";
    private String m_strBattery = "";
    private int m_nSlaveRssi = 0;
    private int m_nMasterRssi = 0;

    public int GetCheckState() {
        return m_nCheckState;
    }

    public String GetSerialNumber() {
        return m_strSerial;
    }

    public String GetNWK() {
        return m_strNWK;
    }

    public String GetBattery() {
        return m_strBattery;
    }

    public int GetSlaveRssi() {
        return m_nSlaveRssi;
    }

    public int GetMasterRssi() {
        return m_nMasterRssi;
    }

    @Override
    public boolean parse(byte[] rxdata) {
        if (super.parse(rxdata) == false) {
            return false;
        }

        m_nCheckState = getHexData(m_nOffset++);

        m_strSerial = parseSerial(m_nOffset, 10);
        m_nOffset += 10;
        m_strNWK = parseNWK(m_nOffset);
        m_nOffset += 4;
        m_strBattery = parseBattery(m_nOffset++);
        m_nSlaveRssi = getHexData(m_nOffset++);
        m_nMasterRssi = getHexData(m_nOffset++);

        return parseCompleted();
    }
}
