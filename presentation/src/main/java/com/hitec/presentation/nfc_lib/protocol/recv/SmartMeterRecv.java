/**
 * <pre>
 * FwVerReport 메시지 Parser
 * </pre>
 */
package com.hitec.presentation.nfc_lib.protocol.recv;

import android.util.Log;

import com.hitec.presentation.nfc_lib.protocol.NfcConstant;
import com.hitec.presentation.nfc_lib.util.DevUtil;
import com.hitec.presentation.nfc_lib.util.Meter;

public class SmartMeterRecv extends NfcRxMessage {

    private static int CONF_DIGITAL_LEN = 18;
    private static int CONF_ULTRA_LEN = 24;
    private static int CONF_METER_DIGITAL_LEN = 23;
    private static int CONF_METER_ULTRA_LEN = 29;
    private int m_nDataLength = 0;

    private int m_nMeterMsgType = 0;
    private boolean m_fMeterMsgValid = true;
    private String m_strSerialNum = "";
    private String m_strFwVersion = "";
    private int m_nMeterCaliberCd = 0; //구경코드
    private String m_strMeterCaliber = ""; //구경정보
    private String m_strMeterSn = ""; //일련번호
    private String m_strMeterQ3Value = "";
    private String m_strMeterQtValue = "";
    private String m_strMeterQsValue = "";
    private String m_strMeterQ2Value = "";
    private String m_strMeterQ1Value = "";
    private String m_strSetTemperature = "";
    private String m_strCurrentTemperature = "";
    private int m_nMeterFwVersion = 0;
    private int m_nMeterMaker = 0; //메이커
    private int m_nSetResult = 0; //설정결과
    private String m_strMeterVal = ""; //검침값
    private int m_nMeterStatus = 0; //계량기 상태 플래그
    private int m_nMeterValid = 0; //계량기 데이타 유효성 정보
    //계량기 유량부 종류
    private int m_nMeterFlowType = 0;
    //계량기벨브제어
    private int m_nMeterValveControl = 0;
    //자동보정모드
    private int m_nAutoMode = 0;
    //자동보정Qn
    private int m_nAutoQnSelect = 0;
    //자동보정오류
    private int m_nAutoErrorCode = 0;

    public int GetMeterMsgType() {
        return m_nMeterMsgType;
    }

    public boolean GetMeterMsgValid() {
        return m_fMeterMsgValid;
    }

    public String GetSerialNumber() {
        return m_strSerialNum;
    }

    public String GetFwVersion() {
        return m_strFwVersion;
    }

    public int GetMeterCaliberCd() {
        return m_nMeterCaliberCd;
    }

    public String GetMeterCaliber() {
        return m_strMeterCaliber;
    }

    public String GetMeterSerial() {
        return m_strMeterSn;
    }

    public String GetMeterQ3Value() {
        return m_strMeterQ3Value;
    }

    public String GetMeterQtValue() {
        return m_strMeterQtValue;
    }

    public String GetMeterQsValue() {
        return m_strMeterQsValue;
    }

    public String GetMeterQ2Value() {
        return m_strMeterQ2Value;
    }

    public String GetMeterQ1Value() {
        return m_strMeterQ1Value;
    }

    public String GetSetTemperature() {
        return m_strSetTemperature;
    }

    public String GetCurrentTemperature() {
        return m_strCurrentTemperature;
    }

    public String GetMeterFwVersion() {
        if (m_nMeterFwVersion > 0) {
            return String.format("%d", m_nMeterFwVersion);
        } else {
            return "";
        }
    }

    public String GetMeterMaker() {
        String strMakerName = "";
        if (m_nMeterMaker > 0) {
            if (m_nMeterMaker == METER_MAKER_HT) {
                strMakerName = METER_MAKER_NAME_HT;
            } else if (m_nMeterMaker == METER_MAKER_DH) {
                strMakerName = METER_MAKER_NAME_DH;
            } else if (m_nMeterMaker == METER_MAKER_KS) {
                strMakerName = METER_MAKER_NAME_KS;
            } else if (m_nMeterMaker == METER_MAKER_HT_S) {
                strMakerName = METER_MAKER_NAME_HT_S;
            } else if (m_nMeterMaker == METER_MAKER_DS) {
                strMakerName = METER_MAKER_NAME_DS;
            } else if (m_nMeterMaker == METER_MAKER_HT_U) {
                strMakerName = METER_MAKER_NAME_HT_U;
            }
            return strMakerName;
            //return String.format("%d", m_nMeterMaker);
        } else {
            return "";
        }
    }

    public int GetSetResult() {
        return m_nSetResult;
    }

    public String GetMeterValue() {
        return m_strMeterVal;
    }

    public String GetMeterStatus() {
        int nStatus = m_nMeterStatus;
        return DevUtil.convertIntToHexLPad(nStatus, 2, "0");
    }

    public String GetMeteredTime() {
        return getMessageTime();
    }

    public boolean GetMeterValid() {
        return m_nMeterValid == METER_VALID_OK;
    }

    public int GetMeterFlowType() {
        return m_nMeterFlowType;
    }

    public int GetMeterValveControl() {
        return m_nMeterValveControl;
    }

    public int GetAutoMode() {
        return m_nAutoMode;
    }

    public int GetAutoQnSelect() {
        return m_nAutoQnSelect;
    }

    public int GetAutoErrorCode() {
        return m_nAutoErrorCode;
    }

    private void InitMeterInfo() {
        m_strMeterCaliber = "";
        m_strMeterSn = "";
        m_strMeterQ3Value = "";
        m_strMeterQtValue = "";
        m_strMeterQsValue = "";
        m_strMeterQ2Value = "";
        m_strMeterQ1Value = "";
        m_strMeterVal = "";
        m_nSetResult = 0;
    }

    @Override
    public boolean parse(byte[] rxdata) {
        if (!super.parse(rxdata)) {
            return false;
        }

        InitMeterInfo();

        m_strSerialNum = parseSerial(m_nOffset, 12);
        m_nOffset += 12;


        m_strFwVersion = parseFwVersion(m_nOffset);
        m_nOffset += 4;

        m_nDataLength = getHexData(m_nOffset++);

        int posChecksum = m_nOffset + m_nDataLength - 1;
        byte c_checksum;
        byte r_checksum;

        r_checksum = hexData[posChecksum];
        c_checksum = DevUtil.calcChecksum(hexData, m_nOffset, m_nDataLength - 1);
        Log.i(TAG, "SmartMeterRecv ==>02 m_nDataLength:" + m_nDataLength + " r_checksum:" + r_checksum + " c_checksum:" + c_checksum);

        m_nMeterMsgType = getHexData(m_nOffset++); //C Field

        if (r_checksum != c_checksum) {
            m_fMeterMsgValid = false;
        } else {
            if (m_nMeterMsgType == NfcConstant.METER_RECV_METER_REPORT) {
                //int aField = getHexData(m_nOffset++);	//A Field
                //int ciField = getHexData(m_nOffset++); //CI Field
                //int mdh = getHexData(m_nOffset++); //MDH
                m_nOffset += 3;

                parserMeterReport(hexData, m_nOffset);
            } else if (m_nMeterMsgType == METER_RECV_CONF_REPORT) {
                parserConfReport(hexData, m_nOffset);
            } else if (m_nMeterMsgType == METER_RECV_CONF_METER_REPORT) {
                parserConfMeterReport(hexData, m_nOffset);
            } else if (m_nMeterMsgType == METER_RECV_CERTI_COMP_REPORT) {
                parserCertiCompReport(hexData, m_nOffset);
            } else if (m_nMeterMsgType == METER_RECV_VALVE_CONTROL_REPORT) {
                parserMeterValveControlReport(hexData, m_nOffset);
            } else if (m_nMeterMsgType == METER_RECV_CONF_AUTO_REPORT) {
                parserAutoCompReport(hexData, m_nOffset);
            }
        }

        return parseCompleted();
    }

    private void parserConfReport(byte[] pBuff, int nOffSet) {
        if (m_nDataLength == CONF_ULTRA_LEN) {
            m_nMeterFlowType = METER_FLOW_TYPE_ULTRA;
        } else {
            m_nMeterFlowType = METER_FLOW_TYPE_DIGITAL;
        }

        m_nMeterCaliberCd = getHexData(nOffSet++);
        m_strMeterCaliber =
                Meter.ParserStandardMeterCaliberString(m_nMeterCaliberCd);
        //계량기 일련번호
        byte[] pMeterSn = new byte[4];
        for (int i = 0; i < 4; i++) {
            //순서가 바뀌어서 다시 저장
            pMeterSn[i] = (byte) (pBuff[nOffSet + (3 - i)] & 0xFF);
        }
        m_strMeterSn = Meter.ParserMeterSerialBCD(pMeterSn, 0, 4);
        nOffSet += 4;

        m_strMeterQ3Value = DevUtil.ParserQnUnionVal(pBuff, nOffSet);
        nOffSet += 2;

        m_strMeterQtValue = DevUtil.ParserQnUnionVal(pBuff, nOffSet);
        nOffSet += 2;

        if (m_nMeterFlowType == METER_FLOW_TYPE_ULTRA) {
            m_strMeterQsValue = DevUtil.ParserQnUnionVal(pBuff, nOffSet);
            nOffSet += 2;
        } else {
            m_strMeterQsValue = "";
        }

        m_strMeterQ2Value = DevUtil.ParserQnUnionVal(pBuff, nOffSet);
        nOffSet += 2;

        m_strMeterQ1Value = DevUtil.ParserQnUnionVal(pBuff, nOffSet);
        nOffSet += 2;

        if (m_nMeterFlowType == METER_FLOW_TYPE_ULTRA) {
            m_strSetTemperature = DevUtil.ParserQnUnionVal(pBuff, nOffSet);
            nOffSet += 2;
            m_strCurrentTemperature = DevUtil.ParserQnUnionVal(pBuff, nOffSet);
            nOffSet += 2;
        } else {
            m_strSetTemperature = "";
            m_strCurrentTemperature = "";
        }

        m_nMeterMaker = getHexData(nOffSet++);
        m_nMeterFwVersion = getHexData(nOffSet++);

        m_nSetResult = getHexData(nOffSet++);
        m_strMeterVal = "0.0";
    }

    private void parserMeterReport(byte[] pBuff, int nOffSet) {
        int nValuePoint = 0;
        boolean fValueValid = true;
        byte[] pMeterVal = new byte[4];

        //계량기 일련번호
        byte[] pMeterSn = new byte[4];
        for (int i = 0; i < 4; i++) {
            //순서가 바뀌어서 다시 저장
            pMeterSn[i] = (byte) (pBuff[nOffSet + (3 - i)] & 0xFF);
        }
        m_strMeterSn = Meter.ParserMeterSerialBCD(pMeterSn, 0, 4);
        nOffSet += 4;

        m_nMeterStatus = getHexData(nOffSet++);
        int nDif = getHexData(nOffSet++);
        int nVif = getHexData(nOffSet++);

        m_nMeterCaliberCd = (nDif >> 4) & 0x0F;
        nValuePoint = nVif & 0x0F;

        m_strMeterCaliber =
                Meter.ParserStandardMeterCaliberString(m_nMeterCaliberCd);

        if (!Meter.CheckDecValid(pBuff, nOffSet, 4)) {
            fValueValid = false;
            m_nMeterValid = METER_VALID_VALUE_ERROR;
        }
        for (int i = 0; i < 4; i++) {
            //순서가 바뀌어서 다시 저장
            pMeterVal[i] = (byte) (pBuff[nOffSet + (3 - i)] & 0xFF);
        }
        nOffSet += 4;

        if (fValueValid) {
            m_strMeterVal =
                    Meter.ParserStandardMeterVal(pMeterVal, 0, 4, nValuePoint);
        }
        Log.i(TAG, "parserMeterStandardDigital ==> 02 m_strMeterVal:" + m_strMeterVal + " m_nMeterCaliberCd:" + m_nMeterCaliberCd);

        //계량기 오류인경우
        if (m_nMeterStatus == 0xFF) {
            m_nMeterValid = METER_VALID_VALUE_ERROR;
            m_strMeterVal = "";
        }
    }

    private void parserConfMeterReport(byte[] pBuff, int nOffSet) {
        if (m_nDataLength == CONF_METER_ULTRA_LEN) {
            m_nMeterFlowType = METER_FLOW_TYPE_ULTRA;
        } else {
            m_nMeterFlowType = METER_FLOW_TYPE_DIGITAL;
        }

        m_nMeterCaliberCd = getHexData(nOffSet++);
        m_strMeterCaliber =
                Meter.ParserStandardMeterCaliberString(m_nMeterCaliberCd);

        //계량기 일련번호
        byte[] pMeterSn = new byte[4];
        for (int i = 0; i < 4; i++) {
            //순서가 바뀌어서 다시 저장
            pMeterSn[i] = (byte) (pBuff[nOffSet + (3 - i)] & 0xFF);
        }
        m_strMeterSn = Meter.ParserMeterSerialBCD(pMeterSn, 0, 4);
        nOffSet += 4;

        m_strMeterQ3Value = DevUtil.ParserQnUnionVal(pBuff, nOffSet);
        nOffSet += 2;

        m_strMeterQtValue = DevUtil.ParserQnUnionVal(pBuff, nOffSet);
        nOffSet += 2;

        if (m_nMeterFlowType == METER_FLOW_TYPE_ULTRA) {
            m_strMeterQsValue = DevUtil.ParserQnUnionVal(pBuff, nOffSet);
            nOffSet += 2;
        } else {
            m_strMeterQsValue = "";
        }

        m_strMeterQ2Value = DevUtil.ParserQnUnionVal(pBuff, nOffSet);
        nOffSet += 2;

        m_strMeterQ1Value = DevUtil.ParserQnUnionVal(pBuff, nOffSet);
        nOffSet += 2;

        if (m_nMeterFlowType == METER_FLOW_TYPE_ULTRA) {
            m_strSetTemperature = DevUtil.ParserQnUnionVal(pBuff, nOffSet);
            nOffSet += 2;
            m_strCurrentTemperature = DevUtil.ParserQnUnionVal(pBuff, nOffSet);
            nOffSet += 2;
        } else {
            m_strSetTemperature = "";
            m_strCurrentTemperature = "";
        }

        m_nMeterMaker = getHexData(nOffSet++);
        m_nMeterFwVersion = getHexData(nOffSet++);

        int nValuePoint = 6;
        boolean fValueValid = true;
        byte[] pMeterVal = new byte[6];
        if (!Meter.CheckDecValid(pBuff, nOffSet, 6)) {
            fValueValid = false;
            m_nMeterValid = METER_VALID_VALUE_ERROR;
        }
        for (int i = 0; i < 6; i++) {
            //순서가 바뀌어서 다시 저장
            pMeterVal[i] = (byte) (pBuff[nOffSet + (5 - i)] & 0xFF);
        }
        nOffSet += 6;

        if (fValueValid) {
            m_strMeterVal = Meter.ParserSmartMeterVal(pMeterVal, 0, 6, nValuePoint);
        }
    }

    private void parserCertiCompReport(byte[] pBuff, int nOffSet) {
        m_nMeterCaliberCd = getHexData(nOffSet++);
        m_strMeterCaliber =
                Meter.ParserStandardMeterCaliberString(m_nMeterCaliberCd);

        m_nSetResult = getHexData(nOffSet++);

        m_nMeterMaker = getHexData(nOffSet++);
        m_nMeterFwVersion = getHexData(nOffSet++);

        int nDeviceSnLength = m_strSerialNum.length();
        if (nDeviceSnLength > 8) {
            m_strMeterSn = m_strSerialNum.substring(nDeviceSnLength - 8, nDeviceSnLength);
        }

        int nValuePoint = 6;
        boolean fValueValid = true;
        byte[] pMeterVal = new byte[6];
        if (!Meter.CheckDecValid(pBuff, nOffSet, 6)) {
            fValueValid = false;
            m_nMeterValid = METER_VALID_VALUE_ERROR;
        }
        for (int i = 0; i < 6; i++) {
            //순서가 바뀌어서 다시 저장
            pMeterVal[i] = (byte) (pBuff[nOffSet + (5 - i)] & 0xFF);
        }
        nOffSet += 6;

        if (fValueValid) {
            m_strMeterVal = Meter.ParserSmartMeterVal(pMeterVal, 0, 6, nValuePoint);
        }
    }

    private void parserAutoCompReport(byte[] pBuff, int nOffSet) {
        m_nAutoMode = getHexData(nOffSet++);
        m_nAutoQnSelect = getHexData(nOffSet++);
        m_nSetResult = getHexData(nOffSet++);
        m_nAutoErrorCode = getHexData(nOffSet++);
    }

    private void parserMeterValveControlReport(byte[] pBuff, int nOffSet) {
        m_nMeterValveControl = getHexData(nOffSet++);
    }
}
