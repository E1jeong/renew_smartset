/**
 * <pre>
 * FlashDateListReport 메시지 Parser
 * </pre>
 */
package com.hitec.presentation.nfc_lib.protocol.recv;

import com.hitec.presentation.nfc_lib.util.bLog;

public class FlashDateListReport extends NfcRxMessage {

    private int m_nResultState = 0;
    private int m_nTotalBlock = 0;
    private int m_nFirstYear = 0;
    private int m_nFirstMonth = 0;
    private int m_nFirstDay = 0;
    private int m_nEndYear = 0;
    private int m_nEndMonth = 0;
    private int m_nEndDay = 0;

    public int GetResultState() {
        return m_nResultState;
    }

    public int GetTotalBlock() {
        return m_nTotalBlock;
    }

    public String GetStartDate() {
        return String.format(
                "%04d-%02d-%02d",
                m_nFirstYear,
                m_nFirstMonth,
                m_nFirstDay
        );
    }

    public String GetEndDate() {
        return String.format("%04d-%02d-%02d", m_nEndYear, m_nEndMonth, m_nEndDay);
    }

    @Override
    public boolean parse(byte[] rxdata) {
        if (super.parse(rxdata) == false) {
            return false;
        }

        m_nResultState = getHexData(m_nOffset++);

        m_nTotalBlock = 0;
        bLog.i(TAG, " FlashDateListReport ==>01 m_nOffset:" + m_nOffset);

        int nMonthSize = getHexData(m_nOffset++);
        bLog.i(TAG, " FlashDateListReport ==>01 nMonthSize:" + nMonthSize);
        for (int i = 0; i < nMonthSize; i++) {
            int nYear = getHexData(m_nOffset++) + 2000;
            int nMonth = getHexData(m_nOffset++);
            bLog.i(TAG, " FlashDateListReport ==>01 nYear:" + nYear);
            bLog.i(TAG, " FlashDateListReport ==>01 nMonth:" + nMonth);

            m_nTotalBlock += getDayFlagCount(nYear, nMonth, 1, m_nOffset); //1 ~ 8
            m_nOffset++;
            bLog.i(TAG, " FlashDateListReport ==>02 m_nTotalBlock:" + m_nTotalBlock);

            m_nTotalBlock += getDayFlagCount(nYear, nMonth, 9, m_nOffset); //9 ~ 16
            m_nOffset++;
            bLog.i(TAG, " FlashDateListReport ==>03 m_nTotalBlock:" + m_nTotalBlock);

            m_nTotalBlock += getDayFlagCount(nYear, nMonth, 17, m_nOffset); //17 ~ 24
            m_nOffset++;
            bLog.i(TAG, " FlashDateListReport ==>04 m_nTotalBlock:" + m_nTotalBlock);

            m_nTotalBlock += getDayFlagCount(nYear, nMonth, 25, m_nOffset); //25 ~ 31
            m_nOffset++;
            bLog.i(TAG, " FlashDateListReport ==>05 m_nTotalBlock:" + m_nTotalBlock);
        }

        return parseCompleted();
    }

    private int getDayFlagCount(int nYear, int nMonth, int nStartDay, int index) {
        int nCount = 0;
        int nFlag = getHexData(index);
        int[] anComp = {0x80, 0x40, 0x20, 0x10, 0x08, 0x04, 0x02, 0x01};

        for (int i = 0; i < 8; i++) {
            if ((nFlag & anComp[i]) == anComp[i]) {
                nCount++;

                if (m_nFirstDay == 0) {
                    m_nFirstYear = nYear;
                    m_nFirstMonth = nMonth;
                    m_nFirstDay = nStartDay + i;
                }

                m_nEndYear = nYear;
                m_nEndMonth = nMonth;
                m_nEndDay = nStartDay + i;
            }
        }

        return nCount;
    }
}
