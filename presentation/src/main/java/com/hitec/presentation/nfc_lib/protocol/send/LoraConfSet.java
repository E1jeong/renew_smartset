/**
 * <pre>
 * NodeConfSet 메시지
 * </pre>
 */
package com.hitec.presentation.nfc_lib.protocol.send;

import com.hitec.presentation.nfc_lib.protocol.NfcConstant;
import com.hitec.presentation.nfc_lib.util.DevUtil;

public class LoraConfSet extends NfcTxMessage {

    //Msg Type : 1
    public LoraConfSet(
            String serialNo,
            int sleepMode,
            int amiMeteringInterval,
            int amiReportInterval,
            int dataFormat,
            int meterNum,
            int meterType0,
            int meterPort0,
            int meterType1,
            int meterPort1,
            int meterType2,
            int meterPort2
    ) {
        m_nOffset = m_nHeaderSize;

        hexData[m_nOffset++] = (byte) NfcConstant.NODE_SEND_LORA_CONF_SET;
        hexData[m_nOffset++] = (byte) 0; //MsgVersion

        m_nOffset += fillSerialNumber(m_nOffset, serialNo, 12);

        hexData[m_nOffset++] = (byte) sleepMode;
        //time
        m_nOffset += fillCurrentTime(m_nOffset);

        hexData[m_nOffset++] = (byte) amiMeteringInterval;
        hexData[m_nOffset++] = (byte) amiReportInterval;
        hexData[m_nOffset++] = (byte) dataFormat;

        hexData[m_nOffset++] = (byte) meterNum;
        hexData[m_nOffset++] = (byte) meterType0;
        hexData[m_nOffset++] = (byte) meterPort0;
        hexData[m_nOffset++] = (byte) meterType1;
        hexData[m_nOffset++] = (byte) meterPort1;
        hexData[m_nOffset++] = (byte) meterType2;
        hexData[m_nOffset++] = (byte) meterPort2;
    }

    //Msg Type : 2
    public LoraConfSet(
            String serialNo,
            int sleepMode,
            int amiMeteringInterval,
            int amiReportInterval,
            int dataFormat,
            String pan,
            String nwk,
            String strSubId
    ) {
        m_nOffset = m_nHeaderSize;

        hexData[m_nOffset++] = (byte) NfcConstant.NODE_SEND_LORA_CONF_SET;
        hexData[m_nOffset++] = (byte) 1; //MsgVersion : 2

        m_nOffset += fillSerialNumber(m_nOffset, serialNo, 12);

        hexData[m_nOffset++] = (byte) sleepMode;
        //time
        m_nOffset += fillCurrentCompressTime(m_nOffset);

        hexData[m_nOffset++] = (byte) amiMeteringInterval;
        hexData[m_nOffset++] = (byte) amiReportInterval;
        hexData[m_nOffset++] = (byte) dataFormat;

        m_nOffset += addPanAddress(m_nOffset, pan);
        m_nOffset += addNwkAddress(m_nOffset, nwk);
        hexData[m_nOffset++] =
                (byte) DevUtil.convertStringIntegerToInteger(strSubId);
    }
}
