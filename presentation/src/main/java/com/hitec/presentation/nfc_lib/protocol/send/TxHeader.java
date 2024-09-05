/**
 * <pre>
 * RxHeader 메시지 Parser
 * </pre>
 */
package com.hitec.presentation.nfc_lib.protocol.send;

public class TxHeader extends NfcTxMessage {

    int m_nMsgLen = 0;
    int m_nNodeMsgType = 0;
    byte[] timeData = new byte[7];

    public int GetMsgLen() {
        return m_nMsgLen;
    }

    public int GetNodeMsgType() {
        return m_nNodeMsgType;
    }

    public byte[] updateTime() {
        updateCurrentTime(0, timeData);
        return timeData;
    }

    public boolean parse(byte[] txdata) {
        int nOffset = m_nHeaderSize;
        m_nMsgLen = txdata[1] & 0xff;
        m_nNodeMsgType = txdata[nOffset] & 0xff;
        return true;
    }
}
