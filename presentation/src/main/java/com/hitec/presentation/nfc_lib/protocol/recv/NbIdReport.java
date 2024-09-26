/**
 * <pre>
 * NodeConfReport 메시지 Parser
 * </pre>
 */
package com.hitec.presentation.nfc_lib.protocol.recv;

import com.hitec.presentation.nfc_lib.util.DevUtil;

public class NbIdReport extends NfcRxMessage {

    private int m_nReportType = 0;
    private String m_strCseId = "";
    private String serviceCode = "";
    private String m_strIccId = "";

    public int GetReportType() {
        return m_nReportType;
    }

    public String GetCseId() {
        return m_strCseId;
    }

    public String getServiceCode() {
        return serviceCode;
    }

    public String getIccId() {
        String dstIccId = "";
        if (m_strIccId.length() >= 19) {
            dstIccId = m_strIccId.substring(0, 19);
        }
        return dstIccId;
    }

    @Override
    public boolean parse(byte[] rxData) {
        if (!super.parse(rxData)) {
            return false;
        }

        m_nReportType = getHexData(m_nOffset++);

        if (m_nNodeMsgVersion == 1) { //Version 2
            serviceCode = parseAsciiData(m_nOffset, 4);
            m_nOffset += 4;
        }

        m_strCseId = parseCseId(m_nOffset, 25);
        m_nOffset += 25;

        m_strIccId = DevUtil.convHexToStringCode(hexData, m_nOffset, 10);
        m_nOffset += 10;

        if (m_nNodeMsgVersion == 0) { //version 1
            if (m_strCseId.length() >= 25) {
                serviceCode = m_strCseId.substring(21, 25);
            }
        }

        if (m_strCseId.isEmpty()) {
            m_strIccId = "";
        }

        return parseCompleted();
    }
}
