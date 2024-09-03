/**
 * <pre>
 * NodeConfSet 메시지
 * </pre>
 */
package com.hitec.data.nfc_lib.protocol.send;

import com.hitec.data.nfc_lib.protocol.NfcConstant;

public class AccountNoSet extends NfcTxMessage {

    //Msg Type : 1
    public AccountNoSet(String accountNo) {
        m_nOffset = m_nHeaderSize;

        hexData[m_nOffset++] = (byte) NfcConstant.NODE_SEND_ACCOUNT_NO_SET;
        hexData[m_nOffset++] = (byte) 0; //MsgVersion

        m_nOffset += fillAccountNo(m_nOffset, accountNo, 20);
    }
}
