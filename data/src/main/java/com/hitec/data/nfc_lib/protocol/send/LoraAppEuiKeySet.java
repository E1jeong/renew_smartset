/**
 * <pre>
 * NodeConfReq 메시지
 * </pre>
 */
package com.hitec.data.nfc_lib.protocol.send;

import com.hitec.data.nfc_lib.protocol.NfcConstant;

public class LoraAppEuiKeySet extends NfcTxMessage {

    public LoraAppEuiKeySet(
            String strDeviceSn,
            int nJoinMode,
            String strAppEui,
            String strAppKey
    ) {
        m_nOffset = m_nHeaderSize;

        hexData[m_nOffset++] = (byte) NfcConstant.NODE_SEND_LORA_EUI_KEY_SET;
        hexData[m_nOffset++] = (byte) 0; //MsgVersion

        m_nOffset += fillSerialNumber(m_nOffset, strDeviceSn, 12);

        hexData[m_nOffset++] = (byte) nJoinMode;

        //App EUI
        m_nOffset += fillHexData(m_nOffset, strAppEui, 8);
        //App Key
        m_nOffset += fillHexData(m_nOffset, strAppKey, 16);
    }
}
