/**
 * <pre>
 * NodeMeterDataReq 메시지
 * </pre>
 */
package com.hitec.data.nfc_lib.protocol.send;

import com.hitec.data.nfc_lib.protocol.NfcConstant;

public class ServerConnectReq extends NfcTxMessage {

    public ServerConnectReq(int reqType) {
        m_nOffset = m_nHeaderSize;

        hexData[m_nOffset++] = (byte) NfcConstant.NODE_SEND_SERVER_CONNECT_REQ;
        hexData[m_nOffset++] = (byte) 0; //MsgVersion

        hexData[m_nOffset++] = (byte) reqType;
    }
}
