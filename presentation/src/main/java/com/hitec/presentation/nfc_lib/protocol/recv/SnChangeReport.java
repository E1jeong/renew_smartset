/**
 * <pre>
 * SnChangeReq 장비일련번호 변경 요청
 * </pre>
 */
package com.hitec.presentation.nfc_lib.protocol.recv;

public class SnChangeReport extends NfcRxMessage {

    private int resultCode;

    public int getResultCode() {
        return resultCode;
    }

    @Override
    public boolean parse(byte[] rxData) {
        if (!super.parse(rxData)) {
            return false;
        }

        resultCode = getHexData(m_nOffset++);

        return parseCompleted();
    }
}
