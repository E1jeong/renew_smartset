/**
 * <pre>
 * ChangeMinuteIntervalReq 주기보고 분단위 변경
 * 검침주기와 보고주기는 동일해야함
 * </pre>
 */
package com.hitec.data.nfc_lib.protocol.send;

import com.hitec.data.nfc_lib.protocol.NfcConstant;

public class ChangeMinuteIntervalReq extends NfcTxMessage {

    public ChangeMinuteIntervalReq(int Value) {
        m_nOffset = m_nHeaderSize;

        hexData[m_nOffset++] =
                (byte) NfcConstant.NODE_SEND_CHANGE_MINUTE_INTEVAL_REQ;
        hexData[m_nOffset++] = (byte) 0; //MsgVersion
        hexData[m_nOffset++] = (byte) Value; // 검침주기 (= 보고주기)
        hexData[m_nOffset++] = (byte) Value; // 보고주기
    }
}
