/**
 * <pre>
 * NFC 메시지 생성을 위한 기본 class
 * </pre>
 */
package com.hitec.data.nfc_lib.protocol.send;

import com.hitec.data.nfc_lib.protocol.NfcConstant;
import com.hitec.data.nfc_lib.util.DevUtil;

import java.util.Calendar;

public class NfcTxMessage implements NfcConstant {

    protected static final int m_nHeaderSize = 10; // DeviceCode(1) + Len(1) + DeviceId(8)
    //  송신 Data Frame
    protected byte[] hexData = new byte[MAX_LEN_RF_MESSAGE];
    protected int m_nTxLen = 0;
    protected int m_nOffset = 0;
    private int m_nMsgType = 0;
    private int m_nNwkLen = 0;

    public int GetTxLen() {
        return m_nTxLen + 1;
    } //CheckSum(1)

    public int GetMsgType() {
        return m_nMsgType;
    }

    public int GetNwkLen() {
        return m_nNwkLen;
    }

    public void SetNwkLen(int n) {
        m_nNwkLen = n;
        m_nTxLen = m_nNwkLen + m_nHeaderSize; // DeviceCode(1) + Len(1) + DeviceId(8)
    }

    // Data Frame 요청
    public byte[] GetDataFrame() {
        int i;
        byte checksum = 0;
        makeHeader();

        m_nMsgType = hexData[10] & 0xFF;

        byte[] txData;
        if (m_nTxLen != 0) {
            txData = new byte[m_nTxLen + 1];
            for (i = 0; i < m_nTxLen; i++) {
                txData[i] = hexData[i];
                checksum += hexData[i];
            }
            txData[i] = checksum;
        } else {
            txData = new byte[m_nHeaderSize];
        }
        return txData;
    }

    protected byte string2byte(String str, int n) {
        byte a = 0;
        try {
            a = (byte) Integer.parseInt(str, n);
        } catch (Exception e) {
        }
        return a;
    }

    protected byte byte2bcd(int nSrc) {
        byte byDst = 0;
        byte byUp = (byte) (nSrc / 10);
        byte byDn = (byte) (nSrc % 10);

        byDst = (byte) (byUp * 16 + byDn);

        return byDst;
    }

    private void makeHeader() {
        if (m_nOffset > m_nHeaderSize) {
            SetNwkLen(m_nOffset - m_nHeaderSize);
        } else {
            SetNwkLen(0);
        }

        //MsgLen
        int nMsgLen = m_nOffset - 2; // 2 = DevCode(1) + Len(1)
        hexData[0] = (byte) DEVICE_CODE_SMART;
        hexData[1] = (byte) nMsgLen;

        //Device ID
        DevUtil.convHexStringToHexCode(DEVICE_ID_SMART, hexData, 2, 8);
    }

    /**
     * <pre>
     * 현재 시간 구하기 Time
     * </pre>
     */
    protected int fillCurrentTime(int index) {
        Calendar cal = Calendar.getInstance();

        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH) + 1;
        int date = cal.get(Calendar.DATE);
        int hour = cal.get(Calendar.HOUR_OF_DAY);
        int minute = cal.get(Calendar.MINUTE);
        int second = cal.get(Calendar.SECOND);

        hexData[index + 0] = (byte) (year % 0x100);
        hexData[index + 1] = (byte) (year / 0x100);
        hexData[index + 2] = (byte) month;
        hexData[index + 3] = (byte) date;
        hexData[index + 4] = (byte) hour;
        hexData[index + 5] = (byte) minute;
        hexData[index + 6] = (byte) second;

        return 7;
    }

    /**
     * <pre>
     * 현재 시간 구하기 Time2
     *
     * Bit 0 ~ 5 (6Bit) : Year(2000년 이후)
     * Bit 6 ~ 9 (4Bit) : Month
     * Bit 10 ~ 14 (5Bit) : Date
     * Bit 15 ~ 19 (5Bit) : Hour
     * Bit 20 ~ 25 (6Bit) : Minute
     * Bit 26 ~ 31 (6Bit) : Second
     * </pre>
     */
    protected int fillCurrentCompressTime(int index) {
        Calendar cal = Calendar.getInstance();

        byte year = (byte) (cal.get(Calendar.YEAR) - 2000);
        byte month = (byte) (cal.get(Calendar.MONTH) + 1);
        byte date = (byte) cal.get(Calendar.DATE);
        byte hour = (byte) cal.get(Calendar.HOUR_OF_DAY);
        byte minute = (byte) cal.get(Calendar.MINUTE);
        byte second = (byte) cal.get(Calendar.SECOND);

        hexData[index + 0] = (byte) (((month & 0x03) << 6) | (year & 0x3F));
        hexData[index + 1] =
                (byte) (
                        ((hour & 0x01) << 7) | ((date & 0x1F) << 2) | ((month & 0x0C) >> 2)
                );
        hexData[index + 2] = (byte) (((minute & 0x3F) << 4) | ((hour & 0x1F) >> 1));
        hexData[index + 3] =
                (byte) (((second & 0x3F) << 2) | ((minute & 0x3F) >> 4));

        return 4;
    }

    /**
     * <pre>
     * 현재 시간 update
     * </pre>
     */
    protected int updateCurrentTime(int index, byte[] txdata) {
        Calendar cal = Calendar.getInstance();

        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH) + 1;
        int date = cal.get(Calendar.DATE);
        int hour = cal.get(Calendar.HOUR_OF_DAY);
        int minute = cal.get(Calendar.MINUTE);
        int second = cal.get(Calendar.SECOND);

        txdata[index + 0] = (byte) (year % 0x100);
        txdata[index + 1] = (byte) (year / 0x100);
        txdata[index + 2] = (byte) month;
        txdata[index + 3] = (byte) date;
        txdata[index + 4] = (byte) hour;
        txdata[index + 5] = (byte) minute;
        txdata[index + 6] = (byte) second;

        return 7;
    }

    protected int fillZeroData(int index, int nLen) {
        for (int i = 0; i < nLen; i++) {
            hexData[index + i] = (byte) 0x00;
        }

        return nLen;
    }

    protected int fillSerialNumber(int index, String serial, int nLen) {
        for (int i = 0; i < serial.length(); i++) {
            hexData[index + i] = (byte) serial.charAt(i);
        }

        return nLen;
    }

    protected int fillAccountNo(int index, String accountNo, int nLen) {
        int nSize = accountNo.length();
        for (int i = 0; i < nSize; i++) {
            hexData[index + i] = (byte) accountNo.charAt(i);
        }

        for (int i = nSize; i < nLen; i++) {
            hexData[index + i] = 0x00;
        }

        return nLen;
    }

    protected int addIpAddress(int index, String addr) {
        int len = addr.length();
        int startPos = 0;
        int n = 0;

        for (int i = 0; i < len; i++) {
            if (addr.charAt(i) == '.') {
                hexData[index + 3 - n] = string2byte(addr.substring(startPos, i), 10);
                n++;
                startPos = i + 1;
            }
        }

        hexData[index] = string2byte(addr.substring(startPos, len), 10);

        return 4;
    }

    protected int addNwkAddress(int index, String addr) {
        int len = addr.length();
        int startPos = 0;
        int n = 0;

        for (int i = 0; i < len; i++) {
            if (addr.charAt(i) == '.') {
                hexData[index + 3 - n] = string2byte(addr.substring(startPos, i), 10);
                n++;
                startPos = i + 1;
            }
        }

        hexData[index] = string2byte(addr.substring(startPos, len), 10);

        return 4;
    }

    protected int addPanAddress(int index, String addr) {
        if (addr.length() != 4) {
            addr = "0000";
        }
        hexData[index + 0] = string2byte(addr.substring(2, 4), 16);
        hexData[index + 1] = string2byte(addr.substring(0, 2), 16);

        return 2;
    }

    protected int addServiceCode(int index, String serviceCode) {
        for (int i = 0; i < serviceCode.length(); i++) {
            hexData[index + i] = (byte) serviceCode.charAt(i);
        }

        return 4;
    }

    protected int addFwVersion(int index, String fwVersion) {
        for (int i = 0; i < fwVersion.length(); i++) {
            hexData[index + i] = (byte) fwVersion.charAt(i);
        }

        return 4;
    }

    protected int fillAsciiData(int index, String ascii, int nLen) {
        for (int i = 0; i < ascii.length(); i++) {
            hexData[index + i] = (byte) ascii.charAt(i);
        }

        return nLen;
    }

    protected int fillHexData(int index, String strSrc, int nLen) {
        for (int i = 0; i < nLen; i++) {
            hexData[index + i] = string2byte(strSrc.substring(i * 2, i * 2 + 2), 16);
        }

        return nLen;
    }

    protected int fillDomain(int index, String domain, int nLen) {
        for (int i = 0; i < domain.length(); i++) {
            hexData[index + i] = (byte) domain.charAt(i);
        }

        return nLen;
    }
}
