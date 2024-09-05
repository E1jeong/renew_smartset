/**
 * <pre>
 * 계량기 설정 메시지
 * </pre>
 */
package com.hitec.presentation.nfc_lib.protocol.send;

import com.hitec.presentation.nfc_lib.protocol.NfcConstant;
import com.hitec.presentation.nfc_lib.util.DevUtil;

public class SmartConfSet extends NfcTxMessage {

    public SmartConfSet(
            int changeMode,
            int nFlowType,
            String strDeviceSerial,
            int nMeterCaliber,
            String strMeterSerial,
            int nQ3Value,
            int nQtValue,
            int nQsValue,
            int nQ2Value,
            int nQ1Value,
            int nTemperature,
            int nMaker
    ) {
        m_nOffset = m_nHeaderSize;

        hexData[m_nOffset++] = (byte) NfcConstant.NODE_SEND_SMART_METER_SEND;
        hexData[m_nOffset++] = (byte) 0; //MsgVersion

        DevUtil.convStringToASCIICode(strDeviceSerial, hexData, m_nOffset, 12);
        m_nOffset += 12;

        if (changeMode == NfcConstant.SMART_METER_CHANGE_MODE_WRITE_SN) {
            hexData[m_nOffset++] =
                    (byte) NfcConstant.SMART_METER_CHANGE_MODE_WRITE_SN; //Change Mode - 1 : 일련번호 변경
        } else {
            hexData[m_nOffset++] =
                    (byte) NfcConstant.SMART_METER_CHANGE_MODE_WRITE_METER; //Change Mode - 2 : 계량기 설정 변경
        }

        int nLenPos = (int) m_nOffset;
        hexData[m_nOffset++] = (byte) 0; //Data length

        int nStart = (int) m_nOffset;
        hexData[m_nOffset++] = (byte) NfcConstant.METER_SEND_CONF_SET; //C Field
        hexData[m_nOffset++] = (byte) 0x01; //A Field

        hexData[m_nOffset++] = (byte) nMeterCaliber;

        if (strMeterSerial.length() < 8) {
            DevUtil.convMeterStringToBCD2Code(null, hexData, m_nOffset, 4);
        } else {
            DevUtil.convMeterStringToBCD2Code(strMeterSerial, hexData, m_nOffset, 4);
        }
        m_nOffset += 4;

        DevUtil.convInt16ToByte(nQ3Value, hexData, m_nOffset);
        m_nOffset += 2;
        DevUtil.convInt16ToByte(nQtValue, hexData, m_nOffset);
        m_nOffset += 2;
        if (nFlowType == NfcConstant.METER_FLOW_TYPE_ULTRA) {
            DevUtil.convInt16ToByte(nQsValue, hexData, m_nOffset);
            m_nOffset += 2;
        }
        DevUtil.convInt16ToByte(nQ2Value, hexData, m_nOffset);
        m_nOffset += 2;
        DevUtil.convInt16ToByte(nQ1Value, hexData, m_nOffset);
        m_nOffset += 2;

        if (nFlowType == NfcConstant.METER_FLOW_TYPE_ULTRA) {
            DevUtil.convInt16ToByte(nTemperature, hexData, m_nOffset);
            m_nOffset += 2;
        }

        hexData[m_nOffset++] = (byte) nMaker;

        byte checksum = DevUtil.calcChecksum(hexData, nStart, m_nOffset - nStart);
        hexData[m_nOffset++] = (byte) checksum;

        int nEnd = (int) m_nOffset;
        int nSize = nEnd - nStart;
        hexData[nLenPos] = (byte) nSize;
    }
}
