/**
 * <pre>
 * PeriodMeterReq 메시지
 * </pre>
 */
package com.hitec.presentation.nfc_lib.protocol.send;

import com.hitec.presentation.nfc_lib.protocol.NfcConstant;

public class PeriodMeterReq extends NfcTxMessage {

    public PeriodMeterReq(int meterPort, String dateFrom, String dateTo) {
        m_nOffset = m_nHeaderSize;

        hexData[m_nOffset++] = (byte) NfcConstant.NODE_SEND_PERIOD_METER_REQ;
        hexData[m_nOffset++] = (byte) 0; //MsgVersion

        hexData[m_nOffset++] = (byte) meterPort;
        m_nOffset += fillCurrentTime(m_nOffset);

        m_nOffset += fillDateFromString(m_nOffset, dateFrom);
        m_nOffset += fillDateFromString(m_nOffset, dateTo);
    }

    private int fillDateFromString(int offset, String dateStr) {
        // example: 2015-04-14
        //          0123456789
        int year = 2000;
        int mon = 1;
        int date = 1;
        try {
            year = Integer.parseInt(dateStr.substring(0, 4));
            mon = Integer.parseInt(dateStr.substring(5, 7));
            date = Integer.parseInt(dateStr.substring(8));
        } catch (Exception e) {
        }

        hexData[offset + 0] = (byte) (year % 0x100);
        hexData[offset + 1] = (byte) (year / 0x100);
        hexData[offset + 2] = (byte) mon;
        hexData[offset + 3] = (byte) date;

        return 4;
    }
}
