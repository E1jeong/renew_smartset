/**
 * <pre>
 * NodeConfSet 메시지
 * </pre>
 */
package com.hitec.presentation.nfc_lib.protocol.send;

import com.hitec.presentation.nfc_lib.protocol.NfcConstant;

public class DisplayConfSet extends NfcTxMessage {

    //Msg Type : 1
    public DisplayConfSet(
            String serialNo,
            int dataFormat,
            int meterNum,
            int meterType0,
            int meterPort0,
            int meterType1,
            int meterPort1,
            int meterType2,
            int meterPort2
    ) {
        m_nOffset = m_nHeaderSize;

        hexData[m_nOffset++] = (byte) NfcConstant.NODE_SEND_DISPLAY_CONF_SET;
        hexData[m_nOffset++] = (byte) 0; //MsgVersion

        m_nOffset += fillSerialNumber(m_nOffset, serialNo, 10);

        //time
        m_nOffset += fillCurrentTime(m_nOffset);

        hexData[m_nOffset++] = (byte) dataFormat;

        //meter
        hexData[m_nOffset++] = (byte) meterNum;
        hexData[m_nOffset++] = (byte) meterType0;
        hexData[m_nOffset++] = (byte) meterPort0;
        hexData[m_nOffset++] = (byte) meterType1;
        hexData[m_nOffset++] = (byte) meterPort1;
        hexData[m_nOffset++] = (byte) meterType2;
        hexData[m_nOffset++] = (byte) meterPort2;
    }
}
