/**
 * <pre>
 * PeriodMeterReq 메시지
 * </pre>
 */
package com.hitec.data.nfc_lib.protocol.send;

import com.hitec.data.nfc_lib.protocol.NfcConstant;

public class FlashDataReq extends NfcTxMessage {

    public FlashDataReq(String dateFrom, String dateTo) {
        m_nOffset = m_nHeaderSize;

        hexData[m_nOffset++] = (byte) NfcConstant.NODE_SEND_FLASH_DATA_REQ;
        hexData[m_nOffset++] = (byte) 0; //MsgVersion

        m_nOffset += fillDateFromString(m_nOffset, dateFrom);
        m_nOffset += fillDateFromString(m_nOffset, dateTo);
    }

    private int fillDateFromString(int offset, String dateStr) {
        // example: 2021-04-14
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

        hexData[offset + 0] = (byte) (year - 2000);
        hexData[offset + 1] = (byte) mon;
        hexData[offset + 2] = (byte) date;

        return 3;
    }
}
