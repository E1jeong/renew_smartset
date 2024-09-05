/**
 * <pre>
 * NfcRxMessage Class - 모든 NFC 메시지 Parser의 기본형
 * </pre>
 */
package com.hitec.presentation.nfc_lib.protocol.recv;

import android.annotation.SuppressLint;

import com.hitec.presentation.nfc_lib.protocol.NfcConstant;
import com.hitec.presentation.nfc_lib.util.DevUtil;
import com.hitec.presentation.nfc_lib.util.bLog;

import java.text.SimpleDateFormat;
import java.util.Date;

@SuppressLint("SimpleDateFormat")
public class NfcRxMessage implements NfcConstant {

    protected static final String TAG = "NFC_RX";

    /*
     * Parsing을 위한 데이터, 정보
     */
    protected static final int m_nHeaderSize = 10; // DeviceCode(1) + Len(1) + DeviceId(8)

    // 입력된 문자열을 파싱하기 위해 byte array로 변환하여 담아두는 곳
    protected byte[] hexData = new byte[MAX_LEN_RF_MESSAGE];
    protected int m_nHexLen = 0;
    protected int m_nOffset = 0;

    /*
     * Header
     */
    protected int m_nHeaderDevCode = 0;
    protected int m_nMsgLen = 0;
    protected String m_strSrcDevId = "";
    // Msg 타입
    protected int m_nNodeMsgType = 0;
    protected int m_nNodeMsgVersion = 0;
    // Date/Time
    protected int nYear = 0;
    protected int nMon = 0;
    protected int nDay = 0;
    protected int nWeek = 0;
    protected int nHour = 0;
    protected int nMin = 0;
    protected int nSec = 0;

    public static String GetNodeMsgName(int mtype) {
        String name = "unknown msg message" + "[" + mtype + "]";
        switch (mtype) {
            case NODE_RECV_LORA_CONF_REPORT:
                name = "NODE_RECV_LORA_CONF_REPORT";
                break;
            case NODE_RECV_NB_CONF_REPORT:
                name = "NODE_RECV_NB_CONF_REPORT";
                break;
            case NODE_RECV_METER_REPORT:
                name = "NODE_RECV_METER_REPORT";
                break;
            case NODE_RECV_SERVER_CONNECT_REPORT:
                name = "NODE_RECV_SERVER_CONNECT_REPORT";
                break;
            case NODE_RECV_BD_CONTROL_ACK:
                name = "NODE_RECV_BD_RESET_ACK";
                break;
            case NODE_RECV_DEVICE_INFO_REPORT:
                name = "NODE_RECV_DEVICE_INFO_REPORT";
                break;
            case NODE_RECV_SLAVE_CHECK_REPORT:
                name = "NODE_RECV_SLAVE_CHECK_REPORT";
                break;
            case NODE_RECV_PERIOD_METER_REPORT:
                name = "NODE_RECV_PERIOD_METER_REPORT";
                break;
            case NODE_RECV_PULSE_METER_VALUE_ACK:
                name = "NODE_RECV_PULSE_METER_VALUE_ACK";
                break;
            case NODE_RECV_NB_ID_REPORT:
                name = "NODE_RECV_NB_ID_REPORT";
                break;
            case NODE_RECV_DISPLAY_CONF_REPORT:
                name = "NODE_RECV_DISPLAY_CONF_REPORT";
                break;
            case NODE_RECV_TIME_INFO_REPORT:
                name = "NODE_RECV_TIME_INFO_REPORT";
                break;
            case NODE_RECV_ACCOUNT_NO_REPORT:
                name = "NODE_RECV_ACCOUNT_NO_REPORT";
                break;
            case NODE_RECV_LORA_EUI_KEY_REPORT:
                name = "NODE_RECV_LORA_EUI_KEY_REPORT";
                break;
            case NODE_RECV_SMART_METER_RECV:
                name = "NODE_RECV_SMART_METER_RECV";
                break;
            case NODE_RECV_FLASH_DATE_LIST_REPORT:
                name = "NODE_RECV_FLASH_DATE_LIST_REPORT";
                break;
            case NODE_RECV_FLASH_DATA_REPORT:
                name = "NODE_RECV_FLASH_DATA_REPORT";
                break;
        }
        return name;
    }

    public int GetHeaderDevCode() {
        return m_nHeaderDevCode;
    }

    public int GetMsgLen() {
        return m_nMsgLen;
    }

    public String GetHeaderSrcDevId() {
        return m_strSrcDevId.toLowerCase(); //소문자;
    }

    public int GetNodeMsgType() {
        return m_nNodeMsgType;
    }

    public int GetNodeMsgVersion() {
        return m_nNodeMsgVersion;
    }

    protected String getMessageTime() {
        return String.format(
                "%04d-%02d-%02d %02d:%02d:%02d",
                nYear,
                nMon,
                nDay,
                nHour,
                nMin,
                nSec
        );
    }

    protected int getHexData(int index) {
        if (index >= m_nHexLen) {
            return 0;
        }
        return hexData[index] & 0xff;
    }

    protected int getHexDataBcd2int(int index) {
        if (index >= m_nHexLen) {
            return 0;
        }
        return bcd2int(hexData[index] & 0xff);
    }

    // RF Header와 msgType을 파싱함.
    protected Boolean parseHeaderAndMsgType(byte[] rxdata) {
        int i;

        m_nHexLen = rxdata.length;
        if (m_nHexLen > MAX_LEN_RF_MESSAGE || m_nHexLen < MIN_LEN_RF_MESSAGE) {
            parseFailed(
                    "RF message: too long or too shord length(" + m_nHexLen + ")"
            );
            return false;
        }

        for (i = 0; i < MAX_LEN_RF_MESSAGE; i++) {
            if (i < m_nHexLen) {
                hexData[i] = (byte) (rxdata[i] & 0xFF);
            } else {
                hexData[i] = 0;
            }
        }

        int nMsgLen = getHexData(1);
        int nRxLen = 2 + nMsgLen; //DevCode(1) + Len(1) + MsgLen(n)
        byte c_checksum = 0;
        byte r_checksum = 0;

        r_checksum = (byte) hexData[nRxLen];
        for (i = 0; i < nRxLen; i++) {
            c_checksum += hexData[i];
        }

        if (r_checksum != c_checksum) {
            parseFailed(
                    "Checksum Error [R:" +
                            String.format("%02X ", r_checksum) +
                            ", C:" +
                            String.format("%02X ", c_checksum) +
                            "]"
            );
            return false;
        }

        m_nOffset = 0;

        m_nHeaderDevCode = getHexData(m_nOffset++);
        m_nMsgLen = getHexData(m_nOffset++);

        m_strSrcDevId = parseDeviceId(hexData, m_nOffset, 8);
        m_nOffset += 8;

        m_nNodeMsgType = getHexData(m_nOffset++);
        m_nNodeMsgVersion = getHexData(m_nOffset++);
        return true;
    }

    protected String parseDeviceId(byte[] pBuff, int nOffSet, int nLen) {
        String srcId = DevUtil.convHexToStringCode(pBuff, nOffSet, nLen);
        String dstId = srcId;
        String[] azDevId = new String[4];
        if (srcId == null) {
            srcId = "";
        }

        if (srcId.length() < nLen * 2) {
            for (int i = srcId.length(); i < nLen * 2; i++) {
                srcId += "?";
            }
        }

        if (m_nHeaderDevCode == DEVICE_CODE_LORA) {
            //123456-1234-123456
            azDevId[0] = srcId.substring(0, 6);
            azDevId[1] = srcId.substring(6, 10);
            azDevId[2] = srcId.substring(10, 16);

            dstId = azDevId[0] + "-" + azDevId[1] + "-" + azDevId[2];
        } else if (
                m_nHeaderDevCode == DEVICE_CODE_NB || m_nHeaderDevCode == DEVICE_CODE_GSM
        ) {
            //35-936908-005795-9
            azDevId[0] = srcId.substring(0, 2);
            azDevId[1] = srcId.substring(2, 8);
            azDevId[2] = srcId.substring(8, 14);
            azDevId[3] = srcId.substring(14, 15);

            dstId =
                    azDevId[0] + "-" + azDevId[1] + "-" + azDevId[2] + "-" + azDevId[3];
        } else if (m_nHeaderDevCode == DEVICE_CODE_DISPLAY) {
            //1234567890123456
            dstId = srcId;
        }
        return dstId;
    }

    protected String parseSerial(int index, int len) {
        String serial = "";
        for (int i = 0; i < len; i++) {
            int ch = (getHexData(index + i)) & 0xff;
            if (ch != 0x00) {
                serial = serial.concat(String.format("%C", ch));
            }
        }
        return serial;
    }

    protected String parsePhoneNumber(int index) {
        String number = "";
        for (int i = 0; i < 11; i++) {
            int ch = (getHexData(index + i)) & 0xff;
            number = number.concat(String.format("%C", ch));
        }
        return number;
    }

    protected String parseAccountNo(int index, int nLen) {
        String number = "";
        for (int i = 0; i < nLen; i++) {
            int ch = (getHexData(index + i)) & 0xff;
            if (ch > 0) {
                number = number.concat(String.format("%C", ch));
            }
        }
        return number;
    }

    protected String parseHexData(int index, int nLen) {
        String strData = "";
        for (int i = 0; i < nLen; i++) {
            int nData = (getHexData(index + i)) & 0xff;
            strData = strData.concat(String.format("%02x", nData));
        }
        return strData;
    }

    protected String parseAsciiData(int index, int nLen) {
        String strData = "";
        for (int i = 0; i < nLen; i++) {
            int nData = (getHexData(index + i)) & 0xff;
            if (nData > 0) {
                strData = strData.concat(String.format("%C", nData));
            }
        }
        return strData;
    }

    protected String parseBattery(int index) {
        int value = getHexData(index);
        return String.format("%d.%d", value / 10, value % 10);
    }

    protected int parseYear(int index) {
        return (getHexData(index + 1) * 0x100 + getHexData(index));
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
    protected void parseCompressDateTime(int index) {
        nYear = (getHexData(index) & 0x3F) + 2000; //6bit
        nMon = ((getHexData(index + 1) << 2) & 0x0C) + (getHexData(index) >> 6); //4bit
        nDay = ((getHexData(index + 1) >> 2) & 0x1F); //5bit
        nHour =
                ((getHexData(index + 2) << 1) & 0x1E) + (getHexData(index + 1) >> 7); //5bit
        nMin = ((getHexData(index + 3) << 4) & 0x30) + (getHexData(index + 2) >> 4); //6bit
        nSec = ((getHexData(index + 3) >> 2) & 0x3F); //6bit
    }

    protected int word2int(int index) {
        return (getHexData(index + 1) * 0x100 + getHexData(index));
    }

    protected String parseFwVersion(int index) {
        String ver = "";

        if (getHexData(index + 0) == 0x00) {
            //4바이트중 하위 2바이트만 유효하게 사용
            //0x00, 0x00, 0x12, 0x12
            int nSelect = ((getHexData(index + 2) >> 4) & 0x0F);
            int nMajor = (getHexData(index + 2) & 0x0F);
            int nManor = getHexData(index + 3);
            String strConnect = "";

            if (m_nHeaderDevCode == DEVICE_CODE_LORA) {
                strConnect = "L";
                if (nSelect == 0) {
                    strConnect += "S"; //SKT 플랫폼
                } else if (nSelect == 1) {
                    strConnect += "A"; //HITEC(AS923) 플랫폼
                } else {
                    strConnect += String.format("%X", nSelect);
                }
            } else if (m_nHeaderDevCode == DEVICE_CODE_NB) {
                strConnect = "N";
                if (nSelect == 0) {
                    strConnect += "H"; //HITEC 플랫폼
                } else if (nSelect == 1) {
                    strConnect += "L"; //LG 플랫폼
                } else if (nSelect == 2) {
                    strConnect += "K"; //KT 플랫폼
                } else {
                    strConnect += String.format("%X", nSelect);
                }
            } else {
                strConnect = "V";
                strConnect += String.format("%X", nSelect);
            }

            ver = ver.concat(String.format("%s%d.%d", strConnect, nMajor, nManor));
        } else {
            for (int i = 0; i < 4; i++) {
                int ch = (getHexData(index + i)) & 0xff;
                ver = ver.concat(String.format("%C", ch));
            }
        }
        return ver;
    }

    protected int GetDeviceFwType(int nDevType, String strFwVersion) {
        int nFwVersion = 0;
        //int nParseVer;
        //String strVerHead;
        //String strVerBody;

        //strVerHead = strFwVersion.substring(0, 1);
        //strVerBody = strFwVersion.substring(1, 4);

        //nParseVer = DevUtil.convertStringIntegerToInteger(strVerBody);

        if (nDevType == DEVICE_CODE_LORA) {
            nFwVersion = LORA_VERSION_L100;
        } else if (nDevType == DEVICE_CODE_NB) {
            nFwVersion = NB_VERSION_N100;
        } else if (nDevType == DEVICE_CODE_DISPLAY) {
            nFwVersion = DISPLAY_VERSION_D100;
        }

        return nFwVersion;
    }

    protected String parsePAN(int index) {
        return String.format("%02X%02X", getHexData(index + 1), getHexData(index));
    }

    protected String parseNWK(int index) {
        return String.format(
                "%d.%d.%d.%d",
                getHexData(index + 3),
                getHexData(index + 2),
                getHexData(index + 1),
                getHexData(index)
        );
    }

    protected int bcd2int(int bcd) {
        return ((bcd / 0x10) * 10 + bcd % 0x10);
    }

    protected String parseCompressedTime(int index) {
        int year = getHexData(index) + 2000;
        int mon = getHexData(index + 1) >> 4;
        int day = (getHexData(index + 1) & 0x0F) * 2;
        if ((getHexData(index + 2) & 0x80) != 0) {
            day += 1;
        }
        int hour = (getHexData(index + 2) & 0x7F) >> 2;

        return String.format("%04d-%02d-%02d %02d:00:00", year, mon, day, hour);
    }

    protected String addHour(String time, int hourDiff) {
        // 0123456789012345678
        // 2015-04-08 17:27:30
        int year = 2000;
        int mon = 1;
        int day = 1;
        int hour = 1;
        int min = 1;
        int sec = 1;
        //                   1   2   3   4   5   6   7   8   9  10  11  12
        int[] nDays = {0, 31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};

        try {
            year = Integer.parseInt(time.substring(0, 4));
            mon = Integer.parseInt(time.substring(5, 7));
            day = Integer.parseInt(time.substring(8, 10));
            hour = Integer.parseInt(time.substring(11, 13));
            min = Integer.parseInt(time.substring(14, 16));
            sec = Integer.parseInt(time.substring(17, 19));
        } catch (Exception e) {
        }

        hour = hour + hourDiff;
        if (hour > 23) {
            hour = hour - 24;

            int nMaxDays = nDays[mon];
            if ((mon == 2 && (year % 4) == 0)) {
                nMaxDays++;
            }

            if (++day > nMaxDays) {
                day = 1;
                if (++mon > 12) {
                    mon = 1;
                    year++;
                }
            }
        }

        return String.format(
                "%04d-%02d-%02d %02d:%02d:%02d",
                year,
                mon,
                day,
                hour,
                min,
                sec
        );
    }

    protected int parseMeterValue(int index) {
        int value = 0;
        for (int i = 0; i < 4; i++) {
            value *= 100;
            value += bcd2int(getHexData(index + i));
        }
        return value;
    }

    protected String parseServerIP(int index) {
        return String.format(
                "%d.%d.%d.%d",
                getHexData(index + 3),
                getHexData(index + 2),
                getHexData(index + 1),
                getHexData(index)
        );
    }

    protected int parseServerPort(int index) {
        return (getHexData(index + 1) * 0x100 + getHexData(index));
    }

    protected boolean parseCompleted() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        String currDateTime = sdf.format(new Date());

        bLog.i(TAG, "[" + currDateTime + "] " + GetNodeMsgName());
        return true;
    }

    protected boolean parseFailed(String reason) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        String currDateTime = sdf.format(new Date());

        bLog.e(TAG, "[" + currDateTime + "] " + GetNodeMsgName() + ": " + reason);
        return false;
    }

    protected String parseMeterSerial(int index) {
        // 4바이트 BCD로 사용
        return String.format(
                "%02X%02X%02X%02X",
                getHexData(index + 0),
                getHexData(index + 1),
                getHexData(index + 2),
                getHexData(index + 3)
        );
    }

    protected String parseCseId(int index, int len) {
        String cseId = "";
        for (int i = 0; i < len; i++) {
            int ch = (getHexData(index + i)) & 0xff;
            if (ch != 0x00) {
                cseId = cseId.concat(String.format("%c", ch)); //대소문자 그대로 저장
            }
        }
        return cseId;
    }

    protected int parseMeterHexAddVal(int index) {
        return (getHexData(index + 1) * 0x100 + getHexData(index));
    }

    public String GetNodeMsgName() {
        return GetNodeMsgName(m_nNodeMsgType);
    }

    public boolean parse(byte[] rxdata) {
        if (parseHeaderAndMsgType(rxdata) == false) {
            return false;
        }
        return true;
    }
}
