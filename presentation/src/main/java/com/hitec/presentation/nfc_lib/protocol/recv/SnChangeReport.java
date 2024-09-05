/**
 * <pre>
 * SnChangeReq 장비일련번호 변경 요청
 * </pre>
 */
package com.hitec.presentation.nfc_lib.protocol.recv;

public class SnChangeReport extends NfcRxMessage {

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
