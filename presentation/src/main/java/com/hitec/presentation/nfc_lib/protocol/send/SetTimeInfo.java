/**
 * <pre>
 * MeterReq 메시지
 * </pre>
 */
package com.hitec.presentation.nfc_lib.protocol.send;

import com.hitec.presentation.nfc_lib.protocol.NfcConstant;

public class SetTimeInfo extends NfcTxMessage {

    public SetTimeInfo() {
        m_nOffset = m_nHeaderSize;

        hexData[m_nOffset++] = (byte) NfcConstant.NODE_SEND_TIME_INFO_SET;
        hexData[m_nOffset++] = (byte) 0; //MsgVersion

        m_nOffset += fillCurrentTime(m_nOffset);
    }
}
