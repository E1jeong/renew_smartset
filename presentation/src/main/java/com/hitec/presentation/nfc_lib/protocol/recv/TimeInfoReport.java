/**
 * <pre>
 * NodeConfReport 메시지 Parser
 * </pre>
 */
package com.hitec.presentation.nfc_lib.protocol.recv;

public class TimeInfoReport extends NfcRxMessage {

    private int m_nReportType = 0;

    public int GetReportType() {
        return m_nReportType;
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

        if (m_nNodeMsgVersion == 0) {
            nYear = parseYear(m_nOffset);
            m_nOffset += 2;
            nMon = getHexData(m_nOffset++);
            nDay = getHexData(m_nOffset++);
            nHour = getHexData(m_nOffset++);
            nMin = getHexData(m_nOffset++);
            nSec = getHexData(m_nOffset++);
        }

        return parseCompleted();
    }
}
