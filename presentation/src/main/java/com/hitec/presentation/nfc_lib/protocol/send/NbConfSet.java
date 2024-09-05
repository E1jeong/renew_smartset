/**
 * <pre>
 * NodeConfSet 메시지
 * </pre>
 */
package com.hitec.presentation.nfc_lib.protocol.send;

import com.hitec.presentation.nfc_lib.protocol.NfcConstant;
import com.hitec.presentation.nfc_lib.util.DevUtil;

public class NbConfSet extends NfcTxMessage {


    //Msg Type : 1, 3, 5
    public NbConfSet(
            int msgVersion,
            String serialNo,
            int sleepMode,
            int amiMeteringInterval,
            int amiReportInterval,
            int terminalProtocol,
            String serviceCode,
            String strServerIp,
            String strServerPort,
            int meterNum,
            int meterType0,
            int meterPort0,
            int meterType1,
            int meterPort1,
            int meterType2,
            int meterPort2
    ) {
        m_nOffset = m_nHeaderSize;

        hexData[m_nOffset++] = (byte) NfcConstant.NODE_SEND_NB_CONF_SET;
        hexData[m_nOffset++] = (byte) msgVersion; //MsgVersion


        m_nOffset += fillSerialNumber(m_nOffset, serialNo, 12);


        hexData[m_nOffset++] = (byte) sleepMode;
        //time
        m_nOffset += fillCurrentTime(m_nOffset);

        hexData[m_nOffset++] = (byte) amiMeteringInterval;
        hexData[m_nOffset++] = (byte) amiReportInterval;

        if (msgVersion >= 2) {
            hexData[m_nOffset++] = (byte) terminalProtocol;
        } else {
            hexData[m_nOffset++] = 0; //DataFormat, Not Use
        }

        if (msgVersion >= 2) {
            if (serviceCode.length() == 4) {
                m_nOffset += addServiceCode(m_nOffset, serviceCode);
            } else {
                m_nOffset += fillZeroData(m_nOffset, 4);
            }
        }

        // server ip1, port1
        m_nOffset += addIpAddress(m_nOffset, strServerIp);
        int portNo = DevUtil.convertStringIntegerToInteger(strServerPort);

        hexData[m_nOffset++] = (byte) (portNo % 256);
        hexData[m_nOffset++] = (byte) (portNo / 256);

        hexData[m_nOffset++] = (byte) meterNum;
        hexData[m_nOffset++] = (byte) meterType0;
        hexData[m_nOffset++] = (byte) meterPort0;
        hexData[m_nOffset++] = (byte) meterType1;
        hexData[m_nOffset++] = (byte) meterPort1;
        hexData[m_nOffset++] = (byte) meterType2;
        hexData[m_nOffset++] = (byte) meterPort2;
    }

    //보조중계기 Msg Type : 2, 4, 6
    public NbConfSet(
            int msgVersion,
            String serialNo,
            int sleepMode,
            int amiMeteringInterval,
            int amiReportInterval,
            int amiReportRange,
            String serviceCode,
            String strServerIp,
            String strServerPort,
            String pan,
            String nwk,
            String strSubId
    ) {
        m_nOffset = m_nHeaderSize;

        hexData[m_nOffset++] = (byte) NfcConstant.NODE_SEND_NB_CONF_SET;
        hexData[m_nOffset++] = (byte) 1; //MsgVersion

        m_nOffset += fillSerialNumber(m_nOffset, serialNo, 12);

        hexData[m_nOffset++] = (byte) sleepMode;
        //time
        m_nOffset += fillCurrentCompressTime(m_nOffset);

        hexData[m_nOffset++] = (byte) amiMeteringInterval;
        hexData[m_nOffset++] = (byte) amiReportInterval;

        if (msgVersion >= 2) {
            hexData[m_nOffset++] = (byte) amiReportRange;
        } else {
            hexData[m_nOffset++] = 0; //DataFormat, Not Use
        }

        if (msgVersion >= 2) {
            if (serviceCode.length() == 4) {
                m_nOffset += addServiceCode(m_nOffset, serviceCode);
            } else {
                m_nOffset += fillZeroData(m_nOffset, 4);
            }
        }

        // server ip1, port1
        m_nOffset += addIpAddress(m_nOffset, strServerIp);
        int portNo = DevUtil.convertStringIntegerToInteger(strServerPort);

        hexData[m_nOffset++] = (byte) (portNo % 256);
        hexData[m_nOffset++] = (byte) (portNo / 256);

        m_nOffset += addPanAddress(m_nOffset, pan);
        m_nOffset += addNwkAddress(m_nOffset, nwk);
        hexData[m_nOffset++] =
                (byte) DevUtil.convertStringIntegerToInteger(strSubId);
    }
}
