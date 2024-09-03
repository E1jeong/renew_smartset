/**
 * <pre>
 * ServerConnectAck 메시지 Parser
 * </pre>
 */
package com.hitec.data.nfc_lib.protocol.recv;

public class ServerJoinReport extends NfcRxMessage {

    private int m_nServerAccess = 0;
    private int m_nServerResult = 0;
    private int m_nModemRssi = 0;
    private int m_nModemSnr = 0;
    private int m_nModemRsrp = 0;
    private int m_nModemRsrq = 0;
    private int m_nModemCid = 0;
    // Date/Time
    private int m_nLastYear = 0;
    private int m_nLastMon = 0;
    private int m_nLastDay = 0;
    private int m_nLastHour = 0;
    private int m_nLastMin = 0;
    private int m_nLastSec = 0;

    public int GetServerAccess() {
        return m_nServerAccess;
    }

    public int GetServerResult() {
        return m_nServerResult;
    }

    public int GetModemRssi() {
        return m_nModemRssi;
    }

    public int GetModemSnr() {
        return m_nModemSnr;
    }

    public int GetModemRsrp() {
        return m_nModemRsrp;
    }

    public int GetModemRsrq() {
        return m_nModemRsrq;
    }

    public int GetModemCid() {
        return m_nModemCid;
    }

    public String GetNodeTime() {
        return getMessageTime();
    }

    public String GetLastTime() {
        return String.format(
                "%04d-%02d-%02d %02d:%02d:%02d",
                m_nLastYear,
                m_nLastMon,
                m_nLastDay,
                m_nLastHour,
                m_nLastMin,
                m_nLastSec
        );
    }

    @Override
    public boolean parse(byte[] rxdata) {
        if (super.parse(rxdata) == false) {
            return false;
        }

        nYear = parseYear(m_nOffset);
        m_nOffset += 2;
        nMon = getHexData(m_nOffset++);
        nDay = getHexData(m_nOffset++);
        nHour = getHexData(m_nOffset++);
        nMin = getHexData(m_nOffset++);
        nSec = getHexData(m_nOffset++);

        m_nLastYear = parseYear(m_nOffset);
        m_nOffset += 2;
        m_nLastMon = getHexData(m_nOffset++);
        m_nLastDay = getHexData(m_nOffset++);
        m_nLastHour = getHexData(m_nOffset++);
        m_nLastMin = getHexData(m_nOffset++);
        m_nLastSec = getHexData(m_nOffset++);

        m_nServerAccess = getHexData(m_nOffset++);
        m_nServerResult = getHexData(m_nOffset++);
        m_nModemRssi = getHexData(m_nOffset++);

        m_nModemSnr = getHexData(m_nOffset++);
        if (m_nModemSnr > 0x80) {
            m_nModemSnr -= 256;
        }
        m_nModemRsrp = getHexData(m_nOffset++);
        m_nModemRsrq = getHexData(m_nOffset++);
        m_nModemCid = getHexData(m_nOffset++);

        return parseCompleted();
    }
}
