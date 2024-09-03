/**
 * <pre>
 * PeriodMeterAck 메시지
 * </pre>
 */
package com.hitec.data.nfc_lib.protocol.send;

import com.hitec.data.nfc_lib.protocol.NfcConstant;

public class PeriodMeterAck extends NfcTxMessage {

    public PeriodMeterAck(int nTotalBlock, int nCurrentblock) {
        m_nOffset = m_nHeaderSize;

        hexData[m_nOffset++] = (byte) NfcConstant.NODE_SEND_PERIOD_METER_ACK;
        hexData[m_nOffset++] = (byte) 0; //MsgVersion

        hexData[m_nOffset++] = (byte) nTotalBlock;
        hexData[m_nOffset++] = (byte) nCurrentblock;
    }
}
