/**
 * <pre>
 * GsmChangeDomainReport gsm도메인변경에 대한 응답
 * </pre>
 */
package com.hitec.data.nfc_lib.protocol.recv;

public class GsmChangeDomainReport extends NfcRxMessage {

    private int result;

    public int getResult() {
        return result;
    }

    @Override
    public boolean parse(byte[] rxdata) {
        if (super.parse(rxdata) == false) {
            return false;
        }

        result = getHexData(m_nOffset++);

        return parseCompleted();
    }
}
