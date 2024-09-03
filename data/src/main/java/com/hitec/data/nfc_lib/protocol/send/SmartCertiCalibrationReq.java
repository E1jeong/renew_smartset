/**
 * <pre>
 * NodeConfReq 메시지
 * </pre>
 */
package com.hitec.data.nfc_lib.protocol.send;

import com.hitec.data.nfc_lib.protocol.NfcConstant;
import com.hitec.data.nfc_lib.util.DevUtil;

public class SmartCertiCalibrationReq extends NfcTxMessage {

    public SmartCertiCalibrationReq() {
        m_nOffset = m_nHeaderSize;

        hexData[m_nOffset++] = (byte) NfcConstant.NODE_SEND_SMART_METER_SEND;
        hexData[m_nOffset++] = (byte) 0; //MsgVersion


        DevUtil.convStringToASCIICode(null, hexData, m_nOffset, 12);
        m_nOffset += 12;


        hexData[m_nOffset++] = (byte) NfcConstant.SMART_METER_CHANGE_MODE_READ; //Change Mode - 0 : 읽기 모드

        int nLenPos = (int) m_nOffset;
        hexData[m_nOffset++] = (byte) 0; //Data length

        int nStart = (int) m_nOffset;
        hexData[m_nOffset++] = (byte) NfcConstant.METER_SEND_CERTI_COMP_REQ; //C Field
        hexData[m_nOffset++] = (byte) 0x01; //A Field

        byte checksum = DevUtil.calcChecksum(hexData, nStart, m_nOffset - nStart);
        hexData[m_nOffset++] = (byte) checksum;

        int nEnd = (int) m_nOffset;
        int nSize = nEnd - nStart;
        hexData[nLenPos] = (byte) nSize;
    }
}
