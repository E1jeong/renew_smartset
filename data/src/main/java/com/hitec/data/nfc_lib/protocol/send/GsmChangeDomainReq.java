/**
 * <pre>
 * GsmChangeDomain gsm 주기보고 domain 변경
 * </pre>
 */
package com.hitec.data.nfc_lib.protocol.send;

import com.hitec.data.nfc_lib.protocol.NfcConstant;

public class GsmChangeDomainReq extends NfcTxMessage {

    public GsmChangeDomainReq(String domain) {
        m_nOffset = m_nHeaderSize;

        hexData[m_nOffset++] = (byte) NfcConstant.NODE_SEND_GSM_CHANGE_DOMAIN; //0x19
        hexData[m_nOffset++] = (byte) 0; //MsgVersion
        hexData[m_nOffset++] = (byte) domain.length();
        m_nOffset += fillDomain(m_nOffset, domain, domain.length());

        StringBuilder hexString = new StringBuilder();
        for (byte b : hexData) {
            // 각 바이트를 16진수 형태로 변환하고 문자열로 추가
            hexString.append(String.format("%02X ", b));
        }
    }
}
