/**
 * <pre>
 * FwVerReport 메시지 Parser
 * </pre>
 */
package com.hitec.data.nfc_lib.protocol.recv;

public class AccountNoReport extends NfcRxMessage {

    private String strAccountNo = "";

    public String GetAccountNo() {
        return strAccountNo;
    }

    @Override
    public boolean parse(byte[] rxdata) {
        if (super.parse(rxdata) == false) {
            return false;
        }
        strAccountNo = parseAccountNo(m_nOffset, 20);
        m_nOffset += 20;

        return parseCompleted();
    }
}
