/**
 * <pre>
 * NodeMeterDataReq 메시지
 * </pre>
 */
package com.hitec.presentation.nfc_lib.protocol.send;

import com.hitec.presentation.nfc_lib.protocol.NfcConstant;

public class BdControlReq extends NfcTxMessage {

    public BdControlReq(int reset, int sleepMode) {
        m_nOffset = m_nHeaderSize;

        hexData[m_nOffset++] = (byte) NfcConstant.NODE_SEND_BD_CONTROL_REQ;
        hexData[m_nOffset++] = (byte) 0; //MsgVersion

        hexData[m_nOffset++] = (byte) reset;
        hexData[m_nOffset++] = (byte) sleepMode;
    }

    public BdControlReq(int reset, int sleepMode, int reportMode) {
        m_nOffset = m_nHeaderSize;

        hexData[m_nOffset++] = (byte) NfcConstant.NODE_SEND_BD_CONTROL_REQ;
        hexData[m_nOffset++] = (byte) 1; //MsgVersion

        hexData[m_nOffset++] = (byte) reset;
        hexData[m_nOffset++] = (byte) sleepMode;
        hexData[m_nOffset++] = (byte) reportMode;
    }

    public BdControlReq(
            int reset,
            int sleepMode,
            int reportMode,
            int periodMode
    ) {
        m_nOffset = m_nHeaderSize;

        hexData[m_nOffset++] = (byte) NfcConstant.NODE_SEND_BD_CONTROL_REQ;
        hexData[m_nOffset++] = (byte) 2; //MsgVersion

        hexData[m_nOffset++] = (byte) reset;
        hexData[m_nOffset++] = (byte) sleepMode;
        hexData[m_nOffset++] = (byte) reportMode;
        hexData[m_nOffset++] = (byte) periodMode;
    }

    public BdControlReq(
            int reset,
            int sleepMode,
            int reportMode,
            int periodMode,
            int debugMode
    ) {
        m_nOffset = m_nHeaderSize;

        hexData[m_nOffset++] = (byte) NfcConstant.NODE_SEND_BD_CONTROL_REQ;
        hexData[m_nOffset++] = (byte) 3; //MsgVersion

        hexData[m_nOffset++] = (byte) reset;
        hexData[m_nOffset++] = (byte) sleepMode;
        hexData[m_nOffset++] = (byte) reportMode;
        hexData[m_nOffset++] = (byte) periodMode;
        hexData[m_nOffset++] = (byte) debugMode;
    }

    public BdControlReq(
            int reset,
            int sleepMode,
            int reportMode,
            int periodMode,
            int debugMode,
            int dataSkipMode
    ) {
        m_nOffset = m_nHeaderSize;

        hexData[m_nOffset++] = (byte) NfcConstant.NODE_SEND_BD_CONTROL_REQ;
        hexData[m_nOffset++] = (byte) 4; //MsgVersion

        hexData[m_nOffset++] = (byte) reset;
        hexData[m_nOffset++] = (byte) sleepMode;
        hexData[m_nOffset++] = (byte) reportMode;
        hexData[m_nOffset++] = (byte) periodMode;
        hexData[m_nOffset++] = (byte) debugMode;
        hexData[m_nOffset++] = (byte) dataSkipMode;
    }
}
