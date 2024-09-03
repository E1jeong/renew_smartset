/**
 * <pre>
 * PeriodMeterReport 메시지 Parser
 * </pre>
 */
package com.hitec.data.nfc_lib.protocol.recv;

import android.util.Log;

import com.hitec.data.nfc_lib.util.Meter;

public class FlashDataReport extends NfcRxMessage {

    private static final int MAX_NUM_OF_DATA = 12;
    private int m_nResultState = 0;
    // Total Block
    private int m_nTotalBlock = 0;
    // Current Block
    private int m_nCurrentBlock = 0;
    private int m_nMeterValuePoint = 3; //자릿수
    private int m_nMeterValid = METER_VALID_OK; //계량기 데이타 유효성 정보
    private int m_nDataRefPos = 0;
    private int m_nDataHour = 0;
    private int m_nNumOfData = 0;
    private String[] m_strFirstMeteredTime = new String[MAX_NUM_OF_DATA];
    //내부적으로 사용
    private int m_nMeterConnection = METER_VALID_OK;
    private String m_strFirstMeterValue = "";
    private String[] m_stFlashMeterValue = new String[MAX_NUM_OF_DATA];
    private String m_strNWK = "";

    public int GetResultState() {
        return m_nResultState;
    }

    public int GetTotalBlock() {
        return m_nTotalBlock;
    }

    public int GetCurrentBlock() {
        return m_nCurrentBlock;
    }

    public int GetMeterValuePoint() {
        return m_nMeterValuePoint;
    }

    public boolean GetMeterValid() {
        if (m_nMeterValid == METER_VALID_OK) {
            return true;
        } else {
            return false;
        }
    }

    public int GetNumOfData() {
        return m_nNumOfData;
    }

    public String GetFirstMeteredTime(int n) {
        if (n >= MAX_NUM_OF_DATA) return "";
        return m_strFirstMeteredTime[n];
    }

    // Term battery level
    public String GetTermBattery() {
        return "";
    }

    // Meter battery level
    public String GetMeterBattery() {
        return "";
    }

    public String GetFirstMeterValue(int nData) {
        if (nData >= MAX_NUM_OF_DATA) return "";
        return m_stFlashMeterValue[nData];
    }

    private void initArrayMember() {
        for (int i = 0; i < MAX_NUM_OF_DATA; i++) {
            m_strFirstMeteredTime[i] = "";

            m_stFlashMeterValue[i] = "";
        }
    }

    public String GetNWK() {
        return m_strNWK;
    }

    @Override
    public boolean parse(byte[] rxdata) {
        if (super.parse(rxdata) == false) {
            return false;
        }

        initArrayMember();
        m_nMeterConnection = METER_VALID_OK;

        m_nResultState = getHexData(m_nOffset++);

        m_nTotalBlock = getHexData(m_nOffset++);
        m_nCurrentBlock = getHexData(m_nOffset++);
        Log.e(
                "meter",
                "FlashDataReport ==> " +
                        " m_nTotalBlock:" +
                        m_nTotalBlock +
                        " m_nCurrentBlock:" +
                        m_nCurrentBlock
        );

        if (m_nTotalBlock > 0 && m_nCurrentBlock > 0) {
            ParserMeterBlock();

            m_nNumOfData = 0;
            m_strFirstMeteredTime[0] = parseMeasureTime(m_nOffset);
            m_nOffset += 5;

            if (m_nDataRefPos < 12) {
                ParserMeterValue(hexData, m_nOffset, m_nMeterValuePoint);
                m_nOffset += 4;
            } else {
                m_nMeterConnection = METER_VALID_ERROR;
            }

            int nIndex = 0;
            if (m_nMeterConnection == METER_VALID_OK) {
                m_nNumOfData++;
                m_nMeterValid = METER_VALID_OK;
                m_stFlashMeterValue[0] = m_strFirstMeterValue;
                //log.i("meter", "FlashDataReport ==> i: 0" + " nIndex:" + nIndex + " m_stFlashMeterValue:"  + m_stFlashMeterValue[nIndex]);

                for (int i = 0; i < MAX_NUM_OF_DATA; i++) {
                    int nMeterAddVal = parseMeterHexAddVal(m_nOffset);
                    m_nOffset += 2;

                    if (i > m_nDataRefPos && nMeterAddVal < 0xffff) {
                        m_nNumOfData++;
                        nIndex++;
                        m_strFirstMeteredTime[nIndex] =
                                getMeteredHour(m_strFirstMeteredTime[0], m_nDataHour + i);
                        m_stFlashMeterValue[nIndex] =
                                Meter.ParserMeterAddValInt(
                                        m_stFlashMeterValue[nIndex - 1],
                                        nMeterAddVal,
                                        m_nMeterValuePoint
                                );
                        //log.i("meter", "FlashDataReport ==> i: " + i + " nIndex:" + nIndex + " m_stFlashMeterValue:"  + m_stFlashMeterValue[nIndex]);
                    }
                }
            } else {
                m_nMeterValid = METER_VALID_ERROR;
            }
        }

        return parseCompleted();
    }

    private String getMeteredHour(String startTime, int nHour) {
        //2021-09-01 10:00:00
        String strDate = startTime.substring(0, 10);

        return String.format("%s %02d:00:00", strDate, nHour);
    }

    private String parseMeasureTime(int index) {
        int year = getHexData(index) + 2000;
        int mon = getHexData(index + 1);
        int day = getHexData(index + 2);
        int hour = getHexData(index + 3);
        int refPos = getHexData(index + 4);

        m_nDataHour = hour;
        m_nDataRefPos = refPos;

        hour = hour + refPos;
        return String.format("%04d-%02d-%02d %02d:00:00", year, mon, day, hour);
    }

    //pasre data
    private void ParserMeterBlock() {
        int nVifDif;

        nVifDif = getHexData(m_nOffset++);
        m_nMeterValuePoint = GetStandardMeterValuePoint(nVifDif);
    }

    private int GetStandardMeterValuePoint(int nVifDif) {
        int nValuePoint = 3;

        if (nVifDif != 0x00) {
            nValuePoint = nVifDif & 0x0F;
            if (nValuePoint > 7) nValuePoint = 7;
        }

        return nValuePoint;
    }

    //계량기 MT_DOWN 확인
    private boolean checkMeterValid(byte[] pBuff, int nOffSet, int nLen) {
        boolean fResult = false;
        int temp;

        for (int i = 0; i < nLen; i++) {
            temp = getHexData(nOffSet++);
            if (temp != 0xff) {
                fResult = true;
                break;
            }
        }

        return fResult;
    }

    private void ParserMeterValue(
            byte[] pBuff,
            int nOffSet,
            int nMeterValuePoint
    ) {
        boolean fFirstValid = checkMeterValid(pBuff, nOffSet, 4);

        if (fFirstValid == false) {
            m_nMeterConnection = METER_VALID_ERROR;
            m_strFirstMeterValue = "";
            return;
        }

        parserMeterStandardDigital(pBuff, nOffSet, nMeterValuePoint);
    }

    //표준 프로토콜
    private int parserMeterStandardDigital(
            byte[] pBuff,
            int nOffSet,
            int nMeterValuePoint
    ) {
        //Meter Data(4Byte)
        if (Meter.CheckDecValid(pBuff, nOffSet, 4) == false) {
            m_nMeterConnection = METER_VALID_VALUE_ERROR;
            m_strFirstMeterValue = "";

            return nOffSet;
        } else {
            m_strFirstMeterValue =
                    Meter.ParserStandardMeterVal(pBuff, nOffSet, 4, nMeterValuePoint);
        }
        nOffSet += 4;

        return nOffSet;
    }
}
