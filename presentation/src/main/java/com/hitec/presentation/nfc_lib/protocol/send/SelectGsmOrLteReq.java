/**
 * <pre>
 * SelectGsmOrLteReq
 * gsm 단말기에서 모뎀의 gsm or lte 모드 변경
 * </pre>
 */
package com.hitec.presentation.nfc_lib.protocol.send;

import com.hitec.presentation.nfc_lib.protocol.NfcConstant;

public class SelectGsmOrLteReq extends NfcTxMessage {

    public SelectGsmOrLteReq(int Value) {
        m_nOffset = m_nHeaderSize;

        hexData[m_nOffset++] = (byte) NfcConstant.NODE_SEND_SELECT_GSM_OR_LTE_REQ;
        hexData[m_nOffset++] = (byte) 0; //MsgVersion
        hexData[m_nOffset++] = (byte) Value; // 0: auto, 1:gsm, 3: lte
    }
}
