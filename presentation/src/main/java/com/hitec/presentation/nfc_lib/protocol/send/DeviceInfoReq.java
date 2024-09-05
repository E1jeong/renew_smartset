/**
 * <pre>
 * NodeConfReq 메시지
 * </pre>
 */
package com.hitec.presentation.nfc_lib.protocol.send;

import com.hitec.presentation.nfc_lib.protocol.NfcConstant;

public class DeviceInfoReq extends NfcTxMessage {

    public DeviceInfoReq() {
        m_nOffset = m_nHeaderSize;

        hexData[m_nOffset++] = (byte) NfcConstant.NODE_SEND_DEVICE_INFO_REQ;
        hexData[m_nOffset++] = (byte) 0; //MsgVersion
    }
}
