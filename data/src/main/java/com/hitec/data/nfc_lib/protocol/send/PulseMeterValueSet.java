/**
 * <pre>
 * NbConfSet 메시지
 * </pre>
 */
package com.hitec.data.nfc_lib.protocol.send;

import com.hitec.data.nfc_lib.protocol.NfcConstant;

public class PulseMeterValueSet extends NfcTxMessage {

    public PulseMeterValueSet(
            String serialNo,
            int meterType,
            int meterPort,
            String pulseValue
    ) {
        m_nOffset = m_nHeaderSize;

        hexData[m_nOffset++] = (byte) NfcConstant.NODE_SEND_PULSE_METER_VALUE_SET;
        hexData[m_nOffset++] = (byte) 0; //MsgVersion

        m_nOffset += fillSerialNumber(m_nOffset, serialNo, 12);

        hexData[m_nOffset++] = (byte) meterType;
        hexData[m_nOffset++] = (byte) meterPort;
        m_nOffset += fillMeterBCDVal(m_nOffset, pulseValue);

        m_nOffset += fillCurrentTime(m_nOffset);
    }

    private int fillMeterBCDVal(int index, String strMeterVal) {
        int i;
        int nCnt;
        int METER_VAL_LEN = 4;
        byte[] pDstMeterVal = new byte[METER_VAL_LEN * 3];

        for (i = 0; i < METER_VAL_LEN; i++) {
            pDstMeterVal[i] = 0x00;
            hexData[index + i] = 0x00;
        }

        if (strMeterVal.length() > 0) {
            char[] szSrcArray = strMeterVal.toCharArray();
            byte upData, dnData;

            nCnt = METER_VAL_LEN * 2 - 1;
            for (i = strMeterVal.length() - 1; i >= 0; i--) {
                if ((szSrcArray[i] != ('.')) && nCnt >= 0) {
                    pDstMeterVal[nCnt] = (byte) szSrcArray[i];
                    nCnt--;
                }
            }

            for (i = 0; i < METER_VAL_LEN; i++) {
                upData = (byte) ((pDstMeterVal[i * 2 + 0] << 4) & 0xF0);
                dnData = (byte) ((pDstMeterVal[i * 2 + 1]) & 0x0F);

                hexData[index + i] = (byte) (upData | dnData);
            }
        }

        return 4;
    }
}
