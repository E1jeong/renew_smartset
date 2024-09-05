/**
 * <pre>
 * NFC 프로토콜 클래스
 * </pre>
 */
package com.hitec.presentation.nfc_lib.util;

import java.math.BigInteger;
import java.util.Calendar;

public class DevUtil {

    private static String tag = "util";

    /**
     * <pre>
     * hex to binary
     * </pre>
     */
    public static String hexToBin(String s) {
        String binaryStr = "";
        try {
            binaryStr = new BigInteger(s, 16).toString(2);
        } catch (Exception e) {
        }

        return getLPad(binaryStr, 8, "0");
    }

    /**
     * <pre>
     * 일/월 두자리수 채우기..
     * </pre>
     *
     * @param c int
     */
    public static String pad(int c) {
        if (c >= 10) return String.valueOf(c);
        else return "0" + String.valueOf(c);
    }

    /**
     * <pre>
     * 우측에 자릿수 채우기
     * </pre>
     */
    public static String getRPad(String str, int size, String strFillText) {
        try {
            for (int i = (str.getBytes()).length; i < size; i++) {
                str += strFillText;
            }
        } catch (Exception e) {
        }
        return str;
    }

    /**
     * <pre>
     * 좌측에 자릿수 채우기
     * </pre>
     */
    public static String getLPad(String str, int size, String strFillText) {
        try {
            for (int i = (str.getBytes()).length; i < size; i++) {
                str = strFillText + str;
            }
        } catch (Exception e) {
        }
        return str;
    }

    /**
     * <pre>
     * int to hex, 좌측에 자릿수 채우기
     * </pre>
     */
    public static String convertIntToHexLPad(
            int num,
            int nSize,
            String strFillText
    ) {
        String strConv = Integer.toHexString((char) num).toUpperCase();

        for (int i = (strConv.getBytes()).length; i < nSize; i++) {
            strConv = strFillText + strConv;
        }
        return strConv;
    }

    /**
     * <pre>
     * int to hex
     * </pre>
     */
    public static String convertIntegerToStrHex(int i) {
        return Integer.toHexString(i).toUpperCase();
    }

    /**
     * <pre>
     * String int to int
     * </pre>
     */
    public static int convertStringIntegerToInteger(String strNum) {
        int nNum = 0;

        try {
            nNum = Integer.parseInt(strNum, 10);
        } catch (Exception e) {
        }

        return nNum;
    }

    /**
     * <pre>
     * String long to long
     * </pre>
     */
    public static long convertStringLongToLong(String strNum) {
        long nNum = 0;

        try {
            nNum = Long.parseLong(strNum, 10);
        } catch (Exception e) {
        }

        return nNum;
    }

    /**
     * <pre>
     * String hex to long
     * </pre>
     */
    public static long convertStringHexToLong(String strNum) {
        long nNum = 0;

        try {
            nNum = Long.parseLong(strNum, 16);
        } catch (Exception e) {
        }

        return nNum;
    }

    /**
     * <pre>
     * hex to int
     * </pre>
     */
    public static int convertStringHexToInteger(String hex) {
        int nNum = 0;

        try {
            nNum = Integer.parseInt(hex, 16);
        } catch (Exception e) {
        }

        return nNum;
    }

    /**
     * <pre>
     * int to hex
     * </pre>
     */
    public static String convertStrIntegerToStrHex(String strNum) {
        String strRet = "";

        try {
            int nNum = Integer.parseInt(strNum, 10);
            String strHex = Integer.toHexString((char) nNum).toUpperCase();
            strRet = String.valueOf(strHex);
        } catch (Exception e) {
        }

        return strRet;
    }

    /**
     * <pre>
     * int to int
     * </pre>
     */
    public static String convertStrIntegerToStrInteger(String strNum) {
        String strRet = "";

        try {
            //int nNum = Integer.parseInt(strNum, 10);
            long nNum = Long.parseLong(strNum, 10);
            strRet = String.format("%d", nNum);
        } catch (Exception e) {
        }

        return strRet;
    }

    public static String convertStrLongToStrLong(String strNum) {
        String strRet = "";

        try {
            long nNum = Long.parseLong(strNum, 10);
            strRet = String.format("%d", nNum);
        } catch (Exception e) {
        }

        return strRet;
    }

    /**
     * <pre>
     * hex to int
     * </pre>
     */
    public static String convertStrHexToStrInteger(String strHex) {
        String strRet = "";
        try {
            int nNum = Integer.parseInt(strHex, 16);
            strRet = String.valueOf(nNum);
        } catch (Exception e) {
        }
        return strRet;
    }

    /**
     * <pre>
     * hex to String
     * </pre>
     */
    public static String convHexToStringCode(
            byte[] pBuff,
            int nOffSet,
            int nLen
    ) {
        String strRet = "";
        for (int nIdx = 0; nIdx < nLen; nIdx++) {
            strRet += String.format("%02X", pBuff[nOffSet + nIdx] & 0xff);
        }

        return strRet;
    }

    /**
     * <pre>
     * String hex to Hex code
     * </pre>
     */
    public static void convHexStringToHexCode(
            String strData,
            byte[] pBuff,
            int nOffSet,
            int nLen
    ) {
        int nIdx;
        int nStrHalfLen;

        if (strData != null) {
            nStrHalfLen = strData.length() / 2;

            if (nStrHalfLen > nLen) nStrHalfLen = nLen;

            for (nIdx = 0; nIdx < nStrHalfLen; nIdx++) {
                String strTmp = strData.substring(nIdx * 2, nIdx * 2 + 2);
                pBuff[nOffSet + nIdx] = (byte) convertStringHexToInteger(strTmp);
            }

            if (nStrHalfLen < nLen) {
                for (nIdx = nStrHalfLen; nIdx < nLen; nIdx++) {
                    pBuff[nOffSet + nIdx] = 0x00;
                }
            }
        } else {
            for (nIdx = 0; nIdx < nLen; nIdx++) {
                pBuff[nOffSet + nIdx] = 0x00;
            }
        }
    }

    /**
     * <pre>
     * 현재 시간 구하기 Time
     * </pre>
     */
    public static String getCurrentTime() {
        Calendar cal = Calendar.getInstance();

        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH) + 1;
        int date = cal.get(Calendar.DATE);

        int hour = cal.get(Calendar.HOUR_OF_DAY);
        int minute = cal.get(Calendar.MINUTE);
        int second = cal.get(Calendar.SECOND);

        return (
                String.valueOf(year).substring(2, 4) +
                        pad(month) +
                        pad(date) +
                        pad(hour) +
                        pad(minute) +
                        pad(second)
        );
    }

    /**
     * <pre>
     * 현재 시간 구하기 Week + Time
     * </pre>
     */
    public static String getCurrentWeekTime() {
        Calendar cal = Calendar.getInstance();

        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH) + 1;
        int date = cal.get(Calendar.DATE);

        int week = cal.get(Calendar.DAY_OF_WEEK);

        int hour = cal.get(Calendar.HOUR_OF_DAY);
        int minute = cal.get(Calendar.MINUTE);
        int second = cal.get(Calendar.SECOND);

        return (
                String.valueOf(year).substring(2, 4) +
                        pad(month) +
                        pad(date) +
                        pad(week - 1) +
                        pad(hour) +
                        pad(minute) +
                        pad(second)
        );
    }

    /**
     * <pre>
     * 현재 시간 구하기 Time + Week
     * </pre>
     */
    public static String getCurrentTimeWeek() {
        Calendar cal = Calendar.getInstance();

        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH) + 1;
        int date = cal.get(Calendar.DATE);

        int hour = cal.get(Calendar.HOUR_OF_DAY);
        int minute = cal.get(Calendar.MINUTE);
        int second = cal.get(Calendar.SECOND);

        int week = cal.get(Calendar.DAY_OF_WEEK);

        return (
                String.valueOf(year).substring(2, 4) +
                        pad(month) +
                        pad(date) +
                        pad(hour) +
                        pad(minute) +
                        pad(second) +
                        pad(week - 1)
        );
    }

    /**
     * <pre>
     * 일자, 시간 데이터 변환
     * </pre>
     */
    public static String convDateToFormatStr(
            String readMessage,
            int nOffset,
            int nLen
    ) {
        String strDateTime = "";

        String year = readMessage.substring(nOffset, nOffset + 4);
        year = convByteToStrInt16(year);
        nOffset += 4;

        String month = convDateTimeLPad(
                readMessage.substring(nOffset, nOffset + 2),
                2
        );
        nOffset += 2;

        String date = convDateTimeLPad(
                readMessage.substring(nOffset, nOffset + 2),
                2
        );
        nOffset += 2;

        String hour = convDateTimeLPad(
                readMessage.substring(nOffset, nOffset + 2),
                2
        );
        nOffset += 2;

        String minute = convDateTimeLPad(
                readMessage.substring(nOffset, nOffset + 2),
                2
        );
        nOffset += 2;

        strDateTime = year + "-" + month + "-" + date + " " + hour + ":" + minute;
        return strDateTime;
    }

    /**
     * <pre>
     * 16bit integer 데이터 변환
     * </pre>
     */
    public static String convByteToStrInt16(String strSrc) {
        String strConv = "";
        try {
            String low = strSrc.substring(0, 2);
            String high = strSrc.substring(2, 4);
            String strHex = high + low;
            int nSrc = Integer.parseInt(strHex, 16);
            strConv = String.valueOf(nSrc);
        } catch (Exception e) {
        }

        return strConv;
    }

    /**
     * <pre>
     * 일자, 시간 데이터 변환
     * </pre>
     */
    public static String convDateTimeLPad(String strSrc, int nLen) {
        String strConv = "";

        try {
            int nSrc = Integer.parseInt(strSrc, 16);
            strConv = getLPad(String.valueOf(nSrc), nLen, "0");
        } catch (Exception e) {
        }

        return strConv;
    }

    //시간 증가
    public static String SetDateHour(String strSrcDate, int nSetHour) {
        //2011-01-01 01:00  형식
        String strDateTime = strSrcDate;

        String strYear = strSrcDate.substring(0, 4);
        String strMonth = strSrcDate.substring(5, 7);
        String strDay = strSrcDate.substring(8, 10);
        //String strHour = strSrcDate.substring(11, 13);
        //String strMinute = strSrcDate.substring(14, 16);

        try {
            int nYear = convertStringIntegerToInteger(strYear);
            int nMon = convertStringIntegerToInteger(strMonth);
            int nDay = convertStringIntegerToInteger(strDay);
            int nHour = nSetHour;
            int nMin = 0;
            //int nSec = 0;

            //Calendar cal = Calendar.getInstance();

            //cal.set(nYear, nMon, nDay, nHour, nMin, nSec);
            //cal.add(Calendar.HOUR_OF_DAY, nSetHour);

            return String.format(
                    "%04d-%02d-%02d %02d:%02d",
                    nYear,
                    nMon,
                    nDay,
                    nHour,
                    nMin
            );
        } catch (Exception e) {
            return strDateTime;
        }
    }

    /**
     * <pre>
     * 문자나 숫자가 아니면 false
     * </pre>
     */
    public static boolean checkStr(String str) {
        String hexStr = "0123456789ABCDEF";
        char[] strChar = str.toCharArray();

        for (int i = 0; i < strChar.length; i++) {
            // log.e("log", "checkStr: " + strChar[i]);
            if (hexStr.indexOf(strChar[i]) == -1) return false;
        }

        return true;
    }

    /**
     * <pre>
     * ASCII String to hex..
     * </pre>
     */
    public static String convertASCIIStringsToHex(String ascii) {
        StringBuffer sb = new StringBuffer();
        char[] temp = ascii.toCharArray();
        for (int i = 0; i < ascii.length(); i++) {
            sb.append(Integer.toHexString((int) temp[i]));
        }
        return sb.toString();
    }

    /**
     * <pre>
     * hex to String..
     * </pre>
     */
    public static String convertHexStringsToString(String hex) {
        StringBuilder output = new StringBuilder();

        for (int i = 0; i < hex.length(); i += 2) {
            String str = hex.substring(i, i + 2);
            try {
                output.append((char) Integer.parseInt(str, 16));
            } catch (Exception e) {
            }
        }

        return output.toString();
    }

    /**
     * <pre>
     * bytes to String
     * </pre>
     */
    public static String convertHexBytesToString(byte[] bytes) {
        StringBuffer sb = new StringBuffer(bytes.length * 2);
        for (int i = 0; i < bytes.length; i++) {
            if (((int) bytes[i] & 0xff) < 0x10) {
                sb.append('0');
            }

            sb.append(Integer.toString(bytes[i] & 0xff, 16));
        }

        return sb.toString();
    }

    /**
     * <pre>
     * String to bytes...
     * </pre>
     */
    public static byte[] hexToBytes(String hex) {
        byte[] result = null;
        if (hex != null) {
            result = new byte[hex.length() / 2];
            for (int i = 0; i < result.length; i++) {
                try {
                    result[i] =
                            (byte) Integer.parseInt(hex.substring(2 * i, 2 * i + 2), 16);
                } catch (Exception e) {
                }
            }
        }

        return result;
    }

    /**
     * <pre>
     * String to hex
     * </pre>
     */
    public static String convertStringToHex(String str) {
        char[] chars = str.toCharArray();

        StringBuffer hex = new StringBuffer();
        for (int i = 0; i < chars.length; i++) {
            hex.append(Integer.toHexString((int) chars[i]));
        }

        return hex.toString();
    }

    /**
     * <pre>
     * bytes to String
     * </pre>
     */
    public static String debugAsciiToString(byte[] bytes, int nLen) {
        StringBuffer sb = new StringBuffer(nLen * 3);
        char cTemp;
        for (int i = 0; i < nLen; i++) {
            cTemp = (char) (bytes[i]);
            sb.append(cTemp);
        }

        return sb.toString();
    }

    /**
     * <pre>
     * 검침값 소수형으로 변환
     * </pre>
     */
    public static String getConvertMeterValue(String meterValue) {
        try {
            String point = meterValue.substring(0, 1);
            String value = meterValue.substring(1);

            int pointInt = convertStringHexToInteger(point);
            int valueInt = convertStringHexToInteger(value);

            String valueStr = String.valueOf(valueInt / Math.pow(10, pointInt));

            String convertValue = "";
            if (pointInt < 3) {
                String[] valueArray = valueStr.split("\\.");

                if (valueArray.length > 1) {
                    convertValue = valueArray[0] + ".";
                    convertValue += getRPad(valueArray[1], 3, "0");
                } else {
                    convertValue += valueStr + ".000";
                }
            } else {
                convertValue = valueStr;
            }

            return convertValue;
        } catch (Exception e) {
            e.printStackTrace();

            bLog.e(tag, "Exception ==> getConvertMeterValue");

            return "";
        }
    }

    /**
     * <pre>
     * 검침값  정수형으로 변환
     * </pre>
     */
    public static String getMeterDoubleToInteger(String meterValue) {
        try {
            String convertInt = "0";

            if (meterValue.length() <= 0) return convertInt;

            String[] valueArray = meterValue.split("\\.");
            convertInt = valueArray[0];

            return convertInt;
        } catch (Exception e) {
            e.printStackTrace();

            bLog.e(tag, "Exception ==> getMeterDoubleToInteger");
            return "";
        }
    }

    public static int multi(int a, int b) {
        bLog.v("TEST", "nulti in DevComm");
        return (a * b);
    }

    /**
     * <pre>
     * Byte to BCD형태에서 변환
     * </pre>
     */
    public static String ConvByteToBCDString(byte pBuff) {
        String strBcd = String.format("%X", pBuff);

        return strBcd;
    }

    public static byte ConvByteToBCD(byte bySrc) {
        byte byDst = 0;
        byte byUp = (byte) (bySrc / 10);
        byte byDn = (byte) (bySrc % 10);

        byDst = (byte) (byUp * 16 + byDn);

        return byDst;
    }

    public static byte ConvBCDToByte(byte bySrc) {
        byte byDst = 0;

        byDst = (byte) (((bySrc >> 4) & 0x0f) * 10 + (bySrc & 0x0f));

        return byDst;
    }

    public static int Conv2ByteToInt(byte[] pBuff, int nOffSet) {
        int nNum = 0;
        int nHi, nLow;
        String strHi, strLow;
        strHi = String.format("%d", pBuff[nOffSet + 1] & 0xff);
        strLow = String.format("%d", pBuff[nOffSet + 0] & 0xff);

        nHi = convertStringIntegerToInteger(strHi);
        nLow = convertStringIntegerToInteger(strLow);
        nNum = nHi * 0x100 + nLow;

        return nNum;
    }

    public static byte ConvBCDStringToByte(String strSrc) {
        byte byDst = 0;

        try {
            byDst = Byte.parseByte(strSrc, 16);
        } catch (Exception e) {
        }

        return byDst;
    }

    public static String ConvBCDToString(byte bySrc) {
        byte byDst = 0;

        byDst = (byte) (((bySrc >> 4) & 0x0f) * 10 + (bySrc & 0x0f));

        String strBcd = String.format("%d", byDst);

        return strBcd;
    }

    public static void convStringToBCD(
            String strData,
            byte[] pBuff,
            int nOffSet,
            int nLen
    ) {
        int nIdx;
        byte[] pStrBuff = new byte[nLen];

        if (strData != null) {
            convStringToByteArry(strData, pStrBuff, 0, nLen);

            for (nIdx = 0; nIdx < nLen; nIdx++) {
                pBuff[nOffSet + nIdx] = pStrBuff[nIdx];
            }
        } else {
            for (nIdx = 0; nIdx < nLen; nIdx++) {
                pBuff[nOffSet + nIdx] = 0x00;
            }
        }
    }

    public static String convertIntegerToStrInteger(int i) {
        String strNum = "";
        try {
            strNum = String.valueOf(Integer.toString(i));
        } catch (Exception e) {
        }
        return strNum;
    }

    public static void convStringToByteArry(
            String strData,
            byte[] pBuff,
            int nOffSet,
            int nLen
    ) {
        int nIdx;
        int nStrHalfLen;

        if (strData != null) {
            nStrHalfLen = strData.length() / 2;

            for (nIdx = 0; nIdx < nStrHalfLen; nIdx++) {
                String strTmp = strData.substring(nIdx * 2, nIdx * 2 + 2);
                pBuff[nOffSet + nIdx] = ConvBCDStringToByte(strTmp);
            }

            if (nStrHalfLen < nLen) {
                for (nIdx = nStrHalfLen; nIdx < nLen; nIdx++) {
                    pBuff[nOffSet + nIdx] = 0x00;
                }
            }
        } else {
            for (nIdx = 0; nIdx < nLen; nIdx++) {
                pBuff[nOffSet + nIdx] = 0x00;
            }
        }
    }

    public static String ParserStringToASCII(
            byte[] pBuff,
            int nOffSet,
            int nLen
    ) {
        String strAscii = "";
        int i;
        char cBuff;

        for (i = 0; i < nLen; i++) {
            if (pBuff[nOffSet + i] > 0) {
                cBuff = (char) pBuff[nOffSet + i];
                strAscii += cBuff;
            }
        }
        return strAscii;
    }

    public static void convStringToASCIICode(
            String strData,
            byte[] pBuff,
            int nOffSet,
            int nLen
    ) {
        int nIdx;
        int nStrLen;

        if (strData != null) {
            nStrLen = strData.length();
            if (nStrLen > nLen) nStrLen = nLen;

            char[] temp = strData.toCharArray();
            for (nIdx = 0; nIdx < nStrLen; nIdx++) {
                pBuff[nOffSet + nIdx] = (byte) temp[nIdx];
            }

            if (nStrLen < nLen) {
                for (nIdx = nStrLen; nIdx < nLen; nIdx++) {
                    pBuff[nOffSet + nIdx] = 0x00;
                }
            }
        } else {
            for (nIdx = 0; nIdx < nLen; nIdx++) {
                pBuff[nOffSet + nIdx] = 0x00;
            }
        }
    }

    public static void convMeterStringToBCD2Code(
            String strData,
            byte[] pBuff,
            int nOffSet,
            int nLen
    ) {
        int nIdx;
        int nStrLen;
        byte[] pConv = new byte[nLen];

        if (strData != null) {
            nStrLen = strData.length() / 2;

            if (nStrLen > nLen) nStrLen = nLen;

            for (nIdx = 0; nIdx < nStrLen; nIdx++) {
                int nPos = nIdx * 2;
                String strTmp = strData.substring(nPos, nPos + 2);
                pConv[nIdx] = (byte) convertStringHexToInteger(strTmp);
            }

            if (nStrLen < nLen) {
                for (nIdx = nStrLen; nIdx < nLen; nIdx++) {
                    pConv[nIdx] = 0x00;
                }
            }

            //순서 변환 예) 09-123456 => 56/34/12/09
            for (nIdx = 0; nIdx < nLen; nIdx++) {
                pBuff[nOffSet + nIdx] = pConv[nLen - nIdx - 1];
            }
        } else {
            for (nIdx = 0; nIdx < nLen; nIdx++) {
                pBuff[nOffSet + nIdx] = 0x00;
            }
        }
    }

    public static void convStringToHexCode(
            String strData,
            byte[] pBuff,
            int nOffSet,
            int nLen
    ) {
        int nIdx;
        int nStrLen;

        if (strData != null) {
            nStrLen = strData.length() / 2;

            if (nStrLen > nLen) nStrLen = nLen;

            for (nIdx = 0; nIdx < nStrLen; nIdx++) {
                int nPos = nIdx * 2;
                String strTmp = strData.substring(nPos, nPos + 2);
                pBuff[nOffSet + nIdx] = (byte) convertStringHexToInteger(strTmp);
            }

            if (nStrLen < nLen) {
                for (nIdx = nStrLen; nIdx < nLen; nIdx++) {
                    pBuff[nOffSet + nIdx] = 0x00;
                }
            }
        } else {
            for (nIdx = 0; nIdx < nLen; nIdx++) {
                pBuff[nOffSet + nIdx] = 0x00;
            }
        }
    }

    public static void convMeterStringToHexCode(
            String strData,
            byte[] pBuff,
            int nOffSet,
            int nLen
    ) {
        int nIdx;
        int nStrHalfLen;
        byte[] pConv = new byte[nLen];

        if (strData != null) {
            nStrHalfLen = strData.length() / 2;

            if (nStrHalfLen > nLen) nStrHalfLen = nLen;

            for (nIdx = 0; nIdx < nStrHalfLen; nIdx++) {
                int nPos = nIdx * 2;
                String strTmp = strData.substring(nPos, nPos + 2);
                pConv[nIdx] = (byte) convertStringHexToInteger(strTmp);
            }

            if (nStrHalfLen < nLen) {
                for (nIdx = nStrHalfLen; nIdx < nLen; nIdx++) {
                    pConv[nIdx] = 0x00;
                }
            }

            for (nIdx = 0; nIdx < nLen; nIdx++) {
                pBuff[nOffSet + nIdx] = pConv[nLen - nIdx - 1];
            }
        } else {
            for (nIdx = 0; nIdx < nLen; nIdx++) {
                pBuff[nOffSet + nIdx] = 0x00;
            }
        }
    }

    /**
     * <pre>
     * Qn Union value parser
     * </pre>
     */
    public static String ParserQnUnionVal(byte[] pBuff, int nOffset) {
        String strTmpVal = "";

        strTmpVal = String.format("%02X", pBuff[nOffset + 1]);
        strTmpVal += String.format("%02X", pBuff[nOffset + 0]);

        String strQnVal = "";

        int nConvInt;
        int nIntPart;
        int nDecimalPart;

        nConvInt = convertStringHexToInteger(strTmpVal);

        nIntPart = nConvInt / 10;
        nDecimalPart = nConvInt % 10;

        strQnVal = String.format("%d.%d", nIntPart, nDecimalPart);

        return strQnVal;
    }

    /**
     * <pre>
     * 인증용 Calibration flow parser
     * </pre>
     */
    public static String ParserCertiCalibrationFlow(byte[] pBuff, int nOffset) {
        String strTmpVal = "";

        strTmpVal = String.format("%02X", pBuff[nOffset + 1]);
        strTmpVal += String.format("%02X", pBuff[nOffset + 0]);

        String strCompVal = "";
        int nConvInt;

        nConvInt = convertStringHexToInteger(strTmpVal);
        strCompVal = String.format("%d", nConvInt);

        return strCompVal;
    }

    public static void convInt16ToByte(int nData, byte[] pBuff, int nOffSet) {
        int nUp = nData / 0x100;
        int nDn = nData % 0x100;

        pBuff[nOffSet + 0] = (byte) nDn;
        pBuff[nOffSet + 1] = (byte) nUp;
    }

    public static byte calcChecksum(byte[] pBuff, int nStart, int nLen) {
        byte checksum = 0;
        for (int i = 0; i < nLen; i++) {
            checksum += pBuff[nStart + i];
        }
        return checksum;
    }
}
