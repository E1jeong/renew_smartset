/**
 * <pre>
 * MeterReq 메시지
 * </pre>
 */
package com.hitec.data.nfc_lib.protocol.send;

import com.hitec.data.nfc_lib.protocol.NfcConstant;

public class MeterReq extends NfcTxMessage {

    int m_nTimePosition = 0;

    public MeterReq(int meterPort) {
        m_nOffset = m_nHeaderSize;

        hexData[m_nOffset++] = (byte) NfcConstant.NODE_SEND_METER_REQ;
        hexData[m_nOffset++] = (byte) 0; //MsgVersion

        hexData[m_nOffset++] = (byte) meterPort;
        m_nOffset += fillCurrentTime(m_nOffset);
    }

    public MeterReq() {
        m_nTimePosition = m_nHeaderSize + 3; //MsgType(1) + MsgVer(1) + MeterPort(1)
    }

    public int GetTimePosition() {
        return m_nTimePosition;
    }
}
