/**
 * <pre>
 * NodeConfReq 메시지
 * </pre>
 */
package com.hitec.presentation.nfc_lib.protocol.send;

import com.hitec.presentation.nfc_lib.protocol.NfcConstant;

public class NbIdSet extends NfcTxMessage {

    public NbIdSet(String serviceCode) {
        m_nOffset = m_nHeaderSize;

        hexData[m_nOffset++] = (byte) NfcConstant.NODE_SEND_NB_ID_SET;
        hexData[m_nOffset++] = (byte) 0; //MsgVersion
        m_nOffset += addServiceCode(m_nOffset, serviceCode);
    }
}
