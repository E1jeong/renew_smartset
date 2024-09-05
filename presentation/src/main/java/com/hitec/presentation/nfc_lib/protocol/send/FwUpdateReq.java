/**
 * <pre>
 * NodeConfReq 메시지
 * </pre>
 */
package com.hitec.presentation.nfc_lib.protocol.send;

import com.hitec.presentation.nfc_lib.protocol.NfcConstant;

public class FwUpdateReq extends NfcTxMessage {

    public FwUpdateReq(String serialNo, int reqMode, String fwVersion) {
        m_nOffset = m_nHeaderSize;

        hexData[m_nOffset++] = (byte) NfcConstant.NODE_SEND_FW_UPDATE_REQ;
        hexData[m_nOffset++] = (byte) 0; //MsgVersion

        m_nOffset += fillSerialNumber(m_nOffset, serialNo, 12);

        hexData[m_nOffset++] = (byte) reqMode;

        m_nOffset += addFwVersion(m_nOffset, fwVersion);
    }
}
