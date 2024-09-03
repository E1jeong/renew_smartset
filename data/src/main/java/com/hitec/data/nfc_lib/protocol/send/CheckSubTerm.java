/**
 * <pre>
 * NodeConfReq 메시지
 * </pre>
 */
package com.hitec.data.nfc_lib.protocol.send;

import com.hitec.data.nfc_lib.protocol.NfcConstant;

public class CheckSubTerm extends NfcTxMessage {

    public CheckSubTerm() {
        m_nOffset = m_nHeaderSize;

        hexData[m_nOffset++] = (byte) NfcConstant.NODE_SEND_SLAVE_CHECK_REQ;
        hexData[m_nOffset++] = (byte) 0; //MsgVersion
    }
}
