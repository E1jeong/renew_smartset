/**
 * <pre>
 * SnChangeReq 장비일련번호 변경 요청
 * </pre>
 */
package com.hitec.presentation.nfc_lib.protocol.send;

import com.hitec.presentation.nfc_lib.protocol.NfcConstant;

public class SnChangeReq extends NfcTxMessage {

    public SnChangeReq(String serialNumber, int serialNumberLength) {
        m_nOffset = m_nHeaderSize;

        hexData[m_nOffset++] = (byte) NfcConstant.NODE_SEND_SN_CHANGE_REQ;
        hexData[m_nOffset++] = (byte) 0; //MsgVersion
        hexData[m_nOffset++] = (byte) serialNumberLength;
        m_nOffset += fillSerialNumber(m_nOffset, serialNumber, serialNumberLength);
    }
}
