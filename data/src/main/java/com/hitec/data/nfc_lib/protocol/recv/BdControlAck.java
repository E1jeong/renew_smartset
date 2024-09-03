/**
 * <pre>
 * ServerConnectAck 메시지 Parser
 * </pre>
 */
package com.hitec.data.nfc_lib.protocol.recv;

public class BdControlAck extends NfcRxMessage {

    private int m_nReset = 0;
    private int m_nSleepMode = 0;
    private int m_nReportMode = 0;
    private int m_nPeriodMode = 0;
    private int m_nDebugMode = 0;
    private int m_nDataSkipMode = 0;

    public int GetReset() {
        return m_nReset;
    }

    public int GetSleepMode() {
        return m_nSleepMode;
    }

    public int GetReportMode() {
        return m_nReportMode;
    }

    public int GetPeriodMode() {
        return m_nPeriodMode;
    }

    public int GetDebugMode() {
        return m_nDebugMode;
    }

    public int GetDataSkipMode() {
        return m_nDataSkipMode;
    }

    @Override
    public boolean parse(byte[] rxdata) {
        if (super.parse(rxdata) == false) {
            return false;
        }
        m_nReset = getHexData(m_nOffset++);
        m_nSleepMode = getHexData(m_nOffset++);
        int nMsgLen = m_nMsgLen - 8; //Devicd Id(8)

        //장비에서 메세지 버전정보를 요청한 버전으로 전송되어 길이에 따라 버전 정정
        if (nMsgLen == 4) {
            m_nNodeMsgVersion = 0;
        } else if (nMsgLen == 5) {
            m_nNodeMsgVersion = 1;
        } else if (nMsgLen == 6) {
            m_nNodeMsgVersion = 2;
        } else if (nMsgLen == 7) {
            m_nNodeMsgVersion = 3;
        } else if (nMsgLen == 8) {
            m_nNodeMsgVersion = 4;
        }

        if (m_nNodeMsgVersion >= 1) {
            m_nReportMode = getHexData(m_nOffset++);
        }

        if (m_nNodeMsgVersion >= 2) {
            m_nPeriodMode = getHexData(m_nOffset++);
        }

        if (m_nNodeMsgVersion >= 3) {
            m_nDebugMode = getHexData(m_nOffset++);
        }

        if (m_nNodeMsgVersion >= 4) {
            m_nDataSkipMode = getHexData(m_nOffset++);
        }

        return parseCompleted();
    }
}
