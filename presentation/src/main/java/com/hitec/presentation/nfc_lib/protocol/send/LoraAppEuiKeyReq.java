/**
 * <pre>
 * NodeConfReq 메시지
 * </pre>
 */
package com.hitec.presentation.nfc_lib.protocol.send;

import com.hitec.presentation.nfc_lib.protocol.NfcConstant;

public class LoraAppEuiKeyReq extends NfcTxMessage {

    public LoraAppEuiKeyReq() {
        m_nOffset = m_nHeaderSize;

        hexData[m_nOffset++] = (byte) NfcConstant.NODE_SEND_LORA_EUI_KEY_REQ;
        hexData[m_nOffset++] = (byte) 0; //MsgVersion
    }
}