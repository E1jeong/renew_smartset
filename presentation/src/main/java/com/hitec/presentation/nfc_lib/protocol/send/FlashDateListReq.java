/**
 * <pre>
 * PeriodMeterReq 메시지
 * </pre>
 */
package com.hitec.presentation.nfc_lib.protocol.send;

import com.hitec.presentation.nfc_lib.protocol.NfcConstant;

public class FlashDateListReq extends NfcTxMessage {

    public FlashDateListReq() {
        m_nOffset = m_nHeaderSize;

        hexData[m_nOffset++] = (byte) NfcConstant.NODE_SEND_FLASH_DATE_LIST_REQ;
        hexData[m_nOffset++] = (byte) 0; //MsgVersion
    }
}
