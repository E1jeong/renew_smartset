/**
 * <pre>
 * gsm 통신모드 변경 요청에 대한 응답
 * </pre>
 */
package com.hitec.presentation.nfc_lib.protocol.recv;

public class SelectGsmOrLteReport extends NfcRxMessage {

    private int result;

    public int getResult() {
        return result;
    }

    @Override
    public boolean parse(byte[] rxdata) {
        if (super.parse(rxdata) == false) {
            return false;
        }

        //요청에 대한 응답 result code = 0,1
        result = getHexData(m_nOffset++);

        return parseCompleted();
    }
}
